package a7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OthelloWidget extends JPanel implements ActionListener, SpotListener{

	private enum Player {BLACK, WHITE};
	
	private JSpotBoard board;
	private JLabel message;
	private Player nextPlayer;
	private boolean gameOver;
	private ArrayList<Spot> flip;
	private Color playerColor;
	private Color oppositeColor;
	
	public OthelloWidget() {
		
		board = new JSpotBoard(8, 8);
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
			if (board.getSpotAt(3, 3) == s || s == board.getSpotAt(4, 4)) {
				s.setSpotColor(Color.WHITE);
				s.toggleSpot();
			}
			else if (board.getSpotAt(3, 4) == s || s == board.getSpotAt(4, 3)){
				s.setSpotColor(Color.BLACK);
				s.toggleSpot();
			}			
		}
		nextPlayer = Player.BLACK;
		playerColor = Color.BLACK;
		oppositeColor = Color.BLACK;
		message.setText("Ready to play Othello? Black goes first!");
		gameOver = false;
	}
	
	public void actionPerformed(ActionEvent e) {
		resetGame();
	}
	
	public void spotClicked (Spot s) {
		if (gameOver) {
			return;
		}
		if (!s.isEmpty()) {
			return;
		}
		flip = isSpotValid(s, oppositeColor);
		if (!(flip.size() > 0)) {
			return;
		}
		
	//	switchTurns(playerColor);
		
		String nextPlayerName = null;
		
		if (nextPlayer == Player.BLACK) {
			playerColor = Color.BLACK;
			oppositeColor = Color.WHITE;
			nextPlayerName = "White";
			nextPlayer = Player.WHITE;
		} else {
			playerColor = Color.WHITE;
			oppositeColor = Color.BLACK;
			nextPlayerName = "Black";
			nextPlayer = Player.BLACK;			
		}
		
		for (Spot f: flip) {
			if (f.isEmpty()) {
				f.setSpotColor(playerColor);
				f.toggleSpot();
			}
			else {
				f.clearSpot();
				f.setSpotColor(playerColor);
				f.toggleSpot();
			}
		}
		gameOver = isGameOver(playerColor);
		
		if (gameOver) {
			int countB = 0;
			int countW = 0;
			for (Spot x: board) {
				if (!x.isEmpty()) {
					if (x.getSpotColor() == Color.BLACK) {
						countB++;
					}
					if (x.getSpotColor() == Color.WHITE) {
						countW++;
					}
				}	
			}
			if (countB > countW) {
				message.setText("Black wins by a score of " + countB + " to " + countW);
			}
			else if (countW > countB) {
				message.setText("White wins by a score of " + countW + " to " + countB);
			}
			else
				message.setText("Draw by score of " + countW + " to " + countB);
		}
		else if (switchTurns(oppositeColor)) {
			if (nextPlayer == Player.BLACK) {
				playerColor = Color.WHITE;
				oppositeColor = Color.WHITE;
				nextPlayerName = "White";
				nextPlayer = Player.WHITE;
				
				message.setText(nextPlayerName + "'s turn. Black had no valid move."); 
			} else {
				playerColor = Color.BLACK;
				oppositeColor = Color.BLACK;
				nextPlayerName = "Black";
				nextPlayer = Player.BLACK;		
				
				message.setText(nextPlayerName + "'s turn. White had no valid move.");
			}
		}
		else {
			message.setText(nextPlayerName + "'s turn.");
		}
		
	}
	
	public void spotEntered(Spot s) {
		ArrayList<Spot> list = isSpotValid(s, oppositeColor);
		if (list.size() > 0 ) {
			s.highlightSpot();
		}
	}
	
	public void spotExited(Spot s) {
		s.unhighlightSpot();
	}
	
	private ArrayList<Spot> isSpotValid(Spot s, Color c) {
		ArrayList<Spot> arr = new ArrayList<Spot>();
		if (!s.isEmpty()) {
			return arr;
		}
		
		boolean flank = false;
		boolean next = false;	// variable that toggles to true if neighbor color is opposite
		Spot cap = null;
		// vertical down
		for (int i = s.getSpotY() + 1; i < 8; i++) {
			if (!next) {
				if (!board.getSpotAt(s.getSpotX(), i).isEmpty()) {
					if (board.getSpotAt(s.getSpotX(), i).getSpotColor() != c) {
						next = true;
					}
					else {
						break;
					}
				}
				else {
					break;
				}
			}
			else {
				if (board.getSpotAt(s.getSpotX(), i).isEmpty()) {
					break;
				}
				if (board.getSpotAt(s.getSpotX(), i).getSpotColor() == c) {
					cap = board.getSpotAt(s.getSpotX(), i);
					flank = true;
					break;
				}
			}
		}
		if (flank) {
			for (int i = s.getSpotY(); i < cap.getSpotY(); i++) {
				arr.add(board.getSpotAt(s.getSpotX(), i));
			}
		}
		
		flank = false;
		next = false;
		cap = null;
		// vertical up
		for (int i = s.getSpotY()-1; i > -1; i--) {
			if (!next) {
				if (!board.getSpotAt(s.getSpotX(), i).isEmpty()) {
					if (board.getSpotAt(s.getSpotX(), i).getSpotColor() != c) {
						next = true;
					}
					else {
						break;
					}
				}
				else {
					break;
				}
			}
			else {
				if (board.getSpotAt(s.getSpotX(), i).isEmpty()) {
					break;
				}
				if (board.getSpotAt(s.getSpotX(), i).getSpotColor() == c) {
					cap = board.getSpotAt(s.getSpotX(), i);
					flank = true;
					break;
				}
			}
		}
		if (flank) {
			for (int i = s.getSpotY(); i > cap.getSpotY(); i--) {
				if (!arr.contains(board.getSpotAt(s.getSpotX(), i))) {
					arr.add(board.getSpotAt(s.getSpotX(), i)); 
				}
			}
		}
		// horizontal right
		flank = false;
		next = false;
		cap = null;
		for (int i = s.getSpotX() + 1; i < 8; i++) {
			if (!next) {
				if (!board.getSpotAt(i, s.getSpotY()).isEmpty()) {
					if (board.getSpotAt(i, s.getSpotY()).getSpotColor() != c) {
						next = true;
					}
					else {
						break;
					}
				}
				else {
					break;
				}
			}
			else {
				if (board.getSpotAt(i, s.getSpotY()).isEmpty()) {
					break;
				}
				if (board.getSpotAt(i, s.getSpotY()).getSpotColor() == c) {
					cap = board.getSpotAt(i, s.getSpotY());
					flank = true;
					break;
				}
			}
		}
		if (flank) {
			for (int i = s.getSpotX(); i < cap.getSpotX(); i++) {
				if (!arr.contains(board.getSpotAt(i, s.getSpotY()))) {
					arr.add(board.getSpotAt(i, s.getSpotY()));
				}
			}
		}
		// horizontal left
		flank = false;
		next = false;
		cap = null;
		for (int i = s.getSpotX() - 1; i > -1; i--) {
			if (!next) {
				if (!board.getSpotAt(i, s.getSpotY()).isEmpty()) {
					if (board.getSpotAt(i, s.getSpotY()).getSpotColor() != c) {
						next = true;
					}
					else {
						break;
					}
				}
				else {
					break;
				}
			}
			else {
				if (board.getSpotAt(i, s.getSpotY()).isEmpty()) {
					break;
				}
				if (board.getSpotAt(i, s.getSpotY()).getSpotColor() == c) {
					cap = board.getSpotAt(i, s.getSpotY());
					flank = true;
					break;
				}
			}
		}
		if (flank) {
			for (int i = s.getSpotX(); i > cap.getSpotX(); i--) {
				if (!arr.contains(board.getSpotAt(i, s.getSpotY()))) {
					arr.add(board.getSpotAt(i, s.getSpotY()));
				}
			}
		}
	    //diagonal left down
		flank = false;
		next = false;
		cap = null;
		for (int i = 1; i < 8; i++) {
			if (!next) {
				if (s.getSpotX()+i < 8 && s.getSpotY() +i < 8 && 
						!board.getSpotAt(s.getSpotX()+i, s.getSpotY()+i).isEmpty() &&
						board.getSpotAt(s.getSpotX()+i, s.getSpotY()+i).getSpotColor() != c) {
					next = true;
				}
				else {
					break;
				}
			}
			else {
				if (!(s.getSpotX()+i < 8 && s.getSpotY()+i < 8)) {
					break;
				}
				if (board.getSpotAt(s.getSpotX()+i, s.getSpotY()+i).isEmpty()) {
					break;
				}
				if (board.getSpotAt(s.getSpotX()+i, s.getSpotY()+i).getSpotColor() == c) {
					cap = board.getSpotAt(s.getSpotX()+i, s.getSpotY()+i);
					flank = true;
					break;
				}
			}
		}
		if (flank) {
			for (int i = 0; i < cap.getSpotX()-s.getSpotX(); i++) {
				if(!arr.contains(board.getSpotAt(s.getSpotX()+i, s.getSpotY()+i))) {
					arr.add(board.getSpotAt(s.getSpotX()+i, s.getSpotY()+i));
				}
			}
		}
		// diagonal left up
		flank = false;
		next = false;
		cap = null;
		for (int i = 1; i < 8; i++) {
			if (!next) {
				if (s.getSpotX()-i > -1 && s.getSpotY() -i > -1 && 
						!board.getSpotAt(s.getSpotX()-i, s.getSpotY()-i).isEmpty() &&
						board.getSpotAt(s.getSpotX()-i, s.getSpotY()-i).getSpotColor() != c) {
					next = true;
				}
				else {
					break;
				}
			}
			else {
				if (!(s.getSpotX()-i > -1 && s.getSpotY() -i > -1)) {
					break;
				}
				if (board.getSpotAt(s.getSpotX()-i, s.getSpotY()-i).isEmpty()) {
					break;
				}
				if (board.getSpotAt(s.getSpotX()-i, s.getSpotY()-i).getSpotColor() == c) {
					cap = board.getSpotAt(s.getSpotX()-i, s.getSpotY()-i);
					flank = true;
					break;
				}
			}
		}
		if (flank) {
			for (int i = 0; i < s.getSpotX()-cap.getSpotX(); i++) {
				if(!arr.contains(board.getSpotAt(s.getSpotX()-i, s.getSpotY()-i))) {
					arr.add(board.getSpotAt(s.getSpotX()-i, s.getSpotY()-i));
				}
			}
		}
		
		// diagonal right down
		flank = false;
		next = false;
		cap = null;
		for (int i = 1; i < 8; i++) {
			if (!next) {
				if (s.getSpotX()-i > -1 && s.getSpotY() +i < 8 && 
						!board.getSpotAt(s.getSpotX()-i, s.getSpotY()+i).isEmpty() &&
						board.getSpotAt(s.getSpotX()-i, s.getSpotY()+i).getSpotColor() != c) {
					next = true;
				}
				else {
					break;
				}
			}
			else {
				if (!(s.getSpotX()-i > -1 && s.getSpotY()+i < 8)) {
					break;
				}
				if (board.getSpotAt(s.getSpotX()-i, s.getSpotY()+i).isEmpty()) {
					break;
				}
				if (board.getSpotAt(s.getSpotX()-i, s.getSpotY()+i).getSpotColor() == c) {
					cap = board.getSpotAt(s.getSpotX()-i, s.getSpotY()+i);
					flank = true;
					break;
				}
			}
		}
		if (flank) {
			for (int i = 0; i < s.getSpotX()-cap.getSpotX(); i++) {
				if(!arr.contains(board.getSpotAt(s.getSpotX()-i, s.getSpotY()+i))) {
					arr.add(board.getSpotAt(s.getSpotX()-i, s.getSpotY()+i));
				}
			}
		}
		
		// diagonal right up
		flank = false;
		next = false;
		cap = null;
		for (int i = 1; i < 8; i++) {
			if (!next) {
				if (s.getSpotX()+i < 8 && s.getSpotY() -i > -1 && 
						!board.getSpotAt(s.getSpotX()+i, s.getSpotY()-i).isEmpty() &&
						board.getSpotAt(s.getSpotX()+i, s.getSpotY()-i).getSpotColor() != c) {
					next = true;
				}
				else {
					break;
				}
			}
			else {
				if (!(s.getSpotX()+i < 8 && s.getSpotY()-i > -1)) {
					break;
				}
				if (board.getSpotAt(s.getSpotX()+i, s.getSpotY()-i).isEmpty()) {
					break;
				}
				if (board.getSpotAt(s.getSpotX()+i, s.getSpotY()-i).getSpotColor() == c) {
					cap = board.getSpotAt(s.getSpotX()+i, s.getSpotY()-i);
					flank = true;
					break;
				}
			}
		}
		if (flank) {
			for (int i = 0; i < cap.getSpotX()-s.getSpotX(); i++) {
				if(!arr.contains(board.getSpotAt(s.getSpotX()+i, s.getSpotY()-i))) {
					arr.add(board.getSpotAt(s.getSpotX()+i, s.getSpotY()-i));
				}
			}
		}
		
		return arr;
	}

	private boolean switchTurns(Color c) {
		for (Spot s: board) {
			ArrayList<Spot> list = isSpotValid(s, c);
			if (list.size() > 0) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isGameOver(Color c) {
		for (Spot s: board) {
			ArrayList<Spot> list = isSpotValid(s, c);
			if (list.size() > 0) {
				return false;
			}
		}
		if (c == Color.BLACK) {
			c = Color.WHITE;
		}
		else {
			c = Color.BLACK;
		}
		
		for (Spot s : board) {
			ArrayList<Spot> list = isSpotValid(s, c);
			if (list.size() > 0) {
				return false;
			}
		}
		
		return true;
	}
}
