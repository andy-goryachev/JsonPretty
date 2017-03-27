// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.common.util.TextTools;


/**
 * Caret Spot.
 */
public class CaretSpot
{
	private final int position;
	private final String text;


	public CaretSpot(int position, String text)
	{
		this.position = position;
		this.text = text;
	}
	
	
	public String toString()
	{
		return "CaretSpot(" + position + "," + TextTools.trimNicely(text, 8) + ")";
	}
	
	
	public String getText()
	{
		return text;
	}
	
	
	public int getPosition()
	{
		return position;
	}
}
