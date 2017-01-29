package application.ui_controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import util.Util;
import util.WrappedImageView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import model.Session;

public class ImageScreenController implements Initializable
{
	@FXML
	WrappedImageView imageView;
	@FXML
	Label timeLabel;
	@FXML
	ToggleButton pauseToggleButton;

	private Thread thread;
	private boolean stop, pause, showNextImage, breakImage;
	private int imageGroupPos, ig, imageListPos;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		imageView.setPreserveRatio(true);
		stop = false;
		pause = false;
		showNextImage = false;
		breakImage = false;
		imageGroupPos = 0;
		ig = 0;
		imageListPos = 0;

		// thread to start the slide show after the initialize method finished
		thread = new Thread()
		{
			public void run()
			{
				slideShow();
			}
		};
		thread.start();
	}

	/**
	 * This method starts and maintains the slide show
	 */
	private void slideShow()
	{
		// TODO break this up into more methods		
		Session session = (Session) ScreenNavigator.getUserData();
		ArrayList<File> imageList = session.getImageList();
		
		Image image = new Image(imageList.get(0).toURI().toString());
		imageView.setImage(image);
		imageView.setClip(new ImageView(image));
		
		// image group loop
		for (ig = 0; ig < session.getImageGroupList().size(); ig++)
		{
			if (stop)
			{
				break;
			}
			int time = session.getImageGroupList().get(ig).getTimePerPic();
			int numberOfPicsInGroup = session.getImageGroupList().get(ig).getNumberOfPicsInGroup();
			if (numberOfPicsInGroup == 0)
			{
				numberOfPicsInGroup = imageList.size();
			}

			// image loop
			for (imageGroupPos = 0; imageListPos < imageList.size() && imageGroupPos < numberOfPicsInGroup; imageListPos++, imageGroupPos++)
			{
				if (stop || breakImage)
				{
					breakImage = false;
					break;
				}
				image = new Image(imageList.get(imageListPos).toURI().toString());
				imageView.setImage(image);
				imageView.setClip(new ImageView(image));
				
				if (imageListPos == imageList.size() - 1)
				{
					imageListPos = -1;
				}

				timeLoop(time);
			}
		}
		if(!stop)
		{
			sessionEnd(session.isPractice());
		}		
	}

	/**
	 * Waits the specified amount of time in seconds and changes the time label
	 * every second
	 * 
	 * @param time
	 *           how many seconds should pass before this method ends
	 */
	private void timeLoop(int time)
	{
		// time loop counts down from time to 0 and changes label every 1s
		for (int j = 0; j < time * 10; j++)
		{
			if (stop || showNextImage)
			{
				showNextImage = false;
				break;
			}
			while (pause && !showNextImage)
			{
				try
				{
					Thread.sleep(100);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			try
			{				
				final String str = Util.makeTimeLabelString(time - j  / 10);
				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{
						timeLabel.setText(str);
					}
				});
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Decides What screen to call if the session ends naturally (no more images to show)
	 * 
	 * @param isPractice
	 */
	private void sessionEnd(boolean isPractice)
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				if(isPractice)
				{
					ScreenNavigator.loadScreen(ScreenNavigator.PRACTICECOMPLETE);
				}
				else
				{
					ScreenNavigator.loadScreen(ScreenNavigator.CUSTOMCOMPLETE);
				}
			}
		});
	}

	/**
	 * Go back to the previous Image in the imageList. May also switch imageGroups to keep the correct time
	 * 
	 * @param event
	 */
	@FXML
	private void previous(ActionEvent event)
	{
		// if it is not the first image
		if (imageListPos > 0)
		{
			showNextImage = true;
			imageListPos = imageListPos - 2;
			
			// if it is the first image in the group but not the first image group
			// then the image group needs to change because the time might be
			// different
			if (imageGroupPos == 0 && ig != 0)
			{
				breakImage = true;
				ig = ig - 2;
			}
		}
	}

	/**
	 * Pauses the imageLoop
	 * 
	 * @param event
	 */
	@FXML
	private void pause(ActionEvent event)
	{
		System.out.println("pause called");
		if (pause)
		{
			pause = false;
			pauseToggleButton.setSelected(false);
		} else
		{
			pause = true;
			pauseToggleButton.setSelected(true);
		}
	}

	/**
	 * skips to the next image in the image list ends the current time loop may
	 * take 1 second
	 * 
	 * @param event
	 */
	@FXML
	private void next(ActionEvent event)
	{
		showNextImage = true;
	}

	/**
	 * Toggles the image between normal and Black & White mode
	 * 
	 * @param event
	 */
	@FXML
	private void blackAndWhite(ActionEvent event)
	{
		if (imageView.getEffect() == null)
		{
			ColorAdjust monochrome = new ColorAdjust();
			monochrome.setSaturation(-1.0);
			Blend blackAndWhite = new Blend(BlendMode.MULTIPLY, monochrome,
					new ColorInput(0, 0, imageView.getBoundsInParent().getWidth()/imageView.getImage().getWidth(), imageView.getBoundsInParent().getHeight()/imageView.getImage().getHeight(), Color.WHITE));
			imageView.setEffect(blackAndWhite);
		} else
		{
			imageView.setEffect(null);
		}
	}
	
	/**
	 * Ends all slide show loops and switches the screen back to the start screen
	 */
	@FXML
	private void toStartScreen(ActionEvent event)
	{
		stop = true;
		ScreenNavigator.loadScreen(ScreenNavigator.START);
	}
}