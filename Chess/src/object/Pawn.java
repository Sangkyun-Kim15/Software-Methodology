package object;


public class Pawn extends Piece {

	public Pawn(boolean white) {
		super(white);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void printPices() {
		// TODO Auto-generated method stub
		if(this.getWhite() == true) {
			System.out.print("wP");
		} else {
			System.out.print("bP");
		}
	}

	@Override
	public boolean canMove(int fFile, int fRank, int sFile, int sRank, Board board) {
		// TODO Auto-generated method stub
		int deltaX, deltaY;
		Piece sourcePiece = board.getBox(fFile, fRank).getPiece();
		Tile dest = board.getBox(sFile, sRank);
		
		if(sourcePiece.getWhite()) {
			deltaY = sFile - fFile;
		} else {
			deltaY = fFile - sFile;
		}
		
		deltaX = Math.abs(fRank - sRank);
		
		/*
		System.out.print(fFile);
		System.out.println(fRank);
		System.out.print(sFile);
		System.out.println(sRank);
		System.out.println(deltaX);
		System.out.println(deltaY);
		*/
		if(dest.getPiece() != null) {
			if(sourcePiece.getWhite() == dest.getPiece().getWhite()) {
				return false;
			}
		}
		
		if(deltaY == 1 && deltaX == 0 && dest.getPiece() == null) {
			return true;
		} else if(deltaX == 1 && deltaY == 1 && dest.getPiece() != null) {
			return true;
		} else if(deltaY == 2 && deltaX == 0 && dest.getPiece() == null && sourcePiece.isNeverMoved()) {
			return true;
		}
		
		
		return false;
	}

}
