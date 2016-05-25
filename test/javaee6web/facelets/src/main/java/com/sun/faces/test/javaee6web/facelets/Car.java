package com.sun.faces.test.javaee6web.facelets;

public class Car {

  private String id;
  private String brand;
  private int year;
  private String color;
  private int price;
  private boolean soldState;

  public Car(String id, String brand, int year, String color, int price, boolean soldState) {
    this.id = id;
    this.brand = brand;
    this.year = year;
    this.color = color;
    this.price = price;
    this.soldState = soldState;
  }

  public String getId() {
    return id;
  }

  public String getBrand() {
    return brand;
  }

  public int getYear() {
    return year;
  }

  public String getColor() {
    return color;
  }

  public int getPrice() {
    return price;
  }

  public boolean isSoldState() {
    return soldState;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public void setSoldState(boolean soldState) {
    this.soldState = soldState;
  }

}


