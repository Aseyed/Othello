package othello.ai;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import othello.model.Board;


// your AI here. currently will choose first possible move

//Hossein's AI
public class MyPlayerAI2 extends ReversiAI {
    private int Depth;
    private Point bestPoint;


    private int valueMax(Board board, int alpha, int beta) {
        this.Depth++;
        int score;
        if (board.getMoveCount(true) == 0) {
            // board.print();
            score = board.getTotal(true);
            if (score > alpha)
                alpha = score;
        } else if (this.Depth <= 8) {
            Set<Point> possibleMoves = explore(board);
            if (!possibleMoves.isEmpty()) {
                for (Point nextPossibleMove : possibleMoves) {
                    Board subBoard = new Board(board);
                    subBoard.move(nextPossibleMove.x, nextPossibleMove.y);
                    subBoard.turn();

                    int result = valueMin(subBoard, alpha, beta);
                    this.Depth--;
                    //alpha = maxscore;
                    if (result > alpha) {
                        alpha = result;
                        if (this.Depth == 1)
                            bestPoint = nextPossibleMove;

                    }

                    if (alpha >= beta) {
                        break;
                    }
                }

            }
        } else {
            alpha = board.getTotal(true);
        }
        return alpha;
    }

    private int valueMin(Board board, int alpha, int beta) {
        this.Depth++;
        int score;
        if (board.getMoveCount(true) == 0) {
            // board.print();
            score = board.getTotal(false);
            if (score < beta)
                beta = score;
        } else {
            Set<Point> possibleMoves = explore(board);
            if (!possibleMoves.isEmpty()) {
                for (Point nextPossibleMove : possibleMoves) {
                    Board subBoard = new Board(board);
                    subBoard.move(nextPossibleMove.x, nextPossibleMove.y);
                    subBoard.turn();

                    int result = valueMax(subBoard, alpha, beta);
                    this.Depth--;
                    if (result < beta) {
                        beta = result;
                        //bestPoint = nextPossibleMove;
                    }
                    if (alpha >= beta) {
                        break;

                    }
                }
            }
        }
        return beta;
    }

    private Set<Point> explore(Board board) {
        Set<Point> possibleMoves = new HashSet<Point>();

        Board tmp = new Board(board);


        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                Board tmp2 = new Board(tmp);
                if (tmp2.move(i, j))
                    possibleMoves.add(new Point(i, j));
            }
        }

        return possibleMoves;
    }

    private int evaluate(Board board) {
        return board.getTotal(true) - board.getTotal(false);


    }

    protected int max(int a, int b) {
        return Math.max(a, b);
    }

    protected int min(int a, int b) {
        return Math.min(a, b);
    }

    @Override
    public Point nextMove(Board b) {
        this.Depth = 0;
        valueMax(b, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return bestPoint;
//		for (int j = 0; j < size; j++)
//			for (int i = 0; i < size; i++)
//				if (b.move(i, j))
//					return new Point(i, j);
//		return null;



		/*{
			b.isCapturedByMe(x, y);					// square (x, y) is mine
			b.isCapturedByMyOppoenet(x, y);			// square (x, y) is for my opponent
			b.isEmptySquare(x, y);					// square (x, y) is empty
			b.move(x, y);							// attempt to place a piece at specified coordinates, and update
													// the board appropriately, or return false if not possible
			b.turn();								// end current player's turn
			b.print();								// ASCII printout of the current board
			if(b.getActive() == Board.WHITE)		//I am White
			if(b.getActive() == Board.BLACK)		//I am Black

			b.getMoveCount(true);					//number of possible moves for me
			b.getMoveCount(false);					//number of possible moves for my opponent
			b.getTotal(true);						//number of cells captured by me
			b.getTotal(false);						//number of cells captured by my opponent
			this.size;								//board size (always 8)
		}*/
    }

    @Override
    public String getName() {
        //IMPORTANT: your student number here
        return new String("MyPlayerAI2");
    }
}