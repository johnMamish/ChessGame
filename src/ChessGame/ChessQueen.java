package ChessGame;

import java.awt.Point;

public class ChessQueen extends ChessSoldier
{
	protected String spritePath = "ChessQueenSprites";
	
	public ChessQueen(int team)
	{
		super(team);
		this.LoadSprites(spritePath);
		type = "queen";
	}
	
	public ChessQueen(int team, Point startingPoint)
	{
		super(team, startingPoint);
		this.LoadSprites(spritePath);
		type = "queen";
	}
	
	public boolean movementLegal(Point newPosition, ChessSoldier occupant)
	{
		Point dP = new Point(this.position.x-newPosition.x, this.position.y-newPosition.y);
		
		//queen is calculated by bishop or rook.
		return (Math.abs(dP.x) == Math.abs(dP.y)) || !(((dP.x) != 0) && ((dP.y) != 0));
	}
}
