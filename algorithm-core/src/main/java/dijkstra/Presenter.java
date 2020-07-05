package dijkstra;

import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.ui.InteractivePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
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

    private JFrame showInFrame() {
        JFrame frame = new JFrame(getTitle());
        frame.getContentPane().add(this, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(getPreferredSize());
        frame.setVisible(true);
        return frame;
    }

    private Presenter(List<Node> aNodes) {
        super(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);

        oLineDataTables = createLineDataTable(aNodes);
        setNodeToDataTable(aNodes);
        oPlot = new XYPlot(getAllDataTableArray());
        add(createPlot(), BorderLayout.CENTER);

        showInFrame();
        updatePanel(aNodes);
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
        }
        oPlot.getPointRenderers(oDataAlive).get(0).setColor(BLUE);
        oPlot.getPointRenderers(oDataDead).get(0).setColor(RED);

        setRange(oPlot);

        return new InteractivePanel(oPlot);
    }

    private List<DataTable> createLineDataTable(List<Node> aNodes){
        List<DataTable> lineDataTables = new ArrayList<>();

        for(Node node : aNodes){
            DataTable connectedData = new DataTable(Integer.class, Integer.class);
            connectedData.add(node.getPosition().oX, node.getPosition().oY);
            for(Node connectedNode : node.getConnectedNodes()){
                connectedData.add(connectedNode.getPosition().oX, connectedNode.getPosition().oY);
            }
            lineDataTables.add(connectedData);
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
}
