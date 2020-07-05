package dijkstra;

import org.omg.CORBA.NO_IMPLEMENT;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.exit;

public class Dijkstra {
	private final List<Node> oNodes;
	private final List<Node> oCandidates;
	private Node oNextNode = null;
	private final Presenter oPresenter;
	private int count = 0;
	
	public Dijkstra() {
		List<Position> positions = Position.createPositions();
		oNodes = Node.createNodes(positions);
		createConnection();
		oCandidates = new ArrayList<>(oNodes);
		Node.START_NODE.setCost(0);
		for(Node node : oNodes) {
			node.printPositions();
			System.out.println(node.getConnectedNodesNumber());
		}
		oPresenter = Presenter.createPresenter(oNodes);
	}
	
	public void launch() {
		while(isContinue()) {
			process();
			count++;
//			System.out.println("oCandidates.size : " + oCandidates.size());
//			System.out.println("oNextNode : " + oNextNode);
		}
		
		System.out.println(Node.END_NODE.showRoot());
	}
	
	private boolean isContinue() {
		if(oCandidates.size() == 0) {
			return false;
		}
		return true;
	}
	
	private void process() {
		update();
		if(count != 0 && oNextNode.getConnectedNodes().stream().allMatch(node -> node.getCost() == Node.INITIAL_COST)){
			System.out.println("not reached");
			exit(0);
		}
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
		oNextNode.setIsAlive(false);
		oCandidates.remove(oNextNode);
		oPresenter.updatePanel(oNodes);
		sleepForMoment(100);

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

	private void sleepForMoment(int aTime){
		try{
			Thread.sleep(aTime);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}	