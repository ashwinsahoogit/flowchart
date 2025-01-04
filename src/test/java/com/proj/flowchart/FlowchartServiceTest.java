package com.proj.flowchart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.proj.flowchart.entity.Flowchart;
import com.proj.flowchart.service.FlowchartService;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

class FlowchartServiceTest {
    @Mock
    private FlowchartRepository flowchartRepository;

    @InjectMocks
    private FlowchartService flowchartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createFlowchart_shouldCreateFlowchartSuccessfully() {
        String flowchartId = "flowchart1";

        when(flowchartRepository.existsById(flowchartId)).thenReturn(false);
        when(flowchartRepository.save(any(Flowchart.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Flowchart flowchart = flowchartService.createFlowchart(flowchartId);

        assertNotNull(flowchart);
        assertEquals(flowchartId, flowchart.getId());
        verify(flowchartRepository, times(1)).save(flowchart);
    }

    @Test
    void getFlowchart_shouldReturnFlowchartIfExists() {
        String flowchartId = "flowchart1";
        Flowchart flowchart = new Flowchart(flowchartId);

        when(flowchartRepository.findById(flowchartId)).thenReturn(Optional.of(flowchart));

        Flowchart result = flowchartService.getFlowchart(flowchartId);

        assertNotNull(result);
        assertEquals(flowchartId, result.getId());
        verify(flowchartRepository, times(1)).findById(flowchartId);
    }

    @Test
    void getFlowchart_shouldThrowExceptionIfNotFound() {
        String flowchartId = "flowchart1";

        when(flowchartRepository.findById(flowchartId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> flowchartService.getFlowchart(flowchartId));
        verify(flowchartRepository, times(1)).findById(flowchartId);
    }

    @Test
    void updateFlowchart_shouldAddNodesAndEdgesSuccessfully() {
        String flowchartId = "flowchart1";
        Flowchart flowchart = new Flowchart(flowchartId);

        when(flowchartRepository.findById(flowchartId)).thenReturn(Optional.of(flowchart));

        Set<String> newNodes = Set.of("Node1", "Node2");
        Set<Edge> newEdges = Set.of(new Edge("Node1", "Node2"));

        flowchartService.updateFlowchart(flowchartId, newNodes, newEdges);

        assertTrue(flowchart.getNodes().containsAll(newNodes));
        assertTrue(flowchart.getEdges().containsAll(newEdges));
        verify(flowchartRepository, times(1)).save(flowchart);
    }

    @Test
    void deleteFlowchart_shouldDeleteSuccessfully() {
        String flowchartId = "flowchart1";

        when(flowchartRepository.existsById(flowchartId)).thenReturn(true);

        flowchartService.deleteFlowchart(flowchartId);

        verify(flowchartRepository, times(1)).deleteById(flowchartId);
    }

    @Test
    void deleteFlowchart_shouldThrowExceptionIfNotFound() {
        String flowchartId = "flowchart1";

        when(flowchartRepository.existsById(flowchartId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> flowchartService.deleteFlowchart(flowchartId));
        verify(flowchartRepository, never()).deleteById(flowchartId);
    }

    @Test
    void getOutgoingEdges_shouldReturnOutgoingEdgesForNode() {
        String flowchartId = "flowchart1";
        Flowchart flowchart = new Flowchart(flowchartId);
        flowchart.getEdges().add(new Edge("Node1", "Node2"));
        flowchart.getEdges().add(new Edge("Node1", "Node3"));

        when(flowchartRepository.findById(flowchartId)).thenReturn(Optional.of(flowchart));

        Set<String> outgoingEdges = flowchartService.getOutgoingEdges("Node1", flowchartId);

        assertEquals(2, outgoingEdges.size());
        assertTrue(outgoingEdges.contains("Node2"));
        assertTrue(outgoingEdges.contains("Node3"));
    }

    @Test
    void getAllConnectedNodes_shouldReturnAllConnectedNodes() {
        String flowchartId = "flowchart1";
        Flowchart flowchart = new Flowchart(flowchartId);
        flowchart.getNodes().addAll(Set.of("Node1", "Node2", "Node3", "Node4"));
        flowchart.getEdges().add(new Edge("Node1", "Node2"));
        flowchart.getEdges().add(new Edge("Node2", "Node3"));
        flowchart.getEdges().add(new Edge("Node3", "Node4"));

        when(flowchartRepository.findById(flowchartId)).thenReturn(Optional.of(flowchart));

        Set<String> connectedNodes = flowchartService.getAllConnectedNodes("Node1", flowchartId);

        assertEquals(4, connectedNodes.size());
        assertTrue(connectedNodes.containsAll(Set.of("Node1", "Node2", "Node3", "Node4")));
    }
}
