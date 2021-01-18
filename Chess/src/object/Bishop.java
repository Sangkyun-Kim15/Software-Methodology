package object;
/**
 * 
 * @author sangkyun
 *
 */
public class Bishop extends Piece implements CheckRoute {

	public Bishop(boolean white) {
		super(white);
	}

	@Override
	public void printPices() {
		// TODO Auto-generated method stub
		if(this.getWhite() == true) {
			System.out.print("wB");
		} else {
			System.out.print("bB");
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
		
		if(hasPieceInRoute(fFile, fRank, sFile, sRank, board)) {
			return false;
		}
		
		if(dest.getPiece() != null) {
			if(sourcePiece.getWhite() == dest.getPiece().getWhite()) {
				return false;
			}
		}
		
		if(deltaX == deltaY) {
			return true;
		}
		return false;
	}
}
