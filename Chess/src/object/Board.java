package object;
/**
 * 
 * @author sangkyun
 *
 */
public class Board {
	private Tile[][] boxes;

	public Board() {
		boxes = new Tile[8][8];
		this.resetBoard();
	}

	public void resetBoard() {
		boxes[0][0] = new Tile(0, 0, new Rook(true));
		boxes[0][1] = new Tile(0, 1, new Knight(true));
		boxes[0][2] = new Tile(0, 2, new Bishop(true));
		boxes[0][3] = new Tile(0, 3, new Queen(true));
		boxes[0][4] = new Tile(0, 4, new King(true));
		boxes[0][5] = new Tile(0, 5, new Bishop(true));
		boxes[0][6] = new Tile(0, 6, new Knight(true));
		boxes[0][7] = new Tile(0, 7, new Rook(true));

		for (int i = 0; i < 8; i++) {
			boxes[1][i] = new Tile(1, i, new Pawn(true));
		}

		boxes[7][0] = new Tile(0, 0, new Rook(false));
		boxes[7][1] = new Tile(0, 1, new Knight(false));
		boxes[7][2] = new Tile(0, 2, new Bishop(false));
		boxes[7][3] = new Tile(0, 3, new Queen(false));
		boxes[7][4] = new Tile(0, 4, new King(false));
		boxes[7][5] = new Tile(0, 5, new Bishop(false));
		boxes[7][6] = new Tile(0, 6, new Knight(false));
		boxes[7][7] = new Tile(0, 7, new Rook(false));

		for (int i = 0; i < 8; i++) {
			boxes[6][i] = new Tile(1, i, new Pawn(false));
		}

		for (int i = 2; i < 6; i++) {
			for (int j = 0; j < 8; j++) {
				boxes[i][j] = new Tile(i, j, null);
			}
		}
	}
	
	public Tile getBox(int x, int y) {
		return boxes[x][y];
	}
}
