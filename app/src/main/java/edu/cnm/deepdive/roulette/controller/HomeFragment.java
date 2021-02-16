package edu.cnm.deepdive.roulette.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.roulette.databinding.FragmentHomeBinding;
import edu.cnm.deepdive.roulette.viewmodel.HomeViewModel;
import java.security.SecureRandom;

public class HomeFragment extends Fragment {

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
    binding.spinWheel.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!spinning) {
          spinning = true;
          binding.spinWheel.setEnabled(false);
          binding.rouletteValue.setVisibility(View.VISIBLE);
          homeViewModel.spinWheel();
        }
      }
    });
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    homeViewModel.getRouletteValue().observe(getViewLifecycleOwner(), new Observer<String>() {
      @Override
      public void onChanged(String s) {
        binding.rouletteValue.setText(s);
      }
    });
    homeViewModel.getPocketIndex().observe(getViewLifecycleOwner(), new Observer<Integer>() {
      @Override
      public void onChanged(Integer pocketIndex) {
        float centerX = binding.rouletteWheel.getWidth() / 2f;
        float centerY = binding.rouletteWheel.getHeight() / 2f;
        float currentRotation = binding.rouletteWheel.getRotation();
        float finalRotation = -360 * pocketIndex / 38f;
        binding.rouletteWheel.setPivotX(centerX);
        binding.rouletteWheel.setPivotY(centerY);
        RotateAnimation rotation = new RotateAnimation(
            0, (finalRotation - currentRotation) - 360 * (3 + rng.nextInt(3)), centerX, centerY);
        rotation.setDuration(2000 + rng.nextInt(3000));
        rotation.setAnimationListener(new AnimationFinalizer(finalRotation));
        binding.rouletteWheel.startAnimation(rotation);
      }
    });
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
