package model;

import java.util.ArrayList;

public class SessionListBroker
{
	private static SessionListBroker instance = null;
	private ArrayList<Session> sessionList;
	
	@SuppressWarnings("unchecked")
	protected SessionListBroker()
	{
		sessionList = (ArrayList<Session>) PreferenceBroker.getInstance().getPref("sessionList");
		if (sessionList == null)
		{
			sessionList = new ArrayList<Session>();
		}
	}
	
	public static SessionListBroker getInstance()
	{
		if(instance == null) 
      {
    	  instance = new SessionListBroker();
	  }
	  return instance;
	}
	
	public ArrayList<Session> addSession(Session session)
	{
		sessionList.add(session);
		saveSessionList();
		return this.sessionList;
	}
	
	public ArrayList<Session> removeSession(int index)
	{
		sessionList.remove(index);
		saveSessionList();
		return this.sessionList;
	}

	public ArrayList<Session> getSessionList()
	{
		return this.sessionList;
	}

	public void setSessionList(ArrayList<Session> sessionList)
	{
		this.sessionList = sessionList;
		saveSessionList();
	}
	
	private void saveSessionList()
	{
		PreferenceBroker.getInstance().setPref(this.sessionList, "sessionList");
	}
}
