// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Log;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import goryachev.pretty.parser.ParseResult;
import goryachev.pretty.parser.ResilientJsonParser;
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
	protected static final Log log = Log.get("StyleProcessor");

	
	public StyleProcessor()
	{
	}


	public List<Node> process(String s)
	{
		try
		{
			// parse
			List<Segment> cs = parse(s);
			
			// format
			CList<Segment> formatted = format(cs);
				
			CList<Node> rv = new CList<>(formatted.size());
			for(Segment c: formatted)
			{
				rv.add(createText(c.getText(), c.getType()));
			}
			return rv;
		}
		catch(Exception e)
		{
			log.err(e);
			return new CList<>(createText(CKit.stackTrace(e), Type.ERROR));
		}
	}
	
	
	protected List<Segment> parse(String text)
	{
		ParseResult r = new ResilientJsonParser(text).parse();
		return r.getSegments();
	}
	
	
	// TODO format
	protected CList<Segment> format(List<Segment> cs)
	{
		int sz = cs.size() * 2;
		CList<Segment> rv = new CList<>(sz);
		
		for(Segment c: cs)
		{
			rv.add(c);
		}
		
		return rv;
	}


	protected Paint getColor(Type t)
	{
		switch(t)
		{
		case COMMENT:
			return Color.RED;
		case ERROR:
			return Color.MAGENTA;
		case NAME:
			return Color.DARKGREEN;
		case IGNORE:
			return Color.GRAY;
		case VALUE:
			return Color.BLUE;
		}
		return Color.BLACK;
	}
	

	protected Text createText(String text, Type type)
	{
		Text t = new Text(text);
		t.setFill(getColor(type));
		return t;
	}
}
