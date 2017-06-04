// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.view;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.D;
import goryachev.common.util.Hex;
import goryachev.common.util.Log;
import goryachev.common.util.SB;
import goryachev.common.util.html.HtmlTools;
import goryachev.fx.edit.CTextFlow;
import goryachev.fx.edit.Edit;
import goryachev.fx.edit.EditorSelection;
import goryachev.fx.edit.FxEditorModel;
import goryachev.fx.edit.Marker;
import goryachev.fx.edit.SelectionSegment;
import goryachev.pretty.ColorScheme;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


/**
 * Segment FxEditor Model.
 */
public class SegmentEditorModel
	extends FxEditorModel
{
	private CList<Line> lines = new CList<>();
	
	
	public SegmentEditorModel()
	{
	}


	public LoadInfo getLoadInfo()
	{
		return null;
	}


	public int getLineCount()
	{
		return lines.size();
	}


	public String getPlainText(int ix)
	{
		return lines.get(ix).getText();
	}
	
	
	public Edit edit(Edit ed) throws Exception
	{
		throw new Exception();
	}

	
	protected void copyOtherFormats(CMap<DataFormat,Object> clipboardData, EditorSelection sel)
	{
		try
		{
			StringWriter wr = new StringWriter();
			writeHtml(sel, wr);
			String html = wr.toString();
			
			D.print(html); // FIX
			
			clipboardData.put(DataFormat.HTML, html);
		}
		catch(Exception e)
		{
			// TODO communicate error to the ui
			Log.ex(e);
		}
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
			
			Segment[] ss = lines.get(i).segments;
			
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


	protected void writeSegments(Writer wr, Segment[] segments, int start, int end) throws Exception
	{
		int off = 0;
		for(Segment s: segments)
		{
			String text = s.getText();
			
			// FIX
			// whole, partial, none
			
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
			
			off += text.length();
		}
	}


	public Region getDecoratedLine(int ix)
	{
		Line line = lines.get(ix);
		CTextFlow flow = new CTextFlow();
		for(Segment s: line.segments)
		{
			flow.getChildren().add(createText(s.getText(), s.getType()));
		}
		return flow;
	}
	
	
	protected Text createText(String text, Type type)
	{
		Text t = new Text(text);
		t.setFill(ColorScheme.getColor(type));
		return t;
	}


	public void setSegments(List<Segment> formatted)
	{
		CList<Line> ln = new CList<>(256);
		CList<Segment> segs = new CList<>();
		for(Segment c: formatted)
		{
			switch(c.getType())
			{
			case LINEBREAK:
				Segment[] ss = new Segment[segs.size()];
				segs.toArray(ss);
				ln.add(new Line(ss));
				segs.clear();
				break;
			default:
				segs.add(c);
				break;
			}
		}
		
		if(segs.size() > 0)
		{
			Segment[] ss = new Segment[segs.size()];
			segs.toArray(ss);
			ln.add(new Line(ss));
		}
		
		lines = ln;
		fireAllChanged();
	}
	
	
	//
	
	
	protected static class Line
	{
		public final Segment[] segments;
		
		
		public Line(Segment[] segments)
		{
			this.segments = segments;
		}


		public String getText()
		{
			SB sb = new SB(128);
			for(Segment s: segments)
			{
				sb.a(s.getText());
			}
			return sb.toString();
		}
	}
}
