package com.bsuir.dip.bean;

public class DetectedItem {
    private int number;
    private int space;
    private int claster;
    private int perimeter;
    private float compactness;
    private int massCenterX;
    private int massCenterY;

    public int getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(int perimeter) {
        this.perimeter = perimeter;
    }

    public float getCompactness() {
        return compactness;
    }

    public void setCompactness(float compactness) {
        this.compactness = compactness;
    }

    public int getMassCenterX() {
        return massCenterX;
    }

    public void setMassCenterX(float massCenterX) {
        this.massCenterX = (int) massCenterX;
    }

    public int getMassCenterY() {
        return massCenterY;
    }

    public void setMassCenterY(float massCenterY) {
        this.massCenterY = (int) massCenterY;
    }

    public DetectedItem(int number, int weight) {
        this.number = number;
        this.space = weight;
        this.claster = 0;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int weight) {
        this.space = weight;
    }

    public int getClaster() {
        return claster;
    }

    public void setClaster(int claster) {
        this.claster = claster;
    }

}
