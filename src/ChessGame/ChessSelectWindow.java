package ChessGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

public class ChessSelectWindow extends JDialog
{
	final private int squareSize = 70;
	//inner classes
	private class AnimationTask extends TimerTask
	{
		public void run()
		{
			selectPanel.repaint();
		}
	}

	private class CloseWindowListener implements ActionListener
	{
		private JDialog parent;
		
		public CloseWindowListener(JDialog parent)
		{
			this.parent = parent;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			parent.setVisible(false);
		}
	}
	
	class PieceSelector extends JPanel implements MouseListener
	{
		int foocount = 0;
		private Rectangle boundries = new Rectangle();
		
		private ChessSoldier[] pieceOptions;
		private int selected;
		
		public PieceSelector(ChessSoldier[] pieces)
		{
			this.pieceOptions = pieces;
			if(this.pieceOptions.length > 0)
			{
				selected = 0;
			}
			
			this.addMouseListener(this);
		}
		
		//return piece currently selected
		public ChessSoldier getSelected()
		{
			return pieceOptions[selected];
		}

		public void mouseReleased(MouseEvent e)
		{
			boundries.x = (this.getWidth()-pieceOptions.length*squareSize)/2;
			boundries.y = (this.getHeight()-squareSize)/2;
			boundries.width = pieceOptions.length*squareSize;
			boundries.height = squareSize;
			int point = (e.getPoint().x-boundries.x)/squareSize;
			if(point < pieceOptions.length)
			{
				selected = point;
			}
		}

		public void mouseClicked(MouseEvent e){}
		public void mousePressed(MouseEvent e){}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
		
		public void paintComponent(Graphics g)
		{
			boundries.x = (this.getWidth()-pieceOptions.length*squareSize)/2;
			boundries.y = (this.getHeight()-squareSize)/2;
			boundries.width = pieceOptions.length*squareSize;
			boundries.height = squareSize;
			
			super.paintComponent(g);
			
			Graphics2D drawing = (Graphics2D)g;
			
			//drawing.
			
			for(int i = 0;i < pieceOptions.length;i++)
			{
				pieceOptions[i].drawAbsolute(drawing, boundries.x+i*squareSize+((squareSize-pieceOptions[i].getWidth())/2), boundries.y+((squareSize-pieceOptions[i].getHeight())/2));
				
				if(selected == i)
				{
					drawing.drawRect(boundries.x+i*squareSize, boundries.y, squareSize, squareSize);
				}
			}
		}
	}
	
	private PieceSelector selectPanel;
	private JLabel titleLabel;
	
	private JButton ok;
	//private JButton cancel;
	
	public ChessSelectWindow(JFrame parent, String title, boolean modal, String message, final ChessSoldier[] options)
	{
		super(parent, title, modal);
        //setLocationRelativeTo(parent);
        this.setSize(options.length*squareSize+100, 250);
		
		//layout buttons on root
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		titleLabel = new JLabel("Select a piece");
		this.add(titleLabel, c);
		
		c.gridy = 1;
		c.weighty = 3;
		c.fill = GridBagConstraints.BOTH;
		selectPanel = new PieceSelector(options);
		this.add(selectPanel, c);
		
		c.gridy = 2;
		c.weighty = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		ok = new JButton("select");
		ok.addActionListener(new CloseWindowListener(this));
		this.add(ok, c);
		
		this.setResizable(false);
		//add timer to update animations
		AnimationTask update = new AnimationTask();
		Timer animationTimer = new Timer();
		animationTimer.scheduleAtFixedRate(update, 20, 20);
	}
	
	public ChessSoldier getResponse()
	{
		return selectPanel.getSelected();
	}
}
