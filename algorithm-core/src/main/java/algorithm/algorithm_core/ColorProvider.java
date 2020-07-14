package algorithm.algorithm_core;

import java.awt.*;

public interface ColorProvider {
    public final Color BLUE = new Color( 55, 170, 200);
    public final Color RED = new Color(200,  80,  75);
    public final Color BLACK = new Color(0,  0,  0);
    public void setPointColor(Graphics g, Node aNode);
    default public void setLineColor(Graphics g){
        g.setColor(BLACK);
    }
    default public void setPathColor(Graphics g){
        g.setColor(BLUE);
    }
    default public void setStringColor(Graphics g){
        g.setColor(BLACK);
    }
}
