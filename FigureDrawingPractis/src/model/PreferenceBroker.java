package model;

import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import util.PrefObj;

public class PreferenceBroker
{
	private static PreferenceBroker instance = null;
	
	protected PreferenceBroker()
	{		
	}
	
	public static PreferenceBroker getInstance()
	{
	      if(instance == null) 
	      {
	    	  instance = new PreferenceBroker();
		  }
		  return instance;
	}
	
	public void setPref(Object o, String id)
	{
		Preferences prefs = Preferences.userNodeForPackage(PreferenceBroker.class);
		try
		{
			PrefObj.putObject( prefs, id, o );
		} catch (ClassNotFoundException | IOException | BackingStoreException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Object getPref(String id)
	{
		Preferences prefs = Preferences.userNodeForPackage(PreferenceBroker.class);		
		Object o = null;
		try
		{
			o = PrefObj.getObject( prefs, id );
		} catch (ClassNotFoundException | IOException | BackingStoreException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
}
