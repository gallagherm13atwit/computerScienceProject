import javafx.stage.Stage;

public class computerScienceProject {
	
	public static void main(String[] args)
	{
		TetrisGUI something  = new TetrisGUI();
		Stage mainStage = new Stage();
		something.start(mainStage);
	}
}