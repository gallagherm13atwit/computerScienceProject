import java.util.ArrayList;

import javafx.scene.paint.Color;

public class Tetrimino
{	
	int[] fourBlocks = new int[4];	//int[] array of the four blocks that make up the tetrimino
	ArrayList<Integer> bottomBlocks = new ArrayList<Integer>();	//ArrayList<Integer> that holds the indexes of the tetrimino blocks on the bottom
	int currX = 0;	//int for the current X value for both sideBlocksLeft() and sideBlocksRight() methods
	ArrayList<Integer> sideBlocksLeft = new ArrayList<Integer>();	//ArrayList<Integer> that holds the indexes of the tetrimino blocks that will collide with rectangles on the left side (if any are present)
	int mostLeft = 0;	//int that holds the current index of a rectangle that is closest to the left side of the screen given currX
	ArrayList<Integer> sideBlocksRight = new ArrayList<Integer>();	//ArrayList<Integer> that holds the indexes of the tetrimino blocks that will collide with rectangles on the right side (if any are present)
	int mostRight = 0;	//int that holds the current index of a rectangle that is closest to the right side of the screen given currX
	int rotationPoint = 0;	//int that holds the index of the fourBlocks[] that doesn't move relative to rotation index
	int oldRotPointVal = 0;
	int rotWidth = 0; // Width of rotation of Tetrimino
	int[] rotPointDiff = new int [4]; // Array that stores the location of the other 3 blocks in the Tetrimino surrounding the rotationPoint
	int type = 0;	//int that holds the type of Tetrimino of value 0-6 (in order: Square, Line, T-Block, L-Block, Reverse L-Block, Z Block, and Reverse Z-Block)
	Color colorBlock = Color.BLACK;	//Color object that stores the color of the Tetrimino (set during construction)
	int blockOrientation = 0; // what is the orientation of the block (default is 0)
	int[] colisionBlocks;	// finds the blocks surrounding the Tetrimino that could colide with it if it were to rotate **UNLESS** its a line
	int [] colisionBlocksLine; // finds the blocks surrounding the Tetrimino that could colide with it if it were to rotate **IF** its a line
	int[] simRot = {0, 0, 0, 0};
	int[] temp = {0, 0, 0, 0};
	int [] rotRules = {-11, -10, -9, 1, 11, 10, 9, -1}; //the location of the block in relation to the origin on the grid

	/**
	 * Constructor method that creates a Tetrimino object using
	 * the given integers blockType and rotationWidth. Based on the value of
	 * blockType, fourBlocks[] will be made with the indexes of the
	 * Tetrimino's blocks (from left to right, top to bottom)
	 * as arranged in TetrisGUI as well as their color and
	 * the index of the rotation point.
	 * 
	 * @param rotationWidth
	 * @param blockType
	 */
	public Tetrimino(int rotationWidth, int blockType)
	{
		type = blockType;
		rotWidth = rotationWidth;

		if (rotationWidth == 2 && blockType == 0)	//Square
		{
			fourBlocks[0] = 4;
			fourBlocks[1] = 5;
			fourBlocks[2] = 14;
			fourBlocks[3] = 15;
			this.rotationPoint = 0;
			colorBlock = Color.YELLOW;
		}
		else if (rotationWidth == 3 && blockType > 1)
		{
			if (blockType == 2)	//T-Block
			{
				fourBlocks[0] = 4; 
				fourBlocks[1] = 13;
				fourBlocks[2] = 14;
				fourBlocks[3] = 15;
				this.rotationPoint = 2;
				colorBlock = Color.PURPLE;
			}
			else if (blockType == 3)	//L-Block
			{
				fourBlocks[0] = 5; 
				fourBlocks[1] = 13;
				fourBlocks[2] = 14;
				fourBlocks[3] = 15;
				this.rotationPoint = 2;
				colorBlock = Color.ORANGE;
			}
			else if (blockType == 4)	//Reverse L-Block
			{
				fourBlocks[0] = 3; 
				fourBlocks[1] = 13;
				fourBlocks[2] = 14;
				fourBlocks[3] = 15;
				this.rotationPoint = 2;
				colorBlock = Color.BLUE;
			}
			else if (blockType == 5)	//Z Block
			{
				fourBlocks[0] = 3; 
				fourBlocks[1] = 4;
				fourBlocks[2] = 14;
				fourBlocks[3] = 15;
				this.rotationPoint = 2;
				colorBlock = Color.RED;
			}
			else if (blockType == 6)	//Reverse Z-Block
			{
				fourBlocks[0] = 4; 
				fourBlocks[1] = 5;
				fourBlocks[2] = 13;
				fourBlocks[3] = 14;
				this.rotationPoint = 3;
				colorBlock = Color.LIGHTGREEN;
			}
		}
		else if (rotationWidth == 4 && blockType == 1)	//Line Block
		{
			fourBlocks[0] = 3; 
			fourBlocks[1] = 4;
			fourBlocks[2] = 5;
			fourBlocks[3] = 6;
			this.rotationPoint = 0;
			colorBlock = Color.LIGHTBLUE;

		}
		
		this.oldRotPointVal = fourBlocks[rotationPoint];
	}
	
	public boolean alreadyIndex(int value)
	{
		for (int i = 0; i < 4; i++)
		{
			if (value == fourBlocks[i])
				return true;
		}
		
		return false;
	}
	
	public void updateOrder()
	{
		boolean allCorrect = false;
		
		while (!allCorrect)
		{
			int prevVal = 0;
			int tempVal = 0;
			int order = 0;
			for (int i = 1; i < 4; i++)
			{
				if (fourBlocks[i] < fourBlocks[prevVal])
				{
					tempVal = fourBlocks[i];
					fourBlocks[i] = fourBlocks[prevVal];
					fourBlocks[prevVal] = tempVal;
				}
				else
					order++;
				
				prevVal++;
			}
			if (order == 3)
			{
				allCorrect = true;
			}
		}
		
		for (int j = 0; j < 4; j++)
		{
			if (fourBlocks[j] == oldRotPointVal)
			{
				rotationPoint = j;
				oldRotPointVal = fourBlocks[j];
			}
		}
	}
	
	public int[] simRotateRight()
	{
		simRot = this.getRotPointDiff();
		temp = this.getRotPointDiff();
		for (int j = 0; j < 4; j++)
		{
			for (int i = 0; i < 8; i++)
			{
				if (j == rotationPoint)
				{
					simRot[j] = fourBlocks[j];
				}
				else if (temp[j] == rotRules[i])
				{
					simRot[j] = fourBlocks[rotationPoint] + rotRules[(i+2)%8];
				}
			}
		}
		
		return simRot;
	}
	
	public void rotateRight(int condition)
	{
		simRot = this.getRotPointDiff();
		
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				if (simRot[j] == rotRules[i])
				{
					fourBlocks[j] = fourBlocks[rotationPoint] + rotRules[(i+2)%8];
				}
			}
		}
		
		if (condition == 0)
		{
			for (int i = 0; i < 4; i++)
			{
				fourBlocks[i] += 1;
			}
			oldRotPointVal += 1;
		}
		else if (condition == 1)
		{
			for (int i = 0; i < 4; i++)
			{
				fourBlocks[i] -= 1;
			}
			oldRotPointVal-=1;
		}
		else if (condition == 2)
		{
			for (int i = 0; i < 4; i++)
			{
				fourBlocks[i] -= 10;
			}
			oldRotPointVal-=10;
		}
		
		this.changeOrientation(true);
		this.updateOrder();
	}
	
	/**
	 * This is mainly going to be the same thing as simRotateRight
	 * but instead of "rotRules[(i+2)%8]" it'll be
	 * "rotRules[(i-2)%8] (like you said).
	 */
	public int[] simRotateLeft()
	{
		simRot = this.getRotPointDiff();
		temp = this.getRotPointDiff();
		for (int j = 0; j < 4; j++)
		{
			for (int i = 0; i < 8; i++)
			{
				if (j == rotationPoint)
				{
					simRot[j] = fourBlocks[j];
				}
				else if (temp[j] == rotRules[i])
				{
					simRot[j] = fourBlocks[rotationPoint] + rotRules[(i+6)%8];
				}
			}
		}
		
		return simRot;
	}
	
	/**
	 * This is mainly going to be the same thing as rotateRight
	 * but instead of "rotRules[(i+2)%8]" it'll be
	 * "rotRules[(i-2)%8] (like you said).
	 */
	public void rotateLeft(int condition)
	{
		simRot = this.getRotPointDiff();
		
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				if (simRot[j] == rotRules[i])
				{
					fourBlocks[j] = fourBlocks[rotationPoint] + rotRules[(i+6)%8];
				}
			}
		}
		
		if (condition == 0)
		{
			for (int i = 0; i < 4; i++)
			{
				fourBlocks[i] += 1;
			}
			oldRotPointVal += 1;
		}
		else if (condition == 1)
		{
			for (int i = 0; i < 4; i++)
			{
				fourBlocks[i] -= 1;
			}
			oldRotPointVal-=1;
		}
		else if (condition == 2)
		{
			for (int i = 0; i < 4; i++)
			{
				fourBlocks[i] -= 10;
			}
			oldRotPointVal-=10;
		}
		
		this.changeOrientation(false);
		this.updateOrder();
	}
	
	/**
	 * This is going to be called every time the rotation point
	 * is up against a wall/block, hence the boolean wall parameter.
	 * If it's true, then the wall/block will be on the RIGHT side
	 * and result in the block being pushed left (aka, subtract 1 from the result).
	 * If it's false, then the wall/block will be on the LEFT side and 
	 * need to be pushed right (aka, add 1 to the result).
	 * Rotation is needed to figure out which simRotate to call (that'll be
	 * used to update simRot). You should only call this ONCE to see if it'll work.
	 * If it doesn't then I'd try and repeat the process in TetrisGUI but
	 * update the already existing int[] used for this call and try it then,
	 * I don't think this'll work if you call it twice since the Tetrimino
	 * values aren't being updated yet.
	 * 
	 * @param wall: tells if the wall/block next to the rotationPoint is on the right (true) or the left (false)
	 * @param rotation: tells if the block is meant to be rotated right (true) or left (false)
	 * @return a new simRot that will be a version of simRotate[Left/Right] pushed left or simRotate[Left/Right] pushed right
	 */
	public int[] simWallKick(boolean wall, boolean rotation)
	{
		simRot = this.getRotPointDiff();
		
		if (rotation)	//if rotating right
		{
			simRot = this.simRotateRight();
		}
		else//if rotating left
		{
			simRot = this.simRotateLeft();
		}
		
		if (wall)	//if wall is right of rotPoint
		{
			for (int i = 0; i < 4; i++)
				simRot[i] -= 1;
		}
		else //if wall is left of rotPoint
		{
			for (int i = 0; i < 4; i++)
				simRot[i] += 1;
		}
		
		return simRot;
	}
	
	/**
	 * This is going to be called every time the rotation point
	 * is up against the floor. You'll need to call simRotateRight or simRotateLeft (hence the rotation boolean)
	 * and return that result - 10. Just like simWallKick, this should really only be called once,
	 * and if it doesn't work in TetrisGUI then it should be updated INSIDE TetrisGUI
	 * (again, the Tetrimino values themselves aren't being updated, so it wouldn't work out).
	 * 
	 * @param rotation: true if the user is rotating right, false if rotating left
	 * @return a new simRot that will subtract ten from all values
	 */
	public int[] simFloorKick(boolean rotation)
	{
		simRot = this.getRotPointDiff();
		
		if (rotation)
			simRot = simRotateRight();
		else
			simRot = simRotateLeft();
		
		for (int i = 0; i < 4; i++)
		{
			simRot[i] -= 10;
		}
		
		return simRot;
	}

	//Returns the fourBlocks integer array
	public int[] getBlocks()
	{
		return fourBlocks;
	}

	//Returns colorBlock
	public Color getColor()
	{
		return colorBlock;
	}

	//Returns the Type of Tetrimino;
	public int getType()
	{
		return type;
	}
	
	//Returns the RotationWidth of the Tetrimino
	public int getRotWidth()
	{
		return rotWidth;
	}
	
	//Returns the rotationPoint of the Tetrimino
	public int getRotationPoint()
	{
		return rotationPoint;
	}
	
	public int getRotPointIndex()
	{
		return fourBlocks[rotationPoint];
	}
	
	//Returns the current orientation of the Tetrimino
	public int getOrientation()
	{
		return blockOrientation;
	}
	
	//Returns the value of fourBlocks[index]
	public int getBlockPlace(int index)
	{
		return fourBlocks[index];
	}

	//Updates the values of fourBlocks[index] by valAdded
	public void changeBlock(int index, int valAdded)
	{
		fourBlocks[index] += valAdded;
		if (oldRotPointVal == fourBlocks[index] - valAdded)
			oldRotPointVal = fourBlocks[index];
	}
	
	
	//Updates the Orientation of the current Tetrimino -- rotate clockwise if true, rotate counterclockwise if false
	public void changeOrientation(boolean rotation)
	{
		if (rotation == true) { 
			blockOrientation = (blockOrientation + 1) % 4;
		} else { 
			if (blockOrientation == 0) {
				blockOrientation = 3;
			} else {
				blockOrientation = (blockOrientation - 1) % 4;
			}
		}
	}
	
	public int[] getRotPointDiff() {
		
		for (int i = 0; i < 4; ++i) {
			rotPointDiff[i] = fourBlocks[i] - fourBlocks[rotationPoint];
		}
		
		return rotPointDiff;
	}
		
	public void changeColisionBlocks() {
		
	}

	/**
	 * Returns bottomBlocks<Integer> after finding and
	 * adding the Tetrimino's blocks which have no
	 * block found under itself (example: a square
	 * would return the indexes of the 3rd and 4th block)
	 * 
	 * @return
	 */
	public ArrayList<Integer> bottomBlocks()
	{
		bottomBlocks.clear();	//clears the array to "freshen it up"

		bottomBlocks.add(fourBlocks[3]);

		if (type == 1 && fourBlocks[0]/10 == fourBlocks[3]/10)
		{
			bottomBlocks.add(fourBlocks[2]);
			bottomBlocks.add(fourBlocks[1]);
			bottomBlocks.add(fourBlocks[0]);
		}
		else
		{
			for (int j = 2; j > -1; j--)
			{
				if (bottomBlocks.indexOf(fourBlocks[j] + 10) == -1
						&& bottomBlocks.indexOf(fourBlocks[j] + 20) == -1)
					bottomBlocks.add(fourBlocks[j]);
			}
		}

		return bottomBlocks;
	}

	/**
	 * Returs sideBlocksRight<Integer> after finding
	 * and adding the Tetrimino's blocks (from top to bottom)
	 * which are closest to the right-hand side of the screen
	 * for each row (example: a Z-Block would return
	 * the indexes of the 2nd and 4th block)
	 * 
	 * @return
	 */
	public ArrayList<Integer> sideBlocksRight()
	{
		sideBlocksRight.clear();	//clear the array to "freshen it up"

		currX = fourBlocks[0]/10;	//evaluates currX to the first block
		mostRight = fourBlocks[0];	//evaluates mostRight to the first block
		for (int i = 1; i < 4; i++)	//for-loop that goes through each block starting from the 2nd block
		{
			if (fourBlocks[i]/10 == currX)	//checks if the current block is in the same row as currX
				mostRight = fourBlocks[i];
			else if (fourBlocks[i]/10 > currX)	//checks if the current block is the row below currX
			{
				sideBlocksRight.add(mostRight);	//adds the block deemed mostRight from the row add the current block
				currX = fourBlocks[i]/10;	//changes currX to the row of the current block
				mostRight = fourBlocks[i];	//changes mostRight to the current block
			}
		}
		sideBlocksRight.add(mostRight);	//adds the final block to sideBlocksRight<Integer>

		return sideBlocksRight;	//returns sideBlocksRight<Integer>
	}

	/**
	 * Returns sideBlocksLeft<Integer> after finding
	 * and adding the Tetrimino's blocks (from bottom to top)
	 * which are closest to the left-hand side of the screen
	 * for each row (example: a Z-Block would return
	 * the indexes of the 1st and 3rd block)
	 * 
	 * @return
	 */
	public ArrayList<Integer> sideBlocksLeft()
	{
		sideBlocksLeft.clear();	//clears the array to "freshen it up"

		currX = fourBlocks[3]/10;	//evaluates currX to the last block
		mostLeft = fourBlocks[3];	//evaluates mostLeft to the last block
		for (int i = 2; i > -1; i--)	//for-loop that goes through each block starting from the 3rd block
		{
			if (fourBlocks[i]/10 == currX)	//checks if the current block is in the same row as currX
				mostLeft = fourBlocks[i];
			else if (fourBlocks[i]/10 < currX)	//checks if the current block is the row above currX
			{
				sideBlocksLeft.add(mostLeft);	//adds the block deemed mostLeft from the row below the current block
				currX = fourBlocks[i]/10;	//changes currX to the row of the current block
				mostLeft = fourBlocks[i];	//changes mostLeft to the current block
			}
		}
		sideBlocksLeft.add(mostLeft);	//adds the final block to sideBlocksLeft<Integer>

		return sideBlocksLeft;	//returns sideBlocksLeft<Integer>
	}
}