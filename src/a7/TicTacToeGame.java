package a7;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TicTacToeGame {

	public static void main(String[] args) {
		
		JFrame mainFrame = new JFrame();
		mainFrame.setTitle("TicTacToe");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		mainFrame.setContentPane(topPanel);

		TicTacToeWidget game = new TicTacToeWidget();
		topPanel.add(game, BorderLayout.CENTER);
		
		mainFrame.pack();
		mainFrame.setVisible(true);

	}

}
