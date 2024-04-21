package com.example.automata.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Node {
    private int id;
    private String group;
    private String label;
    private int x;
    private int y;

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("group")
    public String getGroup() {
        return group;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("x")
    public int getX() {
        return x;
    }

    @JsonProperty("y")
    public int getY() {
        return y;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("group")
    public void setGroup(String group) {
        this.group = group;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("x")
    public void setX(int x) {
        this.x = x;
    }

    @JsonProperty("y")
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", group='" + group + '\'' +
                ", label='" + label + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
