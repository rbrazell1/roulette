package edu.cnm.deepdive.roulette.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import edu.cnm.deepdive.roulette.R;
import edu.cnm.deepdive.roulette.databinding.ActivityLoginBinding;
import edu.cnm.deepdive.roulette.service.GoogleSignInService;

public class LoginActivity extends AppCompatActivity {

  private static final int LOGIN_REQUEST_CODE = 24;

  private GoogleSignInService service;

  private ActivityLoginBinding binding;


  @SuppressLint("CheckResult")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    service = GoogleSignInService.getInstance();
    //noinspection ResultOfMethodCallIgnored
    service
        .refresh()
        .subscribe(
            (account) -> {/* TODO update account and switch to main*/},
            (throwable) -> {
              binding = ActivityLoginBinding.inflate(getLayoutInflater());
              binding.signIn.setOnClickListener((v) -> {/* TODO start sign in*/});
              setContentView(binding.getRoot());
            }
        );
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode == LOGIN_REQUEST_CODE) {
      service
          .completeSignIn(data)
          .addOnSuccessListener((account) -> {/*TODO update and switch to main activity*/})
          .addOnFailureListener((throwable) ->
              Snackbar.make(binding.getRoot(), R.string.login_failure,
                  BaseTransientBottomBar.LENGTH_INDEFINITE).show()
          );
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  private void updateAndSwitch(GoogleSignInAccount account) {
    // If we have a local data base with a user table update based on login
    startActivity(
        new Intent(this, MainActivity.class)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
    );
  }


}