package edu.cnm.deepdive.roulette.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ConfigurationDto {

  @Expose
  @SerializedName("pockets")
  private List<PocketDto> pocketDtoList;

  @Expose
  @SerializedName("colors")
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
