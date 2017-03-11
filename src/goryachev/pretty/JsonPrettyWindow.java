// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty;
import goryachev.common.util.CKit;
import goryachev.fx.FxDump;
import goryachev.fx.FxWindow;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;


/**
 * JsonPretty Window.
 */
public class JsonPrettyWindow
	extends FxWindow
{
	public static final Duration PERIOD = Duration.millis(200);
	public final TextArea textField; // TODO replace with styled text
	protected final Clipboard clipboard;
	protected String oldContent;
	
	
	public JsonPrettyWindow()
	{
		super("JsonPrettyWindow");
		
		textField = new TextArea();
		textField.addEventFilter(KeyEvent.ANY, (ev) -> ev.consume());
		
		setTitle("Pretty Print JSON/XML " + Version.VERSION);
		setCenter(textField);
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
		textField.setText(s);
	}
}