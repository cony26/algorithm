package dijkstra;

import algorithm.algorithm_core.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Dijkstra {
	private final List<Node> oNodes;
	private final List<Node> oCandidates;
	private final List<Node> oRemovedNodes;
	private Node oNextNode = null;
	private final Presenter oPresenter;
	private int count = 0;
	
	public Dijkstra() {
		List<Position> positions = Position.createRandomPositions(Node.NODE_NUMBER);
//		List<Position> positions = Position.createConstantPositions();
		oNodes = Node.createNodes(positions, Node.ConnectionCreator.RANDOM);
		oCandidates = new ArrayList<>(oNodes);
		oRemovedNodes = new ArrayList<>();
		Node.START_NODE.setCost(0);
		Helper.printNodeDetails(oNodes);
//		oPresenter = SwingPresenter.createPresenter(oNodes);
		oPresenter = GralPresenter.createPresenter(oNodes);
		oPresenter.setColorProvider(new ColorProvider() {
			@Override
			public void setPointColor(Graphics g, Node aNode) {
				if(aNode.isAlive()){
					g.setColor(BLUE);
				}else{
					g.setColor(RED);
				}
			}
		});
	}
	
	public void launch() {
		while(isContinue()) {
			process();
			count++;
		}
		
		Helper.printCalculatedPath();
	}
	
	private boolean isContinue() {
		System.out.println("oCandidates:" + oCandidates);
		if(oCandidates.size() == 0) {
			return false;
		}

		if(Node.END_NODE.getConnectedNodes().stream().noneMatch(node -> node.isAlive())){
			Node.END_NODE.setIsAlive(false);
			oPresenter.updatePanel(oNodes);
			oPresenter.highLightPathPlan();
			return false;
		}

		List<Node> connectedNodes = Node.candidatesConnectedRemovedNodes(oCandidates, oRemovedNodes);
		if(count != 0 && connectedNodes.size() == 0 && Node.END_NODE.getPrev() == null){
			System.out.println("not reached");
			return false;
		}

		if(count == 0) {
			oNextNode = oCandidates.stream().min(Node::compareTo).get();
		}else {
			oNextNode = connectedNodes.stream().min(Node::compareTo).get();
		}

		oNextNode.setIsAlive(false);
		oCandidates.remove(oNextNode);
		oRemovedNodes.add(oNextNode);
		return true;
	}
	
	private void process() {
		for(Node node : oNextNode.getConnectedNodes()) {
			if (node.getCost() > oNextNode.getCost() + node.getDistance(oNextNode)) {
				node.setCost(oNextNode.getCost() + node.getDistance(oNextNode));
				node.setPrev(oNextNode);
				System.out.println(node + " is updated");
			} else {
				System.out.println(node + " is NOT updated");
			}
		}

		oPresenter.updatePanel(oNodes);
		Helper.sleepForMoment(100);
	}
	

}	