package application.ui_controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class DelayController implements Initializable
{
	@FXML
	private Label timeLabel;
	
	private Thread thread;
	private boolean cancel;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		cancel = false;
		
		thread = new Thread()		
		{
			public void run()
			{
				delayLoop();
			}
		};
		thread.start();
	}
	
	
	public void delayLoop()
	{
		System.out.println("delay loop start");
		for(int i = 30; i > 0; i--)
		{
			if(cancel)
			{
				return;
			}
			final String time = ""+ (i + 10) / 10;
			Platform.runLater(new Runnable()
			{				
				@Override
				public void run()
				{
					timeLabel.setText(time);
				}
			});
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				ScreenNavigator.loadScreen(ScreenNavigator.IMAGE);
			}
		});
	}
	
	/**
	 * 
	 * @param event
	 */
	@FXML
	private void cancel(ActionEvent event)
	{
		cancel = true;
		ScreenNavigator.loadScreen(ScreenNavigator.START);
	}
	
}