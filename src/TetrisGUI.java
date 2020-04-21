import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This is the TetrisGUI class that has been completed
 * as of 4/20/2020
 * 
 * Program can be run either in this file or in Tetrimino.java
 * 
 * @author Matthew Gallagher & Matthew Fairneny
 *
 */
public class TetrisGUI extends Application{

	Timeline a;
	int speedInMilli = 1000;	//speed of blocks falling in milliseconds
	double totalSpeed = 1.0;
	double speedMult = 0;
	boolean speedUp = false;
	boolean gameOver = false;
	boolean alreadyHeld = false;
	int sceneWidth = 500;	//holds the width of the scene and grid[]
	int sceneHeight = 1000;	//holds the height of the scene and grid[]
	int numGrid = ((sceneWidth/50)*(sceneHeight/50));	//number of grid spaces
	double currX = 0.0;	//current X value used to construct grid[]
	double currY = 0.0;	//current Y value used to construct grid[]
	int rotWidth = 0;	//holds the rotation width of a Tetrimino object
	int randomBlockType = 0;	//creates the block type of a Tetrimino object

	public static void main(String[] args)
	{
		launch(args);
	}	

	@Override
	public void start(Stage primaryStage)
	{
		Rectangle[] grid = new Rectangle[numGrid];	//Rectangle array that creates the grid that shows the Tetrimino objects and allows the user to play
		for (int c = 0; c < grid.length; c++)	//for-loop that initializes the Rectangles in grid[]
		{
			grid[c] = new Rectangle(50, 50);
		}

		ArrayList<Tetrimino> tetInControl = new ArrayList<Tetrimino>();	//ArrayList that holds Tetrimino objects
		for (int a = 0; a < 4; a++)	//for-loop that creates the first 4 Tetrimino objects within tetInControl
		{
			randomBlockType = (int) (Math.random()*7);	//uses Math.random() to store a value from 0-6 to randomBlockType

			tetInControl.add(new Tetrimino(randomBlockType));	//initializes a new tetrimino into index i of tetInControl
		}

		Pane p = new Pane();
		for (int i = 0; i < grid.length; i++)	//for-loop that places the Rectangle in grid[] within the scene
		{
			grid[i].setX(currX);
			grid[i].setY(currY);
			grid[i].setStroke(Color.GREY);
			grid[i].setFill(Color.WHITE);
			p.getChildren().add(grid[i]);
			if (currX == sceneWidth - 50)
			{
				currX = 0.0;
				currY += 50.0;
			}
			else
				currX+=50.0;
		}
		
		Text gameStatus = new Text(175, 330, String.format(""));
		gameStatus.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		Text controls = new Text(525, 800, String.format("LEFT ARROW - Move Left%n%nRIGHT ARROW - Move Right%n%nUP ARROW - Rotate Right%n%nDOWN ARROW - Rotate Left%n%nSPACE - Hard Drop%n%nZ - Soft Drop"));
		controls.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
		Text holding = new Text(525, 20, String.format("Currently Holding"));
		holding.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
		Rectangle containHolding = new Rectangle(200, 200);
		containHolding.setFill(Color.WHITE);
		containHolding.setStroke(Color.BLACK);
		containHolding.setX(525);
		containHolding.setY(30);
		Text upNext = new Text(525, 270, String.format("Next 3 Blocks"));
		upNext.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
		Rectangle containNext = new Rectangle(200, 500);
		containNext.setFill(Color.WHITE);
		containNext.setStroke(Color.BLACK);
		containNext.setX(525);
		containNext.setY(280);
		p.getChildren().add(gameStatus);
		p.getChildren().add(controls);
		p.getChildren().add(holding);
		p.getChildren().add(containHolding);
		p.getChildren().add(upNext);
		p.getChildren().add(containNext);
		
		Rectangle[] tetHeld = new Rectangle[4];
		for (int i = 0; i < 4; i++)
		{
			tetHeld[i] = new Rectangle(40, 40);
			tetHeld[i].setStroke(Color.GREY);
			tetHeld[i].setX(-50);
			tetHeld[i].setY(-50);
			p.getChildren().add(tetHeld[i]);
		}
		
		Rectangle[] tetComing = new Rectangle[12];
		for (int i = 0; i < 4; i++)
		{
			tetComing[i] = new Rectangle(40, 40);
			tetComing[i].setFill(tetInControl.get(1).getColor());
			tetComing[i].setStroke(Color.GREY);
			tetComing[i].setX((tetInControl.get(1).getBlockPlace(i)%10)*40 + 430);
			tetComing[i].setY((tetInControl.get(1).getBlockPlace(i)/10)*40 + 330);
			p.getChildren().add(tetComing[i]);
		}
		for (int i = 4; i < 8; i++)
		{
			tetComing[i] = new Rectangle(40, 40);
			tetComing[i].setFill(tetInControl.get(2).getColor());
			tetComing[i].setStroke(Color.GREY);
			tetComing[i].setX((tetInControl.get(2).getBlockPlace(i-4)%10)*40 + 430);
			tetComing[i].setY((tetInControl.get(2).getBlockPlace(i-4)/10)*40 + 490);
			p.getChildren().add(tetComing[i]);
		}
		for (int i = 8; i < 12; i++)
		{
			tetComing[i] = new Rectangle(40, 40);
			tetComing[i].setFill(tetInControl.get(3).getColor());
			tetComing[i].setStroke(Color.GREY);
			tetComing[i].setX((tetInControl.get(3).getBlockPlace(i-8)%10)*40 + 430);
			tetComing[i].setY((tetInControl.get(3).getBlockPlace(i-8)/10)*40 + 650);
			p.getChildren().add(tetComing[i]);
		}

		for (int k = 0; k < 4; k++)	//adds the blocks from currentBlocks into the grid[] by filling the indexes with the Tetrimino's color
		{
			grid[tetInControl.get(0).getBlockPlace(k)].setFill(tetInControl.get(0).getColor());
		}

		//creates the scene
		Scene scene = new Scene(p, sceneWidth + 300, sceneHeight);
		primaryStage.setTitle("Tetris");
		primaryStage.setScene(scene);
		primaryStage.show();

		//EventHandler that is called every second to process the movement of Tetrimino's and when a new one is added into the scene
		EventHandler<ActionEvent> time = new EventHandler<ActionEvent>()
		{
			int holding = -1;
			Tetrimino tetHolding = new Tetrimino(-1);
			int temp = -1;
			int index = 0;	//index of tetInControl
			Color currTetriminoColor = tetInControl.get(0).getColor();	//color of the current Tetrimino
			ArrayList<Integer> bottomBlocks = tetInControl.get(0).bottomBlocks();	//Array List that stores the blocks at the bottom of the Tetrimino
			ArrayList<Integer> leftBlocks = tetInControl.get(0).sideBlocksLeft();	//Array List that stores the blocks at the left-most side of the Tetrimino
			ArrayList<Integer> rightBlocks = tetInControl.get(0).sideBlocksRight();	//Array List that stores the block at the right-most side of the Tetrimino
			boolean blocksAreAboveNothing = true;	//holds the number of blocks that from bottomBlocks that have no blocks immediately underneth them
			int newRandomBlockType;	//creates the new Tetrimino type for the new Tetrimino
			boolean cantMoveLeft = false;	//boolean status for if the tetrimino can move left
			boolean cantMoveRight = false;	//boolean status for if the tetrimino can move right
			int[] simRot = new int[4];
			int condition = -1;
			boolean kickedWallRight = false;
			boolean kickedWallLeft = false;
			boolean kickedFloor = false;

			@Override
			public void handle(ActionEvent time)
			{
				if (gameOver)
				{
					totalSpeed = 0;
					grid[63].setFill(Color.WHITE);
					grid[64].setFill(Color.WHITE);
					grid[65].setFill(Color.WHITE);
					grid[66].setFill(Color.WHITE);
					grid[63].setStroke(Color.WHITE);
					grid[64].setStroke(Color.WHITE);
					grid[65].setStroke(Color.WHITE);
					grid[66].setStroke(Color.WHITE);
					gameStatus.setText(String.format("GAME OVER"));
				}
				else
				{
					if (speedMult % 10 == 0 && speedMult != 0) {
						totalSpeed = 1.0 + speedMult*.1;
						a.setRate(totalSpeed);
					}
					if (index > 54)	//if statement that will remove old Tetrimino objects from tetInControl
					{
						tetInControl.remove(0);
						index--;
					}
					
					for (int a = 0; a < bottomBlocks.size(); a++)	//for-loop that checks for any blocks immediately below the indexes of bottomBlocks
					{
						if (bottomBlocks.get(a) < numGrid - 10 && blocksAreAboveNothing)
						{
							if (!grid[bottomBlocks.get(a) + 10].getFill().equals(Color.WHITE))
							{
								blocksAreAboveNothing = false;
							}
						}
						else
							blocksAreAboveNothing = false;
					}

					if (blocksAreAboveNothing)	//if statement that drops the blocks of Tetrimino into the next blocks below based on the value of blocksAboveNothing
					{
						for (int j = 3; j > -1; j--)	//for-loop that updates the indexes of the blocks in the current Tetrimino
						{
							grid[tetInControl.get(index).getBlockPlace(j)].setFill(Color.WHITE);	//makes the old index white
							tetInControl.get(index).changeBlock(j, 10);
							grid[tetInControl.get(index).getBlockPlace(j)].setFill(currTetriminoColor);	//makes the new index the color of the Tetrimino
						}
						bottomBlocks = tetInControl.get(index).bottomBlocks();	//updates bottomBlocks
						leftBlocks = tetInControl.get(index).sideBlocksLeft();	//updates leftBlocks
						rightBlocks = tetInControl.get(index).sideBlocksRight();	//updates rightBlocks
						blocksAreAboveNothing = true;	//updates blocksAboveNothing
						cantMoveLeft = false;	//updates cantMoveLeft
						cantMoveRight = false;	//updates cantMoveRight	
					}
					else	//else statement that activates if the Tetrimino is at the bottom of the screen or is immediately above another block
					{
						clearLines();
						index++;	//increases index to the next Tetrimino
						bottomBlocks = tetInControl.get(index).bottomBlocks();	//updates bottomBlocks to the bottomBlocks of the next Tetrimino
						leftBlocks = tetInControl.get(index).sideBlocksLeft();	//updates leftBlockss to the sideBlocksLeft of the next Tetrimino
						rightBlocks = tetInControl.get(index).sideBlocksRight();	//updates rightBlockss to the sideBlocksRight of the next Tetrimino
						currTetriminoColor = tetInControl.get(index).getColor();	//updates the color of the indexes to that of the next Tetrimino
						cantMoveLeft = false;	//updates cantMoveLeft
						cantMoveRight = false;	//updates cantMoveRight
						alreadyHeld = false;
						for (int b = 0; b < 4; b++)	//for-loop that creates the next Tetrimino on grid[]
						{
							if (!grid[tetInControl.get(index).getBlockPlace(b)].getFill().equals(Color.WHITE))
							{
								gameOver = true;
								b = 5;
							}
							else
								grid[tetInControl.get(index).getBlockPlace(b)].setFill(currTetriminoColor);
						}
						blocksAreAboveNothing = true;
						
						//creates a new Tetrimino and adds it to tetInControl
						newRandomBlockType = (int) (Math.random()*7);
						tetInControl.add(new Tetrimino(newRandomBlockType));
						updateTetComing();
					}
				}
				
				grid[0].setOnKeyReleased(new EventHandler<KeyEvent>(){
					@Override
					public void handle(KeyEvent event)
					{
						if (event.getCode() == KeyCode.Z && !gameOver)
                        {
                                a.setRate(totalSpeed * 1.0);
                        }
						
					}
				});

				grid[0].setOnKeyPressed(new EventHandler<KeyEvent>(){
					@Override
					public void handle(KeyEvent event)
					{
						
						if (event.getCode() == KeyCode.H && !alreadyHeld && !gameOver)
						{
							if (holding == -1)
							{
								holding = tetInControl.get(index).getType();
								tetHolding = new Tetrimino(holding);
								for (int i = 0; i < 4; i++)
								{
									grid[tetInControl.get(index).getBlockPlace(i)].setFill(Color.WHITE);
									tetHeld[i].setFill(currTetriminoColor);
								}
								index++;
								bottomBlocks = tetInControl.get(index).bottomBlocks();
								leftBlocks = tetInControl.get(index).sideBlocksLeft();
								rightBlocks = tetInControl.get(index).sideBlocksRight();
								currTetriminoColor = tetInControl.get(index).getColor();
								cantMoveLeft = false;
								cantMoveRight = false;
								blocksAreAboveNothing = true;
								for (int b = 0; b < 4; b++)
								{
									grid[tetInControl.get(index).getBlockPlace(b)].setFill(currTetriminoColor);
								}

								newRandomBlockType = (int) (Math.random()*7);
								tetInControl.add(new Tetrimino(newRandomBlockType));
								updateTetComing();
							}
							else
							{
								temp = holding;
								holding = tetInControl.get(index).getType();
								tetHolding = new Tetrimino(holding);
								for (int i = 0; i < 4; i++)
								{
									grid[tetInControl.get(index).getBlockPlace(i)].setFill(Color.WHITE);
									tetHeld[i].setFill(currTetriminoColor);
								}
								tetInControl.set(index, new Tetrimino(temp));
								bottomBlocks = tetInControl.get(index).bottomBlocks();
								leftBlocks = tetInControl.get(index).sideBlocksLeft();
								rightBlocks = tetInControl.get(index).sideBlocksRight();
								currTetriminoColor = tetInControl.get(index).getColor();
								cantMoveLeft = false;
								cantMoveRight = false;
								blocksAreAboveNothing = true;
								for (int b = 0; b < 4; b++)
								{
									grid[tetInControl.get(index).getBlockPlace(b)].setFill(currTetriminoColor);
								}
							}
							
							for (int i = 0; i < 4; i++)
							{
								tetHeld[i].setX((tetHolding.getBlockPlace(i)%10)*40 + 430);
								tetHeld[i].setY((tetHolding.getBlockPlace(i)/10)*40 + 80);
							}
							
							alreadyHeld = true;
						}
						
						if (event.getCode() == KeyCode.Z && !gameOver)
                        {
							a.setRate(totalSpeed * 10.0);
                        }
						
						if (event.getCode() == KeyCode.SPACE && !gameOver)
						{
							hardDrop();
						}
						
						if (event.getCode() == KeyCode.UP && !gameOver) // rotate Tetrimino clockwise	
						{
							if (tetInControl.get(index).getType() > 1 && canRotate(true)) {
								
								for (int d = 0; d < 4; d++)
									grid[tetInControl.get(index).getBlockPlace(d)].setFill(Color.WHITE);
								
								//gives a value to condition based on what kind of wall/floor kick was used
								if (kickedWallLeft)
								{
									condition = 0;
								}
								else if (kickedWallRight)
								{
									condition = 1;
								}
								else if (kickedFloor)
								{
									condition = 2;
								}
								
								tetInControl.get(index).rotateRight(condition);	//rotates the Tetrimino right
								
								
								for (int j = 0; j < 4; j++)	//shows the Tetrimino in grid[]
								{
									grid[tetInControl.get(index).getBlockPlace(j)].setFill(currTetriminoColor);
								}
								
								bottomBlocks = tetInControl.get(index).bottomBlocks();	//updates bottomBlocks
								leftBlocks = tetInControl.get(index).sideBlocksLeft();	//updates leftBlocks
								rightBlocks = tetInControl.get(index).sideBlocksRight();	//updates rightBlocks
							}
							else if (tetInControl.get(index).getType() == 1 && canRotate(true))
							{
								for (int d = 0; d < 4; d++)
									grid[tetInControl.get(index).getBlockPlace(d)].setFill(Color.WHITE);
								
								//gives a value to condition based on what kind of wall/floor kick was used
								if (kickedWallLeft)
								{
									condition = 0;
								}
								else if (kickedWallRight)
								{
									condition = 1;
								}
								else if (kickedFloor)
								{
									condition = 2;
								}
								
								tetInControl.get(index).rotateLineRight(condition);	//rotates the Tetrimino right
								
								
								for (int j = 0; j < 4; j++)	//shows the Tetrimino in grid[]
								{
									grid[tetInControl.get(index).getBlockPlace(j)].setFill(currTetriminoColor);
								}
								
								bottomBlocks = tetInControl.get(index).bottomBlocks();	//updates bottomBlocks
								leftBlocks = tetInControl.get(index).sideBlocksLeft();	//updates leftBlocks
								rightBlocks = tetInControl.get(index).sideBlocksRight();	//updates rightBlocks
							}
						}
						
						if (event.getCode() == KeyCode.DOWN && !gameOver)
						{
							if (tetInControl.get(index).getType() > 1 && canRotate(false))
							{
								for (int d = 0; d < 4; d++)
									grid[tetInControl.get(index).getBlockPlace(d)].setFill(Color.WHITE);
								
								//gives a value to condition based on what kind of wall/floor kick was used
								if (kickedWallLeft)
								{
									condition = 0;
								}
								else if (kickedWallRight)
								{
									condition = 1;
								}
								else if (kickedFloor)
								{
									condition = 2;
								}
								
								tetInControl.get(index).rotateLeft(condition);	//rotates the Tetrimino left
								
								for (int j = 0; j < 4; j++) //shows the Tetrimino in grid[]
								{
									grid[tetInControl.get(index).getBlockPlace(j)].setFill(currTetriminoColor);
								}
								
								bottomBlocks = tetInControl.get(index).bottomBlocks();	//updates bottomBlocks
								leftBlocks = tetInControl.get(index).sideBlocksLeft();	//updates leftBlocks
								rightBlocks = tetInControl.get(index).sideBlocksRight();	//updates rightBlocks
							}
							else if (tetInControl.get(index).getType() == 1 && canRotate(false))
							{
								for (int d = 0; d < 4; d++)
									grid[tetInControl.get(index).getBlockPlace(d)].setFill(Color.WHITE);
								
								//gives a value to condition based on what kind of wall/floor kick was used
								if (kickedWallLeft)
								{
									condition = 0;
								}
								else if (kickedWallRight)
								{
									condition = 1;
								}
								else if (kickedFloor)
								{
									condition = 2;
								}
								
								tetInControl.get(index).rotateLineLeft(condition);	//rotates the Tetrimino left
								
								for (int j = 0; j < 4; j++) //shows the Tetrimino in grid[]
								{
									grid[tetInControl.get(index).getBlockPlace(j)].setFill(currTetriminoColor);
								}
								
								bottomBlocks = tetInControl.get(index).bottomBlocks();	//updates bottomBlocks
								leftBlocks = tetInControl.get(index).sideBlocksLeft();	//updates leftBlocks
								rightBlocks = tetInControl.get(index).sideBlocksRight();	//updates rightBlocks
							}
						}
						
						if (event.getCode() == KeyCode.LEFT && !gameOver)	//if statement for when the user wants to move the Tetrimino left
						{
							for (int i = 0; i < leftBlocks.size(); i++)	//for loop that sees if the Tetrimino can move left
							{
								try {
									if (!grid[leftBlocks.get(i) - 1].getFill().equals(Color.WHITE) || leftBlocks.get(i)%10 == 0)
										cantMoveLeft = true;
								} catch (ArrayIndexOutOfBoundsException e) {
									cantMoveLeft = true;
								}
							}
							if (!cantMoveLeft)	//if statement for when the Tetrimino can move left
							{
								for (int d = 0; d < 4; d++)	//for loop that changes the index values within the Tetrimino object aand updates the grid
								{
									grid[tetInControl.get(index).getBlockPlace(d)].setFill(Color.WHITE);
									tetInControl.get(index).changeBlock(d, -1);
									grid[tetInControl.get(index).getBlockPlace(d)].setFill(currTetriminoColor);
								}
								bottomBlocks = tetInControl.get(index).bottomBlocks();	//updates bottomBlocks
								leftBlocks = tetInControl.get(index).sideBlocksLeft();	//updates leftBlocks
								rightBlocks = tetInControl.get(index).sideBlocksRight();	//updates rightBlocks
							}
						}

						if (event.getCode() == KeyCode.RIGHT && !gameOver)	//if statement for when the user wants to move the Tetrimino right
						{
							for (int i = 0; i < rightBlocks.size(); i++)	//for loop that sees if the Tetrimino can move right
							{
								try {
									if (!grid[rightBlocks.get(i) + 1].getFill().equals(Color.WHITE) || rightBlocks.get(i)%10 == 9)
										cantMoveRight = true;
								} catch (ArrayIndexOutOfBoundsException e) {
									cantMoveRight = true;
								}
							}
							if (!cantMoveRight)	//if statement for when the Tetrimino can move right
							{
								for (int d = 3; d > -1; d--)	//for loop that changes the index values for the Tetrimino object and updates the grid
								{
									grid[tetInControl.get(index).getBlockPlace(d)].setFill(Color.WHITE);
									tetInControl.get(index).changeBlock(d, 1);
									grid[tetInControl.get(index).getBlockPlace(d)].setFill(currTetriminoColor);
								}
								bottomBlocks = tetInControl.get(index).bottomBlocks();
								leftBlocks = tetInControl.get(index).sideBlocksLeft();
								rightBlocks = tetInControl.get(index).sideBlocksRight();
							}
						}
					}
				});
			}
			
			public void hardDrop()
			{
				int checking = 0;
				
				for (int g = 0; g < 20; g++)
				{
					checking = 0;
					for(int i = 0; i < bottomBlocks.size(); i++) {
						if (bottomBlocks.get(i) < numGrid - 10)
						{
							if (grid[bottomBlocks.get(i) + 10].getFill().equals(Color.WHITE))
							{
								checking++;
							}
							else
								g = 20;
						}
						else
							g = 20;
					}
					
					if (checking == bottomBlocks.size())
					{
						for (int k = 3; k > -1; k--)
						{
							grid[tetInControl.get(index).getBlockPlace(k)].setFill(Color.WHITE);
							tetInControl.get(index).changeBlock(k, 10);
							grid[tetInControl.get(index).getBlockPlace(k)].setFill(currTetriminoColor);
						}
						for (int j = 0; j < bottomBlocks.size(); j++) {
							bottomBlocks.set(j, bottomBlocks.get(j) + 10);
						}
					}
				}
				clearLines();
				index++;	//increases index to the next Tetrimino
				bottomBlocks = tetInControl.get(index).bottomBlocks();	//updates bottomBlocks to the bottomBlocks of the next Tetrimino
				leftBlocks = tetInControl.get(index).sideBlocksLeft();	//updates leftBlockss to the sideBlocksLeft of the next Tetrimino
				rightBlocks = tetInControl.get(index).sideBlocksRight();	//updates rightBlockss to the sideBlocksRight of the next Tetrimino
				currTetriminoColor = tetInControl.get(index).getColor();	//updates the color of the indexes to that of the next Tetrimino
				cantMoveLeft = false;	//updates cantMoveLeft
				cantMoveRight = false;	//updates cantMoveRight
				alreadyHeld = false;
				for (int b = 0; b < 4; b++)	//for-loop that creates the next Tetrimino on grid[]
				{
					if (!grid[tetInControl.get(index).getBlockPlace(b)].getFill().equals(Color.WHITE))
					{
						gameOver = true;
						b = 5;
					}
					else
						grid[tetInControl.get(index).getBlockPlace(b)].setFill(currTetriminoColor);
				}
				blocksAreAboveNothing = true;

				//creates a new Tetrimino and adds it to tetInControl
				newRandomBlockType = (int) (Math.random()*7);
				tetInControl.add(new Tetrimino(newRandomBlockType));
				updateTetComing();
			}
			
			public void clearLines()
			{
				for (int i = 3; i > -1; --i) {
					boolean rowClear = false;
					int rowNum = (tetInControl.get(index).getBlockPlace(i)/10)*10;
					for (int j = 0; j < 10; ++j) {
						if (rowClear == true) { 
							grid[rowNum + j].setFill(Color.WHITE);
							if (j == 9) {
								moveLinesDown(rowNum);
								speedMult++;
							}
						}
						else if (grid[rowNum + j].getFill().equals(Color.WHITE) || tetInControl.get(index).getBlockPlace(i) == -1) {
							j = 10;
						}
						else if(j == 9) {
							rowClear = true;
							j = -1;
						}
					}
				}
			}
			
			public void moveLinesDown(int row) {
				for (int i = row; i > -1 ; i = i - 10) {
					for (int j = 9; j > -1; --j) {
						if (i > 0) {
							grid[i + j].setFill((grid[i + j - 10].getFill()));
							grid[i + j - 10].setFill(Color.WHITE);
							if (tetInControl.get(index).alreadyIndex(i + j) && i != row)
							{
								tetInControl.get(index).changeBlock(tetInControl.get(index).getIndex(i + j), 10);
							}
							else if (tetInControl.get(index).alreadyIndex(i + j) && i == row)
								tetInControl.get(index).changeBlock(tetInControl.get(index).getIndex(i + j), -1*(tetInControl.get(index).getBlockPlace(tetInControl.get(index).getIndex(i + j))+1));
						} else {
							grid[i+ j].setFill(Color.WHITE);
							
						}
					}
				}
			}
			
				
			/**Method that will be called when the user tries to rotate the Tetrimino.
			 * It will be given a true value if the user is rotating right/clockwise
			 * or false if the user is rotating left/counter-clockwise
			 * 
			 * @param rOrL
			 * @return
			 */
			public boolean canRotate(boolean rOrL)
			{
				condition = -1;	//int value that updates based on wall/floor kicks
				kickedWallRight = false;	//true if a right wall kick was needed
				kickedWallLeft = false;	//true if a left wall kick was needed
				kickedFloor = false;	//true if a floor kick was needed
				
				if (tetInControl.get(index).getType() > 1)
				{
					if (rOrL)	//if the user is rotating right
					{
						simRot = tetInControl.get(index).simRotateRight();
					
						for (int k = 0; k < 4; k++)	//turns the Tetrimino black to avoid errors in rotating
						{
							grid[tetInControl.get(index).getBlockPlace(k)].setFill(Color.BLACK);
						}
						
						for (int i = 3; i > -1; i--)
						{
							if (simRot[i] >= grid.length || (!grid[simRot[i]].getFill().equals(Color.WHITE) && !grid[simRot[i]].getFill().equals(Color.BLACK)))
							{
								for (int j = 0; j < 4; j++)
									simRot[j] -= 10;
								
								kickedFloor = true;
								i = -1;
							}
						}
				
						//checks if a left wall kick is needed by seeing if a block either:
						//(a) clipped out of bounds
						//(b) intersects a block left of the rotation point
						//(c) isn't taking up the space of a block within the Tetrimino
						for (int i = 0; i < 4; i++)
						{
							if ((simRot[i]%10 == 9 && tetInControl.get(index).getBlockPlace(i)%10 <= 1)
									|| (simRot[i]%10 < tetInControl.get(index).getRotationPoint()%10 && !grid[simRot[i]].getFill().equals(Color.WHITE) && !grid[simRot[i]].getFill().equals(Color.BLACK)
										&& !tetInControl.get(index).alreadyIndex(simRot[i])))
							{
								for (int j = 0; j < 4; j++)
									simRot[j] += 1;
							
								kickedWallLeft = true;
								i = 0;
							
							}
						}
					
						//checks if a right wall kick is needed by seeing if a block either:
						//(a) clipped out of bounds
						//(b) intersects a block right of the rotation point
						//(c) isn't taking up the space of a block within the Tetrimino
						//(d) didn't need assistance from a left wall kick
						for (int k = 0; k < 4; k++)
						{
							if (!kickedWallLeft && (simRot[k]%10 == 0 && tetInControl.get(index).getBlockPlace(k)%10 >= 8)
									|| (simRot[k]%10 > tetInControl.get(index).getRotationPoint()%10 && !grid[simRot[k]].getFill().equals(Color.WHITE) && !grid[simRot[k]].getFill().equals(Color.BLACK)
										&& !tetInControl.get(index).alreadyIndex(simRot[k])))
							{
								for (int j = 0; j < 4; j++)
									simRot[j] -= 1;
							
								kickedWallRight = true;
								k = 0;
							}
						}
					
						//determines if the Tetrimino is able to rotate based on if the blocks are intersecting an already existing block
						for (int d = 0; d < 4; d++)
						{
							if (!grid[simRot[d]].getFill().equals(Color.WHITE) && !grid[simRot[d]].getFill().equals(Color.BLACK))
							{
								for (int j = 0; j < 4; j++)
								{
									grid[tetInControl.get(index).getBlockPlace(j)].setFill(currTetriminoColor);
								}
								return false;
							}
						}
					}
					else
					{
						simRot = tetInControl.get(index).simRotateLeft();
					
						for (int k = 0; k < 4; k++)
						{
							grid[tetInControl.get(index).getBlockPlace(k)].setFill(Color.BLACK);
						}
						
						for (int i = 3; i > -1; i--)
						{
							if (simRot[i] >= grid.length || (!grid[simRot[i]].getFill().equals(Color.WHITE) && !grid[simRot[i]].getFill().equals(Color.BLACK)))
							{
								for (int j = 0; j < 4; j++)
									simRot[j] -= 10;
								
								kickedFloor = true;
								i = -1;
							}
						}
					
						//checks if a left wall kick is needed by seeing if a block either:
						//(a) clipped out of bounds
						//(b) intersects a block left of the rotation point
						//(c) isn't taking up the space of a block within the Tetrimino
						for (int i = 0; i < 4; i++)
						{
							if ((simRot[i]%10 == 9 && tetInControl.get(index).getBlockPlace(i)%10 <= 1)
									|| (simRot[i]%10 < tetInControl.get(index).getRotationPoint()%10 && !grid[simRot[i]].getFill().equals(Color.WHITE) && !grid[simRot[i]].getFill().equals(Color.BLACK)
										&& !tetInControl.get(index).alreadyIndex(simRot[i])))
							{
								for (int j = 0; j < 4; j++)
									simRot[j] += 1;
							
								kickedWallLeft = true;
								i = 0;
							}
						}
					
						//checks if a right wall kick is needed by seeing if a block either:
						//(a) clipped out of bounds
						//(b) intersects a block right of the rotation point
						//(c) isn't taking up the space of a block within the Tetrimino
						//(d) didn't need assistance from a left wall kick
						for (int k = 0; k < 4; k++)
						{
							if (!kickedWallLeft && (simRot[k]%10 == 0 && tetInControl.get(index).getBlockPlace(k)%10 >= 8)
									|| (simRot[k]%10 > tetInControl.get(index).getRotationPoint()%10 && !grid[simRot[k]].getFill().equals(Color.WHITE) && !grid[simRot[k]].getFill().equals(Color.BLACK)
										&& !tetInControl.get(index).alreadyIndex(simRot[k])))
							{
								for (int j = 0; j < 4; j++)
									simRot[j] -= 1;
							
								kickedWallRight = true;
								k = 0;
							}
						}
					
						//determines if the Tetrimino is able to rotate based on if the blocks are intersecting an already existing block
						for (int d = 0; d < 4; d++)
						{
							if (!grid[simRot[d]].getFill().equals(Color.WHITE) && !grid[simRot[d]].getFill().equals(Color.BLACK))
							{
								for (int j = 0; j < 4; j++)
								{
									grid[tetInControl.get(index).getBlockPlace(j)].setFill(currTetriminoColor);
								}
								return false;
							}
						}
					}
				}
				else if (tetInControl.get(index).getType() == 1)
				{
					if (rOrL)
					{
						simRot = tetInControl.get(index).simRotateLineRight();
						
						for (int k = 0; k < 4; k++)	//turns the Tetrimino black to avoid errors in rotating
						{
							grid[tetInControl.get(index).getBlockPlace(k)].setFill(Color.BLACK);
							if (simRot[k] < 0)
								return false;
						}
						
						for (int i = 3; i > -1; i--)
						{
							if (tetInControl.get(index).getLineOrientation()%2 == 0
									&& (simRot[i] >= grid.length || (!grid[simRot[i]].getFill().equals(Color.WHITE) && !grid[simRot[i]].getFill().equals(Color.BLACK))))
							{
								for (int j = 0; j < 4; j++)
									simRot[j] -= 20;
								
								kickedFloor = true;
							}
						}
				
						//checks if a left wall kick is needed by seeing if a block either:
						//(a) clipped out of bounds
						//(b) intersects a block left of the rotation point
						//(c) isn't taking up the space of a block within the Tetrimino
						for (int i = 0; i < 4; i++)
						{
							if (tetInControl.get(index).getLineOrientation()%2 == 1 && ((tetInControl.get(index).getBlockPlace(i)%10 <= 1 && simRot[i]%10 >= 8)
									|| (!grid[simRot[0]].getFill().equals(Color.WHITE) && !grid[simRot[0]].getFill().equals(Color.BLACK))
									|| (!grid[simRot[1]].getFill().equals(Color.WHITE) && !grid[simRot[1]].getFill().equals(Color.BLACK))))
							{
								for (int j = 0; j < 4; j++)
								{
									if (tetInControl.get(index).getLineOrientation() == 1)
										simRot[j] += 2;
									else if (tetInControl.get(index).getLineOrientation() == 3)
										simRot[j] += 1;
								}
							
								kickedWallLeft = true;
							}
						}
					
						//checks if a right wall kick is needed by seeing if a block either:
						//(a) clipped out of bounds
						//(b) intersects a block right of the rotation point
						//(c) isn't taking up the space of a block within the Tetrimino
						//(d) didn't need assistance from a left wall kick
						for (int i = 0; i < 4; i++)
						{
							if (!kickedWallLeft && tetInControl.get(index).getLineOrientation()%2 == 1 && ((tetInControl.get(index).getBlockPlace(i)%10 >= 8 && simRot[i]%10 <= 1)
										|| (!grid[simRot[3]].getFill().equals(Color.WHITE) && !grid[simRot[3]].getFill().equals(Color.BLACK))
										|| (!grid[simRot[2]].getFill().equals(Color.WHITE) && !grid[simRot[2]].getFill().equals(Color.BLACK))))
							{
								for (int j = 0; j < 4; j++)
								{
									if (tetInControl.get(index).getLineOrientation() == 1)
										simRot[j] -= 1;
									else if (tetInControl.get(index).getLineOrientation() == 3)
										simRot[j] -= 2;
								}
							
								kickedWallRight = true;
							}
						}
					
						//determines if the Tetrimino is able to rotate based on if the blocks are intersecting an already existing block
						for (int d = 0; d < 4; d++)
						{
							if (!grid[simRot[d]].getFill().equals(Color.WHITE) && !grid[simRot[d]].getFill().equals(Color.BLACK))
							{
								for (int j = 0; j < 4; j++)
								{
									grid[tetInControl.get(index).getBlockPlace(j)].setFill(currTetriminoColor);
								}
								return false;
							}
						}
					}
					else
					{
						simRot = tetInControl.get(index).simRotateLineLeft();
						
						for (int k = 0; k < 4; k++)
						{
							grid[tetInControl.get(index).getBlockPlace(k)].setFill(Color.BLACK);
							if (simRot[k] < 0)
								return false;
						}
						
						for (int i = 3; i > -1; i--)
						{
							if (tetInControl.get(index).getLineOrientation()%2 == 0
									&& (simRot[i] >= grid.length || (!grid[simRot[i]].getFill().equals(Color.WHITE) && !grid[simRot[i]].getFill().equals(Color.BLACK))))
							{
								for (int j = 0; j < 4; j++)
									simRot[j] -= 20;
								
								kickedFloor = true;
							}
						}
					
						//checks if a left wall kick is needed by seeing if a block either:
						//(a) clipped out of bounds
						//(b) intersects a block left of the rotation point
						//(c) isn't taking up the space of a block within the Tetrimino
						for( int i = 0; i < 4; i++)
						{
							if (tetInControl.get(index).getLineOrientation()%2 == 1 && ((tetInControl.get(index).getBlockPlace(i)%10 <= 1 && simRot[i]%10 >= 8)
									|| (!grid[simRot[0]].getFill().equals(Color.WHITE) && !grid[simRot[0]].getFill().equals(Color.BLACK))
									|| (!grid[simRot[1]].getFill().equals(Color.WHITE) && !grid[simRot[1]].getFill().equals(Color.BLACK))))
							{
								for (int j = 0; j < 4; j++)
								{
									if (tetInControl.get(index).getLineOrientation() == 1)
										simRot[j] += 2;
									else if (tetInControl.get(index).getLineOrientation() == 3)
										simRot[j] += 1;
								}
							
								kickedWallLeft = true;
							}
						}
					
						//checks if a right wall kick is needed by seeing if a block either:
						//(a) clipped out of bounds
						//(b) intersects a block right of the rotation point
						//(c) isn't taking up the space of a block within the Tetrimino
						//(d) didn't need assistance from a left wall kick
						for( int i = 0; i < 4; i++)
						{
							if (!kickedWallLeft && tetInControl.get(index).getLineOrientation()%2 == 1 && ((tetInControl.get(index).getBlockPlace(i)%10 >= 8 && simRot[i]%10 <= 1)
									|| (!grid[simRot[2]].getFill().equals(Color.WHITE) && !grid[simRot[2]].getFill().equals(Color.BLACK))
									|| (!grid[simRot[3]].getFill().equals(Color.WHITE) && !grid[simRot[3]].getFill().equals(Color.BLACK))))
							{
								for (int j = 0; j < 4; j++)
								{
									if (tetInControl.get(index).getLineOrientation() == 1)
										simRot[j] -= 1;
									else if (tetInControl.get(index).getLineOrientation() == 3)
										simRot[j] -= 2;
								}
							
								kickedWallRight = true;
							}
						}
					
						//determines if the Tetrimino is able to rotate based on if the blocks are intersecting an already existing block
						for (int d = 0; d < 4; d++)
						{
							if (!grid[simRot[d]].getFill().equals(Color.WHITE) && !grid[simRot[d]].getFill().equals(Color.BLACK) && simRot[d] > -1)
							{
								for (int j = 0; j < 4; j++)
								{
									grid[tetInControl.get(index).getBlockPlace(j)].setFill(currTetriminoColor);
								}
								return false;
							}
						}
					}
				}
				
				return true;
			}
			
			public void updateTetComing()
			{
				for (int i = 0; i < 8; i++)
				{
					tetComing[i].setFill(tetComing[i+4].getFill());
					tetComing[i].setX(tetComing[i+4].getX());
					tetComing[i].setY(tetComing[i+4].getY() - 160);
				}
				
				for (int j = 8; j < 12; j++)
				{
					tetComing[j].setFill(tetInControl.get(index+3).getColor());
					tetComing[j].setX((tetInControl.get(index+3).getBlockPlace(j-8)%10)*40 + 430);
					tetComing[j].setY((tetInControl.get(index+3).getBlockPlace(j-8)/10)*40 + 650);
				}
			}
		};
		
		a = new Timeline(new KeyFrame(Duration.millis(speedInMilli), time));	//plays the EventHandler every second
		a.setCycleCount(Timeline.INDEFINITE);
		grid[0].requestFocus();	//request control for the first Rectangle of grid[] [THIS INDEX DOE NOT MOVE, IT ONLY RECEIVES INSTRUCTIONS]
		a.play();
	}
}