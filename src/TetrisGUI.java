import java.net.URL;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This is the TetrisGUI class that has been updated to include
 * collision detection as of 3/31/2020 at 6:08pm
 * 
 * @author mgall
 *
 */
public class TetrisGUI extends Application implements Initializable{

	int speedInMilli = 750;	//speed of blocks falling in milliseconds

	public static void main(String[] args)
	{
		launch(args);
	}	

	@Override
	public void start(Stage primaryStage)
	{
		int sceneWidth = 500;	//holds the width of the scene and grid[]
		int sceneHeight = 1000;	//holds the height of the scene and grid[]
		int numGrid = ((sceneWidth/50)*(sceneHeight/50) - 10);	//number of grid spaces
		double currX = 0.0;	//current X value used to construct grid[]
		double currY = 0.0;	//current Y value used to construct grid[]
		int rotWidth = 0;	//holds the rotation width of a Tetrimino object
		int randomBlockType = 0;	//creates the block type of a Tetrimino object

		Rectangle[] grid = new Rectangle[numGrid];	//Rectangle array that creates the grid that shows the Tetrimino objects and allows the user to play
		for (int c = 0; c < grid.length; c++)	//for-loop that initializes the Rectangles in grid[]
		{
			grid[c] = new Rectangle(50, 50);
		}

		ArrayList<Tetrimino> tetInControl = new ArrayList<Tetrimino>();	//ArrayList that holds Tetrimino objects
		for (int a = 0; a < 4; a++)	//for-loop that creates the first 4 Tetrimino objects within tetInControl
		{
			randomBlockType = (int) (Math.random()*5+2);	//uses Math.random() to store a value from 0-6 to randomBlockType
			if (randomBlockType == 0)	//if-else statements that determines rotWidth based on randomBlockType
				rotWidth = 2;
			else if (randomBlockType == 1)
				rotWidth = 4;
			else if (randomBlockType > 1)
				rotWidth = 3;

			tetInControl.add(new Tetrimino(rotWidth, randomBlockType));	//initializes a new tetrimino into index i of tetInControl
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

		for (int k = 0; k < 4; k++)	//adds the blocks from currentBlocks into the grid[] by filling the indexes with the Tetrimino's color
		{
			grid[tetInControl.get(0).getBlockPlace(k)].setFill(tetInControl.get(0).getColor());
		}

		//creates the scene
		Scene scene = new Scene(p, sceneWidth, sceneHeight);
		primaryStage.setTitle("Falling Block");
		primaryStage.setScene(scene);
		primaryStage.show();

		//EventHandler that is called every second to process the movement of Tetrimino's and when a new one is added into the scene
		EventHandler<ActionEvent> time = new EventHandler<ActionEvent>()
		{
			int index = 0;	//index of tetInControl
			Color currTetriminoColor = tetInControl.get(0).getColor();	//color of the current Tetrimino
			ArrayList<Integer> bottomBlocks = tetInControl.get(0).bottomBlocks();	//Array List that stores the blocks at the bottom of the Tetrimino
			ArrayList<Integer> leftBlocks = tetInControl.get(0).sideBlocksLeft();	//Array List that stores the blocks at the left-most side of the Tetrimino
			ArrayList<Integer> rightBlocks = tetInControl.get(0).sideBlocksRight();	//Array List that stores the block at the right-most side of the Tetrimino
			boolean blocksAreAboveNothing = true;	//holds the number of blocks that from bottomBlocks that have no blocks immediately underneth them
			int newRotWidth;	//holds the new rotation width of the newly created Tetrimino
			int newRandomBlockType;	//creates the new Tetrimino type for the new Tetrimino
			boolean cantMoveLeft = false;	//boolean status for if the tetrimino can move left
			boolean cantMoveRight = false;	//boolean status for if the tetrimino can move right
			//boolean[] collisionBlocks = new boolean[8]; // detects true if surrounding grid blocks are not white
			//int [] rotRules = {-11, -10, -9, 1, 11, 10, 9, -1}; //the location of the block in relation to the origin on the grid
			int[] simRot = new int[4];
			int condition = -1;
			boolean kickedWallRight = false;
			boolean kickedWallLeft = false;
			boolean kickedFloor = false;

			@Override
			public void handle(ActionEvent time)
			{
				if (index > 54)	//if statement that will remove old Tetrimino objects from tetInControl
					tetInControl.remove(0);
				
				for (int a = 0; a < bottomBlocks.size(); a++)	//for-loop that checks for any blocks immediately below the indexes of bottomBlocks
				{
					if (bottomBlocks.get(a) < numGrid - 10)
					{
						if (!grid[bottomBlocks.get(a) + 10].getFill().equals(Color.WHITE))
							blocksAreAboveNothing = false;
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
					index++;	//increases index to the next Tetrimino
					bottomBlocks = tetInControl.get(index).bottomBlocks();	//updates bottomBlocks to the bottomBlocks of the next Tetrimino
					leftBlocks = tetInControl.get(index).sideBlocksLeft();	//updates leftBlockss to the sideBlocksLeft of the next Tetrimino
					rightBlocks = tetInControl.get(index).sideBlocksRight();	//updates rightBlockss to the sideBlocksRight of the next Tetrimino
					currTetriminoColor = tetInControl.get(index).getColor();	//updates the color of the indexes to that of the next Tetrimino
					cantMoveLeft = false;	//updates cantMoveLeft
					cantMoveRight = false;	//updates cantMoveRight
					for (int b = 0; b < 4; b++)	//for-loop that creates the next Tetrimino on grid[]
					{
						grid[tetInControl.get(index).getBlockPlace(b)].setFill(currTetriminoColor);
					}
					blocksAreAboveNothing = true;

					//creates a new Tetrimino and adds it to tetInControl
					newRandomBlockType = (int) (Math.random()*5+2);
					if (newRandomBlockType == 0)
						newRotWidth = 2;
					else if (newRandomBlockType == 1)
						newRotWidth = 4;
					else if (newRandomBlockType > 1 && newRandomBlockType < 7)
						newRotWidth = 3;
					tetInControl.add(new Tetrimino(newRotWidth, newRandomBlockType));
				}


				grid[0].setOnKeyPressed(new EventHandler<KeyEvent>(){
					@Override
					public void handle(KeyEvent event)
					{
						if (event.getCode() == KeyCode.UP) // rotate Tetrimino clockwise	
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
						}
						
						if (event.getCode() == KeyCode.DOWN)
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
						}
						
						if (event.getCode() == KeyCode.LEFT)	//if statement for when the user wants to move the Tetrimino left
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

						if (event.getCode() == KeyCode.RIGHT)	//if statement for when the user wants to move the Tetrimino right
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
				
				if (rOrL)	//if the user is rotating right
				{
					simRot = tetInControl.get(index).simRotateRight();
					
					for (int k = 0; k < 4; k++)	//turns the Tetrimino black to avoid errors in rotating
					{
						grid[tetInControl.get(index).getBlockPlace(k)].setFill(Color.BLACK);
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
				
				return true;
			}
		};

		Timeline a = new Timeline(new KeyFrame(Duration.millis(speedInMilli), time));	//plays the EventHandler every second
		a.setCycleCount(Timeline.INDEFINITE);
		grid[0].requestFocus();	//request control for the first Rectangle of grid[] [THIS INDEX DOE NOT MOVE, IT ONLY RECEIVES INSTRUCTIONS]
		a.play();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}
}