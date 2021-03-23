package edu.cnm.deepdive.roulette.service;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import androidx.preference.PreferenceManager;
import edu.cnm.deepdive.roulette.R;

public class PreferenceRepository {

  private final Context context;
  private final SharedPreferences preferences;
  private final Resources resources;


  public PreferenceRepository(Context context) {
    this.context = context;
    preferences = PreferenceManager.getDefaultSharedPreferences(context);
    resources = context.getResources();
  }

  public int getMaximumWager() {
    return preferences.getInt(resources.getString(R.string.maximum_wager_key),
        resources.getInteger(R.integer.maximum_wager_default));
  }

  public int getStartingPot() {
    return preferences.getInt(resources.getString(R.string.starting_pot_key),
        resources.getInteger(R.integer.starting_pot_default));
  }

  public boolean isLetItRide() {
    return preferences.getBoolean(resources.getString(R.string.let_it_ride_key),
        resources.getBoolean(R.bool.let_it_ride_default));
  }

}
