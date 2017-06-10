// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.pretty.analysis;
import goryachev.common.util.CCalendar;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * Integer Analyzer.
 */
public class IntegerAnalyzer
	extends AbstractAnalyzer
{
	private static final long MIN_TIME_MS = time(1980, Calendar.JANUARY, 1);
	private static final long MAX_TIME_MS = time(2100, Calendar.DECEMBER, 31);
	private static final long MIN_TIME_UNIX = MIN_TIME_MS / 1000;
	private static final long MAX_TIME_UNIX = MAX_TIME_MS / 1000;
	private static final SimpleDateFormat javaFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS z");
	private static final SimpleDateFormat unixFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
	
	
	public IntegerAnalyzer(int pos, String text)
	{
		super(pos, text);
	}
	
	
	private static long time(int year, int month, int day)
	{
		CCalendar c = CCalendar.getInstance(TimeZone.getTimeZone("GMT"));
		c.setYear(year);
		c.setMonth(month);
		c.setDay(day);
		return c.getTime();
	}


	protected boolean isCharSupported(int c)
	{
		switch(c)
		{
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			return true;
		}
		return false;
	}
	
	
	protected void analyze(String s, AnalysisReport rep)
	{
		int val;
		try
		{
			long n = Long.parseLong(s);

			// java time
			if((n > MIN_TIME_MS) && (n < MAX_TIME_MS))
			{
				rep.addSection("java time", javaFormat.format(new Date(n)));
			}
			
			// unix time
			if((n > MIN_TIME_UNIX) && (n < MAX_TIME_UNIX))
			{
				rep.addSection("unix time", unixFormat.format(new Date(n * 1000)));
			}
		}
		catch(Exception ignore)
		{
		}
	}
}
