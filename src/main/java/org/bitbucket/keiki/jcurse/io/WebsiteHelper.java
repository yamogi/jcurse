package org.bitbucket.keiki.jcurse.io;

public class WebsiteHelper {
	
	static String parseAttribute(String line, String attributeName) {
		String attribute = "";
		int indexOf = line.indexOf(attributeName);
		if (indexOf >= 0) {
		    int addonIdFrom = indexOf + attributeName.length() + 2;
			int indexOf2 = line.indexOf('\"', addonIdFrom);
			attribute = line.substring(addonIdFrom, indexOf2);
		}
		return attribute;
	}
}
