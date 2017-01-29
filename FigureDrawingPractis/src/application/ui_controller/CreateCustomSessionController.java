package application.ui_controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import model.ImageGroup;
import model.Session;
import model.SessionListBroker;
import util.Util;

public class CreateCustomSessionController implements Initializable
{
	@FXML
	private Spinner<Integer> numPicsSpinner;
	@FXML
	private Spinner<Integer> timePicsSecondSpinner;
	@FXML
	private Spinner<Integer> timePicsMinuteSpinner;
	@FXML
	private TextField sessionNameTextField;
	@FXML
	private Label timeLabel;
	@FXML
	private ListView<ImageGroup> imageGroupListView;
	private ObservableList<ImageGroup> imageGroupList = FXCollections.observableArrayList();

	private Session session;
	private int index;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		index = (int) ScreenNavigator.getUserData();
		if (index == -1)
		{
			session = new Session();
			sessionNameTextField.setText(session.toString());
		} else
		{
			session = SessionListBroker.getInstance().getSessionList().get(index);
			sessionNameTextField.setText(session.toString());
		}
		imageGroupList.setAll(session.getImageGroupList());
		imageGroupListView.setItems(imageGroupList);

		numPicsSpinner.getValueFactory().setValue(0);
		timePicsSecondSpinner.getValueFactory().setValue(0);
		timePicsMinuteSpinner.getValueFactory().setValue(0);

		numPicsSpinner.setEditable(true);
		timePicsSecondSpinner.setEditable(true);
		timePicsMinuteSpinner.setEditable(true);

		TextFormatter<Integer> numPicFormatter = new TextFormatter<Integer>(
				numPicsSpinner.getValueFactory().getConverter(), numPicsSpinner.getValueFactory().getValue());
		numPicsSpinner.getEditor().setTextFormatter(numPicFormatter);
		numPicsSpinner.getValueFactory().valueProperty().bindBidirectional(numPicFormatter.valueProperty());

		TextFormatter<Integer> timePicSecondFormatter = new TextFormatter<Integer>(
				timePicsSecondSpinner.getValueFactory().getConverter(), timePicsSecondSpinner.getValueFactory().getValue());
		timePicsSecondSpinner.getEditor().setTextFormatter(timePicSecondFormatter);
		timePicsSecondSpinner.getValueFactory().valueProperty().bindBidirectional(timePicSecondFormatter.valueProperty());

		TextFormatter<Integer> timePicMinuteFormatter = new TextFormatter<Integer>(
				timePicsMinuteSpinner.getValueFactory().getConverter(), timePicsMinuteSpinner.getValueFactory().getValue());
		timePicsMinuteSpinner.getEditor().setTextFormatter(timePicMinuteFormatter);
		timePicsMinuteSpinner.getValueFactory().valueProperty().bindBidirectional(timePicMinuteFormatter.valueProperty());
		
		setTimeLabel();

		imageGroupListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ImageGroup>()
		{
			@Override
			public void changed(ObservableValue<? extends ImageGroup> observable, ImageGroup oldValue, ImageGroup newValue)
			{
				// Your action here
				if (imageGroupListView.getSelectionModel().getSelectedItem() != null)
				{
					numPicsSpinner.getValueFactory()
							.setValue(imageGroupListView.getSelectionModel().getSelectedItem().getNumberOfPicsInGroup());
					int minute = imageGroupListView.getSelectionModel().getSelectedItem().getTimePerPic() / 60;
					int second = imageGroupListView.getSelectionModel().getSelectedItem().getTimePerPic() % 60;

					timePicsSecondSpinner.getValueFactory().setValue(second);
					timePicsMinuteSpinner.getValueFactory().setValue(minute);
				} else
				{
					numPicsSpinner.getValueFactory().setValue(0);
					timePicsSecondSpinner.getValueFactory().setValue(0);
					timePicsMinuteSpinner.getValueFactory().setValue(0);
				}
			}
		});
	}

	@FXML
	private void saveRow(ActionEvent event)
	{
		ImageGroup imageGroup = new ImageGroup(timePicsMinuteSpinner.getValue() * 60 + timePicsSecondSpinner.getValue(),
				numPicsSpinner.getValue());
		if (!imageGroupListView.getSelectionModel().getSelectedItems().isEmpty())
		{
			// replace
			int index = imageGroupListView.getSelectionModel().getSelectedIndex();
			imageGroupList.add(index, imageGroup);
			imageGroupList.remove(index + 1);
			imageGroupListView.setItems(imageGroupList);
			imageGroupListView.getSelectionModel().clearSelection();
		} else
		{
			// add
			imageGroupList.add(imageGroup);
			imageGroupListView.setItems(imageGroupList);
		}
		setTimeLabel();
	}

	@FXML
	private void removeSelectedRow(ActionEvent event)
	{
		if (imageGroupListView.getSelectionModel().getSelectedItem() != null)
		{
			int index = imageGroupListView.getSelectionModel().getSelectedIndex();
			imageGroupList.remove(index);
			imageGroupListView.getSelectionModel().clearSelection();
			setTimeLabel();
		}
	}

	@FXML
	private void saveSession(ActionEvent event)
	{
		session = new Session();
		for (int i = 0; i < imageGroupList.size(); i++)
		{
			session.addImageGroup(imageGroupList.get(i));
		}
		if (sessionNameTextField.getText().length() > 0)
		{
			session.setName(sessionNameTextField.getText());
		}
		Session temp = checkForDuplicate();
		if(temp == null)
		{
			//session does not already exist
			if (index == -1)
			{
				SessionListBroker.getInstance().addSession(session);
			} else
			{
				SessionListBroker.getInstance().removeSession(index);
				SessionListBroker.getInstance().addSession(session);
			}
			ScreenNavigator.loadScreen(ScreenNavigator.START);
		}
		else
		{
			//session is a duplicate
			String str = "The created session is a duplicate of the already existing session: " + temp.getName();
			Util.informationDialogWithoutHeader(str);
		}
	}

	private void setTimeLabel()
	{
		String string = new String("Total session time: ");
		int secondsTotal = 0;
		for (int i = 0; i < imageGroupList.size(); i++)
		{
			secondsTotal += imageGroupList.get(i).getTimePerPic() * imageGroupList.get(i).getNumberOfPicsInGroup();
		}
		int second = secondsTotal % 60;
		int minute = secondsTotal / 60;

		if (minute > 0)
		{
			string = string + minute + "m ";
		}
		if (second > 0)
		{
			string = string + second + "s ";
		}

		timeLabel.setText(string);
	}
	
	private Session checkForDuplicate()
	{
		ArrayList<Session> sessionList = SessionListBroker.getInstance().getSessionList();
		for(int i = 0; i < sessionList.size(); i++)
		{
			if(session.equals(sessionList.get(i)))
			{
				return sessionList.get(i);
			}
		}
		return null;
	}

	@FXML
	private void cancel(ActionEvent event)
	{
		ScreenNavigator.loadScreen(ScreenNavigator.START);
	}
}
