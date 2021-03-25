package edu.cnm.deepdive.roulette.model.dto;

import com.google.gson.annotations.Expose;
import java.util.List;

public class ConfigurationDto {

  @Expose
  private List<PocketDto> pocketDtoList;

  @Expose
  private List<ColorDto> colorDtoList;

  public List<PocketDto> getPocketDtoList() {
    return pocketDtoList;
  }

  public void setPocketDtoList(List<PocketDto> pocketDtoList) {
    this.pocketDtoList = pocketDtoList;
  }

  public List<ColorDto> getColorDtoList() {
    return colorDtoList;
  }

  public void setColorDtoList(List<ColorDto> colorDtoList) {
    this.colorDtoList = colorDtoList;
  }
}
