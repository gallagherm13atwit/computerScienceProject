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
	int[] rotPointDiff = new int [4]; // Array that stores the location of the other 3 blocks in the Tetrimino surrounding the rotationPoint
	int type = 0;	//int that holds the type of Tetrimino of value 0-6 (in order: Square, Line, T-Block, L-Block, Reverse L-Block, Z Block, and Reverse Z-Block)
	Color colorBlock = Color.BLACK;	//Color object that stores the color of the Tetrimino (set during construction)
	int[] simRot = {0, 0, 0, 0};	//int[] of the distances between values within fourBlocks[] and fourBlocks[rotationPoint] using corresponding indexes
	int[] temp = {0, 0, 0, 0};	//temporary int[] used to dwitch values of fourBlocks[]
	int[] rotRules = {-11, -10, -9, 1, 11, 10, 9, -1}; //the location of the block in relation to the origin on the grid
	int lineIndex = 0; //holds the int value of the orientation of the line block
	
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
	public Tetrimino(int blockType)
	{
		type = blockType;
		

		if (blockType == 0)	//Square
		{
			fourBlocks[0] = 4;
			fourBlocks[1] = 5;
			fourBlocks[2] = 14;
			fourBlocks[3] = 15;
			this.rotationPoint = 0;
			colorBlock = Color.YELLOW;
		}
		else if (blockType > 1)
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
		else if (blockType == 1)	//Line Block
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
	
	//returns true if value appears within fourBlocks[]
	public boolean alreadyIndex(int value)
	{
		for (int i = 0; i < 4; i++)
		{
			if (value == fourBlocks[i])
				return true;
		}
		
		return false;
	}
	
	//returns the index if it contains the value; returns -1 if no index has that value
	public int getIndex(int value)
	{
		for (int i = 0; i < 4; i++)
		{
			if (fourBlocks[i] == value)
				return i;
		}
		
		return -1;
	}
	
	//updates the order of fourBlocks[] from lowest to highest
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
	
	public int[] simRotateLineRight()
	{
		for (int i = 0; i < 4; i++)
		{
			if (lineIndex == 0)
			{
				if (i == 0)
					simRot[i] = fourBlocks[i] - 8;
				else if (i == 1)
					simRot[i] = fourBlocks[i] + 1;
				else if (i == 2)
					simRot[i] = fourBlocks[i] - 10;
				else
					simRot[i] = fourBlocks[i] + 19;
			}
			else if (lineIndex == 1)
			{
				if (i == 0)
					simRot[i] = fourBlocks[i] + 18;
				else if (i == 1)
					simRot[i] = fourBlocks[i] + 9;
				else if (i == 3)
					simRot[i] = fourBlocks[i] - 9;
			}
			else if (lineIndex == 2)
			{
				if (i == 0)
					simRot[i] = fourBlocks[i] - 19;
				else if (i == 1)
					simRot[i] = fourBlocks[i] - 10;
				else if (i == 2)
					simRot[i] = fourBlocks[i] - 1;
				else
					simRot[i] = fourBlocks[i] + 8;
			}
			else
			{
				if (i == 0)
					simRot[i] = fourBlocks[i] + 9;
				else if (i == 2)
					simRot[i] = fourBlocks[i] - 9;
				else if (i == 3)
					simRot[i] = fourBlocks[i] - 18;
			}
		}
		
		return simRot;
	}
	
	public void rotateLineRight(int condition)
	{
		for (int i = 0; i < 4; i++)
		{
			if (lineIndex == 0)
			{
				if (i == 0)
					fourBlocks[i] -= 8;
				else if (i == 1)
					fourBlocks[i] += 1;
				else if (i == 2)
					fourBlocks[i] += 10;
				else
				{
					fourBlocks[i] += 19;
					lineIndex++;
				}
			}
			else if (lineIndex == 1)
			{
				if (i == 0)
					fourBlocks[i] += 18;
				else if (i == 1)
					fourBlocks[i] += 9;
				else if (i == 3)
				{
					fourBlocks[i] -= 9;
					lineIndex++;
				}
			}
			else if (lineIndex == 2)
			{
				if (i == 0)
					fourBlocks[i] -= 19;
				else if (i == 1)
					fourBlocks[i] -= 10;
				else if (i == 2)
					fourBlocks[i] -= 1;
				else
				{
					fourBlocks[i] += 8;
					lineIndex++;
				}
			}
			else
			{
				if (i == 0)
					fourBlocks[i] += 9;
				else if (i == 2)
					fourBlocks[i] -= 9;
				else if (i == 3)
				{
					fourBlocks[i] -= 18;
					lineIndex = 0;
				}
			}
		}
		
		if (condition == 0)
		{
			for (int i = 0; i < 4; i++)
			{
				if (lineIndex == 2)
					fourBlocks[i] += 2;
				else if (lineIndex == 0)
					fourBlocks[i] += 1;
			}
		}
		else if (condition == 1)
		{
			for (int i = 0; i < 4; i++)
			{
				if (lineIndex == 2)
					fourBlocks[i] -= 1;
				else if (lineIndex == 0)
					fourBlocks[i] -= 2;
			}
		}
		else if (condition == 2)
		{
			for (int i = 0; i < 4; i++)
			{
				fourBlocks[i] -= 20;
			}
		}
		
		this.updateOrder();
	}
	
	//returns an int[] of the values of fourBlocks[] if they were to "rotate" clockwise in accordance with rotRules[]
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
	
	//"rotates" the values of fourBlocks[] clockwise in accordance with rotRules
	//int condition determines if the values in fourBlocks[] need to be moved based on potential wall/floor kicks
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
		
		this.updateOrder();
	}
	
	public int[] simRotateLineLeft()
	{
		for (int i = 0; i < 4; i++)
		{
			if (lineIndex == 0)
			{
				if (i == 0)
					simRot[i] = fourBlocks[i] + 21;
				else if (i == 1)
					simRot[i] = fourBlocks[i] + 10;
				else if (i == 2)
					simRot[i] = fourBlocks[i] - 1;
				else
					simRot[i] = fourBlocks[i] - 12;
			}
			else if (lineIndex == 3)
			{
				if (i == 0)
					simRot[i] = fourBlocks[i] + 19;
				else if (i == 1)
					simRot[i] = fourBlocks[i] + 10;
				else if (i == 2)
					simRot[i] = fourBlocks[i] + 1;
				else
					simRot[i] = fourBlocks[i] - 8;
			}
			else if (lineIndex == 2)
			{
				if (i == 0)
					simRot[i] = fourBlocks[i] - 21;
				else if (i == 1)
					simRot[i] = fourBlocks[i] - 10;
				else if (i == 2)
					simRot[i] = fourBlocks[i] + 1;
				else
					simRot[i] = fourBlocks[i] + 12;
			}
			else
			{
				if (i == 0)
					simRot[i] = fourBlocks[i] + 8;
				else if (i == 1)
					simRot[i] = fourBlocks[i] - 1;
				else if (i == 2)
					simRot[i] = fourBlocks[i] - 10;
				else
					simRot[i] = fourBlocks[i] - 19;
			}
		}
		
		return simRot;
	}
	
	public void rotateLineLeft(int condition)
	{
		for (int i = 0; i < 4; i++)
		{
			if (lineIndex == 0)
			{
				if (i == 0)
					fourBlocks[i] -= 9;
				else if (i == 2)
					fourBlocks[i] += 9;
				else if (i == 3)
				{
					fourBlocks[i] += 18;
					lineIndex = 3;
				}
			}
			else if (lineIndex == 3)
			{
				if (i == 0)
					fourBlocks[i] += 19;
				else if (i == 1)
					fourBlocks[i] += 10;
				else if (i == 2)
					fourBlocks[i] += 1;
				else
				{
					fourBlocks[i] -= 8;
					lineIndex--;
				}
			}
			else if (lineIndex == 2)
			{
				if (i == 0)
					fourBlocks[i] -= 18;
				else if (i == 1)
					fourBlocks[i] -= 9;
				else if (i == 3)
				{
					fourBlocks[i] += 9;
					lineIndex--;
				}
			}
			else
			{
				if (i == 0)
					fourBlocks[i] += 8;
				else if (i == 1)
					fourBlocks[i] -= 1;
				else if (i == 2)
					fourBlocks[i] -= 10;
				else
				{
					fourBlocks[i] -= 19;
					lineIndex--;
				}
			}
		}
		
		if (condition == 0)
		{
			for (int i = 0; i < 4; i++)
			{
				if (lineIndex == 0)
					fourBlocks[i] += 2;
				else if (lineIndex == 2)
					fourBlocks[i] += 1;
			}
		}
		else if (condition == 1)
		{
			for (int i = 0; i < 4; i++)
			{
				if (lineIndex == 0)
					fourBlocks[i] -= 1;
				else if (lineIndex == 2)
					fourBlocks[i] -= 2;
			}
		}
		else if (condition == 2)
		{
			for (int i = 0; i < 4; i++)
			{
				fourBlocks[i] -= 20;
			}
		}
		
		this.updateOrder();
	}
	
	//returns an int[] of the values of fourBlocks[] if they were to "rotate" counter-clockwise in accordance with rotRules[]
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
	
	//"rotates" the values of fourBlocks[] counter-clockwise in accordance with rotRules
	//int condition determines if the values in fourBlocks[] need to be moved based on potential wall/floor kicks
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
		
		this.updateOrder();
	}
	
	//gives an int[] of the distance between values of fourBlocks[] and fourBlocks[rotationPoint]
	public int[] getRotPointDiff() {
		
		for (int i = 0; i < 4; ++i) {
			rotPointDiff[i] = fourBlocks[i] - fourBlocks[rotationPoint];
		}
			
		return rotPointDiff;
	}
	
	//returns the orientation of the line piece 
	public int getLineOrientation()
	{
		return lineIndex;
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
	
	//Returns the rotationPoint of the Tetrimino
	public int getRotationPoint()
	{
		return rotationPoint;
	}
	
	public int getRotPointIndex()
	{
		return fourBlocks[rotationPoint];
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
		else if (type > 1)
		{
			for (int j = 2; j > -1; j--)
			{
				if (bottomBlocks.indexOf(fourBlocks[j] + 10) == -1
						&& bottomBlocks.indexOf(fourBlocks[j] + 20) == -1)
					bottomBlocks.add(fourBlocks[j]);
			}
		}
		else if (type == 0)
			bottomBlocks.add(fourBlocks[2]);

		return bottomBlocks;
	}

	/**
	 * Returns sideBlocksRight<Integer> after finding
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