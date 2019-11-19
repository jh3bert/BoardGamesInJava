package a7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TicTacToeWidget extends JPanel implements ActionListener, SpotListener{

	private enum Player {BLACK, WHITE};
	
	private JSpotBoard board;
	private JLabel message;
	private Player nextPlayer;
	private boolean gameWon;
	private boolean draw;
	
	public TicTacToeWidget() {
		
		board = new JSpotBoard(3, 3);
		message = new JLabel();
		
		setLayout(new BorderLayout());
		add(board, BorderLayout.CENTER);
		
		JPanel resetPanel = new JPanel();
		resetPanel.setLayout(new BorderLayout());
		
		JButton resetButton = new JButton("Restart");
		resetButton.addActionListener(this);
		resetPanel.add(resetButton, BorderLayout.EAST);
		resetPanel.add(message, BorderLayout.CENTER);
		
		add(resetPanel, BorderLayout.SOUTH);
		
		board.addSpotListener(this);
		
		resetGame();
	}
	
	private void resetGame() {
		for (Spot s: board) {
			s.clearSpot();
			s.setBackground(Color.GRAY);
		}
		nextPlayer = Player.WHITE;
		message.setText("Ready to play TicTacToe? White goes first!");
		gameWon = false;
		draw = false;
	}
	
	public void actionPerformed(ActionEvent e) {
		resetGame();
	}
	
	public void spotClicked(Spot s) {
		if (gameWon) {
			return;
		}
		if (s.isEmpty()) {
			String playerName = null;
			String nextPlayerName = null;
			Color playerColor = null;
			
			if (nextPlayer == Player.WHITE) {
				playerColor = Color.WHITE;
				playerName = "White";
				nextPlayerName = "Black";
				nextPlayer = Player.BLACK;
			} else {
				playerColor = Color.BLACK;
				playerName = "Black";
				nextPlayerName = "White";
				nextPlayer = Player.WHITE;			
			}
		
			s.setSpotColor(playerColor);
			s.toggleSpot();
			s.unhighlightSpot();
			
			gameWon = isWon(playerColor);		
			draw = isDraw();
			
			if (gameWon) {
			message.setText(playerName + " won.");
			}
			else if (draw) {
			message.setText("Game is a Draw");
			}
			else {
			message.setText(nextPlayerName + "'s turn.");
			}
		}	
	}
	
	public void spotEntered(Spot s) {
		if (!s.isEmpty() || gameWon) {
			return;
		}
		s.highlightSpot();
	}
	
	public void spotExited(Spot s) {
		s.unhighlightSpot();
	}
	
	private boolean isWon(Color c) {	
		// row check and column check 
		for (int i = 0; i < 3; i++) {
			if (!board.getSpotAt(i, 0).isEmpty() && !board.getSpotAt(i, 1).isEmpty() && !board.getSpotAt(i, 2).isEmpty()) {
				if ((board.getSpotAt(i, 0).getSpotColor() == c) &&
						(board.getSpotAt(i, 1).getSpotColor() == c) &&
						(board.getSpotAt(i, 2).getSpotColor() == c)) {	
					return true;
				}
			}
			if (!board.getSpotAt(0, i).isEmpty() && !board.getSpotAt(1, i).isEmpty() && !board.getSpotAt(2, i).isEmpty()) {
				if ((board.getSpotAt(0, i).getSpotColor() == c) &&
						(board.getSpotAt(1, i).getSpotColor() == c) &&
						(board.getSpotAt(2, i).getSpotColor() == c)) {
					return true;
				}
			}
		}
		// diagonal
		if (!board.getSpotAt(0, 0).isEmpty() && !board.getSpotAt(1,  1).isEmpty() && !board.getSpotAt(2, 2).isEmpty()) {
			if (board.getSpotAt(0, 0).getSpotColor() == c &&
					board.getSpotAt(1, 1).getSpotColor() == c &&
					board.getSpotAt(2, 2).getSpotColor() == c) {
				return true;
			}
		}
		if (!board.getSpotAt(0, 2).isEmpty() && !board.getSpotAt(1,  1).isEmpty() && !board.getSpotAt(2, 0).isEmpty()) {
			if (board.getSpotAt(0, 2).getSpotColor() == c &&
					board.getSpotAt(1, 1).getSpotColor() == c &&
					board.getSpotAt(2, 0).getSpotColor() == c) {
				return true;
			}
		}
	
		return false;	
		
	}

	private boolean isDraw() {
		if(!gameWon) {
			for (Spot s: board) {
				if (s.isEmpty()) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
