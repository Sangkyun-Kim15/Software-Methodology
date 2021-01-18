package object;

import java.util.Scanner;
/**
 * 
 * @author sangkyun
 *
 */
public class Game  implements CheckRoute{
	private Board board;
	private GameStatus status;
	private boolean currentPlayer = true;
	private static Scanner sc = new Scanner(System.in);
	private boolean canCastlingB = true;
	private boolean canCastlingW = true;

	public void run() {
		// TODO Auto-generated method stub
		String input;
		int fFile, fRank, sFile, sRank;
		String prom = "";
		
		board = new Board();
		printGame(board);
		this.status = GameStatus.ACTIVE;
		
		while(this.status == GameStatus.ACTIVE) {
			
			if(isCheck(board, currentPlayer)) {
				if(currentPlayer) {
					System.out.println("White in check");
				} else {
					System.out.println("Black in check");
				}
			}
			
			input = sc.nextLine();
			
			if(isValidFormat(input)) {
				fFile = Character.getNumericValue(input.charAt(1) - 1);
				fRank = input.charAt(0) - 97;
				sFile = Character.getNumericValue(input.charAt(4) - 1);
				sRank = input.charAt(3) - 97;
				
				if(input.length() == 7) {
					prom = input.substring(6, 7);
				}
				
				// need to check valid move
				if(isValidMove(fFile, fRank, sFile, sRank, prom, board, currentPlayer)) {
					
					move(fFile, fRank, sFile, sRank, board);
					
					if(isCheck(board, currentPlayer)) {
						System.out.println("Checkmate");
						if(currentPlayer) {
							System.out.println("Black win");
							this.status = GameStatus.BLACK_WIN;
							System.exit(0);
							
						} else {
							System.out.println("White win");
							this.status = GameStatus.WHITE_WIN;
							System.exit(0);
						}
					}
					printGame(board);
					currentPlayer = !currentPlayer;
				}
			}
		}
	}

	/**
	 * 
	 * @param board
	 * @param currentPlayer
	 * @return
	 */
	private boolean isCheck(Board board, boolean currentPlayer) {
		// TODO Auto-generated method stub
		int kingLocX = 0; 
		int kingLocY = 0;
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board.getBox(i, j).getPiece() != null) {
					if(board.getBox(i, j).getPiece().getWhite() == currentPlayer
							&& board.getBox(i, j).getPiece() instanceof King) {
						kingLocX = j;
						kingLocY = i;
						break;
					}
				}
			}
		}
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board.getBox(i, j).getPiece() != null) {
					if(board.getBox(i, j).getPiece().getWhite() != currentPlayer) {
						if(board.getBox(i, j).getPiece().canMove(i, j, kingLocY, kingLocX, board)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param fFile
	 * @param fRank
	 * @param sFile
	 * @param sRank
	 * @param board
	 * @return
	 */
	private boolean isValidMove(int fFile, int fRank, int sFile, int sRank, String prom, Board board, boolean currentPlayer) {
		// TODO Auto-generated method stub
		Piece sourcePiece = board.getBox(fFile, fRank).getPiece();
		
		if(sourcePiece == null) {
			System.out.println("You selected the empty spot.");
			return false;
		}
		
		if(sourcePiece.getWhite() != currentPlayer) {
			System.out.println("Select your piece");
			return false;
		}
		
		if(sourcePiece instanceof Pawn) {
			if(checkEnpassnt(fFile, fRank, sFile, sRank, board, sourcePiece)) {
				System.out.println("En passant");
				excuteEpssant(fFile, fRank, sFile, sRank, board, sourcePiece);
				this.currentPlayer = !currentPlayer;
				printGame(board);
				return false;
			}
			
			if(checkPromotion(fFile, fRank, sFile, sRank, prom, board, sourcePiece)) {
				System.out.println("Promotion");
				excutePromotion(fFile, fRank, sFile, sRank, board, prom, sourcePiece);
				this.currentPlayer = !currentPlayer;
				printGame(board);
				return false;
			}
		}
		
		if(sourcePiece instanceof King) {
			if(sourcePiece.getWhite()) {
				if(canCastlingW) {
					if(checkCastling(fFile, fRank, sFile, sRank, board, sourcePiece)) {
						this.currentPlayer = !currentPlayer;
						printGame(board);
						return false;
					}
				}
			} else {
				if(canCastlingB) {
					if(checkCastling(fFile, fRank, sFile, sRank, board, sourcePiece)) {
						this.currentPlayer = !currentPlayer;
						printGame(board);
						return false;
					}
				}
			}
		}
		
		if(!sourcePiece.canMove(fFile, fRank, sFile, sRank, board)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param fFile
	 * @param fRank
	 * @param sFile
	 * @param sRank
	 * @param board
	 * @param sourcePiece
	 */
	private void excuteCastling(int fFile, int fRank, int sFile, int sRank, Board board, Piece sourcePiece, Piece rook) {
		// TODO Auto-generated method stub
		board.getBox(sFile, sRank).setPiece(sourcePiece);
		board.getBox(fFile, fRank).setPiece(null);
		
		if(fRank < sRank) {
			if(sourcePiece.getWhite()) {
				board.getBox(0, 5).setPiece(rook);
				board.getBox(0, 7).setPiece(null);
			} else {
				board.getBox(7, 5).setPiece(rook);
				board.getBox(7, 7).setPiece(null);
			}
		} else {
			if(sourcePiece.getWhite()) {
				board.getBox(0, 3).setPiece(rook);
				board.getBox(0, 0).setPiece(null);
			} else {
				board.getBox(7, 3).setPiece(rook);
				board.getBox(7, 0).setPiece(null);
			}
		}
		
		if(sourcePiece.getWhite()) {
			canCastlingW = false;
		} else {
			canCastlingB = false;
		}
	}

	/**
	 * 
	 * @param fFile
	 * @param fRank
	 * @param sFile
	 * @param sRank
	 * @param board
	 * @param sourcePiece
	 * @return
	 */
	private boolean checkCastling(int fFile, int fRank, int sFile, int sRank, Board board, Piece sourcePiece) {
		// TODO Auto-generated method stub
		int deltaX;
		int deltaY;
		Piece rook = null;
		
		deltaX = Math.abs(fRank - sRank);
		deltaY = Math.abs(fFile - sFile);
		
		if(sourcePiece.isNeverMoved() && deltaX == 2 && deltaY == 0) {
			if(fRank < sRank) {
				if(sourcePiece.getWhite()) {
					if(board.getBox(0, 7).getPiece() != null) {
						if(board.getBox(0, 7).getPiece() instanceof Rook) {
							rook = board.getBox(0, 7).getPiece(); 
						}
					}
				} else {
					if(board.getBox(7, 7).getPiece() != null) {
						if(board.getBox(7, 7).getPiece() instanceof Rook) {
							rook = board.getBox(7, 7).getPiece(); 
						}
					}
				}
			} else {
				if(sourcePiece.getWhite()) {
					if(board.getBox(0, 0).getPiece() != null) {
						if(board.getBox(0, 0).getPiece() instanceof Rook) {
							rook = board.getBox(0, 0).getPiece(); 
						}
					}
				} else {
					if(board.getBox(7, 0).getPiece() != null) {
						if(board.getBox(7, 0).getPiece() instanceof Rook) {
							rook = board.getBox(7, 0).getPiece(); 
						}
					}
				}
			}
			
			if(rook != null) {
				if(rook.isNeverMoved() 
						&& !hasPieceInRoute(fFile, fRank, sFile, sRank, board)
						&& board.getBox(sFile, sRank).getPiece() == null) {
					System.out.println("Castling");
					excuteCastling(fFile, fRank, sFile, sRank, board, sourcePiece, rook);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param fFile
	 * @param fRank
	 * @param sFile
	 * @param sRank
	 * @param board
	 * @param prom
	 * @param sourcePiece
	 */
	private void excutePromotion(int fFile, int fRank, int sFile, int sRank, Board board, String prom,
			Piece sourcePiece) {
		// TODO Auto-generated method stub
		if(prom.equals("N")) {
			board.getBox(sFile, sRank).setPiece(new Knight(sourcePiece.getWhite()));
		} else if(prom.equals("Q")) {
			board.getBox(sFile, sRank).setPiece(new Queen(sourcePiece.getWhite()));
			
		} else if(prom.equals("R")) {
			board.getBox(sFile, sRank).setPiece(new Rook(sourcePiece.getWhite()));
			
		} else if(prom.equals("B")) {
			board.getBox(sFile, sRank).setPiece(new Bishop(sourcePiece.getWhite()));
		}
		
		board.getBox(fFile, fRank).setPiece(null);
		board.getBox(sFile, sRank).getPiece().setNeverMoved(false);
		board.getBox(sFile, sRank).getPiece().setFirstMove(false);
	}

	/**
	 * 
	 * @param fFile
	 * @param fRank
	 * @param sFile
	 * @param sRank
	 * @param prom
	 * @param board
	 * @param sourcePiece
	 * @return
	 */
	private boolean checkPromotion(int fFile, int fRank, int sFile, int sRank, String prom, Board board,
			Piece sourcePiece) {
		// TODO Auto-generated method stub
		if(sourcePiece.getWhite()) {
			if(sFile == 7 && !prom.equals("") && board.getBox(sFile, sRank).getPiece() == null) {
				return true;
			}
		} else {
			if(sFile == 0 && !prom.equals("") && board.getBox(sFile, sRank).getPiece() == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param fFile
	 * @param fRank
	 * @param sFile
	 * @param sRank
	 * @param board
	 * @param sourcePiece
	 */
	private void excuteEpssant(int fFile, int fRank, int sFile, int sRank, Board board, Piece sourcePiece) {
		// TODO Auto-generated method stub
		board.getBox(sFile, sRank).setPiece(board.getBox(fFile, fRank).getPiece());
		board.getBox(fFile, fRank).setPiece(null);
		
		if(sourcePiece.getWhite()) {
			board.getBox(sFile - 1, sRank).setPiece(null);
		} else {
			board.getBox(sFile + 1, sRank).setPiece(null);
		}
	}

	/**
	 * 
	 * @param fFile
	 * @param fRank
	 * @param sFile
	 * @param sRank
	 * @param board
	 * @param sourcePiece
	 * @return
	 */
	private boolean checkEnpassnt(int fFile, int fRank, int sFile, int sRank, Board board, Piece sourcePiece) {
		// TODO Auto-generated method stub
		int deltaX, deltaY;
		Tile dest = board.getBox(sFile, sRank);
		
		if(sourcePiece.getWhite()) {
			deltaY = sFile - fFile;
		} else {
			deltaY = fFile - sFile;
		}
		
		deltaX = Math.abs(fRank - sRank);
		
		if(deltaX == 1 && deltaY == 1 && dest.getPiece() == null) {
			if(sourcePiece.getWhite()) {
				if(board.getBox(sFile - 1, sRank).getPiece() instanceof Pawn) {
					if(board.getBox(sFile - 1, sRank).getPiece().isFirstMove() == true) {
						return true;
					}
				}
			} else {
				if(board.getBox(sFile + 1, sRank).getPiece() instanceof Pawn) {
					if(board.getBox(sFile + 1, sRank).getPiece().isFirstMove() == true) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param fFile
	 * @param fRank
	 * @param sFile
	 * @param sRank
	 * @param board
	 */
	private void move(int fFile, int fRank, int sFile, int sRank, Board board) {
		// TODO Auto-generated method stub
		board.getBox(sFile, sRank).setPiece(board.getBox(fFile, fRank).getPiece());
		board.getBox(fFile, fRank).setPiece(null);
		
		if(board.getBox(sFile, sRank).getPiece().isNeverMoved()) {
			board.getBox(sFile, sRank).getPiece().setNeverMoved(false);
			board.getBox(sFile, sRank).getPiece().setFirstMove(true);
		} else if(board.getBox(sFile, sRank).getPiece().isFirstMove()) {
			board.getBox(sFile, sRank).getPiece().setFirstMove(false);
		}
	}

	/**
	 * 
	 * @param input
	 * @return boolean
	 */
	private boolean isValidFormat(String input) {
		// TODO Auto-generated method stub
		boolean result = false;

		if (input.matches("^[a-h][1-8] [a-h][1-8]$") 
				|| input.matches("^[a-h][1-8] [a-h][1-8] draw[?]$")
				|| input.matches("^resign$")
				|| input.matches("^[a-h][1-8] [a-h][1-8] [BNQR]")) {
			result = true;

			if (isDraw(input)) {
				System.out.println("draw");
				System.exit(0);
			}

			if (isResign(input)) {
				if(currentPlayer) {
					System.out.println("black win");
					this.status = GameStatus.BLACK_WIN;
					System.exit(0);
				} else {
					System.out.println("white win");
					this.status = GameStatus.WHITE_WIN;
					System.exit(0);
				}
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param input
	 * @return boolean
	 */
	public boolean isDraw(String input) {
		if (input.toLowerCase().contains("draw?")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	public boolean isResign(String input) {
		if (input.equalsIgnoreCase("resign")) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param board
	 */
	public void printGame(Board board) {
		for (int i = 7; i >= 0; i--) {
			for (int j = 0; j < 8; j++) {
				if(board.getBox(i, j).getPiece() == null) {
					if((i % 2 == 0 && j % 2 == 0) || ((i % 2 != 0 && j % 2 != 0))) {
						System.out.print("##");
					} else {
						System.out.print("  ");
					}
				} else {
					board.getBox(i, j).getPiece().printPices();
				}
				System.out.print(" ");
			}
			System.out.println(i + 1);
		}
		System.out.println(" a  b  c  d  e  f  g  h");
	}
}
