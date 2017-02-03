package com.project.extendedsat.deployment.db;

import org.neo4j.graphdb.RelationshipType;

public enum RelationshipTypes implements RelationshipType {
    RUNNING,
    HAS_IP,
    OPEN_PORT,
    CONNECTED_TO;
}