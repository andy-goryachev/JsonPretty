// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Log;
import goryachev.pretty.json.Chunk;
import goryachev.pretty.json.ChunkType;
import goryachev.pretty.json.ParseResult;
import goryachev.pretty.json.SimpleJsonParser;
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
			ParseResult r = new SimpleJsonParser(s).parse();
			return convert(r);
		}
		catch(Exception e)
		{
			log.err(e);
			return new CList<>(createText(CKit.stackTrace(e), ChunkType.ERROR));
		}
	}


	protected List<Node> convert(ParseResult r)
	{
		Chunk[] cs = r.getChunks();
		CList<Node> rv = new CList<>(cs.length);
		for(Chunk c: cs)
		{
			rv.add(createText(c.getText(), c.getType()));
		}
		return rv;
	}


	protected Paint getColor(ChunkType t)
	{
		switch(t)
		{
		case COMMENT:
			return Color.RED;
		case ERROR:
			return Color.MAGENTA;
		case IDENTIFIER:
			return Color.DARKGREEN;
		case VALUE:
			return Color.BLUE;
		}
		return Color.BLACK;
	}
	

	protected Text createText(String text, ChunkType type)
	{
		Text t = new Text(text);
		t.setFill(getColor(type));
		return t;
	}
}
