package dijkstra;

import java.util.List;

public class Node {
	private boolean oIsAlive;
	private boolean oIsVisited;
	private final int oId;
	private Node oPrev = null;
	private int oCost = 1000;
	
	public Node(int aId) {
		oId = aId;
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
	
	public void setCost(int aCost) {
		oCost = aCost;
	}
	
	public int getCost() {
		return oCost;
	}
	
	public Node getPrev() {
		return oPrev;
	}
	
	public String showRoot() {
		StringBuilder builder = new StringBuilder();
		builder.append(this);
		if(getPrev() == null) {
			builder.append("<- START ");
		}else {
			builder.append("<-" + getPrev().showRoot());
		}
		return builder.toString();
	}
}
