package com.bsuir.dip.type;

import com.bsuir.dip.action.Action;
import com.bsuir.dip.action.TranslationAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Option {

    VIEW_IMAGE(0, "", null),
    TRANSLATION(1, "Translate Image", new TranslationAction());

    private int id;
    private String title;
    private Action handler;

    Option(int id, String title, Action handler) {
        this.id = id;
        this.title = title;
        this.handler = handler;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Action getAction() {
        return handler;
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
