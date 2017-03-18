// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Log;
import goryachev.fx.FxDump;
import goryachev.fx.FxWindow;
import goryachev.pretty.format.JsonPrettyFormatter;
import goryachev.pretty.parser.ParseResult;
import goryachev.pretty.parser.RecursiveJsonParser;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import goryachev.pretty.view.BasedOnTextFlow;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.Clipboard;
import javafx.util.Duration;


/**
 * JsonPretty Window.
 */
public class JsonPrettyWindow
	extends FxWindow
{
	public static final Duration PERIOD = Duration.millis(200);
	public final IContentView view;
	protected final Clipboard clipboard;
	protected String oldContent;
	
	
	public JsonPrettyWindow()
	{
		super("JsonPrettyWindow");
		
		view = new BasedOnTextFlow();
//		view = new BasedOnFxEditor();
		
		setTitle("Pretty Print JSON " + Version.VERSION);
		setCenter(view.getNode());
		setSize(600, 700);

		clipboard = Clipboard.getSystemClipboard();
		
		Timeline timeline = new Timeline(new KeyFrame(PERIOD, (ev) ->
		{
			if(clipboard.hasString())
			{
				String s = clipboard.getString();
				if(CKit.notEquals(oldContent, s))
				{
					updateContent(s);
					oldContent = s;
				}
			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();

		// debugging
		FxDump.attach(this);
	}
	
	
	protected void updateContent(String s)
	{
		List<Segment> ss = parseAndFormat(s);
		view.setParsedSegments(ss);
	}


	protected List<Segment> parseAndFormat(String text)
	{
		if(text == null)
		{
			return new CList();
		}
		
		try
		{
			// parse
			ParseResult r = new RecursiveJsonParser(text).parse();
			List<Segment> segments = r.getSegments();
			//D.list(segments); // FIX
			
			// format
			JsonPrettyFormatter f = new JsonPrettyFormatter(segments);
			// TODO set options
			CList<Segment> formatted = f.format();
			return formatted;
		}
		catch(Exception e)
		{
			Log.ex(e);
			return new CList<Segment>(new Segment(Type.ERROR, CKit.stackTrace(e)));
		}
	}
}