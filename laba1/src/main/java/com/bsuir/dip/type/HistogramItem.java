package com.bsuir.dip.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum HistogramItem {

    EMPTY(0, ""),
    GRAYSCALE(1, "GrayScale"),
    RGB(2, "RGB"),
    R(3, "R"),
    G(4, "G"),
    B(5, "B");

    private int id;
    private String title;

    HistogramItem(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static HistogramItem findById(int id) {
        for (HistogramItem op : HistogramItem.values()) {
            if (id == op.getId()) {
                return op;
            }
        }

        return EMPTY;
    }

    public static String[] getAsArray() {
        List<String> list = new ArrayList<>();
        Arrays.stream(HistogramItem.values()).forEach(op -> list.add(op.getTitle()));

        return list.stream().toArray(String[]::new);
    }
}
