package dijkstra;

import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.ui.InteractivePanel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Presenter extends JPanel {
    private static final Random random = new Random();
    private static final Color BLUE = new Color( 55, 170, 200);
    private static final Color RED = new Color(200,  80,  75);
    private static final Color BLACK = new Color(0,  0,  0);
    private static final int SAMPLE_COUNT = 1000;
    private final XYPlot oPlot;
    private final DataTable oDataAlive = new DataTable(Integer.class, Integer.class);
    private final DataTable oDataDead = new DataTable(Integer.class, Integer.class);
    private final List<DataTable> oLineDataTables;
    private final Map<Set<Node>, DataTable> oSetMap = new HashMap<>();

    private Presenter(List<Node> aNodes) {
        super(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);

        oLineDataTables = createLineDataTable(aNodes);
        oPlot = new XYPlot(getAllDataTableArray());
        setNodeToDataTable(aNodes);
        add(createPlot(), BorderLayout.CENTER);

        showInFrame();
    }

    public static Presenter createPresenter(List<Node> aNodes){
        return new Presenter(aNodes);
    }

    public void updatePanel(List<Node> aNodes){
        oDataAlive.clear();
        oDataDead.clear();
        setNodeToDataTable(aNodes);
        setRange(oPlot);
        repaint();
    }

    public JFrame showInFrame() {
        JFrame frame = new JFrame(getTitle());
        frame.getContentPane().add(this, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(getPreferredSize());
        frame.setVisible(true);
        return frame;
    }

    private String getTitle() {
        return "Node Plot";
    }

    private String getDescription() {
        return String.format("Node Plot", SAMPLE_COUNT);
    }

    private JPanel createPlot(){
        // Format plot
        oPlot.setInsets(new Insets2D.Double(20.0, 40.0, 40.0, 40.0));
        oPlot.getTitle().setText(getDescription());
        oPlot.getAxisRenderer(XYPlot.AXIS_X).setTickSpacing(10.0);
        oPlot.getAxisRenderer(XYPlot.AXIS_Y).setTickSpacing(10.0);
        // Format points
        LineRenderer lines = new DefaultLineRenderer2D();
        for(DataTable data : oLineDataTables){
            oPlot.setLineRenderers(data, lines);
            oPlot.getLineRenderers(data).get(0).setColor(BLACK);
            oPlot.getPointRenderers(data).get(0).setColor(Color.WHITE);
        }

        oPlot.getPointRenderers(oDataAlive).get(0).setColor(BLUE);
        oPlot.getPointRenderers(oDataDead).get(0).setColor(RED);
        setRange(oPlot);

        return new InteractivePanel(oPlot);
    }

    private List<DataTable> createLineDataTable(List<Node> aNodes){
        List<DataTable> lineDataTables = new ArrayList<>();
        Map<Node, List<Node>> map = new HashMap<>();
        for(Node node : aNodes){
            map.put(node, new ArrayList<Node>(node.getConnectedNodes()));
        }

        for(Node node : aNodes){
            for(Node connectedNode : map.get(node)){
                if(map.get(connectedNode).contains(node)){
                    map.get(connectedNode).remove(node);
                }
            }
        }

        for(Node node : aNodes){
            System.out.print("Map : " + node + " : [" );
            for(Node node2 : map.get(node)){
                System.out.print(node2 + " , ");
            }
            System.out.println("]");
        }

        for(Node node : aNodes){
            for(Node connectedNode : map.get(node)){
                DataTable connectedData = new DataTable(Integer.class, Integer.class);
                connectedData.add(node.getPosition().oX, node.getPosition().oY);
                connectedData.add(connectedNode.getPosition().oX, connectedNode.getPosition().oY);
                lineDataTables.add(connectedData);
                oSetMap.put(new HashSet<Node>(Arrays.asList(node, connectedNode)), connectedData);
            }
        }

        return lineDataTables;
    }

    private void setNodeToDataTable(List<Node> aNodes){
        for(Node node : aNodes){
            if(node.isAlive()) {
                oDataAlive.add(node.getPosition().oX, node.getPosition().oY);
            }else{
                oDataDead.add(node.getPosition().oX, node.getPosition().oY);
            }
        }
    }

    private DataTable[] getAllDataTableArray(){
        ArrayList<DataTable> allDataTableList = new ArrayList<>();
        allDataTableList.addAll(oLineDataTables);
        allDataTableList.add(oDataAlive);
        allDataTableList.add(oDataDead);
        DataTable[] allDataTableArray = new DataTable[allDataTableList.size()];
        allDataTableList.toArray(allDataTableArray);

        return allDataTableArray;
    }

    private void setRange(XYPlot aPlot){
        oPlot.getAxis(XYPlot.AXIS_X).setRange(-10,110);
        oPlot.getAxis(XYPlot.AXIS_Y).setRange(-10,110);
    }

    public void highLightLine(){
        List<Set<Node>> list = new ArrayList<>();

        Node node1 = Node.END_NODE;
        Node node2 = Node.END_NODE.getPrev();
        while(node2 != null){
            list.add(new HashSet<Node>(Arrays.asList(node1, node2)));
            node1 = node2;
            node2 = node2.getPrev();
        }

        LineRenderer lines = new DefaultLineRenderer2D();
        for(Set set : list) {
            oPlot.setLineRenderers(oSetMap.get(set), lines);
            oPlot.getLineRenderers(oSetMap.get(set)).get(0).setColor(BLUE);
        }

    }
}
