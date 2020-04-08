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
	int type = 0;	//int that holds the type of Tetrimino of value 0-6 (in order: Square, Line, T-Block, L-Block, Reverse L-Block, Z Block, and Reverse Z-Block)
	Color colorBlock = Color.BLACK;	//Color object that stores the color of the Tetrimino (set during construction)
	
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
		
		if (rotationWidth == 2 && blockType == 0)	//Square
		{
			fourBlocks[0] = 4;
			fourBlocks[1] = 5;
			fourBlocks[2] = 14;
			fourBlocks[3] = 15;
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
				colorBlock = Color.PURPLE;
			}
			else if (blockType == 3)	//L-Block
			{
				fourBlocks[0] = 5; 
				fourBlocks[1] = 13;
				fourBlocks[2] = 14;
				fourBlocks[3] = 15;
				colorBlock = Color.ORANGE;
			}
			else if (blockType == 4)	//Reverse L-Block
			{
				fourBlocks[0] = 3; 
				fourBlocks[1] = 13;
				fourBlocks[2] = 14;
				fourBlocks[3] = 15;
				colorBlock = Color.BLUE;
			}
			else if (blockType == 5)	//Z Block
			{
				fourBlocks[0] = 3; 
				fourBlocks[1] = 4;
				fourBlocks[2] = 14;
				fourBlocks[3] = 15;
				colorBlock = Color.RED;
			}
			else if (blockType == 6)	//Reverse Z-Block
			{
				fourBlocks[0] = 4; 
				fourBlocks[1] = 5;
				fourBlocks[2] = 13;
				fourBlocks[3] = 14;
				colorBlock = Color.LIGHTGREEN;
			}
			rotationPoint = 2;
		}
		else if (rotationWidth == 4 && blockType == 1)	//Line Block
		{
			fourBlocks[0] = 3; 
			fourBlocks[1] = 4;
			fourBlocks[2] = 5;
			fourBlocks[3] = 6;
			colorBlock = Color.LIGHTBLUE;
			
		}
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
	
	//Returns the value of fourBlocks[index]
	public int getBlockPlace(int index)
	{
		return fourBlocks[index];
	}
	
	//Updates the values of fourBlocks[index] by valAdded
	public void changeBlock(int index, int valAdded)
	{
		fourBlocks[index] += valAdded;
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