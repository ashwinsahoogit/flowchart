package com.proj.flowchart.controller;

import java.util.Set;
import org.springframework.web.bind.annotation.*;
import com.proj.flowchart.entity.Flowchart;
import com.proj.flowchart.helper.Edge;
import com.proj.flowchart.service.FlowchartService;


@RestController
@RequestMapping("/api/flowcharts")
public class FlowchartController {
    private final FlowchartService flowchartService;

    public FlowchartController(FlowchartService flowchartService) {
        this.flowchartService = flowchartService;
    }

    @PostMapping
    public Flowchart createFlowchart(@RequestParam String id) {
        return flowchartService.createFlowchart(id);
    }

    @GetMapping("/{id}")
    public Flowchart getFlowchart(@PathVariable String id) {
        return flowchartService.getFlowchart(id);
    }

    @PutMapping("/{id}")
    public void updateFlowchart(
            @PathVariable String id,
            @RequestBody Set<String> nodes,
            @RequestBody Set<Edge> edges) {
        flowchartService.updateFlowchart(id, nodes, edges);
    }

    @DeleteMapping("/{id}")
    public void deleteFlowchart(@PathVariable String id) {
        flowchartService.deleteFlowchart(id);
    }

    @GetMapping("/{id}/outgoing/{node}")
    public Set<String> getOutgoingEdges(@PathVariable String id, @PathVariable String node) {
        return flowchartService.getOutgoingEdges(node, id);
    }

    @GetMapping("/{id}/connected/{node}")
    public Set<String> getAllConnectedNodes(@PathVariable String id, @PathVariable String node) {
        return flowchartService.getAllConnectedNodes(node, id);
    }
}
