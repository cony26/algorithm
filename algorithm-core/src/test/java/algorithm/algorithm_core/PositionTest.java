package algorithm.algorithm_core;

import algorithm.algorithm_core.Node;
import algorithm.algorithm_core.Position;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class PositionTest {
    @RunWith(Enclosed.class)
    public static class passZeroToCreatePositions{
        public static class Constant{
            @Test
            public void passZeroToCreateConstantPositions(){
                List<Position> positions = Position.createConstantPositions(0);
                assertTrue(positions.isEmpty());
            }
        }

        public static class Random{
            @Test
            public void passZeroToCreateRandomPositions(){
                List<Position> positions = Position.createRandomPositions(0);
                assertTrue(positions.isEmpty());
            }
        }
    }

    @RunWith(Enclosed.class)
    public static class passNodeNumberToCreatePositions{
        public static class Constant{
            List<Position> positions;
            @Before
            public void SetUp(){
                positions = Position.createConstantPositions(Node.NODE_NUMBER);
            }

            @Test
            public void firstElementIsSTART(){
                assertThat(positions.get(0), is(Position.START));
            }

            @Test
            public void lastElementIsEND(){
                assertThat(positions.get(Node.NODE_NUMBER - 1), is(Position.END));
            }

            @Test
            public void secondElementIsTenZero(){
                assertThat(positions.get(1).oX, is(10));
                assertThat(positions.get(1).oY, is(10));
            }

            @Test
            public void sizeIsNodeNumber(){
                assertThat(positions.size(), is(Node.NODE_NUMBER));
            }
        }
        public static class Random{
            List<Position> positions;
            @Before
            public void SetUp(){
                positions = Position.createRandomPositions(Node.NODE_NUMBER);
            }
            @Test
            public void firstElementIsSTART(){
                assertThat(positions.get(0), is(Position.START));
            }

            @Test
            public void lastElementIsEND(){
                assertThat(positions.get(Node.NODE_NUMBER - 1), is(Position.END));
            }

            @Test
            public void allElementIsDifferent(){
                for(Position position : positions)
                    assertTrue(positions.stream()
                            .filter(pos -> !(pos == position))
                            .noneMatch(pos -> pos.equals(position))
                            );
            }

            @Test
            public void sizeIsNodeNumber(){
                assertThat(positions.size(), is(Node.NODE_NUMBER));
            }
        }

    }
}