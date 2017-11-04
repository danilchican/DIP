package com.bsuir.danilchican.type;

public enum Channel {

    BLUE(0), GREEN(1), RED(2), ALL(3);

    private int index;

    Channel(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}