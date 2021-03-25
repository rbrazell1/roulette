package edu.cnm.deepdive.roulette.model.dto;

public interface WagerSpot {

  String getName();

  int getSpot();

  int getSpan();

  int getPayout();

  int getColorResource();

  int hashCode();

  boolean equals(Object obj);
}
