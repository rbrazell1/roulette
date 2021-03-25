package edu.cnm.deepdive.roulette.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import edu.cnm.deepdive.roulette.model.dto.PocketDto;
import edu.cnm.deepdive.roulette.model.dto.WagerSpot;
import edu.cnm.deepdive.roulette.model.pojo.SpinWithWagers;
import edu.cnm.deepdive.roulette.service.ConfigurationRepository;
import edu.cnm.deepdive.roulette.service.PreferenceRepository;
import edu.cnm.deepdive.roulette.service.SpinRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlayViewModel extends AndroidViewModel implements LifecycleObserver {

  public static final int POCKET_ON_WHEEL = 38;

  private final ConfigurationRepository configurationRepository;
  private final PreferenceRepository preferenceRepository;
  private final SpinRepository spinRepository;
  private final MutableLiveData<PocketDto> rouletteValue;
  private final MutableLiveData<Integer> pocketIndex;
  private final MutableLiveData<Long> currentPot;
  private final MutableLiveData<Map<WagerSpot, Integer>> wagerAmount;
  private final MutableLiveData<Integer> maxWagerAmount;
  private final MutableLiveData<List<WagerSpot>> wagerSpotList;
  private final MutableLiveData<List<PocketDto>> pocketDtoList;
  private final MutableLiveData<Throwable> throwable;
  private final Random rng;
  private final CompositeDisposable pending;

  public PlayViewModel(@NonNull Application application) {
    super(application);
    configurationRepository = ConfigurationRepository.getInstance();
    preferenceRepository = new PreferenceRepository(application);
    spinRepository = new SpinRepository(application);
    rouletteValue = new MutableLiveData<>();
    pocketIndex = new MutableLiveData<>();
    currentPot = new MutableLiveData<>();
    wagerAmount = new MutableLiveData<>(new HashMap<>());
    maxWagerAmount = new MutableLiveData<>(preferenceRepository.getMaximumWager());
    wagerSpotList = new MutableLiveData<>(configurationRepository.getWagerSpotList());
    throwable = new MutableLiveData<>();
    rng = new SecureRandom();
    pocketDtoList = new MutableLiveData<>(configurationRepository.getPocketDtoList());
    pending = new CompositeDisposable();
    observeMaxWager();
    newGame();
  }

  public LiveData<PocketDto> getRouletteValue() {
    return rouletteValue;
  }

  public LiveData<Integer> getPocketIndex() {
    return pocketIndex;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public LiveData<Long> getCurrentPot() {
    return currentPot;
  }

  public LiveData<Map<WagerSpot, Integer>> getWagerAmount() {
    return wagerAmount;
  }

  public LiveData<List<PocketDto>> getPocketDtoList() {
    return pocketDtoList;
  }

  public LiveData<Integer> getMaxWagerAmount() {
    return maxWagerAmount;
  }

  public LiveData<List<WagerSpot>> getWagerSpotList() {
    return wagerSpotList;
  }

  public void spinWheel() {
    List<PocketDto> pocketDtoList = this.pocketDtoList.getValue();
    @SuppressWarnings("ConstantConditions")
    int selection = rng.nextInt(pocketDtoList.size());
    pocketIndex.setValue(selection);
    rouletteValue.setValue(pocketDtoList.get(selection));
    SpinWithWagers spin = new SpinWithWagers();
    spin.setValue(pocketDtoList.get(selection).getName());
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
    //noinspection ConstantConditions
    rouletteValue.setValue(pocketDtoList.getValue().get(0));
  }

  @SuppressWarnings("ConstantConditions")
  public void incrementWagerAmount(WagerSpot spot) {
    Map<WagerSpot, Integer> wagerAmount = this.wagerAmount.getValue();
    int currentWagers = wagerAmount
        .getOrDefault(spot, 0);
    if (currentWagers < maxWagerAmount.getValue()) {
      wagerAmount.put(spot, 1 + wagerAmount.getOrDefault(spot, 0));
      this.wagerAmount.setValue(wagerAmount);
      currentPot.setValue(currentPot.getValue() - 1);
    }
  }

  @SuppressWarnings("ConstantConditions")
  public void clearWagerAmount(WagerSpot spot) {
    Map<WagerSpot, Integer> wagerAmount = this.wagerAmount.getValue();
    int currentWagers = wagerAmount.getOrDefault(spot, 0);
    if (currentWagers > 0) {
      wagerAmount.remove(spot);
      this.wagerAmount.setValue(wagerAmount);
      currentPot.setValue(currentWagers + currentPot.getValue());
    }
  }

  @SuppressLint("CheckResult")
  private void observeMaxWager() {
    //noinspection ResultOfMethodCallIgnored
    preferenceRepository.maxWager().subscribe(this::adjustMaxWager);
  }

  @SuppressWarnings("ConstantConditions")
  private void adjustMaxWager(int maxWager) {
    Map<WagerSpot, Integer> wagers = this.wagerAmount.getValue();
    int excess = 0;
    for (WagerSpot spot :
        wagers.keySet()) {
      int wager = wagers.get(spot);
      if (wager > maxWager) {
        excess += wager - maxWager;
        wagers.put(spot, maxWager);
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