package edu.cnm.deepdive.roulette.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.roulette.R;
import edu.cnm.deepdive.roulette.adapter.WagerSpaceAdapter.Holder;
import edu.cnm.deepdive.roulette.databinding.ItemWagerSpaceBinding;

public class WagerSpaceAdapter extends RecyclerView.Adapter<Holder> {

  private final Context context;
  private final int[] spaceColors;
  private final String[] spaceValues;

  public WagerSpaceAdapter(Context context) {
    this.context = context;
    Resources res = context.getResources();
    spaceColors = res.getIntArray(R.array.space_colors);
    spaceValues = res.getStringArray(R.array.space_values);
  }


  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemWagerSpaceBinding binding =
        ItemWagerSpaceBinding.inflate(LayoutInflater.from(context), parent, false);
    return new Holder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return spaceColors.length;
  }

  class Holder extends RecyclerView.ViewHolder {

    private final ItemWagerSpaceBinding binding;

    public Holder(@NonNull ItemWagerSpaceBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    private void bind(int position) {
      binding.getRoot().setBackgroundColor(spaceColors[position]);
      binding.value.setText(spaceValues[position]);
    }
  }


}
