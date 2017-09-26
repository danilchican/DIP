package com.bsuir.dip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Option {

    VIEW_IMAGE(0, "View Image"),
    VIEW_HIST(1, "View Histogram"),
    TRANSLATION(2, "Translate"),
    FILTERING(3, "Filter");

    private int id;
    private String title;

    Option(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static Option findById(int id) {
        for (Option op : Option.values()) {
            if (id == op.getId()) {
                return op;
            }
        }

        return VIEW_IMAGE;
    }

    public static String[] getAsArray() {
        List<String> list = new ArrayList<>();
        Arrays.stream(Option.values()).forEach(op -> list.add(op.getTitle()));

        return list.stream().toArray(String[]::new);
    }
}
