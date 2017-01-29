package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Session implements Serializable
{
	private static final long serialVersionUID = 1L;
	private ArrayList<File> imageList;
	private boolean practice;
	private ArrayList<ImageGroup> imageGroupList;
	private String name;

	public Session()
	{
		this.imageGroupList = new ArrayList<ImageGroup>();
		this.name = "Nameless session";
		this.practice = false;
	}

	
	public ArrayList<File> getImageList()
	{
		return imageList;
	}


	public void setImageList(ArrayList<File> imageList)
	{
		this.imageList = imageList;
	}


	public boolean isPractice()
	{
		return practice;
	}

	public void setPractice(boolean practice)
	{
		this.practice = practice;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void addImageGroup(ImageGroup ig)
	{
		this.imageGroupList.add(ig);
	}

	public ArrayList<ImageGroup> getImageGroupList()
	{
		return this.imageGroupList;
	}
	
	public boolean equals(Session session)
	{
		boolean flag = true;
		if(this.imageGroupList.size() == session.getImageGroupList().size())
		{
			for(int i = 0; i < this.imageGroupList.size(); i++)
			{
				if(!this.imageGroupList.get(i).equals(session.getImageGroupList().get(i)))
				{
					flag = false;
					break;
				}
			}
		}
		else
		{
			flag = false;
		}
		
		return flag;
	}
	
	public String toString()
	{
		return name;
	}
}
