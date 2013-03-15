package ChessGame;

import java.awt.Point;


public class ChessDummy extends ChessSoldier
{
	public ChessDummy(int team)
	{
		super(team);
	}
	
	public ChessDummy(int team, Point p)
	{
		super(team, p);
	}
	
	boolean movementLegal(Point newPosition, ChessSoldier occupant)
	{
		return false;
	}
	
}
