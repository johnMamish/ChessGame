package ChessGame;

import java.awt.Point;

public class ChessRook extends ChessSoldier
{
	protected String spritePath = "ChessRookSprites";
	
	public ChessRook(int team)
	{
		super(team);
		this.LoadSprites(spritePath);
		type = "rook";
	}
	
	public ChessRook(int team, Point startingPoint)
	{
		super(team, startingPoint);
		this.LoadSprites(spritePath);
		type = "rook";
	}
	
	public boolean movementLegal(Point newPosition, ChessSoldier occupant)
	{
		//xmove nand ymove.
		return !(((this.position.x-newPosition.x) != 0) && ((this.position.y-newPosition.y) != 0));
	}
}