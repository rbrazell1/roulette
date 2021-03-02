package edu.cnm.deepdive.roulette.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.roulette.adapter.ValueCountAdapter;
import edu.cnm.deepdive.roulette.databinding.FragmentStatsBinding;
import edu.cnm.deepdive.roulette.model.view.ValueCount;
import edu.cnm.deepdive.roulette.viewmodel.StatsViewModel;

public class StatsFragment extends Fragment {

  private StatsViewModel statsViewModel;
  private FragmentStatsBinding binding;

  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentStatsBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    statsViewModel = new ViewModelProvider(this).get(StatsViewModel.class);
    statsViewModel.getCounts().observe(getViewLifecycleOwner(), (counts) -> {
      //noinspection ConstantConditions
      ArrayAdapter<ValueCount> adapter = new ValueCountAdapter(getContext(), counts);
      binding.countsList.setAdapter(adapter);
    });
  }
}