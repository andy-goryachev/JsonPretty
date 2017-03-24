// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.view.fxeditor;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import goryachev.pretty.ColorScheme;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import java.util.List;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import research.fx.edit.CTextFlow;
import research.fx.edit.FxEditorModel;


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


	public String getSearchText(int ix)
	{
		return lines.get(ix).getText();
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
