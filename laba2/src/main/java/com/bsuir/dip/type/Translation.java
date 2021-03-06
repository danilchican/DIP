package com.bsuir.dip.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Translation {

    EMPTY(0, ""),
    COLORIZE(1, "Colorize image"),
    CLASTERIZE(2, "Clasterize"),
    BIN_PREPARING(3, "Binary Preparing"),
    PREPARING(4, "Preparing"),
    SOBIEL(5, "Sobiel operator");

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
