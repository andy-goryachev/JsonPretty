// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Log;
import goryachev.fx.CMenu;
import goryachev.fx.CMenuBar;
import goryachev.fx.FX;
import goryachev.fx.FxDump;
import goryachev.fx.FxWindow;
import goryachev.fx.HPane;
import goryachev.pretty.format.JsonPrettyFormatter;
import goryachev.pretty.parser.ParseResult;
import goryachev.pretty.parser.RecursiveJsonParser;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import goryachev.pretty.view.BasedOnTextFlow;
import goryachev.pretty.view.fxeditor.BasedOnFxEditor;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.input.Clipboard;
import javafx.scene.paint.Color;
import javafx.util.Duration;


/**
 * JsonPretty Window.
 */
public class MainWindow
	extends FxWindow
{
	public static final Duration PERIOD = Duration.millis(200);
	public final IContentView view;
	public final DetailPane detailPane;
	public final SplitPane split;
	protected final Clipboard clipboard;
	protected String oldContent;
	
	
	public MainWindow()
	{
		super("MainWindow");
		
		view = Config.USE_FX_EDITOR ? new BasedOnFxEditor() : new BasedOnTextFlow();
		
		detailPane = new DetailPane();
		
		// FIX
//		view.selectionIndexProperty().addListener((src) -> updateDetailPane());
		
		split = new SplitPane(view.getNode(), detailPane);
		split.setOrientation(Orientation.HORIZONTAL);
		split.setDividerPositions(0.8);
		
		setTitle("Pretty Print JSON " + Version.VERSION);
		setTop(createMenu());
		setCenter(split);
		setBottom(createStatusBar());
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
	
	
	protected CMenuBar createMenu()
	{
		CMenu m;
		CMenuBar mb = new CMenuBar();
		// file
		mb.add(m = new CMenu("File"));
		m.add("Preferences");
		m.separator();
		m.add("Quit", FX.exitAction());
		// edit
		mb.add(m = new CMenu("Edit"));
		m.add("Copy");
		m.add("Save Selection As...");
		// view
		mb.add(m = new CMenu("View"));
		m.add("Layout");
		// help
		mb.add(m = new CMenu("Help"));
		m.add("About");
		return mb;
	}
	
	
	protected Node createStatusBar()
	{
		HPane p = new HPane();
		p.fill();
		p.add(FX.label("copyright © 2017 andy goryachev", Color.GRAY, new Insets(1, 10, 1, 2)));
		return p;
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
	
	
	protected void updateDetailPane()
	{
		// FIX
//		int ix = view.selectionIndexProperty().get();
//		String line = view.getLineAtCaret();
//		D.print(ix, line);
	}
}