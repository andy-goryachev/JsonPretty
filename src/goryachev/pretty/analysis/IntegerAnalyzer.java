// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.analysis;

import java.util.Map;

/**
 * Integer Analyzer.
 */
public class IntegerAnalyzer
	extends AbstractAnalyzer
{
	public IntegerAnalyzer(int pos, String text)
	{
		super(pos, text);
	}


	protected boolean isCharSupported(int c)
	{
		switch(c)
		{
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			return true;
		}
		return false;
	}
	
	
	protected void analyze(String s, Map<String,String> result)
	{
		int val;
		try
		{
			int n = Integer.parseInt(s);
			result.put("integer", String.valueOf(n)); // FIX
			
			// TODO unix time
			// java time
		}
		catch(Exception ignore)
		{
		}
	}
}
