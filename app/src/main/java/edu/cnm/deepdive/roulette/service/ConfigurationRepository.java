package edu.cnm.deepdive.roulette.service;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.roulette.R;
import edu.cnm.deepdive.roulette.model.dto.ConfigurationDto;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ConfigurationRepository {

  private static Context context;

  private ConfigurationRepository() {
    try (
        InputStream inputStream = context.getResources().openRawResource(R.raw.configuration);
        Reader reader = new InputStreamReader(inputStream);
        ) {
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .create();
      ConfigurationDto configurationDto = gson.fromJson(reader, ConfigurationDto.class);
      Log.d(getClass().getName(), "Successfully read config");
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static void ConfigurationRepository(Context context) {
    ConfigurationRepository.context = context;
  }

  public static ConfigurationRepository getInstance() {
    return InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {
    private static final ConfigurationRepository INSTANCE = new ConfigurationRepository();
  }
}
