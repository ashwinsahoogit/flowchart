package com.proj.flowchart.service;

import com.proj.flowchart.entity.Flowchart;
import com.proj.flowchart.helper.Edge;
import com.proj.flowchart.repository.FlowchartRepository;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FlowchartService {
    private final FlowchartRepository flowchartRepository;

    public FlowchartService(FlowchartRepository flowchartRepository) {
        this.flowchartRepository = flowchartRepository;
    }

    public Flowchart createFlowchart(String id) {
        if (flowchartRepository.existsById(id)) {
            throw new IllegalArgumentException("Flowchart with ID " + id + " already exists.");
        }
        Flowchart flowchart = new Flowchart(id);
        return flowchartRepository.save(flowchart);
    }

    public Flowchart getFlowchart(String id) {
        return flowchartRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Flowchart not found."));
    }

    public void updateFlowchart(String id, Set<String> newNodes, Set<Edge> newEdges) {
        Flowchart flowchart = getFlowchart(id);
        flowchart.getNodes().addAll(newNodes);
        flowchart.getEdges().addAll(newEdges);
        flowchartRepository.save(flowchart);
    }

    public void deleteFlowchart(String id) {
        if (!flowchartRepository.existsById(id)) {
            throw new NoSuchElementException("Flowchart not found.");
        }
        flowchartRepository.deleteById(id);
    }

    public Set<String> getOutgoingEdges(String node, String flowchartId) {
        Flowchart flowchart = getFlowchart(flowchartId);
        Set<String> outgoingEdges = new HashSet<>();
        for (Edge edge : flowchart.getEdges()) {
            if (edge.getFrom().equals(node)) {
                outgoingEdges.add(edge.getTo());
            }
        }
        return outgoingEdges;
    }

    public Set<String> getAllConnectedNodes(String node, String flowchartId) {
        Flowchart flowchart = getFlowchart(flowchartId);
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(node);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (visited.add(current)) {
                for (Edge edge : flowchart.getEdges()) {
                    if (edge.getFrom().equals(current) && !visited.contains(edge.getTo())) {
                        queue.add(edge.getTo());
                    }
                }
            }
        }
        return visited;
    }
}
