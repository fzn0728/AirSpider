package com.ileodo.airspider;
 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
 
/**
 * A class generate dates
 * 
 * @author iLeoDo
 * 
 */
public class DateGenerator {
	
	static String dateFormat = "yyyy-MM-dd";
	static SimpleDateFormat format = new SimpleDateFormat(dateFormat);
	
	Date start;
	Date current;
	Date end;

	/**
	 * construct an DateGenerator
	 * @param start start date
	 * @param end end date
	 */
	public DateGenerator(String start, String end){
		try {
			this.start = format.parse(start);
			this.current = this.start;
			this.end = format.parse(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * whether next date in the range
	 * @return {@code true} if the next date is in the range
	 */
	public boolean hasNext(){
		return getNext().compareTo(this.end) <= 0;
	}
	
	/**
	 * get next date in String format
	 * @return a date string
	 */
	public String next(){
		Date prev = current;
		current = this.getNext();
		return format.format(prev);
	}
	
	/**
	 * get next date
	 * @return next date
	 */
	public Date getNext(){
		Calendar cal=Calendar.getInstance();
		cal.setTime(current);
		cal.add(Calendar.DAY_OF_YEAR,+1);
		return cal.getTime();
	}
	
	/**
	 * get current timestamp
	 * @return current timestamp
	 */
	public static String now(){
		SimpleDateFormat tsformatter   =   new   SimpleDateFormat   ("yyyy-MM-dd HH:mm:ss");     
		String tmp = tsformatter.format(new Date(System.currentTimeMillis()));
		return tmp;
	}

 
}