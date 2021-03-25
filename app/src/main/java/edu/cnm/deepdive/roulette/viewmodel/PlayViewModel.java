package edu.cnm.deepdive.roulette.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import edu.cnm.deepdive.roulette.R;
import edu.cnm.deepdive.roulette.model.pojo.SpinWithWagers;
import edu.cnm.deepdive.roulette.service.ConfigurationRepository;
import edu.cnm.deepdive.roulette.service.PreferenceRepository;
import edu.cnm.deepdive.roulette.service.SpinRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PlayViewModel extends AndroidViewModel implements LifecycleObserver {

  public static final int POCKET_ON_WHEEL = 38;

  private final MutableLiveData<String> rouletteValue;
  private final MutableLiveData<Integer> pocketIndex;
  private final MutableLiveData<Long> currentPot;
  private final MutableLiveData<Map<String, Integer>> wagerAmount;
  private final MutableLiveData<Integer> maxWagerAmount;
  private final MutableLiveData<Throwable> throwable;
  private final String[] pocketValues;
  private final Random rng;
  private final PreferenceRepository preferenceRepository;
  private final ConfigurationRepository configurationRepository;
  private final SpinRepository spinRepository;
  private final CompositeDisposable pending;

  public PlayViewModel(@NonNull Application application) {
    super(application);
    rouletteValue = new MutableLiveData<>();
    rng = new SecureRandom();
    pocketIndex = new MutableLiveData<>();
    currentPot = new MutableLiveData<>();
    wagerAmount = new MutableLiveData<>(new HashMap<>());
    throwable = new MutableLiveData<>();
    pocketValues = application.getResources().getStringArray(R.array.pocket_values);
    preferenceRepository = new PreferenceRepository(application);
    configurationRepository = new ConfigurationRepository.getInstance();
    spinRepository = new SpinRepository(application);
    maxWagerAmount = new MutableLiveData<>(preferenceRepository.getMaximumWager());
    pending = new CompositeDisposable();
    observeMaxWager();
    newGame();
  }

  public LiveData<String> getRouletteValue() {
    return rouletteValue;
  }

  public LiveData<Integer> getPocketIndex() {
    return pocketIndex;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public MutableLiveData<Long> getCurrentPot() {
    return currentPot;
  }

  public LiveData<Map<String, Integer>> getWagerAmount() {
    return wagerAmount;
  }

  public MutableLiveData<Integer> getMaxWagerAmount() {
    return maxWagerAmount;
  }

  public void spinWheel() {
    int selection = rng.nextInt(pocketValues.length);
    pocketIndex.setValue(selection);
    rouletteValue.setValue(pocketValues[selection]);
    SpinWithWagers spin = new SpinWithWagers();
    spin.setValue(pocketValues[selection]);
    pending.add(
        spinRepository.save(spin)
            .subscribe(
                (spinWithWagers) -> {
                },
                this::handleThrowable
            )
    );
  }

  public void newGame() {
    currentPot.setValue((long) preferenceRepository.getStartingPot());
    pocketIndex.setValue(0);
    rouletteValue.setValue(pocketValues[0]);
  }

  @SuppressWarnings("ConstantConditions")
  public void incrementWagerAmount(String spaceValue) {
    Map<String, Integer> wagerAmount = this.wagerAmount.getValue();
    int currentWagers = wagerAmount
        .getOrDefault(spaceValue, 0);
    if (currentWagers < maxWagerAmount.getValue()) {
      wagerAmount.put(spaceValue, 1 + wagerAmount.getOrDefault(spaceValue, 0));
      this.wagerAmount.setValue(wagerAmount);
      currentPot.setValue(currentPot.getValue() - 1);
    }
  }

  @SuppressWarnings("ConstantConditions")
  public void clearWagerAmount(String spaceValue) {
    Map<String, Integer> wagerAmount = this.wagerAmount.getValue();
    int currentWagers = wagerAmount.getOrDefault(spaceValue, 0);
    if (currentWagers > 0) {
      wagerAmount.remove(spaceValue);
      this.wagerAmount.setValue(wagerAmount);
      currentPot.setValue(currentWagers + currentPot.getValue());
    }
  }

  private void observeMaxWager() {
    preferenceRepository.maxWager().subscribe(this::adjustMaxWager);
  }

  @SuppressWarnings("ConstantConditions")
  private void adjustMaxWager(int maxWager) {
    Map<String, Integer> wagers = this.wagerAmount.getValue();
    int excess = 0;
    for (String key :
        wagers.keySet()) {
      int wager = wagers.get(key);
      if (wager > maxWager) {
        excess += wager - maxWager;
        wagers.put(key, maxWager);
      }
    }
    if (excess > 0) {
      this.wagerAmount.postValue(wagers);
      currentPot.setValue(currentPot.getValue() + excess);
    }
    this.maxWagerAmount.postValue(maxWager);
  }

  private void handleThrowable(Throwable throwable) {
    Log.e(getClass().getName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
  }

}