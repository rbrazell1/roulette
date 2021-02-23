package edu.cnm.deepdive.roulette.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.cnm.deepdive.roulette.R;


public class WagerFragment extends Fragment {

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO Get args
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // TODO Inflate using viewBinding
    return inflater.inflate(R.layout.fragment_wager, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}