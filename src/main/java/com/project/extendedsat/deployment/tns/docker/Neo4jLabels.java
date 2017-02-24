/**
 * Namesh Sanjitha
 * Aug 30, 2016
 * DockerTraceability
 * com.tns.docker
 */
package com.project.extendedsat.deployment.tns.docker;

import org.neo4j.graphdb.Label;

/**
 * @author root
 *
 */
public enum Neo4jLabels implements Label{
	CONTAINER,
	PORT,
	FILE,
	SOFTWARE
}
