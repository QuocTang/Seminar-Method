package com.rfid.app;

import java.io.Serializable;

public class Products implements Serializable {
    private String Item_code;
//    private String Name;
//    private String Color;
//    private int Price;
    private int Real_value;
    private int Theory_value;
    private int Gap;
    private String Time;

//    String name, String color, int price,

    public Products(String item_code, int real_value, int theory_value, int gap, String time){
        Item_code = item_code;
//        Name = name;
//        Color = color;
//        Price = price;
        Real_value = real_value;
        Theory_value = theory_value;
        Gap = gap;
        Time = time;
    }

    public String getItem_code() {
        return Item_code;
    }

    public void setItem_code(String item_code) {
        Item_code = item_code;
    }

//    public String getName() {
//        return Name;
//    }
//
//    public void setName(String name) {
//        Name = name;
//    }
//
//    public String getColor() {
//        return Color;
//    }
//
//    public void setColor(String color) {
//        Color = color;
//    }
//
//    public int getPrice() {
//        return Price;
//    }
//
//    public void setPrice(int price) {
//        Price = price;
//    }

    public int getReal_value() {
        return Real_value;
    }

    public void setReal_value(int real_value) {
        Real_value = real_value;
    }

    public int getTheory_value() {
        return Theory_value;
    }

    public void setTheory_value(int theory_value) {
        Theory_value = theory_value;
    }

    public int getGap() {
        return Gap;
    }

    public void setGap(int gap) {
        Gap = gap;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}