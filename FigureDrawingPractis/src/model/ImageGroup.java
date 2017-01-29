package model;

import java.io.Serializable;

public class ImageGroup implements Serializable
{
	private static final long serialVersionUID = 1L;
	private int numberOfPicsInGroup; //number of pics in group
	private int timePerPic; //time per pic in seconds

	public ImageGroup()
	{
		this.numberOfPicsInGroup = 0;
		this.timePerPic = 0;
	}

	public ImageGroup(int time)
	{
		this.numberOfPicsInGroup = 0;
		this.timePerPic = time;
	}
	
	public ImageGroup(int time, int numberOfPicsInGroup)
	{
		this.timePerPic = time;
		this.numberOfPicsInGroup = numberOfPicsInGroup;
	}

	public int getNumberOfPicsInGroup()
	{
		return numberOfPicsInGroup;
	}

	/*
	public void setNumberOfPicsInGroup(int numberOfPicsInGroup)
	{
		this.numberOfPicsInGroup = numberOfPicsInGroup;
	}
	*/

	public int getTimePerPic()
	{
		return timePerPic;
	}

	public void setTime(int time)
	{
		this.timePerPic = time;
	}
	
	/**
	 * This method checks if the specified imageGroup has the same time and num values as this imageGroup
	 * 
	 * @param imageGroup the imageGroup to compare to.
	 * @return true if the imageGroups have the same values, false otherwise.
	 */
	public boolean equals(ImageGroup imageGroup)
	{
		boolean flag = false;
		if(this.numberOfPicsInGroup == imageGroup.getNumberOfPicsInGroup() && this.timePerPic == imageGroup.getTimePerPic())
		{
			flag = true;
		}		
		return flag;
	}

	public String toString()
	{
		int minute = timePerPic / 60;
		int second = timePerPic % 60;
		String string = new String(numberOfPicsInGroup + " drawings of ");
		if(minute > 0)
		{
			string = string + minute + "m ";
		}
		if(second > 0)
		{
			string = string + second + "s ";
		}
		string = string + "each";
		
		return string;
	}
}
