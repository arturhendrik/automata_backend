package com.example.automata.application.utils;

import com.example.automata.application.dto.AutomataRequestBody;
import com.example.automata.application.dto.Edge;
import com.example.automata.application.dto.Node;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.StatePair;
import dk.brics.automaton.Transition;

import java.util.*;

public class ControllerUtils {
    public static Automaton convertToAutomaton(AutomataRequestBody automataRequestBody) {
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
        automaton.setDeterministic(false);
        return automaton;
    }
}
