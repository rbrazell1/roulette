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
import edu.cnm.deepdive.roulette.service.PreferenceRepository;
import edu.cnm.deepdive.roulette.service.SpinRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.security.SecureRandom;
import java.util.Random;

public class PlayViewModel extends AndroidViewModel implements LifecycleObserver {

  public static final int POCKET_ON_WHEEL = 38;

  private final MutableLiveData<String> rouletteValue;
  private final MutableLiveData<Integer> pocketIndex;
  private final MutableLiveData<Long> currentPot;
  private final MutableLiveData<Throwable> throwable;
  private final String[] pocketValues;
  private final Random rng;
  private final PreferenceRepository preferenceRepository;
  private final SpinRepository spinRepository;
  private final CompositeDisposable pending;

  public PlayViewModel(@NonNull Application application) {
    super(application);
    rouletteValue = new MutableLiveData<>();
    rng = new SecureRandom();
    pocketIndex = new MutableLiveData<>();
    currentPot = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pocketValues = application.getResources().getStringArray(R.array.pocket_values);
    preferenceRepository = new PreferenceRepository(application);
    spinRepository = new SpinRepository(application);
    pending = new CompositeDisposable();
    startGame();
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

  public void startGame() {
    currentPot.setValue((long)preferenceRepository.getStartingPot());
    pocketIndex.setValue(0);
    rouletteValue.setValue(pocketValues[0]);
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