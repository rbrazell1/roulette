package edu.cnm.deepdive.roulette.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.roulette.databinding.FragmentHomeBinding;
import edu.cnm.deepdive.roulette.viewmodel.HomeViewModel;
import java.security.SecureRandom;

public class HomeFragment extends Fragment {

  private static final int MIN_ROTATION_TIME = 2000;
  public static final int MAX_ROTATION_TIME = 3000;
  public static final int DEGREES_PER_REVOLUTION = 360;
  public static final int MIN_FULL_ROTATIONS = 3;
  public static final int DEGREES_PER_REVOLUTIONS = 360;
  public static final int MAX_FULL_ROTATIONS = 3;

  private HomeViewModel homeViewModel;
  private FragmentHomeBinding binding;
  private boolean spinning;
  private SecureRandom rng;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    rng = new SecureRandom();
  }

  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentHomeBinding.inflate(inflater, container, false);
    binding.spinWheel.setOnClickListener((view) -> spinWheel());
    return binding.getRoot();
  }

  private void spinWheel() {
    if (!spinning) {
      spinning = true;
      binding.spinWheel.setEnabled(false);
      binding.rouletteValue.setVisibility(View.INVISIBLE);
      homeViewModel.spinWheel();
    }
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    homeViewModel.getRouletteValue().observe(getViewLifecycleOwner(),
        (s) -> binding.rouletteValue.setText(s));
    homeViewModel.getPocketIndex().observe(getViewLifecycleOwner(), this::startAnimation);
  }

  private void startAnimation(Integer pocketIndex) {
    float centerX = binding.rouletteWheel.getWidth() / 2f;
    float centerY = binding.rouletteWheel.getHeight() / 2f;
    float currentRotation = binding.rouletteWheel.getRotation();
    float finalRotation = -360 * pocketIndex / 38f;
    binding.rouletteWheel.setPivotX(centerX);
    binding.rouletteWheel.setPivotY(centerY);
    RotateAnimation rotation = new RotateAnimation(
        0, (finalRotation - currentRotation)
        - DEGREES_PER_REVOLUTIONS * (MIN_FULL_ROTATIONS + rng.nextInt(MAX_FULL_ROTATIONS)),
        centerX, centerY);
    rotation.setDuration(MIN_ROTATION_TIME + rng.nextInt(MAX_ROTATION_TIME));
    rotation.setAnimationListener(new AnimationFinalizer(finalRotation));
    binding.rouletteWheel.startAnimation(rotation);
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
      binding.spinWheel.setEnabled(true);
      binding.rouletteValue.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
  }
}
