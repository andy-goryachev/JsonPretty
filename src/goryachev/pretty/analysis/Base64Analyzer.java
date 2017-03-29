// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.analysis;
import goryachev.common.util.Base64;
import goryachev.common.util.CKit;
import goryachev.common.util.Hex;
import java.util.Map;


/**
 * Base64 Analyzer.
 */
public class Base64Analyzer
	extends AbstractAnalyzer
{
	public Base64Analyzer(int pos, String text)
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
		case 'G':
		case 'H':
		case 'I':
		case 'J':
		case 'K':
		case 'L':
		case 'M':
		case 'N':
		case 'O':
		case 'P':
		case 'Q':
		case 'R':
		case 'S':
		case 'T':
		case 'U':
		case 'V':
		case 'W':
		case 'X':
		case 'Y':
		case 'Z':
		case 'a':
		case 'b':
		case 'c':
		case 'd':
		case 'e':
		case 'f':
		case 'g':
		case 'h':
		case 'i':
		case 'j':
		case 'k':
		case 'l':
		case 'm':
		case 'n':
		case 'o':
		case 'p':
		case 'q':
		case 'r':
		case 's':
		case 't':
		case 'u':
		case 'v':
		case 'w':
		case 'x':
		case 'y':
		case 'z':
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
		case '+':
		case '/':
		case '=':
			return true;
		}
		return false;
	}
	
	
	protected void analyze(String s, Map<String,String> result)
	{
		if((s.length() % 3) != 0)
		{
			// not base-64
			return;
		}
		
		try
		{
			byte[] b = Base64.decode(s);
			String hex = Hex.toHexString(b);
			result.put("base64", hex);
			
			// TODO try to decode as a utf8 string
			try
			{
				String dec = new String(b, CKit.CHARSET_UTF8);
				result.put("base64 decoded as UTF-8", dec);
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
