package a7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConnectFourWidget extends JPanel implements ActionListener, SpotListener {

	private enum Player {RED, BLACK};
	
	private JSpotBoard board;
	private JLabel message;
	private Player nextPlayer;
	private boolean gameWon;
	private boolean draw;
	private Spot lastSpot;
	
	private Spot[] win = new Spot[8];
	
	public ConnectFourWidget () {
		board = new JSpotBoard(7,6);
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
			s.unhighlightSpot();
			if (s.getSpotX() % 2 == 0) {
				s.setBackground(Color.GRAY);
			}
			else
				s.setBackground(Color.LIGHT_GRAY);
		}
		gameWon = false;
		draw = false;
		nextPlayer = Player.RED;
		message.setText("Connect Four. Red's Turn.");
	}
	
	public void actionPerformed(ActionEvent e) {
		resetGame();
	}
	public void spotClicked(Spot s) {
		if (gameWon) {
			return;
		}
		
		boolean emptySpot = false;
		for (int i = 0; i < 6; i++) {
			if (board.getSpotAt(s.getSpotX(), i).isEmpty()) {
				emptySpot = true;
				break;
			}
		}
		
		if (emptySpot) {
			String playerName = null;
			String nextPlayerName = null;
			Color playerColor = null;
			
			if (nextPlayer == Player.RED) {
				playerColor = Color.RED;
				playerName = "Red";
				nextPlayerName = "Black";
				nextPlayer = Player.BLACK;
			} else {
				playerColor = Color.BLACK;
				playerName = "Black";
				nextPlayerName = "Red";
				nextPlayer = Player.RED;			
			}
		
			for (int i = 5; i > -1; i--) {
				if (board.getSpotAt(s.getSpotX(), i).isEmpty()) {
					board.getSpotAt(s.getSpotX(), i).setSpotColor(playerColor);
					board.getSpotAt(s.getSpotX(), i).toggleSpot();
					board.getSpotAt(s.getSpotX(), i).unhighlightSpot();
					lastSpot = board.getSpotAt(s.getSpotX(), i);
					break;
				}
			}
			
			gameWon = isWon(playerColor, lastSpot);		
			draw = isDraw();
			
			if (gameWon) {
				message.setText(playerName + " won.");
				
				for (Spot x: board) {
					x.unhighlightSpot();
				}
				
				lastSpot.highlightSpot();
				for (int x = 0; x < 3; x++) {
					win[x].highlightSpot();
				}
			}
			else if (draw) {
				message.setText("Game is a Draw");
			}
			else {
				message.setText(nextPlayerName + "'s turn.");
			}
		}
	}
	
	private boolean isWon(Color c, Spot s) {
		// vertical check
		if (s.getSpotY() < 3) {
			if (!board.getSpotAt(s.getSpotX(), s.getSpotY()+3).isEmpty() && !board.getSpotAt(s.getSpotX(), s.getSpotY()+2).isEmpty() 
					&& !board.getSpotAt(s.getSpotX(), s.getSpotY()+1).isEmpty()) {
				if (board.getSpotAt(s.getSpotX(), s.getSpotY()+3).getSpotColor() == c &&
						board.getSpotAt(s.getSpotX(), s.getSpotY()+2).getSpotColor() == c && 
						board.getSpotAt(s.getSpotX(), s.getSpotY()+1).getSpotColor() == c) {
					win[0] = board.getSpotAt(s.getSpotX(), s.getSpotY()+3);
					win[1] = board.getSpotAt(s.getSpotX(), s.getSpotY()+2);
					win[2] = board.getSpotAt(s.getSpotX(), s.getSpotY()+1);
					return true;		
				}
			}
		}
		// horizontal check
		int count = 0;
		
		for (int i = s.getSpotX()+1; i < 7; i++) {
			if (!board.getSpotAt(i, s.getSpotY()).isEmpty()) {
				if (board.getSpotAt(i, s.getSpotY()).getSpotColor() != c) {
					break;
				}
				win[count] = board.getSpotAt(i, s.getSpotY());
				count++;
			}
			else
				break;
		}
		for (int i = s.getSpotX()-1; i > -1; i--) {
			if (!board.getSpotAt(i, s.getSpotY()).isEmpty()) {
				if (board.getSpotAt(i, s.getSpotY()).getSpotColor() != c) {
					break;
				}
				win[count] = board.getSpotAt(i, s.getSpotY());
				count++;
			}
			else {
				break;
			}
		}
		if (count >= 3) {
			return true;
		}
		else {
			for (int i = 0; i < 4; i++) {
				win[i] = null;
			}
		}
		// diagonal right 
		int countD = 0;
		
		for (int i = 1; i < 4; i++) {
			if (s.getSpotX()+i < 7 && s.getSpotY() -i > -1) {
				if (!board.getSpotAt(s.getSpotX()+i, s.getSpotY()-i).isEmpty()) {
					if (board.getSpotAt(s.getSpotX()+i, s.getSpotY()-i).getSpotColor() != c) {
						break;
					}
					win[countD] = board.getSpotAt(s.getSpotX()+i, s.getSpotY()-i);
					countD++;
				}
			}
			else
				break;
		}
		for (int i = 1; i < 4; i++) {
			if (s.getSpotX()-i > -1 && s.getSpotY() +i < 6) {
				if (!board.getSpotAt(s.getSpotX()-i, s.getSpotY()+i).isEmpty()) {
					if (board.getSpotAt(s.getSpotX()-i, s.getSpotY()+i).getSpotColor() != c) {
						break;
					}
					win[countD] = board.getSpotAt(s.getSpotX()-i, s.getSpotY()+i);
					countD++;
				}
			}
			else
				break;
		}
		if (countD >= 3) {
			return true;
		}
		else {
			for (int i = 0; i < 4; i++) {
				win[i] = null;
			}
		}
		// diagonal left
		int countL = 0;
		
		for (int i = 1; i < 4; i++) {
			if (s.getSpotX()-i > -1 && s.getSpotY() -i > -1) {
				if (!board.getSpotAt(s.getSpotX()-i, s.getSpotY()-i).isEmpty()) {
					if (board.getSpotAt(s.getSpotX()-i, s.getSpotY()-i).getSpotColor() != c) {
						break;
					}
					win[countL] = board.getSpotAt(s.getSpotX()-i, s.getSpotY()-i);
					countL++;
				}
			}
			else
				break;
		}
		for (int i = 1; i < 4; i++) {
			if (s.getSpotX()+i < 7 && s.getSpotY() +i < 6) {
				if (!board.getSpotAt(s.getSpotX()+i, s.getSpotY()+i).isEmpty()) {
					if (board.getSpotAt(s.getSpotX()+i, s.getSpotY()+i).getSpotColor() != c) {
						break;
					}
					win[countL] = board.getSpotAt(s.getSpotX()+i, s.getSpotY()+i);
					countL++;
				}
			}
			else
				break;
		}
		if (countL >= 3) {
			return true;
		}
		else {
			for (int i = 0; i < 4; i++) {
				win[i] = null;
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
	
	public void spotEntered(Spot s) {
		if (gameWon) {
			return;
		}
		for (int i = 0; i < 6; i++) {
			if (board.getSpotAt(s.getSpotX(), i).isEmpty()) {
				board.getSpotAt(s.getSpotX(), i).highlightSpot();
			}
		}
	}
	
	public void spotExited(Spot s) {
		for (int i = 0; i < 6; i++) {
			if (gameWon) {
				if (board.getSpotAt(s.getSpotX(), i) != win[0] &&
						board.getSpotAt(s.getSpotX(), i) != win[1] &&
						board.getSpotAt(s.getSpotX(), i) != win[2] &&
						board.getSpotAt(s.getSpotX(), i) != lastSpot) {
					board.getSpotAt(s.getSpotX(), i).unhighlightSpot();
				}
			}
			else
				board.getSpotAt(s.getSpotX(), i).unhighlightSpot();
		}
	}
}
