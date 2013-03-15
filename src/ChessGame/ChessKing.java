package ChessGame;

import java.awt.Point;

public class ChessKing extends ChessSoldier
{
	protected String spritePath = "ChessKingSprites";
	
	public ChessKing(int team)
	{
		super(team);
		this.LoadSprites(spritePath);
		type = "king";
	}
	
	public ChessKing(int team, Point startingPoint)
	{
		super(team, startingPoint);
		this.LoadSprites(spritePath);
		type = "king";
	}
	
	public boolean movementLegal(Point newPosition, ChessSoldier occupant)
	{
		Point dP = new Point(this.position.x-newPosition.x, this.position.y-newPosition.y);
		
		//king is ok if he moves no more than 1 in any direciton.
		return (Math.abs(dP.x) <= 1) && (Math.abs(dP.y) <= 1);
	}
}