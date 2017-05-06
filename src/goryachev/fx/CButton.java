// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.control.Button;


/**
 * Convenient Button.
 */
public class CButton
	extends Button
{
	public CButton(String text, CAction a)
	{
		super(text);
		a.attach(this);
	}
	
	
	public CButton(String text)
	{
		super(text);
	}
	
	
	public CButton(String text, Runnable handler)
	{
		this(text, new CAction(handler));
	}
}
