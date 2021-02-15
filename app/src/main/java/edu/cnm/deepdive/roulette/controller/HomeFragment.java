package edu.cnm.deepdive.roulette.controller;

import static android.os.Build.VERSION_CODES.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.roulette.databinding.FragmentHomeBinding;
import edu.cnm.deepdive.roulette.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {

  private HomeViewModel homeViewModel;
  private FragmentHomeBinding binding;

  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentHomeBinding.inflate(inflater, container, false);
    binding.spinWheel.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        homeViewModel.spinWheel();
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
      public void onChanged(Integer pocketIdex) {
        float centerX = binding.rouletteWheel.getWidth() / 2f;
        float centerY = binding.rouletteWheel.getHeight() / 2f;
        float currentRotation = binding.rouletteWheel.getRotation();
        float finalRotation = -360 * pocketIdex / 38f;
        binding.rouletteWheel.setPivotX(centerX);
        binding.rouletteWheel.setPivotY(centerY);

      }
    });
  }
}