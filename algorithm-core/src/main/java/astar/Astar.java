package astar;

import algorithm.algorithm_core.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static algorithm.algorithm_core.Helper.sleepForMoment;

public class Astar {
    private final List<Node> oNodes;
    private final List<Node> oOpenList;
    private final List<Node> oCloseList;
    private Node oNextNode = null;
    private final Presenter oPresenter;
    private int count = 0;

    public Astar(){
        List<Position> positions = Position.createRandomPositions(Node.NODE_NUMBER);
        oNodes = Node.createNodes(positions, Node.ConnectionCreator.RANDOM);
        oOpenList = new ArrayList<>();
        oOpenList.add(Node.START_NODE);
        Node.START_NODE.setStatus(Node.States.OPEN);
        oCloseList = new ArrayList<>();
        Helper.printNodeDetails(oNodes);
        oPresenter = SwingPresenter.createPresenter(oNodes, true);
        oPresenter.setColorProvider(new ColorProvider() {
            @Override
            public void setPointColor(Graphics g, Node aNode) {
                if(aNode.getStatus() == Node.States.NONE){
                    g.setColor(BLACK);
                }else if(aNode.getStatus() == Node.States.CLOSE){
                    g.setColor(RED);
                }else{
                    g.setColor(BLUE);
                }
            }
        });
    }

    public void launch(){
        while(isContinue()) {
            process();
            count++;
        }

        Helper.printCalculatedPath();
    }

    private boolean isContinue(){
        if(oOpenList.size() == 0){
            System.out.println("not reached");
            return false;
        }

//        oOpenList.sort(Node::compareTo);

        oNextNode = getNextNode();
        if(oNextNode.equals(Node.END_NODE)){
            oPresenter.updatePanel(oNodes);
            oPresenter.highLightPathPlan();
            return false;
        }

        return true;
    }

    private void process(){
        oOpenList.remove(oNextNode);
        oCloseList.add(oNextNode);
        oNextNode.setStatus(Node.States.CLOSE);

        for(Node node : oNextNode.getConnectedNodes()){
            double estimate = getEstimateCost(oNextNode, node);
            if(!oOpenList.contains(node) && !oCloseList.contains(node)){
                node.setCost(estimate);
                oOpenList.add(node);
                node.setPrev(oNextNode);
                node.setStatus(Node.States.OPEN);
            }else if(oOpenList.contains(node)) {
                double prev_cost = node.getCost();
                if (estimate < prev_cost) {
                    node.setCost(estimate);
//                    oOpenList.remove(node);
                    node.setPrev(oNextNode);
                }
            }else if(oCloseList.contains(node)){
                double prev_cost = node.getCost();
                if (estimate < prev_cost) {
                    node.setCost(estimate);
                    oCloseList.remove(node);
                    oOpenList.add(node);
                    node.setPrev(oNextNode);
                    node.setStatus(Node.States.OPEN);
                }
            }
        }
        oPresenter.updatePanel(oNodes);
        Helper.sleepForMoment(100);
    }

    private double getHeulisticEstimate(Node node){
        return Heuristic.euclideanDistance(node, Node.END_NODE);
    }

    private double getGEstimate(Node node){
        return node.getCost() - getHeulisticEstimate(node);
    }

    private double getEstimateCost(Node aNode, Node aConnectedNode){
        return getGEstimate(aNode) + aNode.getDistance(aConnectedNode) + getHeulisticEstimate(aConnectedNode);
    }

    private Node getNextNode(){
        return oOpenList.stream().min(Node::compareTo).get();
    }

    public static void main(String[] args){
        Astar astar = new Astar();
        astar.launch();
    }
}
