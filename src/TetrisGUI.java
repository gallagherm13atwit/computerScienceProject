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

	int speedInMilli = 1000;	//speed of blocks falling in milliseconds
	
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
		int[] currentBlocks = new int[4];	//holds the indexes of the currently controlled Tetrimino
		
		Rectangle[] grid = new Rectangle[numGrid];	//Rectangle array that creates the grid that shows the Tetrimino objects and allows the user to play
		for (int c = 0; c < grid.length; c++)	//for-loop that initializes the Rectangles in grid[]
		{
			grid[c] = new Rectangle(50, 50);
		}
		
		ArrayList<Tetrimino> blockInControl = new ArrayList<Tetrimino>();	//ArrayList that holds Tetrimino objects
		for (int a = 0; a < 4; a++)	//for-loop that creates the first 4 Tetrimino objects within blockInControl
		{
			randomBlockType = (int) (Math.random()*7);	//uses Math.random() to store a value from 0-6 to randomBlockType
			if (randomBlockType == 0)	//if-else statements that determines rotWidth based on randomBlockType
				rotWidth = 2;
			else if (randomBlockType == 1)
				rotWidth = 4;
			else if (randomBlockType > 1)
				rotWidth = 3;
			
			blockInControl.add(new Tetrimino(rotWidth, randomBlockType));	//initializes a new tetrimino into index i of blockInControl
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
		
		currentBlocks = blockInControl.get(0).getBlocks();	//initializes currentBlocks to the first Tetrimino object's indexes in blockInControl
		
		for (int k = 0; k < 4; k++)	//adds the blocks from currentBlocks into the grid[] by filling the indexes with the Tetrimino's color
		{
			grid[currentBlocks[k]].setFill(blockInControl.get(0).getColor());
		}
	
		//creates the scene
		Scene scene = new Scene(p, sceneWidth, sceneHeight);
		primaryStage.setTitle("Falling Block");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		//EventHandler that is called every second to process the movement of Tetrimino's and when a new one is added into the scene
		EventHandler<ActionEvent> time = new EventHandler<ActionEvent>()
		{
			int index = 0;	//index of blockInControl
			Color currTetriminoColor = blockInControl.get(0).getColor();	//color of the current Tetrimino
			ArrayList<Integer> bottomBlocks = blockInControl.get(0).bottomBlocks();	//Array List that stores the blocks at the bottom of the Tetrimino
			ArrayList<Integer> leftBlocks = blockInControl.get(0).sideBlocksLeft();	//Array List that stores the blocks at the left-most side of the Tetrimino
			ArrayList<Integer> rightBlocks = blockInControl.get(0).sideBlocksRight();	//Array List that stores the block at the right-most side of the Tetrimino
			boolean blocksAreAboveNothing = true;	//holds the number of blocks that from bottomBlocks that have no blocks immediately underneth them
			int newRotWidth;	//holds the new rotation width of the newly created Tetrimino
			int newRandomBlockType;	//creates the new Tetrimino type for the new Tetrimino
			boolean cantMoveLeft = false;	//boolean status for if the tetrimino can move left
			boolean cantMoveRight = false;	//boolean status for if the tetrimino can move right
			
			@Override
			public void handle(ActionEvent time)
			{
				if (index > 54)	//if statement that will remove old Tetrimino objects from blockInControl
					blockInControl.remove(0);
				
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
						grid[blockInControl.get(index).getBlockPlace(j)].setFill(Color.WHITE);	//makes the old index white
						blockInControl.get(index).changeBlock(j, 10);
						grid[blockInControl.get(index).getBlockPlace(j)].setFill(currTetriminoColor);	//makes the new index the color of the Tetrimino
					}
					bottomBlocks = blockInControl.get(index).bottomBlocks();	//updates bottomBlocks
					leftBlocks = blockInControl.get(index).sideBlocksLeft();	//updates leftBlocks
					rightBlocks = blockInControl.get(index).sideBlocksRight();	//updates rightBlocks
					blocksAreAboveNothing = true;	//updates blocksAboveNothing
					cantMoveLeft = false;	//updates cantMoveLeft
					cantMoveRight = false;	//updates cantMoveRight
				}
				else	//else statement that activates if the Tetrimino is at the bottom of the screen or is immediately above another block
				{
					index++;	//increases index to the next Tetrimino
					bottomBlocks = blockInControl.get(index).bottomBlocks();	//updates bottomBlocks to the bottomBlocks of the next Tetrimino
					leftBlocks = blockInControl.get(index).sideBlocksLeft();	//updates leftBlockss to the sideBlocksLeft of the next Tetrimino
					rightBlocks = blockInControl.get(index).sideBlocksRight();	//updates rightBlockss to the sideBlocksRight of the next Tetrimino
					currTetriminoColor = blockInControl.get(index).getColor();	//updates the color of the indexes to that of the next Tetrimino
					cantMoveLeft = false;	//updates cantMoveLeft
					cantMoveRight = false;	//updates cantMoveRight
					for (int b = 0; b < 4; b++)	//for-loop that creates the next Tetrimino on grid[]
					{
						grid[blockInControl.get(index).getBlockPlace(b)].setFill(currTetriminoColor);
					}
					blocksAreAboveNothing = true;
				
					//creates a new Tetrimino and adds it to blockInControl
					newRandomBlockType = (int) (Math.random()*7);
					if (newRandomBlockType == 0)
						newRotWidth = 2;
					else if (newRandomBlockType == 1)
						newRotWidth = 4;
					else if (newRandomBlockType > 1 && newRandomBlockType < 7)
						newRotWidth = 3;
					blockInControl.add(new Tetrimino(newRotWidth, newRandomBlockType));
				}
				
				
				grid[0].setOnKeyPressed(new EventHandler<KeyEvent>(){
					@Override
					public void handle(KeyEvent event)
					{
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
									grid[blockInControl.get(index).getBlockPlace(d)].setFill(Color.WHITE);
									blockInControl.get(index).changeBlock(d, -1);
									grid[blockInControl.get(index).getBlockPlace(d)].setFill(currTetriminoColor);
								}
								bottomBlocks = blockInControl.get(index).bottomBlocks();	//updates bottomBlocks
								leftBlocks = blockInControl.get(index).sideBlocksLeft();	//updates leftBlocks
								rightBlocks = blockInControl.get(index).sideBlocksRight();	//updates rightBlocks
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
									grid[blockInControl.get(index).getBlockPlace(d)].setFill(Color.WHITE);
									blockInControl.get(index).changeBlock(d, 1);
									grid[blockInControl.get(index).getBlockPlace(d)].setFill(currTetriminoColor);
								}
								bottomBlocks = blockInControl.get(index).bottomBlocks();
								leftBlocks = blockInControl.get(index).sideBlocksLeft();
								rightBlocks = blockInControl.get(index).sideBlocksRight();
							}
						}
					}
				});
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