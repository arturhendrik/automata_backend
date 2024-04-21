package com.example.automata.application.utils;

import com.example.automata.application.dto.AutomataRequestBody;
import com.example.automata.application.dto.Edge;
import com.example.automata.application.dto.Node;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.StatePair;
import dk.brics.automaton.Transition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class ControllerUtils {
    public static Automaton convertToAutomaton(AutomataRequestBody automataRequestBody, boolean isDFA) {
        Automaton automaton = new Automaton();
        Map<Integer, State> states = new HashMap<>();
        Set<StatePair> epsilons = new HashSet<>();

        List<Node> nodes = automataRequestBody.getNodes();
        for (Node node : nodes) {
            State state = new State();
            if (node.getGroup().equals("Final") || node.getGroup().equals("Initial_Final")) {
                state.setAccept(true);
            }
            if (node.getGroup().equals("Initial") || node.getGroup().equals("Initial_Final")) {
                automaton.setInitialState(state);
            }
            states.put(node.getId(), state);
        }

        List<Edge> edges = automataRequestBody.getEdges();
        for (Edge edge : edges) {
            String[] labelSplit = edge.getLabel().split("; ");
            for (String label : labelSplit) {
                State fromState = states.get(edge.getFrom());
                State toState = states.get(edge.getTo());
                if (label.equals("λ")) {
                    epsilons.add(new StatePair(fromState, toState));
                } else if (label.length() == 1) {
                    fromState.addTransition(new Transition(label.charAt(0), toState));
                } else {
                    throw new IllegalArgumentException("Multichar transition label");
                }
            }
        }
        automaton.addEpsilons(epsilons);
        automaton.restoreInvariant();
        automaton.setDeterministic(isDFA);
        return automaton;
    }
    public static List<Edge> updateEdgeLabels(List<Edge> edges) {
        Map<String, Edge> mergedMap = new HashMap<>();
        for (Edge edge : edges) {
            String key = edge.getFrom() + "-" + edge.getTo();
            Edge mergedEdge = mergedMap.get(key);
            if (mergedEdge == null) {
                mergedMap.put(key, edge);
            } else {
                mergedEdge.setLabel(mergedEdge.getLabel() + "; " + edge.getLabel());
            }
        }
        return new ArrayList<>(mergedMap.values());
    }
    public static void getNodesAndEdgesForFrontend(List<Edge> edges, List<Node> nodes, Automaton automaton, boolean isDFA) {
        int edgeId = 0;
        int x = 0;
        int y = 0;
        int stateNumber = 0;
        State initialState = automaton.getInitialState();

        Set<State> stateSet = automaton.getStates();
        List<State> stateList = new ArrayList<>(stateSet);
        if (isDFA) {
            Collections.sort(stateList);
        } else {
            Collections.sort(stateList, Collections.reverseOrder());
        }

        for (State a : stateList) {
            Node node = new Node();
            String group = "Normal";
            if (a.equals(initialState) && a.isAccept()) {
                group = "Initial_Final";
            }
            else if (a.equals(initialState)) {
                group = "Initial";
            }
            else if (a.isAccept()) {
                group = "Final";
            }
            node.setGroup(group);
            String[] split = a.toString().split(" ");
            int id = Integer.parseInt(split[1]);
            Set<Transition> transitions = a.getTransitions();
            node.setId(id);
            node.setLabel("q"+id);
            node.setY(y);
            node.setX(x);
            if (stateNumber % 2 == 0) {
                x += 150;
                y += 50;
            } else {
                x += 50;
                y += 150;
            }
            stateNumber++;
            nodes.add(node);
            for (Transition t : transitions) {
                String[] splitTransition = t.toString().split(" ");
                String label = splitTransition[0];
                Edge edge = new Edge();
                edge.setFrom(id);
                edge.setTo(Integer.parseInt(splitTransition[2]));
                if (label.contains("-")) {
                    char startChar = label.charAt(0);
                    char endChar = label.charAt(2);

                    for (char ch = startChar; ch <= endChar; ch++) {
                        Edge newEdge = new Edge();
                        newEdge.setFrom(edge.getFrom());
                        newEdge.setTo(edge.getTo());
                        newEdge.setId(String.valueOf(edgeId));
                        newEdge.setLabel(String.valueOf(ch));
                        edges.add(newEdge);
                        edgeId++;
                    }
                } else {
                    edge.setId(String.valueOf(edgeId));
                    edge.setLabel(splitTransition[0]);
                    edges.add(edge);
                    edgeId++;
                }
            }
        }
    }
    public static ResponseEntity<?> getResponseEntity(Automaton automaton, boolean isDFA) {
        AutomataRequestBody dataForFrontend = new AutomataRequestBody();
        List<Edge> edges = new ArrayList<>();
        List<Node> nodes = new ArrayList<>();
        getNodesAndEdgesForFrontend(edges, nodes, automaton, isDFA);

        List<Edge> newEdges = updateEdgeLabels(edges);
        dataForFrontend.setEdges(newEdges);
        dataForFrontend.setNodes(nodes);

        return new ResponseEntity<>(dataForFrontend, HttpStatus.OK);
    }
}
