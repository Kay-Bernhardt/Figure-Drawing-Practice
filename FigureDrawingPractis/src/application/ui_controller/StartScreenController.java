package application.ui_controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.binding.When;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.ImageGroup;
import model.PreferenceBroker;
import model.Session;
import model.SessionListBroker;
import util.Util;

public class StartScreenController implements Initializable
{
	@FXML
	private ToggleButton thirtyToggleButton;
	@FXML
	private ToggleButton fourtyFiveToggleButton;
	@FXML
	private ToggleButton oneToggleButton;
	@FXML
	private ToggleButton twoToggleButton;
	@FXML
	private ToggleButton fiveToggleButton;
	@FXML
	private ToggleButton tenToggleButton;
	@FXML
	private ToggleButton maximizeToggleButton;
	@FXML
	private ToggleButton onTopToggleButton;
	@FXML
	private ToggleButton randomizeToggleButton;
	@FXML
	private Tab practiceTab;
	@FXML
	private TabPane tabPane;
	@FXML
	private Label folderLabel;
	@FXML
	private Button customSessionButton;
	@FXML
	private ListView<Session> sessionListView;
	private ObservableList<Session> sessionList = FXCollections.observableArrayList();
	
	private File selectedDirectory;
	private ArrayList<File> imageList;
	private int time;
	private ToggleButton[] toggleButtonArray;
	private Thread thread;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		toggleButtonArray = new ToggleButton[]{thirtyToggleButton, fourtyFiveToggleButton, oneToggleButton, twoToggleButton, fiveToggleButton, tenToggleButton};
		time = 30;
		thirtyToggleButton.setSelected(true);
		
		sessionList.setAll(SessionListBroker.getInstance().getSessionList());
		if(sessionList.size() > 0)
		{
			sessionListView.setItems(sessionList);
		}
		
		customSessionButton.textProperty().bind(new When(sessionListView.getSelectionModel().selectedItemProperty().isNull()).then("New Custom Session").otherwise("Edit Selected Session"));
		
		selectedDirectory = (File) PreferenceBroker.getInstance().getPref("dir");
		if (selectedDirectory != null)
		{
			folderLabel.setText(selectedDirectory.toString());
			imageList = Util.readFolderContent(selectedDirectory);
			checkImageList();
		}
		
		thread = new Thread()
		{
			public void run()
			{
					try
					{
						Thread.sleep(100);
						tabPane.getSelectionModel().select(practiceTab);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}			
		};
		thread.start();
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				Stage stage = (Stage) folderLabel.getScene().getWindow();
				stage.setMaximized(false);
				stage.setAlwaysOnTop(false);
			}
		});
	}
	
	@FXML
   private void selectFolder(ActionEvent event) 
	{
		//selecting a folder
		File defaultDirectory;
		if (selectedDirectory != null)
		{
			defaultDirectory = selectedDirectory.getAbsoluteFile().getParentFile();
		}
		else
		{
			defaultDirectory = new File("c:/");
		}
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Choose image folder");
		chooser.setInitialDirectory(defaultDirectory);
		
		File temp = null;
		temp = chooser.showDialog(new Stage());
		if(temp != null)
		{
			selectedDirectory = temp;			
			imageList = Util.readFolderContent(selectedDirectory);
			if(checkImageList())
			{
				folderLabel.setText(selectedDirectory.toString());
				PreferenceBroker.getInstance().setPref(selectedDirectory, "dir");
			}
		}		
	}
	
	@FXML
	private void startSession(ActionEvent event)
	{
		Session session;
		if(sessionListView.getSelectionModel().getSelectedItems().isEmpty())
		{
			//No selection == Practice session
			session = new Session();
			ImageGroup practiceGroup = new ImageGroup(time);
			session.addImageGroup(practiceGroup);
			session.setPractice(true);
		}
		else
		{
			//Custom session selected == Custom session
			session = sessionListView.getSelectionModel().getSelectedItem();			
		}
		
		ScreenNavigator.setUserData(session);
		checkToggles(session);
		if(checkImageList())
		{
			session.setImageList(imageList);
			ScreenNavigator.loadScreen(ScreenNavigator.DELAY);
		}		
	}
	
	private Session checkToggles(Session session)
	{
		Stage stage = (Stage) folderLabel.getScene().getWindow();
		if(maximizeToggleButton.isSelected())
		{
			stage.setMaximized(true);
		}
		if(onTopToggleButton.isSelected())
		{
			stage.setAlwaysOnTop(true);
		}
		if(randomizeToggleButton.isSelected())
		{
			imageList = Util.randomizeList(imageList);
		}
		return session;
	}
	
	@FXML
	private void createCustomSession(ActionEvent event)
	{
		if(sessionListView.getSelectionModel().getSelectedItems().isEmpty())
		{
			ScreenNavigator.setUserData(-1);
		}
		else
		{
			ScreenNavigator.setUserData((Integer)sessionListView.getSelectionModel().getSelectedIndex());
		}
		ScreenNavigator.loadScreen(ScreenNavigator.CREATECUSTOMSESSION);
	}
	
	@FXML
	private void removeSession(ActionEvent event)
	{	
		if(!sessionListView.getSelectionModel().getSelectedItems().isEmpty())
		{
			sessionList.setAll(SessionListBroker.getInstance().removeSession(sessionListView.getSelectionModel().getSelectedIndex()));		
			sessionListView.getSelectionModel().clearSelection();
		}		
	}
	
	@FXML
	private void setTime(ActionEvent event)
	{
		for(int i = 0; i < toggleButtonArray.length; i++)
		{
			//System.out.println(i);
			//System.out.println(toggleButtonArray[i]);
			if (toggleButtonArray[i].isSelected())
			{
				toggleButtonArray[i].setSelected(false);
			}			
		}
		ToggleButton tb = (ToggleButton) event.getSource();
		tb.setSelected(true);
		time = Integer.parseInt(tb.getText().substring(0, tb.getText().length()-1));
		if(time < 30)
		{
			time = time * 60;
		}
	}
	
	/**
	 * checks if the image list is empty or not
	 * 
	 * @return true if the image list is not empty or null, false otherwise.
	 */
	private boolean checkImageList()
	{
		if(imageList.isEmpty() || imageList == null)
		{			
			Util.errorDialog("No image found error", "No image found", "The folder " + selectedDirectory + " contains no images!");
			return false;
		}
		return true;
	}
}
