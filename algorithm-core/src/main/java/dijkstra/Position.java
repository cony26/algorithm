package dijkstra;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//this class is immutable
class Position {
	public final int oX;
	public final int oY;
	public static final int X_RANGE = 100;
	public static final int Y_RANGE = 100;
	public static Position START = new Position(0,0);
	public static Position END = new Position(100, 100);
	private Position(int aX, int aY) {
		oX = aX;
		oY = aY;
	}
	public static List<Position> createRandomPositions() {
		List<Position> positions = new ArrayList<>();
		Random random = new Random();

		positions.add(START);
		for(int i = 1; i < Node.NODE_NUMBER - 1; i++) {
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
		positions.add(END);
		
		return positions;
	}

	public static List<Position> createConstantPositions(){
		List<Position> positions = new ArrayList<>();
		positions.add(START);
		int x = 10;
		int y = 10;
		for(int i = 1; i < Node.NODE_NUMBER - 1; i++) {
				positions.add(new Position(x, y));
				x += 10;
				if(x > X_RANGE){
					x = 10;
					y += 10;
				}
		}
		positions.add(END);
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