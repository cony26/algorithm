package dijkstra;

import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.ui.InteractivePanel;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.List;

public class Presenter extends JPanel {
    private static final Random random = new Random();
    protected static final Color COLOR1 = new Color( 55, 170, 200);
    private static final int SAMPLE_COUNT = 1000;
    private static final Presenter cPresenter = new Presenter();
    private JPanel oPlotPanel;
    private DataTable oData = new DataTable(Integer.class, Integer.class);

    public static Presenter getInstane(){
        return cPresenter;
    }

    private Presenter() {
        super(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);

        oPlotPanel = createPlot(oData);
        add(oPlotPanel, BorderLayout.CENTER);
    }

    private String getTitle() {
        return "Scatter plot";
    }

    private String getDescription() {
        return String.format("Scatter plot with %d data points", SAMPLE_COUNT);
    }

    protected JFrame showInFrame() {
        JFrame frame = new JFrame(getTitle());
        frame.getContentPane().add(this, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(getPreferredSize());
        frame.setVisible(true);
        return frame;
    }

    private JPanel createPlot(DataTable aData){
        // Create a new xy-plot
        XYPlot plot = new XYPlot(aData);

        // Format plot
        plot.setInsets(new Insets2D.Double(20.0, 40.0, 40.0, 40.0));
        plot.getTitle().setText(getDescription());

        // Format points
        plot.getPointRenderers(aData).get(0).setColor(COLOR1);

        return new InteractivePanel(plot);
    }

    public void updatePanel(List<Node> aNodes){
        remove(oPlotPanel);
        oData.clear();
        for(Node node : aNodes){
            oData.add(node.getPosition().oX, node.getPosition().oY);
        }
        add(createPlot(oData), BorderLayout.CENTER);
    }
}
