package com.proj.flowchart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proj.flowchart.entity.Flowchart;

@Repository
public interface FlowchartRepository extends JpaRepository<Flowchart, String> {
}
