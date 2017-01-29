package application.ui_controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class CustomSessionCompleteController implements Initializable
{
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		
	}

	/**
	 * 
	 * @param event
	 */
	@FXML
	private void back(ActionEvent event)
	{
		ScreenNavigator.loadScreen(ScreenNavigator.START);
	}
	
	/**
	 * 
	 * @param event
	 */
	@FXML
	private void restart(ActionEvent event)
	{
		ScreenNavigator.loadScreen(ScreenNavigator.DELAY);
	}
}
