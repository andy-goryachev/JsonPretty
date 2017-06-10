// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.view;
import goryachev.common.util.CKit;
import goryachev.common.util.CMap;
import goryachev.common.util.Hex;
import goryachev.common.util.html.HtmlTools;
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
 * Html Clipboard Handler.
 */
public class HtmlClipboardHandler
	extends ClipboardHandlerBase
{
	private final SegmentEditorModel model;
	
	
	public HtmlClipboardHandler(SegmentEditorModel m)
	{
		super(DataFormat.HTML);
		this.model = m;
	}
	
	
	public Object copy(FxEditorModel m, EditorSelection sel) throws Exception
	{
		if(m != model)
		{
			return null;
		}
		
		StringWriter wr = new StringWriter();
		writeHtml(sel, wr);
		String html = wr.toString();
		return html;
	}
	

	protected void writeHtml(EditorSelection sel, StringWriter wr) throws Exception
	{
		sel = sel.getNormalizedSelection();
		
		wr.write("<html><head><style>\n");
		writeStyles(wr);
		wr.write("</style><body>\n");
		// TODO styles, head
		
		for(SelectionSegment s: sel.getSegments())
		{
			CKit.checkCancelled();
			writeHtmlSegment(s, wr);
		}
		
		wr.write("</body></html>\n");
	}
	
	
	protected void writeStyles(Writer wr) throws Exception
	{
		for(Type t: Type.values())
		{
			wr.write(".s");
			wr.write(String.valueOf(t.ordinal()));
			wr.write(" {color:");
			writeColor(wr, ColorScheme.getColor(t));
			wr.write(";}\n");
		}
	}
	
	
	protected void writeColor(Writer wr, Color c) throws Exception
	{
		wr.write("#");
		wr.write(to255(c.getRed()));
		wr.write(to255(c.getGreen()));
		wr.write(to255(c.getBlue()));
	}
	
	
	protected String to255(double x)
	{
		int v = (int)Math.round(255 * x);
		return Hex.toHexByte(v);
	}


	protected void writeHtmlSegment(SelectionSegment sel, Writer wr) throws Exception
	{
		Marker m0 = sel.getTop();
		Marker m1 = sel.getBottom();
		
		int first = m0.getLine();
		int last = m1.getLine();
		
		for(int i=first; i<=last; i++)
		{
			CKit.checkCancelled();
			
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
				wr.write("<br>\n");
				
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
				wr.write("<br>\n");
				break;
			case WHITESPACE:
				wr.write("&emsp;");
				break;
			default:
				wr.write("<span class=s");
				wr.write(String.valueOf(t.ordinal()));
				wr.write(">");
				wr.write(HtmlTools.safe(text));
				wr.write("</span>");
			}
		}
	}
}
