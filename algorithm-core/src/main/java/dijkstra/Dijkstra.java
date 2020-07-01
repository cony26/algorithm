package dijkstra;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Dijkstra {
	private final List<Node> oNodes;
	private final List<Node> oCandidates;
	private Node oNextNode = null;
	
	public Dijkstra() {
		List<Position> positions = Position.createPositions();
		oNodes = Node.createNodes(positions);
		createConnection();
		oCandidates = new ArrayList<>(oNodes);
		oNodes.get(0).setCost(0);
		for(Node node : oNodes) {
			node.printPositions();
			System.out.println(node.getConnectedNodesNumber());
		}
	}
	
	public void launch() {
		while(isContinue()) {
			process();
//			System.out.println("oCandidates.size : " + oCandidates.size());
//			System.out.println("oNextNode : " + oNextNode);
		}
		
		System.out.println(oNodes.get(6).showRoot());
	}
	
	private boolean isContinue() {
		if(oCandidates.size() == 0) {
			return false;
		}
		return true;
	}
	
	private void process() {
		update();
		for(Node node : oNextNode.getConnectedNodes()) {
			if (node.getCost() > oNextNode.getCost() + node.getDistance(oNextNode)) {
				node.setCost(oNextNode.getCost() + node.getDistance(oNextNode));
				node.setPrev(oNextNode);
				System.out.println(node + " is updated");
			} else {
				System.out.println(node + " is NOT updated");
			}
		}
	}
	
	private void update() {
		System.out.println("oCandidates:" + oCandidates);
		oNextNode = oCandidates.stream().min(Node::compareTo).get();
		oCandidates.remove(oNextNode);
	}
	
	private void createConnection() {
		for(Node node : oNodes) {
			node.setConnectedNodes(
					oNodes.stream().filter(n -> n.isWithinRange(node)).collect(Collectors.toList())
					);
			if(node.getConnectedNodesNumber() == 0) {
				node.setMinimumDistanceNode(oNodes);
			}
		}
	}
}	