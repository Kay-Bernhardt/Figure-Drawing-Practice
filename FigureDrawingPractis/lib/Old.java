package application;
	
import java.io.File;
import java.net.MalformedURLException;

import javafx.application.Application;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
/**

public class Old extends Application 
{
	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
			File[] listOfFiles = readFolderContent(selectFolder(primaryStage));
			
			/*
			//displaying the first item in the folder
			//call this on a pane (root is a pane)
			root.getChildren().add(new ImageView(listOfFiles[0].toURI().toURL().toExternalForm()));
			*
			
			for (int i = 0; i < listOfFiles.length; i++)
			{
				System.out.println(i);
				//root.getChildren().add(new ImageView(listOfFiles[i].toURI().toURL().toExternalForm()));
				displayImage(listOfFiles[i], root);
				Thread.sleep(1000);
			}
			System.out.println("test");
			Thread.sleep(2000);
			
			
		} catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
	
	public File selectFolder(Stage stage)
	{
		//selecting a folder
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("JavaFX Projects");
		File defaultDirectory = new File("c:/");
		chooser.setInitialDirectory(defaultDirectory);
		File selectedDirectory = chooser.showDialog(stage);
		
		return selectedDirectory;
	}
	
	public File[] readFolderContent(File dir)
	{
		//reading all files in a folder
		File folder = new File(dir.getAbsolutePath());
		File[] listOfFiles = folder.listFiles();
		return listOfFiles;
		
		/*
		for (File file : listOfFiles) 
			{
			    if (file.isFile()) 
			    {
			        System.out.println(file.getName());
			    }
			}
		*
	}
	
	public void displayImage(File file, BorderPane pane)
	{
		try {
			pane.getChildren().add(new ImageView(file.toURI().toURL().toExternalForm()));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
**/