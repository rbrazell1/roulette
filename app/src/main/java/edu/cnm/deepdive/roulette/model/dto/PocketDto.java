package edu.cnm.deepdive.roulette.model.dto;

import com.google.gson.annotations.Expose;

public class PocketDto {

  @Expose
  private String name;

  @Expose
  private int Position;

  @Expose
  private int spot;

  @Expose
  private int span;

  @Expose
  private String color;

  @Expose
  private int payout;

  private ColorDto colorDto;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPosition() {
    return Position;
  }

  public void setPosition(int position) {
    this.Position = position;
  }

  public int getSpot() {
    return spot;
  }

  public void setSpot(int spot) {
    this.spot = spot;
  }

  public int getSpan() {
    return span;
  }

  public void setSpan(int span) {
    this.span = span;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public int getPayout() {
    return payout;
  }

  public void setPayout(int payout) {
    this.payout = payout;
  }

  public ColorDto getColorDto() {
    return colorDto;
  }

  public void setColorDto(ColorDto colorDto) {
    this.colorDto = colorDto;
  }
}
