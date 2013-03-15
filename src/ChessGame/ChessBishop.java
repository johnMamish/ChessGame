package ChessGame;

import java.awt.Point;


public class ChessBishop extends ChessSoldier
{
	protected String spritePath = "ChessBishopSprites";
	
	public ChessBishop(int team)
	{
		super(team);
		this.LoadSprites(spritePath);
		type = "bishop";
	}
	
	public ChessBishop(int team, Point startingPoint)
	{
		super(team, startingPoint);
		this.LoadSprites(spritePath);
		type = "bishop";
	}
	
	public boolean movementLegal(Point newPosition, ChessSoldier occupant)
	{
		return (Math.abs(this.position.x-newPosition.x) == Math.abs(this.position.y-newPosition.y));
	}
}