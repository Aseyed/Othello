package othello.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;

import javafx.util.Pair;
import othello.model.Board;
import java.util.concurrent.TimeUnit;

// your AI here. currently will choose first possible move
public class MyPlayerAI5 extends ReversiAI {

    private int turn = 0;
    private double timeLimit = 500;

    private Long beginTime = null;
    private Long openTasks = 0L;

    private Long getElapsedTime() {
        return System.nanoTime() - this.beginTime;
    }

    private double getRemainingTime () {
        return 1000d - TimeUnit.NANOSECONDS.toMicros( getElapsedTime() ) / 1000d;
    }

    private class AlphaBetaNode {
        private Integer height;
        private Integer minValue;
        private Integer maxValue;
        private Board board;
        private ArrayList<Point> neighbourMoves;
        private Boolean isMax;

        public AlphaBetaNode (Board board, Boolean isMax, Integer height) {
            this(board, Integer.MIN_VALUE, Integer.MAX_VALUE, isMax, height);
        }

        public AlphaBetaNode (Board board, Integer minValue, Integer maxValue, Boolean isMax, Integer height) {
            this.board = board;
            this.setMinValue(minValue);
            this.setMaxValue(maxValue);
            this.isMax = isMax;
            this.height = height;

            this.neighbourMoves = new ArrayList<>();
            this.findNeighbourMoves();
//		    System.out.println("Height is " + height.toString());
//		    this.board.print();
            openTasks++;

        }

        public Boolean isMaxNode() {
            return isMax == Boolean.TRUE;
        }

        public Boolean isMinNode() {
            return isMax == Boolean.FALSE;
        }

        public Integer getHeight() {
            return height;
        }

        public Integer getMinValue() {
            return minValue;
        }

        public void setMinValue(Integer minValue) {
            this.minValue = minValue;
        }

        public Integer getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(Integer maxValue) {
            this.maxValue = maxValue;
        }

        public Board getBoard() {
            return board;
        }

        public Integer estimate() {
            int score = 0;
            for (int i=0; i<board.getSize(); i++)
                for (int j=0; j<board.getSize(); j++) {
                    int w = (turn < 10 ? 0 : 1);
                    if ((i + j == 1) || ( i - j == board.getSize() - 2) ||
                            ( j - i == board.getSize() - 2) ||
                            (j + i == 2 * board.getSize() - 3))
                        w = 2;
                    else if ((i + j == 2) || ( i - j == board.getSize() - 3) ||
                            ( j - i == board.getSize() - 3) ||
                            (j + i == 2 * board.getSize() - 4))
                        w = -1;

                    if (turn > 18)
                        w = 1;

                    score += w * (isMaxNode() ? 1 : -1 ) * (board.isCapturedByMe(i, j) ? 1 : (board.isCapturedByMyOppoenet(i, j) ? -1 : 0));
                }
            return score;

//			int myScore = board.getScore();
//			if (isMinNode())
//			    myScore *= -1;
//			return myScore;

        }

        public Boolean isLeaf() {
            if (!board.canMove())
                return Boolean.TRUE;
            timeLimit = 500;

            double remainingTime = getRemainingTime();
            if (remainingTime < 50 && timeLimit < 700)
                timeLimit = timeLimit * 5 / 4;

            if (remainingTime < 10) {
                return Boolean.TRUE;
            }

            if (height > 6 && remainingTime < timeLimit) {
                return Boolean.TRUE;
            }

            if (height > 7 && remainingTime < (timeLimit * 3) / 2) {
                timeLimit -= 0.003;
                if (timeLimit < 200)
                    timeLimit = 200;
                return Boolean.TRUE;
            }

            if (height > 10) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

        public Point getNextMove() {
            return this.runMinMax().getValue();
        }

        public Pair<Integer, Point> runMinMax() {

            if (isLeaf())
                return new Pair<>(estimate(), null);

            Pair<Integer, Point> bestCandidate = null;
            for (int i=0; i<neighbourMoves.size(); i++) {
                Point candidateMove = neighbourMoves.get(i);
                Board nextBoard = new Board(board);
                nextBoard.move(candidateMove.x, candidateMove.y);
                nextBoard.turn();
                AlphaBetaNode nextAlphaBetaNode = new AlphaBetaNode(nextBoard, getMinValue(), getMaxValue(), !isMaxNode(), height+1);
                Pair<Integer, Point> candidateResult = nextAlphaBetaNode.runMinMax();
                candidateResult = new Pair<>(candidateResult.getKey(), candidateMove);

                if (bestCandidate == null)
                    bestCandidate = candidateResult;

                if (isMaxNode()) {
                    if (bestCandidate.getKey() < candidateResult.getKey())
                        bestCandidate = candidateResult;

                    if (bestCandidate.getKey() > minValue)
                        setMinValue(bestCandidate.getKey());
                }

                if (isMinNode()) {
                    if (bestCandidate.getKey() > candidateResult.getKey())
                        bestCandidate = candidateResult;

                    if (bestCandidate.getKey() < maxValue)
                        setMaxValue(bestCandidate.getKey());
                }

                if (getMinValue() >= getMaxValue()) {
//                    System.out.println("Cut Happened in Height " + height.toString() + " Child " + Integer.toString(i));
                    return bestCandidate;
                }


            }

            return bestCandidate;
        }

        private void findNeighbourMoves() {
            for (int i=0; i<board.getSize(); i++) {
                for (int j=0; j<board.getSize(); j++) {
                    Board tmp = new Board(board);
                    if (tmp.move(i, j))
                        neighbourMoves.add(new Point(i, j));
                }
            }
            if (isMaxNode())
                neighbourMoves.sort(COMPARE_MAX_NODE);
            else
                neighbourMoves.sort(COMPARE_MIN_NODE);
        }

        private Comparator<Point> COMPARE_MAX_NODE = new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                Board b1 = new Board(board);
                Board b2 = new Board(board);
                b1.move(o1.x, o1.y);
                b2.move(o2.x, o2.y);

                return Integer.compare(b2.getScore(), b1.getScore());
            }
        };

        private Comparator<Point> COMPARE_MIN_NODE = new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                Board b1 = new Board(board);
                Board b2 = new Board(board);
                b1.move(o1.x, o1.y);
                b2.move(o2.x, o2.y);

                return Integer.compare(b2.getScore(), b1.getScore());
            }
        };

    }

    @Override
    public Point nextMove(Board b) {
        turn ++;
        this.beginTime = System.nanoTime();
        Long stime = System.nanoTime();
        AlphaBetaNode abn = new AlphaBetaNode(b, Boolean.TRUE, 0);
        Point move = abn.getNextMove();
        Long etime = System.nanoTime();

        b.move(move.x, move.y);
        b.print();
        System.out.println(Integer.toString(turn) + ": TL: "+ Double.toString(timeLimit)+ " Finished in " + Long.toString(TimeUnit.NANOSECONDS.toMillis(etime - stime)));
        System.out.println("Remaining Time: " + Double.toString(getRemainingTime()));

        return move;
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
        return "MyPlayerAI5";
    }
}