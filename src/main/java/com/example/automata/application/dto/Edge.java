package com.example.automata.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Edge {
    private int from;
    private int to;
    private String label;
    private String id;

    @JsonProperty("from")
    public int getFrom() {
        return from;
    }

    @JsonProperty("to")
    public int getTo() {
        return to;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("from")
    public void setFrom(int from) {
        this.from = from;
    }

    @JsonProperty("to")
    public void setTo(int to) {
        this.to = to;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "from=" + from +
                ", to=" + to +
                ", label='" + label + '\'' +
                '}';
    }
}
