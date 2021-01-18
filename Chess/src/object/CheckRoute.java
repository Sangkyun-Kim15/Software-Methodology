package object;

public interface CheckRoute {
	default public boolean hasPieceInRoute(int fFile, int fRank, int sFile, int sRank, Board board) {
		int deltaX, deltaY;
		
		deltaX = Math.abs(fRank - sRank);
		deltaY = Math.abs(fFile - sFile);
		
		// diagnol
		if(deltaX == deltaY) {
			// up right
			if(fRank < sRank && fFile < sFile) {
				int i = fFile + 1;
				for(int j = fRank + 1; j < sRank; j++) {
					
					if(board.getBox(i, j).getPiece() != null) {
						return true;
					}
					i++;
				}
			// up left
			} else if(fRank > sRank && fFile < sFile) {
				int i = fFile + 1;
				for(int j = fRank - 1; j > sRank; j--) {
					if(board.getBox(i, j).getPiece() != null) {
						return true;
					}
					i++;
				}
			// down right
			} else if(fRank < sRank && fFile > sFile) {
				int i = fFile - 1;
				for(int j = fRank + 1; j < sRank; j++) {
					if(board.getBox(i, j).getPiece() != null) {
						return true;
					}
					i--;
				}
			// down left
			} else if(fRank > sRank && fFile < sFile) {
				int i = fFile - 1;
				for(int j = fRank - 1; j > sRank; j--) {
					if(board.getBox(i, j).getPiece() != null) {
						return true;
					}
					i--;
				}
			}
			//vertical
		} else if(deltaX == 0 && deltaY != 0) {
			if(fFile < sFile) {
				for(int i = fFile + 1; i < sFile; i++) {
					System.out.println(i);
					if(board.getBox(i, fRank).getPiece() != null) {
						return true;
					}
				}
			} else if(fFile > sFile) {
				for(int i = fFile - 1; i > sFile; i--) {
					if(board.getBox(i, fRank).getPiece() != null) {
						return true;
					}
				}
			}
			// horizontal
		} else if(deltaX != 0 && deltaY == 0) {
			if(fRank < sRank) {
				for(int i = fRank + 1; i < sRank; i++) {
					if(board.getBox(fFile, i).getPiece() != null) {
						return true;
					}
				}
			} else if(fRank > sRank) {
				for(int i = fRank - 1; i > sRank; i--) {
					if(board.getBox(fFile, i).getPiece() != null) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
