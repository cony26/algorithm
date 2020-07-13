package astar;

import algorithm.algorithm_core.Node;

public class Heuristic {
    public static double euclideanDistance(Node aCurrentNode, Node oTargetNode){
        return aCurrentNode.getDistance(oTargetNode);
    }

    public static double manhattanDistance(Node aCurrentNode, Node oTargetNode){
        return Math.abs(aCurrentNode.getPosition().oX - oTargetNode.getPosition().oX)
                + Math.abs(aCurrentNode.getPosition().oY - oTargetNode.getPosition().oY);
    }

}
