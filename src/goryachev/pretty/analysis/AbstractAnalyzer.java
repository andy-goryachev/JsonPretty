// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.analysis;
import java.util.Map;


/**
 * AbstractAnalyzer.
 */
public abstract class AbstractAnalyzer
{
	protected abstract boolean isCharSupported(int c);
	
	protected abstract void analyze(String s, Map<String,String> result);
	
	//
	
	public final int pos;
	public final String initialText;
	
	
	public AbstractAnalyzer(int pos, String text)
	{
		this.pos = pos;
		this.initialText = text;
	}
	
	
	protected int peek(int off)
	{
		if(off < 0)
		{
			return -1;
		}
		else if(off >= initialText.length())
		{
			return -1;
		}
		
		return initialText.charAt(off);
	}
	
	
	protected String extractText()
	{
		int start = pos;
		for(int i=pos; i>=0; i--)
		{
			int c = peek(i - 1);
			if(!isCharSupported(c))
			{
				start = i;
				break;
			}
		}
		
		int end = pos;
		for(int i=pos; i<initialText.length(); i++)
		{
			int c = peek(i);
			if(!isCharSupported(c))
			{
				end = i;
				break;
			}
		}
		
		if(end > start)
		{
			return initialText.substring(start, end);
		}
		
		return null;
	}
	
	
	/** analyzes and adds text to the report if compatible data is found */
	public void report(Map<String,String> result)
	{
		String s = extractText();
		if(s != null)
		{
			analyze(s, result);
		}
	}
	
	
	protected boolean checkPrintableString(String s)
	{
		if(s == null)
		{
			return false;
		}
		
		for(int i=0; i<s.length(); )
		{
			int c = s.codePointAt(i);
			i += Character.charCount(c);
				
			if(c < 0x20)
			{
				switch(c)
				{
				case '\t':
				case '\r':
				case '\n':
					break;
				default:
					return false;
				}
			}
			else
			{
				if(!Character.isValidCodePoint(c))
				{
					return false;
				}
				
				switch(c)
				{
				case 0xfffd:
					return false;
				}
			}
		}
		
		return true;
	}
}
