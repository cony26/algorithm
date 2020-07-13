package algorithm.algorithm_core;

import algorithm.algorithm_core.Position;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class NodeTest {
    @Test
    public void getDistance(){
        assertThat(Node.START_NODE.getDistance(Node.END_NODE), is(Position.X_RANGE * Math.sqrt(2)));
        assertEquals(Node.START_NODE.getDistance(Node.END_NODE), Position.X_RANGE * Math.sqrt(2), 0.1);
    }
}