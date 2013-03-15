package ChessGame;

import javax.swing.JFrame;

public class ChessGame
{
	public static void main(String[] args)
	{
		ChessWindow cw = new ChessWindow();
		cw.setTitle("mamish chess");
		cw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cw.setSize(800, 750);
		cw.setVisible(true);
	}
}