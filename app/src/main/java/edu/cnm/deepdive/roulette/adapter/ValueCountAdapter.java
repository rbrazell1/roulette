package edu.cnm.deepdive.roulette.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import edu.cnm.deepdive.roulette.model.view.ValueCount;
import java.util.List;

public class ValueCountAdapter extends ArrayAdapter<ValueCount> {


  public ValueCountAdapter(@NonNull Context context, int resource,
      @NonNull List<ValueCount> counts) {
    super(context, resource, counts);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    return super.getView(position, convertView, parent);
  }
}
