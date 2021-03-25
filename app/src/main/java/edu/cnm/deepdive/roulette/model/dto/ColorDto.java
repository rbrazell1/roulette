package edu.cnm.deepdive.roulette.model.dto;

import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import edu.cnm.deepdive.roulette.model.type.Color;
import java.util.Objects;

public class ColorDto implements WagerSpot {

  @Expose
  private String name;

  @Expose
  private String resource;

  @Expose
  private int colorResource;

  @Expose
  private int Position;

  @Expose
  private int span;

  @Expose
  private int spot;

  @Expose
  private int payout;

  private Color color;

  private boolean hashComputed;

  private int hash;

  private WagerSpot pocketDto;

  // Gettters and Setters

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public int getPosition() {
    return Position;
  }

  public void setPosition(int position) {
    this.Position = position;
  }

  public int getSpan() {
    return span;
  }

  public void setSpan(int span) {
    this.span = span;
  }

  public int getSpot() {
    return spot;
  }

  public void setSpot(int spot) {
    spot = spot;
  }

  public int getPayout() {
    return payout;
  }

  public void setPayout(int payout) {
    this.payout = payout;
  }

  public int getColorResource() {
    return colorResource;
  }

  public void setColorResource(int colorResource) {
    this.colorResource = colorResource;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public WagerSpot getPocketDto() {
    return pocketDto;
  }

  public void setPocketDto(WagerSpot pocketDto) {
    this.pocketDto = pocketDto;
  }

  @Override
  public int hashCode() {
    if (!hashComputed) {
      hash = Objects.hash(name, spot);
      hashComputed = true;
    }
    return hash;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    boolean equal = false;
    if (this == obj) {
      equal = true;
    } else if (obj instanceof ColorDto) {
      ColorDto other = (ColorDto) obj;
      equal = name.equals(other.name) && spot == other.spot;
    }
    return equal;
  }
}

