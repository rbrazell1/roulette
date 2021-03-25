package edu.cnm.deepdive.roulette.service;

import android.app.Application;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.roulette.R;
import edu.cnm.deepdive.roulette.model.dto.ColorDto;
import edu.cnm.deepdive.roulette.model.dto.ConfigurationDto;
import edu.cnm.deepdive.roulette.model.dto.PocketDto;
import edu.cnm.deepdive.roulette.model.dto.WagerSpot;
import edu.cnm.deepdive.roulette.model.type.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigurationRepository {

  private static Application context;

  private List<WagerSpot> wagerSpotList;
  private List<PocketDto> pocketDtoList;

  private ConfigurationRepository() {
    try (
        InputStream inputStream = context.getResources().openRawResource(R.raw.configuration);
        Reader reader = new InputStreamReader(inputStream);
    ) {
      ConfigurationDto configurationDto = parse(reader);
      Map<String, ColorDto> colorDtoMap = buildColorMap(configurationDto);
      buildPocketList(configurationDto, colorDtoMap);
      buildWagerSpotList(colorDtoMap);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static void setContext(Application context) {
    ConfigurationRepository.context = context;
  }

  public static ConfigurationRepository getInstance() {
    return InstanceHolder.INSTANCE;
  }

  private void buildWagerSpotList(Map<String, ColorDto> colorDtoMap) {
    wagerSpotList = Stream.concat(
        pocketDtoList.stream(),
        colorDtoMap
            .values()
            .stream()
    )
        .sorted(Comparator.comparing(WagerSpot::getSpot))
        .collect(Collectors.toList());
  }

  private void buildPocketList(ConfigurationDto configurationDto,
      Map<String, ColorDto> colorDtoMap) {
    pocketDtoList = configurationDto
        .getPocketDtoList()
        .stream()
        .peek((p) -> p.setColorDto(colorDtoMap.get(p.getColor())))
        .sorted(Comparator.comparing(PocketDto::getPosition))
        .collect(Collectors.toList());
  }

  private Map<String, ColorDto> buildColorMap(ConfigurationDto configurationDto) {
    return configurationDto
        .getColorDtoList()
        .stream()
        .peek((c) -> {
          c.setColor(Color.valueOf(c.getName().toUpperCase()));
          c.setColorResource(context
              .getResources()
              .getIdentifier(c.getResource(),
                  "color",
                  context.getPackageName()));
        })
        .collect(Collectors
            .toMap(ColorDto::getName, Function.identity()));
  }

  private ConfigurationDto parse(Reader reader) {
    Gson gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create();
    return gson.fromJson(reader, ConfigurationDto.class);
  }

  public List<WagerSpot> getWagerSpotList() {
    return wagerSpotList;
  }

  public List<PocketDto> getPocketDtoList() {
    return pocketDtoList;
  }

  private static class InstanceHolder {

    private static final ConfigurationRepository INSTANCE = new ConfigurationRepository();
  }
}
