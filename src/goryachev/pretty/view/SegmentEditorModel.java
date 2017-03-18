// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.view;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import goryachev.pretty.ColorScheme;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import research.fx.edit.FxEditorModel;


/**
 * Segment FxEditor Model.
 */
public class SegmentEditorModel
	extends FxEditorModel
{
	private final CList<Line> lines = new CList<>();
	
	
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
		TextFlow flow = new TextFlow();
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
		CList<Node> rv = new CList<>(formatted.size());
		for(Segment c: formatted)
		{
			rv.add(createText(c.getText(), c.getType()));
		}
		
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
