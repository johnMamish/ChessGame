package ChessGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

public final class ChessBoard extends JComponent
{
	/**
	 * automatically generated serialVersionUID
	 */
	private static final long serialVersionUID = -3216590187533749472L;
	
	//private variables
	//chess variables
	private ArrayList<ChessSoldier> chessPieces;
	private ArrayList<ChessSoldier> defeatedPieces;
	private int holdingPieceIndex;	
	private int currentTeam;
	private final Rectangle chessDomain = new Rectangle(0, 0, 8, 8);
	
	//graphics variables
	public Point topLeftCorner;
	private int squareSize;
	private final int boardWidth = 8;
	private final int boardHeight = 8;
	private final Color[] squareColors = {new Color(0xe5, 0xe2, 0xd3), new Color(0x5e, 0x4b, 0x40)};
	public boolean orientToPlayer;
	private final String[] messages = {"It is white's turn.  Click and drag a piece to move it.", "It is black's turn.  Click and drag a piece to move it."};
	public final String[] teamNames = {"White", "Black"};
	
	//debug vars
	private int pathCount;
	
	//method for resetting the board
	public void initChessPieces()
	{
		this.defeatedPieces.clear();
		this.chessPieces.clear();
		
		//initialize all chess pieces and add them to the arraylist.
		//team 0 (white)
		for(int i = 0;i < 8;i++)
		{
			chessPieces.add(new ChessPawn(0, new Point(i, 6), -1));
		}
		chessPieces.add(new ChessRook(0, new Point(0, 7)));
		chessPieces.add(new ChessRook(0, new Point(7, 7)));
		chessPieces.add(new ChessKnight(0, new Point(1, 7)));
		chessPieces.add(new ChessKnight(0, new Point(6, 7)));
		chessPieces.add(new ChessBishop(0, new Point(2, 7)));
		chessPieces.add(new ChessBishop(0, new Point(5, 7)));
		chessPieces.add(new ChessQueen(0, new Point(3, 7)));
		chessPieces.add(new ChessKing(0, new Point(4, 7)));
		
		//team 1 (black)
		for(int i = 0;i < 8;i++)
		{
			chessPieces.add(new ChessPawn(1, new Point(i, 1), 1));
		}
		chessPieces.add(new ChessRook(1, new Point(0, 0)));
		chessPieces.add(new ChessRook(1, new Point(7, 0)));
		chessPieces.add(new ChessQueen(1, new Point(3, 0)));
		chessPieces.add(new ChessKnight(1, new Point(1, 0)));
		chessPieces.add(new ChessKnight(1, new Point(6, 0)));
		chessPieces.add(new ChessBishop(1, new Point(2, 0)));
		chessPieces.add(new ChessBishop(1, new Point(5, 0)));
		chessPieces.add(new ChessKing(1, new Point(4, 0)));
	}
	
	//constructors
	public ChessBoard()
	{
		orientToPlayer = false;
		topLeftCorner = new Point(0, 0);
		squareSize = 70;
		currentTeam = 0;
		this.chessPieces = new ArrayList<ChessSoldier>();
		this.defeatedPieces = new ArrayList<ChessSoldier>();
		
		this.initChessPieces();
	}
	
	public ChessBoard(Point topLeftCorner, int squareSize)
	{
		orientToPlayer = false;
		this.topLeftCorner = topLeftCorner;
		this.squareSize = squareSize;
		currentTeam = 0;
		
		this.initChessPieces();
	}
	
	//user interface methods
	//given a point where the mouse clicked, "selects" the piece at that point
	public void pickUpPieceAtMouse(Point mouseClick)
	{
		for(int i = 0;i < chessPieces.size();i++)
		{
			if(chessPieces.get(i).getBoundingBox(topLeftCorner.x, topLeftCorner.y, squareSize).contains(mouseClick))
			{
				if(chessPieces.get(i).team == currentTeam)
				{
					System.out.println("picked up " + chessPieces.get(i).toString());
					
					//move piece we picked up to the top of the ArrayList
					chessPieces.add(chessPieces.remove(i));
					holdingPieceIndex = chessPieces.size()-1;
					chessPieces.get(holdingPieceIndex).pickUp(topLeftCorner.x, topLeftCorner.y, squareSize, mouseClick);
				}
				return;
			}
		}
		
		//no piece was clicked.
		holdingPieceIndex = -1;
	}
	
	public ChessSoldier getPieceAtPoint(Point p)
	{
		ChessSoldier occupant = null;
		for(ChessSoldier c:chessPieces)
		{
			if(c.position.equals(p))
			{
				occupant = c;
			}
		}
		
		return occupant;
	}
	//move the piece around in a fluid way.
	public void dragPieceAtMouse(Point mouseCursor)
	{
		if(holdingPieceIndex > -1)
		{
			chessPieces.get(holdingPieceIndex).absoluteMousePosition = mouseCursor;
		}
	}
	
	//returns the point that the king of team is at on the ChessSoldier arraylist board
	Point getKingPosition(ArrayList<ChessSoldier> board, int team)
	{
		for(ChessSoldier s:chessPieces)
		{
			if(s.getType().equals("king") && (s.getTeam() == team))
			{
				return new Point(s.position);
			}
		}
		return new Point(-1, -1);
	}
	
	//checks the spaces between the starting and ending space (non-inclusive) to see if the path between start and end is blocked
	//not very good OOP practice here,... but I've been coding for, like 11 hours and I'm sleepy, darnit!
	boolean isPathOpen(Point start, Point end, ArrayList<ChessSoldier> board)
	{
		Point dP = new Point((int)Math.signum(end.x-start.x), (int)Math.signum(end.y-start.y));
		System.out.println("dP = " + dP);
		ChessSoldier lineSearchOccupant = null;
		for(Point checkSpace = new Point(start.x+dP.x, start.y+dP.y);!checkSpace.equals(end) && chessDomain.contains(checkSpace);checkSpace.x += dP.x, checkSpace.y += dP.y)
		{
			for(int i = 0;i < board.size();i++)
			{
				if(board.get(i).position.equals(checkSpace))
				{
					lineSearchOccupant = board.get(i);
				}
				
				if(lineSearchOccupant != null)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	//returns true if the space can be taken by the given soldier in the current configuration (assuming that there is an arbitrary soldier at Point space)
	boolean spaceCheckedBySoldier(Point space, ChessSoldier s, ArrayList<ChessSoldier> board)
	{
		//we need to see if the movement is legal AND the path is open AND the soldier that is guarding is not about to be killed.
		return s.movementLegal(space, new ChessDummy(-1)) && (s.getType().equals("knight") || isPathOpen(s.position, space, board)) && (!space.equals(s.position));
	}
	
	ArrayList<ChessSoldier> soldiersCheckingSpace(Point space, ArrayList<ChessSoldier> board, int team)
	{
		ArrayList<ChessSoldier> result = new ArrayList<ChessSoldier>();
		
		for(ChessSoldier s:board)
		{
			if((s.getTeam() == team) && spaceCheckedBySoldier(space, s, board))
			{
				result.add(s);
			}
		}
		return result;
	}
	//this method is pretty long and I bet I could have done it better, but I was falling asleep.
	//Essentailly, this method checks to see if the movement we made is valid.
	//the method itself checks for things that involve other pieces, like jumping over pieces or killing teammates.
	//method calls to the piece that we are attempting to move are responsible for checking that the piece is allowed to move as we are moving it.
	public String[] setDownPieceAtMouse(Point mouseClick)
	{
		long foo = System.currentTimeMillis();
		pathCount = 0;
		ArrayList<String> messageList = new ArrayList<String>();
		String[] returnMessages;
		boolean moveRestricted = false;
		boolean castleAttempted = false;
		boolean doCastle = false;
		
		//if we weren't even holding a piece, just leave.
		if(holdingPieceIndex == -1)
		{
			return new String[] {messages[currentTeam]};
		}
		
		//target space's integer coordinates with respect to chess board.
		Point boardTargetCoords = new Point((mouseClick.x-topLeftCorner.x)/squareSize, (mouseClick.y-topLeftCorner.y)/squareSize);
		
		//check to see if we're moving out of bounds
		if(!chessDomain.contains(boardTargetCoords))
		{
			messageList.add("you may not move off the board");
			moveRestricted = true;
		}
		System.out.println("time after checking in bounds = " + (System.currentTimeMillis() - foo) + "ms");
		//search for chess piece with the piece's target coordinates
		ChessSoldier occupant = null;
		int occupantIndex = -1;
		for(int i = 0;i < chessPieces.size();i++)
		{
			if(chessPieces.get(i).position.equals(boardTargetCoords))
			{
				occupantIndex = i;
				occupant = chessPieces.get(i);
			}
		}
		System.out.println("time after finding occupant = " + (System.currentTimeMillis() - foo) + "ms");
		//if the occupant is the same as the piece we are trying to move, we
		//might as well make the occupant null so we don't try to delete ourselves.
		if(occupant == chessPieces.get(holdingPieceIndex))
		{
			occupant = null;
		}

		//special case for castling
		//From wikipedia:
		//Castling can only be done if the king has never moved, the rook involved
		//has never moved, the squares between the king and the rook involved are not
		//occupied, the king is not in check, and the king does not cross over or
		//end on a square in which it would be in check.
		//
		//out of the set of logical restrictions placed upon the player while castling,
		//some are included in the set of restrictions placed upon the player while
		//making any other move.
		//Intersection of castling restrictions and general restricitons:
		//	-you may not jump over other pieces
		//	-you may not end in a check
		//Castling restrictions
		//	-neither piece may have moved
		//	-must be a king and rook of the same team
		//so, here, we test all of the castling restrictions and then leave the
		//other restrictions to the rest of the code.  One more thing: we need
		//to make sure that the restrictions that are included in general restrictions
		//but not in castling restrictions are bypassed in the rest of the code:
		//	-dont kill teammates
		//	-is move legal according to the piece
		if((occupant != null) && ((chessPieces.get(holdingPieceIndex).getType().equals("rook") && occupant.getType().equals("king")) || (chessPieces.get(holdingPieceIndex).getType().equals("king") && occupant.getType().equals("rook"))) && (chessPieces.get(holdingPieceIndex).getTeam() == occupant.getTeam()))
		{
			//let rest of method know we TRIED to castle.
			castleAttempted = true;
			
			//test for currently checked
			int inactiveTeam = (currentTeam+1)%2;
			
			//find the coords of the king
			Point kingCoords = (chessPieces.get(holdingPieceIndex).getType().equals("king"))? chessPieces.get(holdingPieceIndex).position:occupant.position;
			ArrayList<ChessSoldier> theThreats = soldiersCheckingSpace(kingCoords, chessPieces, inactiveTeam);
			if(theThreats.size() > 0)
			{
				messageList.add("You may not castle if the king begins the turn in check!");
				moveRestricted = true;
			}
			else if((chessPieces.get(holdingPieceIndex).getTimesMoved() > 0) || (occupant.getTimesMoved() > 0))
			{
				messageList.add("You may not castle if the king or rook has already moved!");
				moveRestricted = true;
			}
			else
			{
				doCastle = true;
			}
			if(!doCastle)
			{
				occupant = null;
			}
		}
		
		//check to make sure that the move is legal according to what the chess piece "knows".  If it isn't, occupant becomes null and boardCoords becomes the piece's current coordinates.
		if(!castleAttempted && !chessPieces.get(holdingPieceIndex).movementLegal(boardTargetCoords, occupant))
		{
			messageList.add(chessPieces.get(holdingPieceIndex).getType() + " may not move like that.");
			moveRestricted = true;
		}
		System.out.println("time after checking move legal = " + (System.currentTimeMillis() - foo) + "ms");
		
		//check to see if we are cannibalizing a teammate
		if(!castleAttempted && (occupant != null) && (chessPieces.get(holdingPieceIndex).getTeam() == occupant.getTeam()))
		{
			messageList.add("you may not kill members of your own army.");
			moveRestricted = true;
		}
		System.out.println("time after checking for cannabalism = " + (System.currentTimeMillis() - foo) + "ms");
		
		//this is a brute force search, but I don't care.
		//now, we need to check if we are running over any other pieces.  Every piece that is subject to the rule against running over other pieces moves in
		//a straight line, so it is safe to check every square in between chessPieces.get(index).position and boardTargetCoords.
		//first, make sure it isn't a knight.  Knights are exempt from the "no running people over" rule.
		if(!chessPieces.get(holdingPieceIndex).getType().equals("knight"))
		{
			//check to see if anyone was in the way
			if(!isPathOpen(chessPieces.get(holdingPieceIndex).position, boardTargetCoords, this.chessPieces))
			{
				messageList.add("Only knight may jump over other pieces!");
				moveRestricted = true;
			}
			System.out.println();
		}
		System.out.println("time after checking no collision = " + (System.currentTimeMillis() - foo) + "ms");
		
		//check to see if we are even moving
		if(chessPieces.get(holdingPieceIndex).position.equals(boardTargetCoords))
		{
			moveRestricted = true;
		}
		System.out.println("time after checking to make sure move = " + (System.currentTimeMillis() - foo) + "ms");
		
		//check to see if setting the piece down would leave the king in a
		//checked position.  Brute force through each piece of opposite color to
		//see if the path to the king is clear and legal
		Point kingEndOfTurnCoords;
		if(chessPieces.get(holdingPieceIndex).getType().equals("king"))
		{
			kingEndOfTurnCoords = boardTargetCoords;
		}
		else
		{
			//find the king of the currently active team
			kingEndOfTurnCoords = this.getKingPosition(this.chessPieces, currentTeam);
			
			//if the king was not found, freak the hell out.
			if(kingEndOfTurnCoords.x == -1)
			{
				JOptionPane.showMessageDialog(this,
					    "Fatal Error: king not found.",
					    "Fatal error",
					    JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		}
		System.out.println("time after finding king= " + (System.currentTimeMillis() - foo) + "ms");
		
		//construct the hypothetical board if the piece moves.  Disregard rules for movement
		//and assume movement happens.  This is just to check if the movement will result 
		//in the protection of a check.  If the movement is not legal, the previous
		//code has already taken care of that.
		ArrayList<ChessSoldier> hypotheticalBoard = new ArrayList<ChessSoldier>(chessPieces);
		
		//remove the piece being held from the arraylist and add a dummy at its would-be coordinates.
		hypotheticalBoard.add(new ChessDummy(hypotheticalBoard.remove(holdingPieceIndex).team, boardTargetCoords));
		
		//if there is an occupant at the target space, remove it from the board
		if(occupant != null)
		{
			hypotheticalBoard.remove(occupant);
		}
		
		int inactiveTeam = (currentTeam+1)%2;
		
		ArrayList<ChessSoldier> theThreats = soldiersCheckingSpace(kingEndOfTurnCoords, hypotheticalBoard, inactiveTeam);
		if(theThreats.size() > 0)
		{
			messageList.add("your turn cannot end with the king in a check!");
			moveRestricted = true;
		}
		
		//if there were no restrictions on the move, we may castle.
		if(doCastle && !moveRestricted)
		{
			int n = JOptionPane.showConfirmDialog(this,
					"Are you sure you want to castle?",
					"Castle",
					JOptionPane.YES_NO_OPTION);

			if(n == JOptionPane.YES_OPTION)
			{
				//move piece that wasn't picked up and set destination for
				//piece that was picked up
				boardTargetCoords = occupant.position;
				occupant.setDown(chessPieces.get(holdingPieceIndex).position);
			}
			else
			{
				//leave pieces in current position
				moveRestricted = true;
			}
			occupant = null;
		}
		
		
		//we have performed all the checks to see if a normal move is allowed.
		//now we can proceed with the post-decision steps, like actually
		//setting the piece down and killing the piece previously occupying the space.

		//if we actually moved the piece, advance the turn and check for check/checkmate
		if(!moveRestricted)
		{
			//set the piece down at the desired cooridnates.
			chessPieces.get(holdingPieceIndex).setDown(boardTargetCoords);

			//check to see if it was a pawn reaching the other side
			if(chessPieces.get(holdingPieceIndex).type.equals("pawn"))
			{
				ChessPawn transformer = (ChessPawn)chessPieces.get(holdingPieceIndex);
				System.out.println("transformer: " + transformer);
				if(transformer.reachedEnd(0, 7))
				{
					//remove pawn
					chessPieces.remove(holdingPieceIndex);

					//prompt user to select piece and wait.
					ChessSelectWindow cw = new ChessSelectWindow(null, "quux", true, "FOOBAR",
							new ChessSoldier[]
							{new ChessQueen(transformer.getTeam(), transformer.position),
							new ChessKnight(transformer.getTeam(), transformer.position),
							new ChessRook(transformer.getTeam(), transformer.position),
							new ChessBishop(transformer.getTeam(), transformer.position)});
					cw.setVisible(true);
					while(cw.isVisible());
					ChessSoldier response = cw.getResponse(); 
					if(response != null)
					{
						chessPieces.add((ChessSoldier)response);
					}
					else
					{
						chessPieces.add(new ChessQueen(transformer.getTeam(), transformer.position));
					}
				}
			}
			
			//advance the team, save previous team for checking check
			int prevTeam = currentTeam;
			currentTeam++;
			currentTeam %= 2;
			
			boolean checkFlag = false;
			boolean mateFlag = false;
			
			//check and see if king of opposite team is in check.
			Point oppositeKingPoint = this.getKingPosition(this.chessPieces, currentTeam);
			ArrayList<ChessSoldier> checkThreats = this.soldiersCheckingSpace(oppositeKingPoint, chessPieces, prevTeam);
			if(checkThreats.size() > 0)
			{
				System.out.println("check");
				checkFlag = true;
				mateFlag = true;
				
				/*
				 * check to see if the king of the opposite team is in checkmate.  This
				 * condition is met if the king is in check AND any of the following:
				 *		-not all of the checkThreats may be killed.
				 *		-all the surrounding spaces are checked.
				 */
				
				//if there is only one threat, it may be blocked or killed.  If
				//there is more than one threat, they may not be both blocked and/
				//or killed in the same turn.
				if(checkThreats.size() == 1)
				{
					Point threatPoint = new Point(checkThreats.get(0).position);
					
					//check to see if there are any members of the king's team checking
					//the piece that is a threat to the king.  This would disqualify a checkmate.
					if(this.soldiersCheckingSpace(threatPoint, chessPieces, currentTeam).size() > 0)
					{
						System.out.println("kill");
						mateFlag = false;
					}
					else if(!checkThreats.get(0).getType().equals("knight"))
					{
						Point dP = new Point((int)Math.signum(oppositeKingPoint.x-threatPoint.x), (int)Math.signum(oppositeKingPoint.y-threatPoint.y));

						//if the threat piece is not a knight and there is a piece
						//guarding the space between the king and the threat, no checkmate
						for(Point checkSpace = new Point(oppositeKingPoint.x+dP.x, oppositeKingPoint.y+dP.y);!checkSpace.equals(threatPoint) && chessDomain.contains(checkSpace);checkSpace.x += dP.x, checkSpace.y += dP.y)
						{
							if(!this.soldiersCheckingSpace(checkSpace, chessPieces, currentTeam).isEmpty())
							{
								System.out.println("block");
								mateFlag = false;
							}
						}
					}
				}
				//check all surrounding spaces to see if any are open
				Point kingTemp = new Point(oppositeKingPoint);

				for(kingTemp.x = oppositeKingPoint.x-1;kingTemp.x <= oppositeKingPoint.x+1;kingTemp.x++)
				{
					for(kingTemp.y = oppositeKingPoint.y-1;kingTemp.y <= oppositeKingPoint.y+1;kingTemp.y++)
					{
						if(!kingTemp.equals(oppositeKingPoint) && chessDomain.contains(kingTemp))
						{
							//if space is open and not our space, it is not checkmate
							if(this.soldiersCheckingSpace(kingTemp, chessPieces, prevTeam).isEmpty() && (getPieceAtPoint(kingTemp) == null))
							{
								System.out.println("openSpace");
								mateFlag = false;
							}
						}
					}
				}
				
				//at this point, we have a checkmate
				if(mateFlag)
				{
					System.exit(1);
				}
				else
				{
					messageList.add("checknomate");
				}
			}
		}
		
		//we didn't move the piece, set it down where it was.
		else
		{
			chessPieces.get(holdingPieceIndex).setDown(chessPieces.get(holdingPieceIndex).position);
		}
		holdingPieceIndex = -1;
		
		//kill the previous piece.
		if((occupant != null) && !moveRestricted)
		{
			String slayed = "A " + this.teamNames[currentTeam] + " " + chessPieces.get(occupantIndex).getType() + " was slain!";
			messageList.add(0, slayed);
			defeatedPieces.add(chessPieces.remove(occupantIndex));		//we successfully put the piece down.  Whoever was holding the space earlier is defeated.
			System.out.println(defeatedPieces.size());
		}

		//print whose turn it is now
		messageList.add(0, messages[currentTeam]);
		
		System.out.println("time at end = " + (System.currentTimeMillis() - foo) + "ms");
		//return
		returnMessages = new String[messageList.size()];
		messageList.toArray(returnMessages);
		return returnMessages;
	}
	
	//returns the active player
	public int getActivePlayer()
	{
		return currentTeam;	//method calls to the piece that we are attempting to move are responsible for checking that the piece is allowed to move as we are moving it.	
	}
	
	//graphics methods
	//returns the width of the board in pixels
	public int getGraphicWidth()
	{
		//because the bounding lines of the grid have an integral width, we need +1 for the outside lines on the right (or left) side.
		return (boardWidth*squareSize)+1;
	}
	
	//returns the height of the board in pixels
	public int getGraphicHeight()
	{
		//because the bounding lines of the grid have an integral width, we need +1 for the outside lines on the bottom (or top) side.
		return (boardHeight*squareSize)+1;
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D drawing = (Graphics2D)g;
		
		//draw bounding square
		//drawing.drawRect(topLeftCorner.x, topLeftCorner.y, boardWidth*squareSize+1, boardHeight*squareSize+1);
		
		//draw grid
		int colorIndex = 0;
		for(int yGrid = 0;yGrid < boardHeight;yGrid++)
		{
			for(int xGrid = 0;xGrid < boardWidth;xGrid++)
			{
				//fill with alternating color first
				drawing.setColor(squareColors[colorIndex]);
				drawing.fillRect(topLeftCorner.x+xGrid*squareSize, topLeftCorner.y+yGrid*squareSize, squareSize, squareSize);
				
				//draw black frame around square
				drawing.setColor(Color.BLACK);
				drawing.drawRect(topLeftCorner.x+xGrid*squareSize, topLeftCorner.y+yGrid*squareSize, squareSize, squareSize);
				
				//change the color
				colorIndex++;
				colorIndex %= squareColors.length;
			}
			//necessary to offset rows.  Without this, would be 8 vertical strips of the same color.
			colorIndex++;
			colorIndex %= squareColors.length;
		}
		//paint each chess piece
		for(ChessSoldier s:chessPieces)
		{
			s.draw(drawing, topLeftCorner.x, topLeftCorner.y, squareSize);
		}
		
		//paint each defeated chess piece
		int[] paintCounter = {0, 0};
		int[] xOffset = {-70, this.getGraphicWidth()+25};
		for(ChessSoldier s:defeatedPieces)
		{
			s.drawAbsolute(drawing, topLeftCorner.x+xOffset[s.getTeam()], (topLeftCorner.y+paintCounter[s.getTeam()]*60));
			paintCounter[s.getTeam()]++;
		}
	}
}
