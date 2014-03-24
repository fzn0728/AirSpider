package com.ileodo.airspider;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Some Utility tools
 * @author LeoDong
 *
 */
public class Utils {

	/**
	 * get substring by regular expression
	 * 
	 * @param reg
	 *            regular expression
	 * @param body
	 *            string to be analysis
	 * @return the substring satisfied the regular expression.
	 */
	public static String getReg(String reg, String body) {
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(body);
		while (m.find()) {
			return m.group();
		}
		return null;
	}

}
