package object;
/**
 * 
 * @author sangkyun
 *
 */
public abstract class Piece {
	private boolean white = false;
	private boolean neverMoved = true;
	private boolean firstMove = false;
	
	public Piece(boolean white) {
		this.setWhite(white);
	}
	
	public void setWhite(boolean white) {
		this.white = white;
	}
	
	public boolean getWhite() {
		return this.white;
	}
	
	public boolean isNeverMoved() {
		return neverMoved;
	}

	public void setNeverMoved(boolean neverMoved) {
		this.neverMoved = neverMoved;
	}
	
	public boolean isFirstMove() {
		return firstMove;
	}

	public void setFirstMove(boolean firstMove) {
		this.firstMove = firstMove;
	}

	public abstract void printPices();
	public abstract boolean canMove(int fFile, int fRank, int sFile, int sRank, Board board);
}
