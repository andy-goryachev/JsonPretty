// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package research.clipboard;
import goryachev.common.util.CKit;
import goryachev.common.util.D;
import goryachev.common.util.Log;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;
import javafx.stage.Stage;


public class TestClipboard
	extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}


	public void init() throws Exception
	{
		Log.initConsole();
	}


	public void start(Stage stage) throws Exception
	{
		CPane p = new CPane();
		stage.setScene(new Scene(p));
		stage.setWidth(300);
		stage.setHeight(200);
		stage.centerOnScreen();
		stage.show();
		
		new Thread()
		{
			public void run()
			{
				for(;;)
				{
					CKit.sleep(1000);
					FX.later(() -> check());
				}
			}
		}.start();
	}
	

	// test code 侘寂
	protected void check()
	{
		String blue = "blue";
		Clipboard c = Clipboard.getSystemClipboard();
		if(c.hasRtf())
		{
			String s = c.getRtf(); 
			D.p(s);
		}
	}
}
