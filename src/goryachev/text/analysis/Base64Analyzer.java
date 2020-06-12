// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.text.analysis;
import goryachev.common.util.Base64;
import goryachev.common.util.CKit;
import goryachev.common.util.Hex;


/**
 * Base64 Analyzer.
 */
public class Base64Analyzer
	extends AbstractAnalyzer
{
	private boolean classic;
	private boolean urlSafe;
	
	
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
			return true;
		case '+':
		case '/':
		case '=':
			classic = true;
			return true;
		case '-':
		case '_':
			urlSafe = true;
			return true;
		}
		return false;
	}
	
	
	protected byte[] decodeBase64(String s) throws Exception
	{
		if(urlSafe)
		{
			return Base64.decode(s, Base64.URL_SAFE);
		}
		else
		{
			return Base64.decode(s);
		}
	}
	
	
	protected void analyze(String s, AnalysisReport rep)
	{
		if(urlSafe && classic)
		{
			// likely not a base64
			return;
		}
		
		// decode json string
		s = JsonStringDecoder.decode(s);
		
		try
		{
			byte[] b = decodeBase64(s);
			String[] hex = Hex.toHexStringsASCII(b);
			rep.addSection(urlSafe ? "base64 url-safe" : "base64", hex);
			
			try
			{
				//  try decoding as a utf8 string
				String dec = new String(b, CKit.CHARSET_UTF8);
				if(checkPrintableString(dec))
				{
					String[] lines = breakLines(dec);
					rep.addSection("base64-encoded UTF-8", lines);
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
