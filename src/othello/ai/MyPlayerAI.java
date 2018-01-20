package othello.ai;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import othello.model.Board;

// your AI here. currently will choose first possible move
public class MyPlayerAI extends ReversiAI {

    int __depth;
    Point __move = null;


    private double max(Board state, double alpha, double beta) {
        __depth++;
        int score;
        if (state.getMoveCount(true) == 0) {
            // board.print();
            score = state.getTotal(true);
            if (score > alpha)
                alpha = score;
        } else if (__depth < 8) {
//            int bestScore = Integer.MIN_VALUE; //keep track of best score for Max. Start at negative infinite
            List<Point> moves = generateMoves(state); //generate possible moves for this state
            for (Point move : moves) {
                Board moveState = this.applyMoveCloning(move, state); //apply move into state clone
                moveState.turn();

                double s = min(moveState, alpha, beta);
                __depth--;
//                bestScore = (int) Math.max(s, bestScore);
                if (s > alpha) {
                    alpha = s;

                    if (__depth == 1) {
                        __move = new Point((int) move.getX(), (int) move.getY());
                    }
                }
                if (beta <= alpha)
                    break;

            }
        } else {
            alpha = state.getTotal(true);
        }
        return alpha;
    }

    private double min(Board state, double alpha, double beta) {

        __depth++;
        int score;
        if (state.getMoveCount(true) == 0) {
            // board.print();
            score = state.getTotal(false);
            if (score < beta)
                beta = score;
        } else {
//            int bestScore = Integer.MAX_VALUE; //keep track of best score for Min. Start at positive infinite
            List<Point> moves = this.generateMoves(state);
            for (Point move : moves) {
                Board moveState = this.applyMoveCloning(move, state); //apply move into state clone
                moveState.turn();

                double resault = max(moveState, alpha, beta);
                __depth--;
                if (beta > resault) {
                    beta = resault;
                }
//                    bestScore = (int) Math.min(s, bestScore);
//                    beta = Math.min(bestScore, beta);

                if (beta <= alpha)
                    break;

            }
        }

        return beta;
    }

    private List<Point> generateMoves(Board state) {
        List<Point> pointList = new Vector<>();

        for (int j = 0; j < state.getSize(); j++) {
            for (int i = 0; i < state.getSize(); i++) {
                Board tempState = new Board(state);
                if (tempState.move(i, j))
                    pointList.add(new Point(i, j));
            }
        }

        pointList.sort(new Comparator<Point>() {
            @Override
            public int compare(Point point, Point t1) {
                if(__array[(int) point.getX()][(int) point.getX()] < __array[(int) t1.getX()][(int) t1.getX()])
                    return 1;
                return -1;
            }
        });
        return pointList;
    }



    @Override
    public Point nextMove(Board b) {

        __depth = 0;
        long startTime = System.nanoTime();
        double value = max(b, (double) Integer.MIN_VALUE, (double) Integer.MAX_VALUE);
        long endTime = System.nanoTime();
        long duration = ((endTime - startTime) / 1000000);
        System.out.println("Minimax took " + duration + " milliseconds.");
        return __move;
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
        return new String("9325243");
    }

    public Board applyMoveCloning(Point move, Board state) {
        Board newState = clone(state);
        newState.move(move.x, move.y);
        return newState;
    }

    /*
     * Makes a copy of a game state
     */
    public Board clone(Board state) {
        Board newState = new Board(state);
        return newState;
    }

    private int[][] __array = {
            {-99, 5, 4, 3, 3, 4, 5, -99},
            {5, 3, 2, 2, 2, 2, 3, 5},
            {4, 2, 1, 1, 1, 1, 2, 4},
            {3, 2, 1, 1, 1, 1, 2, 3},
            {3, 2, 1, 1, 1, 1, 2, 3},
            {4, 2, 1, 1, 1, 1, 2, 4},
            {5, 3, 2, 2, 2, 2, 3, 5},
            {-99, 5, 4, 3, 3, 4, 5, -99}};
}

