package application;

import java.io.IOException;

import application.ui_controller.MainController;
import application.ui_controller.ScreenNavigator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class Main extends Application
{
	
	@Override
	public void start(Stage stage)
	{
		try
		{
			stage.setTitle("Figure Drawing Practice");
			stage.setScene(createScene(loadMainPane()));			
			stage.show();
			
			//stage.setOnCloseRequest(e -> Platform.exit());
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
			{
            @Override
            public void handle(WindowEvent t) 
            {
                Platform.exit();
                System.exit(0);
            }
			});
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Loads the main fxml layout. Sets up the vista switching VistaNavigator.
	 * Loads the first vista into the fxml layout.
	 *
	 * @return the loaded pane.
	 * @throws IOException
	 *            if the pane could not be loaded.
	 */
	private Pane loadMainPane() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Pane mainPane = (Pane) loader.load(getClass().getResourceAsStream(ScreenNavigator.MAIN)); //mainPane = root?

		MainController mainController = loader.getController();

		ScreenNavigator.setMainController(mainController);
		ScreenNavigator.loadScreen(ScreenNavigator.START);

		return mainPane;
	}

	/**
	 * Creates the main application scene.
	 *
	 * @param mainPane
	 *           the main application layout.
	 *
	 * @return the created scene.
	 */
	private Scene createScene(Pane mainPane)
	{
		Scene scene = new Scene(mainPane);
		scene.getStylesheets().add(this.getClass().getResource("style/application.css").toExternalForm()); //the class that extends application is in the root folder (the application package in this case)

		return scene;
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
