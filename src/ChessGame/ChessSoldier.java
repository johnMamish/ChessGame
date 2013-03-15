package ChessGame;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public abstract class ChessSoldier
{
	//private variables
	//other
	protected String type = "superclass";
	
	//gameplay
	protected int team;					//what team (black/white) the soldier is on.
	protected Point position;			//where the soldier is on the board, from (0..7, 0..7).
	protected boolean pickedUp;
	protected int timesMoved;
	
	//graphics
	protected int width = 40;			//width and height of sprite, in pixels 
	protected int height = 40;
	protected Point absoluteMousePosition;	//used when the mouse is dragging the piece around.
	protected Point mouseOffset;			//distance of mouse from top left corner when first clicked.
	protected BufferedImage[] sprites = new BufferedImage[2];			//holds the sprites
	
	public ChessSoldier(int team)
	{
		this.team = team;
		this.timesMoved = 0;
	}
	
	public ChessSoldier(int team, Point startingPoint)
	{
		this.team = team;
		this.position = startingPoint;
	}
	
	protected void LoadSprites(String spritePath)
	{
		try
		{
			//System.out.println(getClass().getResource("../" + spritePath + "/whiteSprite.png"));
			sprites[0] = ImageIO.read(getClass().getResource("../" + spritePath + "/whiteSprite.png"));
			sprites[1] = ImageIO.read(getClass().getResource("../" + spritePath + "/blackSprite.png"));
		}
		catch(IOException failedToLoadSprite)
		{
			//replace with window
			System.out.println("failed to load sprites");
			System.exit(1);
		}
	}
	
	//switches rendering mode to picked up and gets absolute mouse position in a good starting place for movement
	public void pickUp(int xOffset, int yOffset, int squareSize, Point click)
	{
		this.pickedUp = true;
		this.absoluteMousePosition = click;
		this.mouseOffset = new Point(click.x-this.getTopLeftCorner(xOffset, yOffset, squareSize).x, click.y-this.getTopLeftCorner(xOffset, yOffset, squareSize).y);
	}
	
	//a movement of <0, 0> should always be legal.
	abstract boolean movementLegal(Point newPosition, ChessSoldier occupant);
	
	public final void setDown(Point newPosition)
	{
		if(!this.position.equals(newPosition))
		{
			this.timesMoved++;
			this.position = newPosition;
		}
		this.pickedUp = false;
	}
	
	//accessor methods
	public int getTeam()
	{
		return team;
	}
	
	public int getTimesMoved()
	{
		return timesMoved;
	}
	
	public String getType()
	{
		return type;
	}
	
	public Point getChessBoardPosition()
	{
		return position;
	}
	
	public Point getTopLeftCorner(int xOffset, int yOffset, int squareSize)
	{
		return new Point(xOffset+squareSize*position.x+Math.abs(squareSize-this.width)/2, yOffset+squareSize*position.y+Math.abs(squareSize-this.height)/2);
	}
	
	public Point getCenterPoint(int xOffset, int yOffset, int squareSize)
	{
		return new Point();
	}
	
	public int getWidth()
	{
		return this.sprites[team].getWidth();
	}
	
	public int getHeight()
	{
		return this.sprites[team].getHeight();
	}
	
	public Rectangle getBoundingBox(int xOffset, int yOffset, int squareSize)
	{
		return new Rectangle(this.getTopLeftCorner(xOffset, yOffset, squareSize).x, this.getTopLeftCorner(xOffset, yOffset, squareSize).y, this.width, this.height);
	}
	
	public String toString()
	{
		return this.type;
	}
	
	public void draw(Graphics2D g2D, int xOffset, int yOffset, int squareSize)
	{
		Point topLeftCorner;
		
		if(this.pickedUp)
		{
			topLeftCorner = new Point(this.absoluteMousePosition.x-this.mouseOffset.x, this.absoluteMousePosition.y-this.mouseOffset.y);
		}
		else
		{
			topLeftCorner = this.getTopLeftCorner(xOffset, yOffset, squareSize);
		}
		
		g2D.drawImage(sprites[this.team], null, topLeftCorner.x, topLeftCorner.y);
	}
	
	public void drawAbsolute(Graphics2D g2D, int x, int y)
	{
		g2D.drawImage(sprites[this.team], null, x, y);
	}
}
