package algorithm.algorithm_core;

import sun.management.snmp.jvmmib.EnumJvmMemManagerState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Node implements Comparable<Node>{
	public static final int NODE_NUMBER = 450;
	private static final int CONNECTED_LENGTH = 15;
	public static final int INITIAL_COST = 1000;
	public static enum States{
		NONE,
		CLOSE,
		OPEN;
	}
	public static final Node START_NODE = new Node(0, Position.START){
		@Override
		public String toString(){return "START_NODE";}
	};
	public static final Node END_NODE = new Node(NODE_NUMBER - 1, Position.END){
		@Override
		public String toString(){return "END_NODE";}
	};
	private boolean oIsAlive = true;
	private States oStatus = States.NONE;
	private boolean oIsVisited;
	private final int oId;
	private final Position oPosition;
	private Node oPrev = null;
	private double oCost = INITIAL_COST;
	private List<Node> oConnectedNodes = new ArrayList<>();
	
	private Node(int aId, Position aPosition) {
		oId = aId;
		oPosition = aPosition;
	}
	
	public static List<Node> createNodes(List<Position> aPositions){
		List<Node> nodes = new ArrayList<>();
		nodes.add(START_NODE);
		for(int i = 1; i < NODE_NUMBER - 1; i++) {
			nodes.add(new Node(i, aPositions.get(i)));
		}
		nodes.add(END_NODE);

		createConnection(nodes);
		
		return nodes;
	}

	private static void createConnection(List<Node> aNodes) {
		for(Node node : aNodes) {
			node.setConnectedNodes(
					aNodes.stream().filter(n -> n.isWithinRange(node)).collect(Collectors.toList())
			);
			if(node.getConnectedNodesNumber() == 0) {
				node.setMinimumDistanceNode(aNodes);
			}
		}
	}
	
	@Override
	public String toString() {
		return "Node[" + Integer.toString(oId) + "]";
	}
	
	public int getId() {
		return oId;
	}
	
	public void setPrev(Node aNode) {
		oPrev = aNode;
	}
	
	public void setCost(double aCost) {
		oCost = aCost;
	}

	public Position getPosition(){
		return oPosition;
	}
	
	public double getCost() {
		return oCost;
	}
	
	public Node getPrev() {
		return oPrev;
	}

	public void setIsAlive(boolean aIsAlive){ oIsAlive = aIsAlive;}

	public boolean isAlive(){return oIsAlive;}

	public States getStatus(){
		return oStatus;
	}

	public void setStatus(States aState){
		oStatus = aState;
	}
	
	public String showRoot() {
		StringBuilder builder = new StringBuilder();
		builder.append(this);
		if(oPrev == null) {
			builder.append("");
		}else {
			builder.append("<-" + getPrev().showRoot());
		}
		return builder.toString();
	}
	
	public double getDistance(Node aNode) {
		return Math.sqrt((this.oPosition.oX - aNode.oPosition.oX) * (this.oPosition.oX - aNode.oPosition.oX) + (this.oPosition.oY - aNode.oPosition.oY) * (this.oPosition.oY - aNode.oPosition.oY));
	}
	
	public boolean isWithinRange(Node aNode) {
		if(aNode == this) {
			return false;
		}
		
		return getDistance(aNode) <= CONNECTED_LENGTH;
	}
	
	public void setConnectedNodes(List<Node> aConnectedNodes) {
		oConnectedNodes.addAll(aConnectedNodes);
	}
	
	public void setMinimumDistanceNode(List<Node> oNodes) {
		Node minDistanceNode = null;
		for(Node node : oNodes) {
			if(node.equals(this)){
				continue;
			}
			if(minDistanceNode == null) {
				minDistanceNode = node;
			}
			
			if(getDistance(node) < getDistance(minDistanceNode)) {
				minDistanceNode = node;
			}
		}
		oConnectedNodes.add(minDistanceNode);
		minDistanceNode.getConnectedNodes().add(this);
	}
	
	public int getConnectedNodesNumber() {
		return oConnectedNodes.size();
	}
	
	public List<Node> getConnectedNodes(){
		return oConnectedNodes;
	}
	
	@Override
	public int compareTo(Node node) {
		if(oCost > node.getCost())
			return 1;
		if(oCost < node.getCost())
			return -1;
		return 0;
	}
	
	public void printPositions() {
		System.out.println("(x,y) = (" + oPosition.oX + " , " + oPosition.oY + ")");
	}

	public static List<Node> candidatesConnectedRemovedNodes(List<Node> aCandidates, List<Node> aRemovedNodes) {
		List<Node> connectedNodes = new ArrayList<>();
		for (Node removedNode : aRemovedNodes) {
			connectedNodes.addAll(
					removedNode.getConnectedNodes().stream()
							.filter(node -> !connectedNodes.contains(node))
							.collect(Collectors.toList())
			);
		}
		return aCandidates.stream().filter(node -> connectedNodes.contains(node)).collect(Collectors.toList());
	}
}
