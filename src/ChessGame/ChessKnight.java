package ChessGame;

import java.awt.Point;

public class ChessKnight extends ChessSoldier
{
	protected String spritePath = "ChessKnightSprites";
	
	public ChessKnight(int team)
	{
		super(team);
		this.LoadSprites(spritePath);
		type = "knight";
	}
	
	public ChessKnight(int team, Point startingPoint)
	{
		super(team, startingPoint);
		this.LoadSprites(spritePath);
		type = "knight";
	}
	
	public boolean movementLegal(Point newPosition, ChessSoldier occupant)
	{
		Point dP = new Point(newPosition.x-this.position.x, newPosition.y-this.position.y);
		return ((Math.abs(dP.x) == 1) && (Math.abs(dP.y) == 2)) || ((Math.abs(dP.x) == 2) && (Math.abs(dP.y) == 1));
	}
}