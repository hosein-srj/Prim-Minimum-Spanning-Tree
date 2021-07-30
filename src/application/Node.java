package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Node {

    private String name;

    private List<Node> shortestPath = new LinkedList<>();

    private Integer distance = Integer.MAX_VALUE;

    ArrayList<Node> adjacent = new ArrayList<>();

    Map<Node, Integer> adjacentNodes = new HashMap<>();

    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public void addadjacent(Node destination) {
    	adjacent.add(destination);
    }

    public boolean isAdjacent(Node node){
    	for(Node nodeE : adjacent){
    		if(node.getName().equals(nodeE.getName()))
    			return true;
    	}
    	return false;
    }

    public Node(String name) {
        this.setName(name);
    }

	public List<Node> getShortestPath() {
		return shortestPath;
	}

	public void setShortestPath(List<Node> shortestPath) {
		this.shortestPath = shortestPath;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    public Map<Node, Integer> getAdjacentNodes(){
    	return adjacentNodes;
    }
    public String toString(){
    	return name;
    }
    // getters and setters
}