// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Log;
import goryachev.fx.CAction;
import goryachev.fx.CBooleanProperty;
import goryachev.fx.CButton;
import goryachev.fx.CCheckBox;
import goryachev.fx.CCheckMenuItem;
import goryachev.fx.CMenu;
import goryachev.fx.CMenuBar;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.FxDump;
import goryachev.fx.FxWindow;
import goryachev.fx.HPane;
import goryachev.pretty.analysis.Base64Analyzer;
import goryachev.pretty.analysis.HexAnalyzer;
import goryachev.pretty.analysis.IntegerAnalyzer;
import goryachev.pretty.format.JsonPrettyFormatter;
import goryachev.pretty.parser.ParseResult;
import goryachev.pretty.parser.RecursiveJsonXmlParser;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import goryachev.pretty.view.ContentView;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
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
	public final ContentView view;
	public final DetailPane detailPane;
	public final SplitPane split;
	protected final Clipboard clipboard;
	protected String oldContent;
	protected final CBooleanProperty monitorClipboardProperty = new CBooleanProperty(true, this::updateClipboardMonitoring);
	protected final CCheckBox monitorClipboardCheckbox;
	protected final SimpleBooleanProperty horizontalSplit = new SimpleBooleanProperty(true);
	public final CAction copyAction = new CAction(this::copy); 
	public final CAction pasteAction = new CAction(this::pasteFromClipboard);
	private Timeline timeline;
	
	
	public MainWindow()
	{
		super("MainWindow");
				
		view = new ContentView();
		
		detailPane = new DetailPane();
		
		monitorClipboardCheckbox = new CCheckBox("monitor clipboard");
		monitorClipboardCheckbox.selectedProperty().bindBidirectional(monitorClipboardProperty);
		
		view.caretSpotProperty().addListener((src) -> updateDetailPane());
		
		split = new SplitPane(view.getNode(), detailPane);
		split.setOrientation(Orientation.HORIZONTAL);
		split.setDividerPositions(0.8);
		
		setTitle("Pretty Print JSON " + Version.VERSION);
		setTop(createMenu());
		setCenter(split);
		setBottom(createStatusBar());
		setSize(600, 700);

		clipboard = Clipboard.getSystemClipboard();
		
		// preferences
		bind("HSPLIT", horizontalSplit);
		FX.listen(this::updateSplit, true, horizontalSplit);
		bind("MONITOR_CLIPBOARD", monitorClipboardProperty);
		
		updateClipboardMonitoring();

		// debugging
		FxDump.attach(this);
	}
	
	
	protected Node createMenu()
	{
		CMenu m;
		CMenuBar mb = new CMenuBar();
		// file
		mb.add(m = new CMenu("File"));
		m.add("Save As...");
		m.separator();
		m.add("Preferences");
		m.separator();
		m.add("Quit", FX.exitAction());
		// edit
		mb.add(m = new CMenu("Edit"));
		m.add("Copy", copyAction);
		m.add("Save Selection As...");
		m.separator();
		m.add("Select All", view.textField.selectAllAction);
		m.separator();
		m.add(new CCheckMenuItem("Monitor Clipboard", monitorClipboardProperty));
		m.add("Paste from Clipboard", pasteAction);
		// view
		mb.add(m = new CMenu("View"));
		m.add(new CCheckMenuItem("Detail Pane on a Side", horizontalSplit));
		// help
		mb.add(m = new CMenu("Help"));
		m.add("About");
		
//		mb.addFill();
//		mb.add(monitorClipboardCheckbox);
//		mb.add(new CButton("Paste", pasteAction));
//		return mb;
		
		CPane p = new CPane();
		p.setPadding(1);
		p.setHGap(10);
		p.addColumns
		(
			CPane.FILL,
			CPane.PREF,
			CPane.PREF
		);
		p.add(0, 0, mb);
		p.add(1, 0, monitorClipboardCheckbox);
		p.add(2, 0, new CButton("Paste", pasteAction));
		
		return p;
	}
	
	
	protected Node createStatusBar()
	{
		HPane p = new HPane();
		p.fill();
		p.add(FX.label("copyright © 2017 andy goryachev", Color.GRAY, new Insets(1, 10, 1, 2)));
		return p;
	}
	
	
	protected void updateSplit()
	{
		boolean hor = horizontalSplit.get();
		split.setOrientation(hor ? Orientation.HORIZONTAL : Orientation.VERTICAL);
	}
	
	
	protected void updateContent(String s)
	{
		List<Segment> ss = parseAndFormat(s);
		view.setTextSegments(ss);
		detailPane.clear();
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
			ParseResult r = new RecursiveJsonXmlParser(text).parse();
			List<Segment> segments = r.getSegments();
			
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
		CaretSpot sp = view.caretSpotProperty().get();
		AnalysisReport rep = analyze(sp.getPosition(), sp.getText());
		detailPane.setReport(rep);
	}


	protected AnalysisReport analyze(int pos, String text)
	{		
		AnalysisReport rep = new AnalysisReport();
		
		// analyzers
		new Base64Analyzer(pos, text).report(rep);
		new HexAnalyzer(pos, text).report(rep);
		new IntegerAnalyzer(pos, text).report(rep);
		
		return rep;
	}
	
	
	protected void copy()
	{
		view.copy();
	}
	
	
	protected void pasteFromClipboard()
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
	}
	
	
	protected void updateClipboardMonitoring()
	{
		if(monitorClipboardProperty.get())
		{
			if(timeline == null)
			{
				timeline = new Timeline(new KeyFrame(PERIOD, (ev) -> pasteFromClipboard()));
				timeline.setCycleCount(Timeline.INDEFINITE);
				timeline.play();
			}
		}
		else
		{
			if(timeline != null)
			{
				timeline.stop();
				timeline = null;
			}
		}
	}
}