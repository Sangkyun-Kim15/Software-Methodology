package object;

public class Knight extends Piece {

	public Knight(boolean white) {
		super(white);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void printPices() {
		// TODO Auto-generated method stub
		if (this.getWhite() == true) {
			System.out.print("wN");
		} else {
			System.out.print("bN");
		}
	}

	@Override
	public boolean canMove(int fFile, int fRank, int sFile, int sRank, Board board) {
		// TODO Auto-generated method stub
		int deltaX, deltaY;
		Piece sourcePiece = board.getBox(fFile, fRank).getPiece();
		Tile dest = board.getBox(sFile, sRank);
		
		deltaX = Math.abs(fRank - sRank);
		deltaY = Math.abs(fFile - sFile);
		
		if(dest.getPiece() != null) {
			if(sourcePiece.getWhite() == dest.getPiece().getWhite()) {
				return false;
			}
		}
		
		if(deltaX == 1 && deltaY == 2) {
			return true;
		} else if(deltaX == 2 && deltaY == 1) {
			return true;
		}
		return false;
	}

}
