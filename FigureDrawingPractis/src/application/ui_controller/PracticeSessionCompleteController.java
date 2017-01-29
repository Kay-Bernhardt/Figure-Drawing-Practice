package application.ui_controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.Initializable;

public class PracticeSessionCompleteController implements Initializable
{
	private Thread thread;
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		thread = new Thread()
		{
			public void run()
			{
				delayLoop();
			}
		};
		thread.start();
	}

	private void delayLoop()
	{
		for(int i = 2; i > 0; i--)
		{
			try
			{
				Thread.sleep(1000);
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
				ScreenNavigator.loadScreen(ScreenNavigator.START);
			}
		});
	}
}
