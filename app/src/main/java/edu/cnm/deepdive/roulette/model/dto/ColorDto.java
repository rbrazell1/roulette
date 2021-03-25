package edu.cnm.deepdive.roulette.model.dto;

import com.google.gson.annotations.Expose;
import edu.cnm.deepdive.roulette.model.type.Color;

public class ColorDto {

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
  private int payout;

  @Expose
  private Color color;

  private PocketDto pocketDto;

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

  public PocketDto getPocketDto() {
    return pocketDto;
  }

  public void setPocketDto(PocketDto pocketDto) {
    this.pocketDto = pocketDto;
  }
}

