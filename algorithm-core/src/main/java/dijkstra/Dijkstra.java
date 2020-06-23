package dijkstra;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Dijkstra {
	//Length between u and v equals to length[u][v]
	private final int[][] oLength = {
			{0, 7, 9, 0, 0,14},
			{7, 0,10,15, 0, 0},
			{9,10, 0,11, 0, 2},
			{0,15,11, 0, 6, 0},
			{0, 0, 0, 6, 0, 9},
			{14,0, 2, 0, 9, 0},
		};

	private final List<Node> oNodes = new ArrayList<Node>();
	private final List<Node> oCandidates = new ArrayList<Node>();
	private Node oNextNode = null;
	
	public void launch() {
		
		initialize(6);
		
		while(isContinue()) {
			process();
			System.out.println("oCandidates.size : " + oCandidates.size());
			System.out.println("oNextNode : " + oNextNode);
		}
		
		System.out.println(oNodes.get(4).showRoot());
	}
	
	private void initialize(int NodeNumber) {
		for(int i = 0; i < NodeNumber; i++) {
			Node node = new Node(i);
			oNodes.add(node);
			oCandidates.add(node);
		}
		
		oNodes.get(0).setCost(0);
	}
	
	private boolean isContinue() {
		if(oCandidates.size() == 0) {
			return false;
		}
		return true;
	}
	
	private void process() {
		update();
		for(Node node : getConnectedNodes()) {
			if(node.getCost() > oNextNode.getCost() + getLength(node, oNextNode)) {
				node.setCost(oNextNode.getCost() + getLength(node, oNextNode));
				node.setPrev(oNextNode);
			}
		}
	}
	
	private void update() {
		System.out.println("oCandidates:" + oCandidates);
		oNextNode = oCandidates.stream().min((nodeA,nodeB) -> nodeA.getCost() - nodeB.getCost()).get();
		oCandidates.remove(oNextNode);
	}
	
	private List<Node> getConnectedNodes(){
		List<Node> connectedNodes = new ArrayList<Node>();
		for(int i = 0; i < 6; i++) {
			if(oLength[oNextNode.getId()][i] != 0) {
				connectedNodes.add(oNodes.get(i));
			}
		}
		
		return connectedNodes;
	}
	
	private int getLength(Node a, Node b) {
		return oLength[a.getId()][b.getId()];
	}
}	