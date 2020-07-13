package algorithm.algorithm_core;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SwingPresenter extends JFrame implements Presenter {

    private final PlotCanvas oCanvas;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int MARGIN = 50;

    private SwingPresenter(List<Node> aNodes){
        oCanvas = new PlotCanvas(aNodes);
        oCanvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        oCanvas.setBackground(Color.WHITE);
        getContentPane().add(oCanvas);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(WIDTH + MARGIN,HEIGHT + MARGIN));
        setResizable(false);
        setVisible(true);
    }
    public static Presenter createPresenter(List<Node> aNodes){
        return new SwingPresenter(aNodes);
    }

    @Override
    public void updatePanel(List<Node> aNodes) {
        oCanvas.repaint();
    }

    @Override
    public void highLightPathPlan() {
        List<Node> list = new ArrayList<>();

        Node node = Node.END_NODE;
        while(node != null){
            list.add(node);
            node = node.getPrev();
        }
        oCanvas.highLightPathPlan(list);
    }

    private class PlotCanvas extends Canvas{
        private final Color BLUE = new Color( 55, 170, 200);
        private final Color RED = new Color(200,  80,  75);
        private final Color BLACK = new Color(0,  0,  0);
        //Coefficient unit is %
        private final int X_COEFFICIENT = 100 * SwingPresenter.WIDTH / Position.X_RANGE;
        private final int Y_COEFFICIENT = 100 * SwingPresenter.HEIGHT / Position.Y_RANGE;
        private final int radius = 6;
        private final List<Node> oNodes;
        private List<Node> oPathList = new ArrayList<>();
        private boolean isPaintLine = true;
        Map<Node, List<Node>> oLineMap = new HashMap<>();

        public PlotCanvas(List<Node> aNodes){
            oNodes = aNodes;
            createLineData(aNodes);
        }

        @Override
        public void paint(Graphics g){
            paintPoint(g);
            if(isPaintLine) {
                paintString(g);
                paintLine(g);
                isPaintLine = false;
            }
        }

        @Override
        public void update(Graphics g){
            paint(g);
        }

        public void highLightPathPlan(List<Node> aPathList){
            oPathList.addAll(aPathList);
            isPaintLine = true;
            repaint();
        }

        private void paintPoint(Graphics g){
            for(Node node : oNodes){
//                if(node.isAlive()){
//                    g.setColor(BLUE);
//                }else{
//                    g.setColor(RED);
//                }
                if(node.getStatus() == Node.States.NONE){
                    g.setColor(BLACK);
                }else if(node.getStatus() == Node.States.CLOSE){
                    g.setColor(RED);
                }else{
                    g.setColor(BLUE);
                }
                g.fillOval(convertX(node.getPosition().oX),
                        convertY(node.getPosition().oY),
                        radius * 2, radius * 2);
            }
        }

        private void paintString(Graphics g){
            for(Node node : oNodes){
                g.setColor(BLACK);
                g.drawString(Integer.toString(node.getId()),
                        convertX(node.getPosition().oX),
                        convertY(node.getPosition().oY));
            }
        }

        private void paintLine(Graphics g){
            g.setColor(BLACK);
            for(Node node : oNodes){
                for(Node connectedNode : oLineMap.get(node)){
                    g.drawLine(convertX(node.getPosition().oX) + radius,
                            convertY(node.getPosition().oY) + radius,
                            convertX(connectedNode.getPosition().oX) + radius,
                            convertY(connectedNode.getPosition().oY) + radius);
                }
            }

            if(!oPathList.isEmpty()){
                g.setColor(BLUE);
                for(int i = 0; i < oPathList.size() - 1; i++){
                    Graphics2D g2 = (Graphics2D)g;
                    g2.setStroke(new BasicStroke(5));
                    g2.drawLine(convertX(oPathList.get(i).getPosition().oX) + radius,
                            convertY(oPathList.get(i).getPosition().oY) + radius,
                            convertX(oPathList.get(i+1).getPosition().oX) + radius,
                            convertY(oPathList.get(i+1).getPosition().oY) + radius);
                }
            }
        }

        private void createLineData(List<Node> aNodes){
            for(Node node : aNodes){
                oLineMap.put(node, new ArrayList<Node>(node.getConnectedNodes()));
            }

            for(Node node : aNodes){
                for(Node connectedNode : oLineMap.get(node)){
                    if(oLineMap.get(connectedNode).contains(node)){
                        oLineMap.get(connectedNode).remove(node);
                    }
                }
            }
        }

        private int convertX(int aX){
            return aX * X_COEFFICIENT / 100;
        }

        private int convertY(int aY){
            return SwingPresenter.HEIGHT - aY * Y_COEFFICIENT / 100;
        }
    }
}
