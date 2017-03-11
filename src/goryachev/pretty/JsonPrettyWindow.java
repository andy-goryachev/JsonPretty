// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.common.util.CKit;
import goryachev.fx.FX;
import goryachev.fx.FxDump;
import goryachev.fx.FxWindow;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;


/**
 * JsonPretty Window.
 */
public class JsonPrettyWindow
	extends FxWindow
{
	public static final Duration PERIOD = Duration.millis(200);
	public final TextFlow textField;
	public final ScrollPane scroll;
	protected final Clipboard clipboard;
	protected final StyleProcessor styleProcessor;
	protected String oldContent;
	
	
	public JsonPrettyWindow()
	{
		super("JsonPrettyWindow");
		
		styleProcessor = new StyleProcessor();
		
		textField = new TextFlow();
		textField.setPrefWidth(Region.USE_COMPUTED_SIZE);
		textField.addEventFilter(KeyEvent.ANY, (ev) -> ev.consume());
		
		scroll = new ScrollPane(textField);
		// FIX white background
		
		setTitle("Pretty Print JSON/XML " + Version.VERSION);
		setCenter(scroll);
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

		FxDump.attach(this);
	}
	
	
	protected void updateContent(String s)
	{
		List<Node> cs = styleProcessor.process(s);
		textField.getChildren().setAll(cs);
	}
}