package com.example.automata.application.dto;

import java.util.List;

public class AutomataRequestBody {
    private List<Node> nodes;
    private List<Edge> edges;

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }
}

