package dijkstra;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node>{
	public static final int NODE_NUMBER = 50;
	private static final int CONNECTED_LENGTH = 20;
	private boolean oIsAlive = true;
	private boolean oIsVisited;
	private final int oId;
	private final Position oPosition;
	private Node oPrev = null;
	private double oCost = 1000;
	private List<Node> oConnectedNodes = null;
	
	private Node(int aId, Position aPosition) {
		oId = aId;
		oPosition = aPosition;
	}
	
	public static List<Node> createNodes(List<Position> aPositions){
		List<Node> nodes = new ArrayList<>();
		for(int i = 0; i < NODE_NUMBER; i++) {
			nodes.add(new Node(i, aPositions.get(i)));
		}
		
		return nodes;
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
		oConnectedNodes = aConnectedNodes;
	}
	
	public void setMinimumDistanceNode(List<Node> oNodes) {
		Node minDistanceNode = null;
		for(Node node : oNodes) {
			if(minDistanceNode == null) {
				minDistanceNode = node;
			}
			
			if(getDistance(node) < getDistance(minDistanceNode)) {
				minDistanceNode = node;
			}
		}
		oConnectedNodes.add(minDistanceNode);
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
}
