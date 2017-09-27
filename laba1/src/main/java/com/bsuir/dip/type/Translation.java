package com.bsuir.dip.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Translation {

    EMPTY(0, ""),
    GRAYSCALE(1, "GrayScale"),
    BIN_PREPARING(2, "Binary Preparing"),
    PREPARING(3, "Preparing"),
    SOBIEL(4, "Sobiel operator");

    private int id;
    private String title;

    Translation(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static Translation findById(int id) {
        for (Translation tr : Translation.values()) {
            if (id == tr.getId()) {
                return tr;
            }
        }

        return EMPTY;
    }

    public static String[] getAsArray() {
        List<String> list = new ArrayList<>();
        Arrays.stream(Translation.values()).forEach(tr -> list.add(tr.getTitle()));

        return list.stream().toArray(String[]::new);
    }
}
