package algorithm.algorithm_core;

import java.util.List;

public class Helper {
    public static void sleepForMoment(int aTime){
        try{
            Thread.sleep(aTime);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void printNodeDetails(List<Node> aNodes){
        for(Node node : aNodes) {
            node.printPositions();
            System.out.println("connected nodes : " + node.getConnectedNodesNumber());
        }
    }

    public static void printCalculatedPath(){
        System.out.println(Node.END_NODE.showRoot());
    }
}
