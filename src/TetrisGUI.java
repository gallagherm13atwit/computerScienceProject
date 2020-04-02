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

	public static void main(String[] args)
	{
		launch(args);
	}	

	@Override
	public void start(Stage primaryStage)
	{
		int sceneWidth = 500;	//holds the width of the scene and grid[]
		int sceneHeight = 700;	//holds the height of the scene and grid[]
		double currX = 0.0;	//current X value used to construct grid[]
		double currY = 0.0;	//current Y value used to construct grid[]
		int rotWidth = 0;	//holds the rotation width of a Tetrimino object
		int randomBlockType = 0;	//creates the block type of a Tetrimino object
		int[] currentBlocks = new int[4];	//holds the indexes of the currently controled Tetrimino
		
		Rectangle[] grid = new Rectangle[(sceneWidth/50)*(sceneHeight/50)];	//Rectangle array that creates the grid that shows the Tetrimino objects and allows the user to play
		for (int c = 0; c < 140; c++)	//for-loop that initializes the Rectangles in grid[]
		{
			grid[c] = new Rectangle(50, 50);
		}
		
		ArrayList<Tetrimino> blockInControl = new ArrayList<Tetrimino>();	//ArrayList that holds Tetrimino objects
		for (int a = 0; a < 4; a++)	//for-loop that creates the first 4 Tetrimino objects within blockInControl
		{
			randomBlockType = (int) (Math.random()*6);	//uses Math.random() to store a value from 0-6 to randomBlockType
			if (randomBlockType == 0)	//if-else statements that determines rotWidth based on randomBlockType
				rotWidth = 2;
			else if (randomBlockType == 1)
				rotWidth = 4;
			else if (randomBlockType > 1 && randomBlockType < 7)
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
		
		for (int k = 0; k < 4; k++)	//adds the blocks from currentBlocks into the grid[] by fillin the indexes with the Tetrimino's color
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
			double tempVal = 0.0;	//temporary value that COULD be used for rotating a Tetrimino
			int index = 0;	//index of blockInControl
			Color currTetriminoColor = blockInControl.get(0).getColor();	//color of the current Tetrimino
			int[] tetriminoFalling = blockInControl.get(0).getBlocks();	//int array that stores the indexes of the current Tetrimino
			ArrayList<Integer> bottomBlocks = blockInControl.get(0).bottomBlocks();	//Array List that stores the blocks at the bottom of the Tetrimino
			ArrayList<Integer> leftBlocks = blockInControl.get(0).sideBlocksLeft();	//Array List that stores the blocks at the left-most side of the Tetrimino
			ArrayList<Integer> rightBlocks = blockInControl.get(0).sideBlocksRight();	//Array List that stores the block at the right-most side of the Tetrimino
			int indexFourthBlock = tetriminoFalling[3];	//holds the index of the last block of the current Tetrimino
			int blocksAboveNothing = 0;	//holds the number of blocks that from bottomBlocks that have no blocks immediately underneth them
			int blocksAtSide = 0; //holds the number of blocks that appear immediately next to either leftBlocks or rightBlocks
			int newRotWidth;	//holds the new rotation width of the newly created Tetrimino
			int newRandomBlockType;	//creates the new Tetrimino type for the new Tetrimino
		
			@Override
			public void handle(ActionEvent time)
			{
				for (int a = 0; a < bottomBlocks.size(); a++)	//for-loop that checks for any blocks immediately below the indexes of bottomBlocks
				{
					if (indexFourthBlock < 130 && grid[bottomBlocks.get(a) + 10].getFill().equals(Color.WHITE))
						blocksAboveNothing++;
				}
					
				if (blocksAboveNothing == bottomBlocks.size())	//if statement that drops the blocks of Tetrimino into the next blocks below based on the value of blocksAboveNothing
				{
					for (int j = 3; j > -1; j--)	//for-loop that updates the indexes of the blocks in the current Tetrimino
					{
						grid[tetriminoFalling[j]].setFill(Color.WHITE);	//makes the old index white
						tetriminoFalling[j] += 10;
						grid[tetriminoFalling[j]].setFill(currTetriminoColor);	//makes the new index the color of the Tetrimino
					}
					indexFourthBlock = tetriminoFalling[3];	//updates indexFourthBlock
					for (int c = 0; c < bottomBlocks.size(); c++)	//updates the indexes of the blocks at the bottom of the Tetrimino
					{
						bottomBlocks.set(c, bottomBlocks.get(c) + 10);
					}
					blocksAboveNothing = 0;	//updates blocksAboveNothing
				}
				else	//else statement that activates if the Tetrimino is eith at the bottom of the screen or is immediately above another block
				{
					index++;	//increases index to the next Tetrimino
					tetriminoFalling = blockInControl.get(index).getBlocks();	//stores the indexes of the next Tetrimino into tetriminoFalling
					bottomBlocks = blockInControl.get(index).bottomBlocks();	//updates bottomBlocks to the bottomBlocks of the next Tetrimino
					leftBlocks = blockInControl.get(index).sideBlocksLeft();	//updates leftBlockss to the sideBlocksLeft of the next Tetrimino
					rightBlocks = blockInControl.get(index).sideBlocksRight();	//updates rightBlockss to the sideBlocksRight of the next Tetrimino
					indexFourthBlock = blockInControl.get(index).getFourthBlock();	//updates indexFourthBlock to the 4th block of the next Tetrimino
					currTetriminoColor = blockInControl.get(index).getColor();	//updates the color of the indexes to that of the next Tetrimino
					for (int b = 0; b < 4; b++)	//for-loop that creates the next Tetrimino on grid[]
					{
						grid[tetriminoFalling[b]].setFill(currTetriminoColor);
					}
					blocksAboveNothing = 0;
				
					//creates a new Tetrimino and adds it ot blockInControl
					newRandomBlockType = (int) (Math.random()*6);
					if (newRandomBlockType == 0)
						newRotWidth = 2;
					else if (newRandomBlockType == 1)
						newRotWidth = 4;
					else if (newRandomBlockType > 1 && newRandomBlockType < 7)
						newRotWidth = 3;
					blockInControl.add(new Tetrimino(newRotWidth, newRandomBlockType));
				}
				
				/**
				 * 
				 * PUT MOVEMENT IMPLEMENTATION HERE
				 * 
				 */
			}
		};
		
		Timeline a = new Timeline(new KeyFrame(Duration.millis(1000), time));	//plays the EventHandler every second
		a.setCycleCount(Timeline.INDEFINITE);
		grid[0].requestFocus();	//request control for the first Rectangle of grid[] [THIS INDEX DOE NOT MOVE, IT ONLY RECEIVES INSTRUCTIONS]
		a.play();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}