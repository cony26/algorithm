package algorithm.algorithm_core;

import java.util.List;

public interface Presenter {
    public void updatePanel(List<Node> aNodes);
    public void highLightPathPlan();
    public void highLightPathPlan(List<Node> aNodes);
    public void setColorProvider(ColorProvider aColorProvider);
}
