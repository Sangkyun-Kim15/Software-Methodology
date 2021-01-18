package object;

public class Tile {
	private Piece piece;
	private int x;
	private int y;
	
	public Tile(int x, int y, Piece piece) {
		this.setX(x);
		this.setY(y);
		this.setPiece(piece);
		
	}

	public Piece getPiece() {
		return this.piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
}
