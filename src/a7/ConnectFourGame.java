package a7;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ConnectFourGame {

	public static void main(String[] args) {
		
		JFrame mainFrame = new JFrame();
		mainFrame.setTitle("ConnectFour");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		mainFrame.setContentPane(topPanel);

		ConnectFourWidget game = new ConnectFourWidget();
		topPanel.add(game, BorderLayout.CENTER);
		
		mainFrame.pack();
		mainFrame.setVisible(true);

	}

}
