// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.view;
import goryachev.common.util.CKit;
import goryachev.fx.edit.ClipboardHandlerBase;
import goryachev.fx.edit.EditorSelection;
import goryachev.fx.edit.FxEditorModel;
import goryachev.fx.edit.Marker;
import goryachev.fx.edit.SelectionSegment;
import goryachev.pretty.ColorScheme;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import java.io.StringWriter;
import java.io.Writer;
import javafx.scene.input.DataFormat;
import javafx.scene.paint.Color;


/**
 * RTF Clipboard Handler.
 */
public class RtfClipboardHandler
	extends ClipboardHandlerBase
{
	private final SegmentEditorModel model;
	
	
	public RtfClipboardHandler(SegmentEditorModel m)
	{
		super(DataFormat.RTF);
		this.model = m;
	}


	public Object copy(FxEditorModel m, EditorSelection sel) throws Exception
	{
		if(m != model)
		{
			return null;
		}
		
		StringWriter wr = new StringWriter();
		writeRtf(sel, wr);
		String rtf = wr.toString();
		return rtf;
	}
	
	
	protected void writeRtf(EditorSelection sel, StringWriter wr) throws Exception
	{
		sel = sel.getNormalizedSelection();
		
		wr.write("{\\rtf1\\ansi\\uc1\\deff0{\\fonttbl{\\f0\\fnil Courier New;}}\n");
		
		writeStyles(wr);
		
		wr.write("{\\f0\\fs18 \\fi0\\ql ");
		
		for(SelectionSegment s: sel.getSegments())
		{
			CKit.checkCancelled();
			writeSelectionSegment(s, wr);
		}
		
		wr.write("}}\n");
	}
	
	
	protected void writeStyles(Writer wr) throws Exception
	{
		wr.write("{\\colortbl");
		
		for(Type t: Type.values())
		{
			writeColor(wr, ColorScheme.getColor(t));
		}
		
		wr.write("}\n");
	}
	
	
	protected void writeColor(Writer wr, Color c) throws Exception
	{
		wr.write("\\red");
		wr.write(to255(c.getRed()));
		wr.write("\\green");
		wr.write(to255(c.getGreen()));
		wr.write("\\blue");
		wr.write(to255(c.getBlue()));
		wr.write(";");
	}
	
	
	protected String to255(double x)
	{
		int v = (int)Math.round(255 * x);
		return String.valueOf(v);
	}


	protected void writeSelectionSegment(SelectionSegment sel, Writer wr) throws Exception
	{
		Marker m0 = sel.getTop();
		Marker m1 = sel.getBottom();
		
		int first = m0.getLine();
		int last = m1.getLine();
		boolean lineBreak = false;
		
		for(int i=first; i<=last; i++)
		{
			CKit.checkCancelled();
			
			if(lineBreak)
			{
				wr.write("\\par \\fi0\\ql ");
			}
			else
			{
				lineBreak = true;
			}
			
			Segment[] ss = model.getSegments(i);
			
			if(i == first)
			{
				if(i == last)
				{
					writeSegments(wr, ss, m0.getLineOffset(), m1.getLineOffset());
				}
				else
				{
					writeSegments(wr, ss, m0.getLineOffset(), Integer.MAX_VALUE);
				}
			}
			else
			{
				if(i == last)
				{
					writeSegments(wr, ss, 0, m1.getLineOffset());
				}
				else
				{
					writeSegments(wr, ss, 0, Integer.MAX_VALUE);
				}
			}
		}
	}


	protected void writeSegments(Writer wr, Segment[] segments, int startIndex, int endIndex) throws Exception
	{
		int off = 0;
		for(Segment s: segments)
		{
			String text = s.getText();
			int len = text.length();
			int beg = off;
			int end = off + len;
			off += len;
			
			// 1: *** |    |
			// 2:   **|**  |
			// 3:     | ** |
			// 4:     |  **|**
			// 5:     |    | ***
			// 6:  ***|****|****
			
			if(end < startIndex)
			{
				// #1
				continue;
			}
			else if(beg > endIndex)
			{
				// #5
				continue;
			}
			else if(beg < startIndex)
			{
				if(end < endIndex)
				{
					// #2
					text = text.substring(startIndex - beg);
				}
				else
				{
					// #6
					text = text.substring(startIndex - beg, endIndex - beg);
				}
			}
			else if(end < endIndex)
			{
				// #3
			}
			else
			{
				// #4
				text = text.substring(0, endIndex - beg);
			}
			
			Type t = s.getType();
			switch(t)
			{
			case LINEBREAK:
				break;
			case WHITESPACE:
			default:
				wr.write("{\\cf");
				wr.write(String.valueOf(t.ordinal()));
				wr.write(' ');
				writeText(wr, text);
				wr.write("}\n");
				break;
			}
		}
	}


	protected void writeText(Writer wr, String text) throws Exception
	{
		int len = text.length();
		for(int i=0; i<len; i++)
		{
			char c = text.charAt(i);
			if((c >= 0x20) && (c <= 0x7f))
			{
				switch(c)
				{
				case '\\':
					wr.write("\\\\");
					break;
				case '{':
					wr.write("\\{");
					break;
				case '}':
					wr.write("\\}");
					break;
				default:
					wr.write(c);
				}
			}
			else
			{
				wr.write("\\u");
				wr.write(String.valueOf((short)c));
				wr.write("?");
			}
		}
	}
}
