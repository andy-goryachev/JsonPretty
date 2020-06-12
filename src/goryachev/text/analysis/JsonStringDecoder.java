// Copyright © 2017-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.text.analysis;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;


/**
 * Json String Decoder.
 */
public class JsonStringDecoder
{
	public static String decode(String s)
	{
		if(s.indexOf('\\') < 0)
		{
			return s;
		}
		
		int sz = s.length();
		SB sb = new SB(sz);
		for(int i=0; i<sz; i++)
		{
			char c = s.charAt(i);
			if(c == '\\')
			{
				i++;
				c = s.charAt(i);
				switch(c)
				{
				case 'b':
					c = '\b';
					break;
				case 'f':
					c = '\f';
					break;
				case 'n':
					c = '\n';
					break;
				case 'r':
					c = '\r';
					break;
				case 't':
					c = '\t';
					break;
				case 'u':
					String u = s.substring(i, i + 4);
					try
					{
						c = (char)(Hex.parseInt(u));
						i += 4;
					}
					catch(Exception e)
					{
					}
					break;
				}
			}
			else
			{
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
