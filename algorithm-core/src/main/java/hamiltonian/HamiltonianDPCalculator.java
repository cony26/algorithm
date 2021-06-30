package hamiltonian;

import algorithm.algorithm_core.*;
import groovy.util.PermutationGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.min;

public class HamiltonianDPCalculator {
    private final int oNodeNumber;
    private final Presenter oPresenter;
    private final List<Node> oNodes;
    private int[][] g;
    private int[][] dp;
    private static int INF = 1000000;
    HamiltonianDPCalculator(int aNodeNumber){
        oNodeNumber = aNodeNumber;
        g = new int[oNodeNumber][oNodeNumber];
        dp = new int[1 << oNodeNumber][oNodeNumber];

        List<Position> positions = Position.createRandomPositions(oNodeNumber);
        oNodes = Node.createNodes(positions, Node.ConnectionCreator.ALL);
        oPresenter = SwingPresenter.createPresenter(oNodes, false);
        oPresenter.setColorProvider(new ColorProvider(){});
    }

    public void launch(){
        //initialize
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < oNodeNumber; i++){
            for(int j = 0; j < oNodeNumber; j++){
                g[i][j] = (int)oNodes.get(i).getDistance(oNodes.get(j));
                builder.append(String.format("%3d", g[i][j])).append(",");
            }
            builder.append("\n");
        }
        System.out.println("g[i][j]");
        System.out.println(builder.toString());

        int min = INF;
        int endIndex = -1;
        for(int i = 0; i < oNodeNumber; i++){
            int mask = (1 << oNodeNumber) - 1;
            dp[mask][i] = recursiveSearchMinimum(mask, i);
            if(min > dp[mask][i]){
                min = dp[mask][i];
                endIndex = i;
            }
        }

        System.out.println("shortest hamiltonian path cost:" + min);

        //caution:this shortestList requires to be reversed
        List<Node> shortestList = new ArrayList<>();
        shortestList.add(oNodes.get(endIndex));
        int mask = (1 << oNodeNumber) - 1;
        while(true){
            for(int k = 0; k < oNodeNumber; k++){
                if(k == endIndex){
                    continue;
                }
//                System.out.println("endIndex:" + k);
//                System.out.println("dp[(1 << oNodeNumber) - 1][endIndex]:" + dp[(1 << oNodeNumber) - 1][endIndex]);
//                System.out.println("dp[((1 << oNodeNumber) - 1) ^ (1 << endIndex)][k]:" + dp[((1 << oNodeNumber) - 1) ^ (1 << endIndex)][k] + ", g[k][endIndex]:" + g[k][endIndex]);
                if(dp[mask][endIndex] == (dp[mask ^ (1 << endIndex)][k] + g[k][endIndex])){
//                    System.out.println("updated");
                    mask = mask & ~(1 << endIndex);
                    endIndex = k;
                    shortestList.add(oNodes.get(endIndex));
                    break;
                }
            }

            if(shortestList.size() == oNodeNumber){
                break;
            }
        }

        Collections.reverse(shortestList);

        int length = 0;
        builder.setLength(0);
        for(int i = 0; i < shortestList.size(); i++){
            builder.append("[").append(shortestList.get(i).getId()).append("],");
            if(i < shortestList.size() - 1){
                length += shortestList.get(i).getDistance(shortestList.get(i+1));
            }
        }
        System.out.println("shortest path:" + builder.toString() + " cost:" + length);

        oPresenter.highLightPathPlan(shortestList);

        permutationCheck(g);
    }

    /**
     *
     * @param mask
     * @param i
     * @return
     */
    private int recursiveSearchMinimum(int mask, int i){
//        System.out.println("mask:" + Integer.toBinaryString(mask) + ",i:" + i);

        //mask has only i
//        System.out.println("mask >> i : " + Integer.toBinaryString(mask >> i) + ", mask & ~(1 << i):" + Integer.toBinaryString(mask & ~(1 << i)));
        if(((mask >> i) & 1) == 1 && (mask & ~(1 << i)) == 0){
//            System.out.println("mask has only i:" + i);
            return 0;
        }

        //mask doesn't include i
        if(((mask >> i) & 1) == 0){
//            System.out.println("mask doesn't include i:" + i);
            return INF;
        }

        //if already calculated
        if(dp[mask][i] != 0){
//            System.out.println("dp[mask][i]:" + dp[mask][i] + " is already calculated");
            return dp[mask][i];
        }

        int min = INF;
        for(int j = 0; j < oNodeNumber; j++){
            if(mask >> j == 0){
                //do nothing
            }else{
                min = min(min, recursiveSearchMinimum(mask ^ (1 << i), j) + g[j][i]);
            }
        }
        //update
        dp[mask][i] = min;

        return min;
    }

    private void permutationCheck(int[][] g){
        new Thread(){
            @Override
            public void run() {
                List<Integer> list = new ArrayList<>();
                for(int i = 0; i < oNodeNumber; i++){
                    list.add(i);
                }
                PermutationGenerator<Integer> permutationGenerator = new PermutationGenerator<>(list);

                int min = INF;
                while(permutationGenerator.hasNext()){
                    List<Integer> candidate = permutationGenerator.next();
                    int cost = 0;
                    for(int i = 0; i < candidate.size() - 1; i++){
                        cost += g[candidate.get(i)][candidate.get(i+1)];
                    }
                    if(cost < min){
                        min = cost;
                    }
                }
                System.out.println("permutationCheck:" + min);
            }
        }.start();
    }

}
