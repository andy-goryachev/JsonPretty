// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.common.log.Log;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.FxAction;
import goryachev.fx.FxBoolean;
import goryachev.fx.FxCheckBox;
import goryachev.pretty.analysis.AnalysisReport;
import goryachev.pretty.analysis.Base64Analyzer;
import goryachev.pretty.analysis.HexAnalyzer;
import goryachev.pretty.analysis.IntegerAnalyzer;
import goryachev.pretty.format.PrettyFormatter;
import goryachev.pretty.parser.ParseResult;
import goryachev.pretty.parser.RecursiveJsonXmlParser;
import goryachev.pretty.parser.Segment;
import goryachev.pretty.parser.Type;
import goryachev.pretty.view.ContentView;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.input.Clipboard;
import javafx.util.Duration;


/**
 * Main Pane.
 */
public class MainPane
	extends CPane
{
	public final FxAction copyAction = new FxAction(this::copy);
	public final FxAction copyHtmlAction = new FxAction(this::copyHtml); 
	public final FxAction copyRtfAction = new FxAction(this::copyRtf); 
	public final FxAction pasteAction = new FxAction(this::pasteFromClipboard);
	public final FxCheckBox monitorClipboardCheckbox;
	public final ContentView view;
	public final DetailPane detailPane;
	public final SplitPane split;
	protected final SimpleBooleanProperty horizontalSplit = new SimpleBooleanProperty(true);
	protected static final Duration PERIOD = Duration.millis(200);
	protected final Clipboard clipboard;
	protected final FxBoolean monitorClipboardProperty = new FxBoolean(true);
	private Timeline timeline;
	private String oldContent;

	
	public MainPane()
	{
		view = new ContentView();
		
		detailPane = new DetailPane();
		
		view.caretSpotProperty().addListener((src) -> updateDetailPane());
		
		split = new SplitPane(view.getNode(), detailPane);
		split.setOrientation(Orientation.HORIZONTAL);
		split.setDividerPositions(0.8);

		setCenter(split);
		
		monitorClipboardProperty.addListener((s,p,c) -> updateClipboardMonitoring());
		
		// clipboard
		clipboard = Clipboard.getSystemClipboard();
		monitorClipboardCheckbox = new FxCheckBox("monitor clipboard");
		monitorClipboardCheckbox.selectedProperty().bindBidirectional(monitorClipboardProperty);
		FX.onChange(this::updateSplit, true, horizontalSplit);
	}
	
	
	protected void updateSplit()
	{
		boolean hor = horizontalSplit.get();
		split.setOrientation(hor ? Orientation.HORIZONTAL : Orientation.VERTICAL);
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
	
	
	public void updateContent(String s)
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
			PrettyFormatter f = new PrettyFormatter(segments);
			// TODO set options
			CList<Segment> formatted = f.format();
			return formatted;
		}
		catch(Exception e)
		{
			log.error(e);
			return new CList<Segment>(new Segment(Type.ERROR, CKit.stackTrace(e)));
		}
	}
	
	
	public void copy()
	{
		view.copy();
	}
	
	
	public void copyHtml()
	{
		view.copyHtml();
	}
	
	
	public void copyRtf()
	{
		view.copyRtf();
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
