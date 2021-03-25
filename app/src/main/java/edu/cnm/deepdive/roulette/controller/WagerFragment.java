package edu.cnm.deepdive.roulette.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup;
import androidx.recyclerview.widget.LinearLayoutManager;
import edu.cnm.deepdive.roulette.R;
import edu.cnm.deepdive.roulette.adapter.WagerSpotAdapter;
import edu.cnm.deepdive.roulette.databinding.FragmentWagerBinding;
import edu.cnm.deepdive.roulette.model.dto.WagerSpot;
import edu.cnm.deepdive.roulette.viewmodel.PlayViewModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WagerFragment extends Fragment {

  private static final int FULL_WIDTH = 6;

  private final Map<WagerSpot, Integer> wagers = new HashMap<>();

  private FragmentWagerBinding binding;
  private PlayViewModel viewModel;
  private WagerSpotAdapter adapter;
  private int maxWager = 100;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentWagerBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(getActivity()).get(PlayViewModel.class);
    viewModel.getWagerAmount().observe(getViewLifecycleOwner(), this::updateWagers);
    viewModel.getMaxWagerAmount().observe(getViewLifecycleOwner(), this::updateMaxWager);
    viewModel.getWagerSpotList().observe(getViewLifecycleOwner(), this::setupAdapter);
    //TODO Observe viewmodel livedata as needed
  }

  private void updateMaxWager(Integer maxWager) {
    this.maxWager = maxWager;
    if (adapter != null) {
      updateAdapterMaxWager();
    }
  }

  private void updateAdapterMaxWager() {
    adapter.setMaxWager(maxWager);
    adapter.notifyDataSetChanged();
  }

  private void setupAdapter(List<WagerSpot> wagerSpotList) {
    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), FULL_WIDTH,
        LinearLayoutManager.VERTICAL,
        false);
    layoutManager.setSpanSizeLookup(new WagerSpanLookup(wagerSpotList));
    binding.wagerSpaces.setLayoutManager(layoutManager);
    adapter = new WagerSpotAdapter(
        getContext(),
        wagerSpotList,
        (v, position, value) -> viewModel.incrementWagerAmount(value),
        (v, position, value) -> showWagerActions(v, value));
    binding.wagerSpaces.setAdapter(adapter);
    updateAdapterWagers();
    updateAdapterMaxWager();
  }

  private void updateWagers(Map<WagerSpot, Integer> updatedWagers) {
    this.wagers.clear();
    this.wagers.putAll(updatedWagers);
    if (adapter != null) {
      updateAdapterWagers();
    }
  }

  private void updateAdapterWagers() {
    Map<WagerSpot, Integer> currentWagers = adapter.getWagers();
    currentWagers.clear();
    currentWagers.putAll(wagers);
    adapter.notifyDataSetChanged();
  }

  private void showWagerActions(View view, WagerSpot spot) {
    @SuppressWarnings("ConstantConditions")
    PopupMenu menu = new PopupMenu(getContext(), view);
    MenuInflater menuInflater = menu.getMenuInflater();
    menuInflater.inflate(R.menu.wager_actions, menu.getMenu());
    menu
        .getMenu()
        .findItem(R.id.amount)
        .setTitle(getString(R.string.current_wager_format, wagers.getOrDefault(spot, 0)));
    menu
        .getMenu()
        .findItem(R.id.clear)
        .setOnMenuItemClickListener((item) -> {
          viewModel.clearWagerAmount(spot);
          return true;
        });
    menu.show();
  }

  private static class WagerSpanLookup extends SpanSizeLookup {

    private final List<WagerSpot> wagerSpotList;

    private WagerSpanLookup(
        List<WagerSpot> wagerSpotList) {
      this.wagerSpotList = wagerSpotList;
    }

    @Override
    public int getSpanSize(int position) {
      return wagerSpotList.get(position).getSpan();
    }
  }
}