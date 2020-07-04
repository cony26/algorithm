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
    private JFrame oFrame;
    private DataTable oData = new DataTable(Integer.class, Integer.class);

    public static Presenter getInstance(){
        return cPresenter;
    }

    private Presenter() {
        super(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        showInFrame();
    }

    private String getTitle() {
        return "Scatter plot";
    }

    private String getDescription() {
        return String.format("Scatter plot with %d data points", SAMPLE_COUNT);
    }

    private JFrame showInFrame() {
        oFrame = new JFrame(getTitle());
        oFrame.getContentPane().add(this, BorderLayout.CENTER);
        oFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        oFrame.setSize(getPreferredSize());
        oFrame.setVisible(true);
        return oFrame;
    }

    private JPanel createPlot(DataTable aData){
        // Create a new xy-plot
        XYPlot plot = new XYPlot(aData);

        // Format plot
        plot.setInsets(new Insets2D.Double(20.0, 40.0, 40.0, 40.0));
        plot.getTitle().setText(getDescription());
        plot.getAxisRenderer(XYPlot.AXIS_X).setTickSpacing(10.0);
        plot.getAxisRenderer(XYPlot.AXIS_Y).setTickSpacing(10.0);
        plot.getAxis(XYPlot.AXIS_X).setMin(-10);
        plot.getAxis(XYPlot.AXIS_X).setMax(110);
        plot.getAxis(XYPlot.AXIS_Y).setMin(-10);
        plot.getAxis(XYPlot.AXIS_Y).setMax(110);

        // Format points
        plot.getPointRenderers(aData).get(0).setColor(COLOR1);

        return new InteractivePanel(plot);
    }

    public void updatePanel(List<Node> aNodes){
        removeAll();
        oData.clear();
        for(Node node : aNodes){
            oData.add(node.getPosition().oX, node.getPosition().oY);
        }
        add(createPlot(oData), BorderLayout.CENTER);
        oFrame.revalidate();
    }
}
