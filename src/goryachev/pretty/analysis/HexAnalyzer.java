// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.analysis;
import goryachev.common.util.Base64;
import goryachev.common.util.CKit;
import goryachev.common.util.Hex;
import java.util.Map;


/**
 * Hex Analyzer.
 */
public class HexAnalyzer
	extends AbstractAnalyzer
{
	public HexAnalyzer(int pos, String text)
	{
		super(pos, text);
	}
	

	protected boolean isCharSupported(int c)
	{
		switch(c)
		{
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
		case 'a':
		case 'b':
		case 'c':
		case 'd':
		case 'e':
		case 'f':
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
		if((s.length() % 2) != 0)
		{
			// not hex
			return;
		}
		
		try
		{
			byte[] b = Hex.parseByteArray(s);
			String hex = Hex.toHexString(b);
			
			try
			{
				// try decoding as a utf8 string
				String dec = new String(b, CKit.CHARSET_UTF8);
				if(checkPrintableString(dec))
				{
					result.put("hex UTF-8", dec);
				}
			}
			catch(Exception e)
			{
			}
			
			try
			{
				// try as an ascii string
				String dec = toAscii(b);
				if(dec != null)
				{
					result.put("hex ASCII", dec);
				}
			}
			catch(Exception e)
			{
			}
		}
		catch(Exception ignore)
		{
		}
	}
}
