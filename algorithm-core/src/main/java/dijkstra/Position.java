package dijkstra;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//this class is immutable
class Position {
	public final int oX;
	public final int oY;
	private static final int X_RANGE = 100;
	private static final int Y_RANGE = 100;
	private Position(int aX, int aY) {
		oX = aX;
		oY = aY;
	}
	public static List<Position> createPositions() {
		List<Position> positions = new ArrayList<>();
		Random random = new Random();
		
		for(int i = 0; i < Node.NODE_NUMBER; i++) {
			while(true) {
				int x = random.nextInt(X_RANGE);
				int y = random.nextInt(Y_RANGE);
				Position newPosition = new Position(x, y);
			
				if(!positions.contains(newPosition)) {
					positions.add(newPosition);
					break;
				}
			}
		}
		
		return positions;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		
		if(!(o instanceof Position)) {
			return false;
		}
		
		Position position = (Position)o;
		if(position.oX == this.oX && position.oY == this.oY) {
			return true;
		}else {
			return false;
		}
	}
	
}