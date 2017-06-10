// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.analysis;
import goryachev.common.util.CList;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;


/**
 * Abstract Analyzer.
 */
public abstract class AbstractAnalyzer
{
	protected abstract boolean isCharSupported(int c);
	
	protected abstract void analyze(String s, AnalysisReport rep);
	
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
		int mx = initialText.length() + 1;
		for(int i=pos; i<mx; i++)
		{
			end = i;
			int c = peek(i);
			if(!isCharSupported(c))
			{
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
	public void report(AnalysisReport rep)
	{
		String s = extractText();
		if(s != null)
		{
			analyze(s, rep);
		}
	}
	
	
	protected String toAscii(byte[] b)
	{
		if(b != null)
		{
			SB sb = new SB(b.length);
			for(byte c: b)
			{
				int ch = c & 0xff;
				if(ch < ' ')
				{
					ch = '.';
				}
				else if(ch > 127)
				{
					ch = '.';
				}
				
				sb.append((char)ch);
			}
			return sb.toString();
		}
		return null;
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
	
	
	protected String[] toArray(CList<String> a)
	{
		if(a.size() == 0)
		{
			return null;
		}
		return a.toArray(new String[a.size()]);
	}
	
	
	protected String[] breakLines(String text)
	{
		CList<String> rv = new CList();
		SB sb = new SB();
		
		for(int i=0; i<text.length(); i++)
		{
			char c = text.charAt(i);
			switch(c)
			{
			case '\r':
			case '\n':
				addLine(rv, sb);
				break;
			default:
				sb.append(c);
				break;
			}
		}
		
		addLine(rv, sb);
		return toArray(rv);
	}
	
	
	protected void addLine(CList<String> list, SB sb)
	{
		if(sb.length() > 0)
		{
			list.add(sb.getAndClear());
		}
	}


	protected String[] breakBinary(byte[] bytes)
	{
		CList<String> a = new CList();
		SB sb = new SB();
		
		for(int i=0; i<bytes.length; i++)
		{
			int col = i % 16;
			if(col == 8)
			{
				sb.a("  ");
			}
			
			byte b = bytes[i];
			sb.a(Hex.toHexByte(b));
			sb.a(' ');
			
			if(col == 15)
			{
				a.add(sb.getAndClear());
			}
		}
		
		return toArray(a);
	}
}
