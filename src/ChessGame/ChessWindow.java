package ChessGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.OverlayLayout;


public class ChessWindow extends JFrame
{
	/**
	 *  automatically generated serialVersionUID
	 */
	private static final long serialVersionUID = 5207565084893201311L;
	
	//private classes
	//listens for clicks on the chess board
	private class BoardClickListener implements MouseListener
	{
		private ChessBoard theBoard;
		private JTextArea indicator;
		
		public BoardClickListener(ChessBoard theBoard, JTextArea indicator)
		{
			this.theBoard = theBoard;
			this.indicator = indicator;
		}
		
		public void mousePressed(MouseEvent me)
		{
			this.theBoard.pickUpPieceAtMouse(me.getPoint());
		}

		public void mouseReleased(MouseEvent me)
		{
			//try to move the piece
			String[] errors = this.theBoard.setDownPieceAtMouse(me.getPoint());
			
			//redisplay whose turn it is
			//this.indicator.setText(messages[theBoard.getActivePlayer()] + "\n");
			this.indicator.setText("");
			for(String s:errors)
				this.indicator.append(s + "\n");
		}
		
		public void mouseClicked(MouseEvent arg0){}

		public void mouseEntered(MouseEvent arg0){}

		public void mouseExited(MouseEvent arg0){}
	}
	
	//listens for mouse motion over chess board
	private class BoardMotionListener implements MouseMotionListener
	{
		private ChessBoard theBoard;
		
		public BoardMotionListener(ChessBoard theBoard)
		{
			this.theBoard = theBoard;
		}
		
		public void mouseDragged(MouseEvent me)
		{
			theBoard.dragPieceAtMouse(me.getPoint());
		}
		
		public void mouseMoved(MouseEvent arg0){}
	}
	
	//resets the board when forefit is confirmed
	private class ForefitListener implements ActionListener
	{
		private ChessWindow parent;
		private ChessBoard cb;
		
		public ForefitListener(ChessWindow parent, ChessBoard cb)
		{
			this.parent = parent;
			this.cb = cb;
		}
		
		public void actionPerformed(ActionEvent ae)
		{
			int n = JOptionPane.showConfirmDialog(parent,
					cb.teamNames[cb.getActivePlayer()] + ": Are you sure you want to forefeit?",
				    "Are you sure?",
				    JOptionPane.YES_NO_OPTION);
			if(n == JOptionPane.YES_OPTION)
			{
				JOptionPane.showMessageDialog(parent, cb.teamNames[(cb.getActivePlayer()+1)%2] + " is the victor!!");
				parent.playerStatus.setText("White starts.  Click and drag a piece to move it.");
				cb.initChessPieces();
			}
		}
	}
	
	//updates the chess board every so often
	private class AnimationTask extends TimerTask
	{
		public ArrayList<JPanel> toUpdate;
		
		public AnimationTask()
		{
			toUpdate = new ArrayList<JPanel>();
		}
		
		public void run()
		{
			for(JPanel p:toUpdate)
			{
				p.updateUI();
			}
		}
	}
	
	//private variables
	private JPanel draw;
	private JTextArea playerStatus;
	private ChessBoard theBoard;
	private final int updateRate = 20;
	
	public ChessWindow()
	{
		//draw contains the board, which in turn contains all of the pieces.
		draw = new JPanel();
		draw.setLayout(new OverlayLayout(draw));

		//initialize the text field
		playerStatus = new JTextArea(4, 10);
		playerStatus.setFont(new Font("sansserif", 0, 20));
		playerStatus.setEditable(false);
		playerStatus.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//the chessboard in the JPanel cannot be anonymous on this level so we can recenter it on resize.
		theBoard = new ChessBoard();
		draw.add(theBoard);
		draw.addMouseListener(new BoardClickListener(this.theBoard, this.playerStatus));
		draw.addMouseMotionListener(new BoardMotionListener(this.theBoard));
		draw.setBackground(new Color(0x21610B));
		playerStatus.setText("White starts.  Click and drag a piece to move it.");
		
		JButton forefitButton = new JButton("forefit");
		forefitButton.addActionListener(new ForefitListener(this, theBoard));
		
		//top level layout
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 5;
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		this.add(playerStatus, c);
		
		c.weightx = 1;
		c.gridx = 1;
		c.fill = GridBagConstraints.NONE;
		this.add(forefitButton, c);
		
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		this.add(draw, c);
		
		//add timer to update animations
		AnimationTask update = new AnimationTask();
		update.toUpdate.add(draw);
		Timer animationTimer = new Timer();
		animationTimer.scheduleAtFixedRate(update, 20, updateRate);
	}
	
	//this method is called whenever we resize the window.
	//every time the user resizes the window, we need to shift the chess board about.
	public void validate()
	{
		//call super method so this method can do what it was meant to
		super.validate();
		
		//now my code can freeload.  Center the board if the window is too big, otherwise, put its top left corner at 0, 0.
		this.theBoard.topLeftCorner.x = Math.max((this.draw.getWidth()/2)-this.theBoard.getGraphicWidth()/2, 0);
		this.theBoard.topLeftCorner.y = Math.max((this.draw.getHeight()/2)-this.theBoard.getGraphicHeight()/2, 0);
	}
}
