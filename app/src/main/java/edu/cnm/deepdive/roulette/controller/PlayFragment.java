package edu.cnm.deepdive.roulette.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import edu.cnm.deepdive.roulette.R;
import edu.cnm.deepdive.roulette.databinding.FragmentPlayBinding;
import edu.cnm.deepdive.roulette.viewmodel.PlayViewModel;
import java.security.SecureRandom;

public class PlayFragment extends Fragment {

  public static final int MAX_ROTATION_TIME = 3000;
  public static final int DEGREES_PER_REVOLUTION = 360;
  public static final int MIN_FULL_ROTATIONS = 3;
  public static final int DEGREES_PER_REVOLUTIONS = 360;
  public static final int MAX_FULL_ROTATIONS = 3;
  private static final int MIN_ROTATION_TIME = 2000;

  private PlayViewModel playViewModel;
  private FragmentPlayBinding binding;
  private boolean spinning;
  private SecureRandom rng;
  private float numPockets = Float.POSITIVE_INFINITY;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    rng = new SecureRandom();
    setHasOptionsMenu(true);
  }

  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentPlayBinding.inflate(inflater, container, false);
    binding.rouletteWheel.setOnClickListener((view) -> spinWheel());
    binding.placeWager.setOnClickListener((view) -> {
      Navigation.findNavController(binding.getRoot())
          .navigate(PlayFragmentDirections.actionNavigationPlayToNavigationWager());
    });
    return binding.getRoot();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    inflater.inflate(R.menu.play_options, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled = true;
    switch (item.getItemId()) {
      case R.id.new_game:
        playViewModel.newGame();
        break;
      default:
        handled = super.onOptionsItemSelected(item);
        break;
    }
    return handled;
  }

  private void spinWheel() {
    if (!spinning) {
      spinning = true;
      binding.rouletteWheel.setEnabled(false);
      binding.rouletteValue.setVisibility(View.INVISIBLE);
      binding.currentPotValue.setVisibility(View.INVISIBLE);
      playViewModel.spinWheel();
    }
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //noinspection ConstantConditions
    playViewModel = new ViewModelProvider(getActivity()).get(PlayViewModel.class);
    getLifecycle().addObserver(playViewModel);
    LifecycleOwner lifecycleOwner = getViewLifecycleOwner();
    playViewModel.getRouletteValue().observe(lifecycleOwner,
        (pocket) -> binding.rouletteValue.setText(pocket.getName()));
    playViewModel.getPocketIndex().observe(lifecycleOwner, this::rotateToPocket);
    playViewModel.getThrowable().observe(lifecycleOwner, (throwable) -> {
      if (throwable != null) {
        //noinspection ConstantConditions
        Snackbar.make(getContext(), binding.getRoot(), throwable.getMessage(),
            BaseTransientBottomBar.LENGTH_INDEFINITE).show();
      }
    });
    playViewModel.getCurrentPot().observe(lifecycleOwner, (pot) ->
        binding.currentPotValue.setText(getString(R.string.current_pot_format, pot)));
    playViewModel.getPocketDtoList().observe(lifecycleOwner, (pockets) -> {
      numPockets = pockets.size();
    });
  }

  @Override
  public void onStop() {
    binding.rouletteWheel.clearAnimation();
    super.onStop();
  }

  private void rotateToPocket(Integer pocketIndex) {
    float finalRotation = -DEGREES_PER_REVOLUTIONS * pocketIndex / numPockets;
    if (spinning) {
      float centerX = binding.rouletteWheel.getWidth() / 2f;
      float centerY = binding.rouletteWheel.getHeight() / 2f;
      float currentRotation = binding.rouletteWheel.getRotation();
      binding.rouletteWheel.setPivotX(centerX);
      binding.rouletteWheel.setPivotY(centerY);
      RotateAnimation rotation = new RotateAnimation(0, (finalRotation - currentRotation)
          - DEGREES_PER_REVOLUTION * (MIN_FULL_ROTATIONS
          + rng.nextInt(MAX_FULL_ROTATIONS - MIN_FULL_ROTATIONS + 1)), centerX, centerY);
      rotation.setDuration(
          MIN_ROTATION_TIME + rng.nextInt(MAX_ROTATION_TIME - MIN_ROTATION_TIME));
      rotation.setAnimationListener(new AnimationFinalizer(finalRotation));
      binding.rouletteWheel.startAnimation(rotation);
    } else {
      binding.rouletteWheel.setRotation(finalRotation);
    }
  }

  private class AnimationFinalizer implements AnimationListener {

    private final float finalRotation;

    private AnimationFinalizer(float finalRotation) {
      this.finalRotation = finalRotation;
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
      binding.rouletteWheel.setRotation(finalRotation);
      spinning = false;
      binding.rouletteWheel.setEnabled(true);
      binding.rouletteValue.setVisibility(View.VISIBLE);
      binding.currentPotValue.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
  }
}
