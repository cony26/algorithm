package hamiltonian;

import algorithm.algorithm_core.*;

import java.awt.*;
import java.util.List;

public class HamiltonianDPCalculator {
    private final int oNodeNumber;
    private final Presenter oPresenter;
    HamiltonianDPCalculator(int aNodeNumber){
        oNodeNumber = aNodeNumber;
        List<Position> positions = Position.createRandomPositions(oNodeNumber);
        List<Node> nodes = Node.createNodes(positions, Node.ConnectionCreator.ALL);
        oPresenter = SwingPresenter.createPresenter(nodes, false);
        oPresenter.setColorProvider(new ColorProvider(){});
        oPresenter.highLightPathPlan(nodes);
    }

    public void launch(){

    }


}
