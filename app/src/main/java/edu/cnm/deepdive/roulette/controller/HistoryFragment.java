package edu.cnm.deepdive.roulette.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.roulette.adapter.HistoryItemAdapter;
import edu.cnm.deepdive.roulette.databinding.FragmentHistoryBinding;
import edu.cnm.deepdive.roulette.viewmodel.HistoryViewModel;

public class HistoryFragment extends Fragment {

  private HistoryViewModel viewModel;
  private FragmentHistoryBinding binding;

  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentHistoryBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
    viewModel.getHistory().observe(getViewLifecycleOwner(), (spins) -> {
      binding.spins.setAdapter(new HistoryItemAdapter(getContext(), spins));
    });
  }
}