// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.D;
import goryachev.common.util.Log;
import goryachev.fx.FX;
import goryachev.pretty.format.JsonPrettyFormatter;
import goryachev.pretty.parser.ParseResult;
import goryachev.pretty.parser.RecursiveJsonParser;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;


/**
 * Style Processor.
 */
public class StyleProcessor
{
	// TODO from settings
	private Color commentColor = Color.RED;
	private Color errorColor = Color.MAGENTA;
	private Color ignoreColor = Color.GRAY;
	private Color nameColor = Color.DARKGREEN;
	private Color stringColor = FX.rgb(0x8d71cf);
	private Color textColor = Color.BLACK;
	private Color valueColor = Color.BLUE;
	
	
	public StyleProcessor()
	{
	}
	
	
	protected Paint getColor(Type t)
	{
		switch(t)
		{
		case COMMENT:
			return commentColor;
		case ERROR:
			return errorColor;
		case IGNORE:
			return ignoreColor;
		case NAME:
			return nameColor;
		case STRING_BEGIN:
		case STRING:
		case STRING_END:
			return stringColor;
		case VALUE:
			return valueColor;
		}
		return textColor;
	}


	public List<Node> process(String s)
	{
		try
		{
			// parse
			List<Segment> segments = parse(s);
			D.list(segments); // FIX
			
			// format
			CList<Segment> formatted = format(segments);
				
			CList<Node> rv = new CList<>(formatted.size());
			for(Segment c: formatted)
			{
				rv.add(createText(c.getText(), c.getType()));
			}
			return rv;
		}
		catch(Exception e)
		{
			Log.ex(e);
			return new CList<>(createText(CKit.stackTrace(e), Type.ERROR));
		}
	}
	
	
	protected List<Segment> parse(String text)
	{
		ParseResult r = new RecursiveJsonParser(text).parse();
		return r.getSegments();
	}
	
	
	protected CList<Segment> format(List<Segment> cs)
	{
		JsonPrettyFormatter f = new JsonPrettyFormatter(cs);
		// TODO set options
		return f.format();
	}
	

	protected Text createText(String text, Type type)
	{
		Text t = new Text(text);
		t.setFill(getColor(type));
		return t;
	}
}
