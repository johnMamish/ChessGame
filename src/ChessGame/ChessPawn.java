package ChessGame;

import java.awt.Point;


public class ChessPawn extends ChessSoldier
{
	protected String spritePath = "ChessPawnSprites";
	private int direction;			//tells whether the pawn can move forward or backwards.  In retrospect, it would have been better to do this only with the team variable.
	
	public ChessPawn(int team)
	{
		super(team);
		this.LoadSprites(spritePath);
		type = "pawn";
		this.direction = 0;
	}
	
	public ChessPawn(int team, Point startingPoint, int direction)
	{
		super(team, startingPoint);
		this.LoadSprites(spritePath);
		type = "pawn";
		this.direction = direction;
	}
	
	public boolean movementLegal(Point newPosition, ChessSoldier occupant)
	{
		Point dP = new Point(newPosition.x-this.position.x, newPosition.y-this.position.y);
		
		//make sure we are never moving backwards
		if(Math.signum(dP.y) == -Math.signum(direction))
		{
			return false;
		}
		
		//special case for forward diagonal movement where a piece may be taken
		if((Math.abs(dP.x) == 1) && (Math.abs(dP.y) == 1) && (occupant != null))
		{
			return true;
		}
		
		//if we didn't take a piece diagonally, we may not move horizontally and we may not take a piece in front of us.
		if((dP.x != 0) || (occupant != null))
		{
			return false;
		}
		
		//vertical movement of 2 is allowed iff the number of times moved is 0.
		if(Math.abs(this.position.y-newPosition.y) > 1)
		{
			return (this.timesMoved == 0) && (Math.abs(this.position.y-newPosition.y) == 2);
		}
		
		//only moving one or zero squares and in the correct direciton.  We're good.
		return true;
	}
	
	public boolean reachedEnd(int ymin, int ymax)
	{
		int newY = (this.position.y+this.direction);
		return (newY < ymin) || (newY > ymax);
	}
}
