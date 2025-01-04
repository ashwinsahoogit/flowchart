package com.proj.flowchart.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

import com.proj.flowchart.helper.Edge;

@Entity
@Table(name = "flowcharts")
public class Flowchart {
    @Id
    private String id;

    @ElementCollection
    @CollectionTable(name = "flowchart_nodes", joinColumns = @JoinColumn(name = "flowchart_id"))
    @Column(name = "node")
    private Set<String> nodes = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "flowchart_id")
    private Set<Edge> edges = new HashSet<>();

    // Constructors, Getters, and Setters
    public Flowchart() {}

    public Flowchart(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getNodes() {
        return nodes;
    }

    public void setNodes(Set<String> nodes) {
        this.nodes = nodes;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public void setEdges(Set<Edge> edges) {
        this.edges = edges;
    }
}
