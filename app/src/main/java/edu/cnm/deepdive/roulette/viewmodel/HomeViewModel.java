package edu.cnm.deepdive.roulette.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.roulette.R;
import java.security.SecureRandom;
import java.util.Random;

public class HomeViewModel extends AndroidViewModel {

  private final MutableLiveData<String> rouletteValue;
  private final MutableLiveData<Integer> pocketIndex;
  private final String[] pocketVals;
  private final Random rng;

  public HomeViewModel(@NonNull Application application) {
    super(application);
    rouletteValue = new MutableLiveData<>();
    rng = new SecureRandom();
    pocketIndex = new MutableLiveData<>();
    pocketVals = application.getResources().getStringArray(R.array.pocket_val);
  }

  public LiveData<String> getRouletteValue() {
    return rouletteValue;
  }

  public LiveData<Integer> getPocketIndex() {
    return pocketIndex;
  }

  public void spinWheel() {
    int selection = rng.nextInt(pocketVals.length);
    pocketIndex.setValue(selection);
    rouletteValue.setValue(pocketVals[selection]);
  }
}