package othello.ai;

import java.
import java.awt.Point;
import java.util.List;
import java.util.Vector;

import othello.model.Board;

import javax.swing.*;


// your AI here. currently will choose first possible move
public class MyPlayerAI extends ReversiAI {

    private int __depth;

    public MyPlayerAI(){__depth = 7;}


    double alpha = Double.NEGATIVE_INFINITY;
    double betha = Double.POSITIVE_INFINITY;

    int tmpDepth = 0;

    double tmp;
    Point current_move  = null;

    double max_val = Double.NEGATIVE_INFINITY;

    List<Point> moves = new Vector<>();


    public List findMoves(Board b){
        List<Point> moves = new Vector<>();

        for(int j=0; j < size; j++){
            for(int i=0; i<size; i++ ){
                Board tmpBoard = new Board(b);
                if(tmpBoard.move(i, j))
                    moves.add(new Point(i,j));
            }
        }

        return moves;
    }


    public Point minmax(Board state, double depth){


        return ;

    }

    @Override
	public Point nextMove(Board b) {

        Point move = null;

        Board newBoard = new Board(b);

        long start = System.nanoTime();

        move = minmax(newBoard, this.__depth);

        long end = System.nanoTime();

        long duration = (end-start) / 1000000;
        System.console().printf("Duration: " + duration + "ms");
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
	
	
	
	private double max(Board b, double alpha, double beta,int depth){

		double max_v=-100000;

		if (depth==9){
			return b.getScore();
		}
		depth++;
		for (int j = 0; j < size; j++) {
			for (int i = 0; i < size; i++) {
				if (b.move(i, j))
				{
					max_v = Math.max(min(b,alpha,beta,depth),max_v);
					if (max_v>beta)
					    return max_v;
					alpha = Math.max(max_v,alpha);
				}
			}
		}
		if (max_v == -100000)
		    return 100000;
		else
		    return max_v;
	}

	
	

	@Override
	public String getName() {
		//IMPORTANT: your student number here
		return new String("9325243-9323613");
	}
}
