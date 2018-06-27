package com.tcv.peliculas.model;

public class ChildRow {
    private int icon;
    private String text;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ChildRow(int icon, String text) {
        this.icon = icon;
        this.text = text;
    }
}
