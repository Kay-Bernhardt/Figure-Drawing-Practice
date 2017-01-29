package util;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Util
{
	/**
	 * This method randomizes the given list with the current time as the seed
	 * @param fileList list to be randomized
	 * @return the randomized list
	 */
	public static ArrayList<File> randomizeList(ArrayList<File> fileList)
	{
		ArrayList<File> randomList = new ArrayList<File>();
		Random rnd = new Random(System.currentTimeMillis());
		int index = 0;
		int fileListSize = fileList.size();
		
		for(int i = 0; i < fileListSize; i++)
		{
			index = rnd.nextInt(fileList.size());
			randomList.add(fileList.remove(index));
		}
		return randomList;
	}
	
	/**
	 * Reads the contents of the specified folder and sorts out all image files
	 * 
	 * @param dir
	 *           directory path
	 * 
	 * @return array of Files
	 */
	public static ArrayList<File> readFolderContent(File dir)
	{
		// reading all files in a folder
		File folder = new File(dir.getAbsolutePath());
		File[] listOfFiles = folder.listFiles();
		ArrayList<File> imageList = new ArrayList<File>();

		for (int i = 0; i < listOfFiles.length; i++)
		{
			String mimeType = URLConnection.guessContentTypeFromName(listOfFiles[i].getAbsolutePath());
			if (mimeType != null)
			{
				String type = mimeType.split("/")[0].toLowerCase();
				if (type.equals("image"))
				{
					imageList.add(listOfFiles[i]);
				}
			}
		}
		return imageList;
	}
	
	/**
	 * Creates and shows an error dialog
	 * @param title
	 * @param header
	 * @param content
	 */
	public static void errorDialog(String title, String header, String content)
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				Alert alert = new Alert(AlertType.ERROR);
				DialogPane dialogPane = alert.getDialogPane();
				dialogPane.getStylesheets().add(
				   getClass().getResource("/application/style/application.css").toExternalForm());
				alert.setTitle(title);
				alert.setHeaderText(header);
				alert.setContentText(content);

				alert.showAndWait();
			}
		});
	}
	
	public static void informationDialogWithoutHeader(String message)
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				DialogPane dialogPane = alert.getDialogPane();
				dialogPane.getStylesheets().add(
					   getClass().getResource("/application/style/application.css").toExternalForm());
				alert.setTitle(null);
				alert.setHeaderText(null);
				alert.setContentText(message);
		
				alert.showAndWait();
			}
		});
	}
	
	public static String makeTimeLabelString(int currTime)
	{
		String str = "";
		int minute = currTime / 60;
		int second = currTime % 60;
		str = "";
		if(minute > 0)
		{
			str = str + minute + "m ";
		}
		if(second > 0)
		{
			str = str + second + "s ";
		}
		return str;
	}
	
	public static void playWarningSound()
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				String filepath = "sound.mp3";
			   Media sound = new Media(filepath);
			   MediaPlayer mediaPlayer = new MediaPlayer(sound);
			   mediaPlayer.play();
			}
		});
	}
}
