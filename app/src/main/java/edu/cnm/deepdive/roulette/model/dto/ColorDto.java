package edu.cnm.deepdive.roulette.model.dto;

import com.google.gson.annotations.Expose;
import edu.cnm.deepdive.roulette.model.type.Color;

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
  private int Span;

  @Expose
  private int Spot;

  @Expose
  private int payout;

  private Color color;

  private WagerSpot pocketDto;

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
    return Span;
  }

  public void setSpan(int span) {
    this.Span = span;
  }

  @Override
  public int getSpot() {
    return Spot;
  }

  public void setSpot(int spot) {
    Spot = spot;
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
}

