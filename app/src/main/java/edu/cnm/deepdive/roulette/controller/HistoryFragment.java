package edu.cnm.deepdive.roulette.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import edu.cnm.deepdive.roulette.databinding.FragmentHistoryBinding;
import edu.cnm.deepdive.roulette.viewmodel.DashboardViewModel;

public class HistoryFragment extends Fragment {

  private DashboardViewModel dashboardViewModel;
  private FragmentHistoryBinding binding;

  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentHistoryBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }
}