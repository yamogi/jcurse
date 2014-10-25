package org.bitbucket.keiki.jcurse.io;

public class WebsiteHelper {
    
    private static final int OFFSET_ATTRIBUTE_CONTENT_STARTS = 2;

    static String parseAttribute(String line, String attributeName) {
        String attribute = "";
        int indexOf = line.indexOf(attributeName);
        if (indexOf >= 0) {
            int addonIdFrom = indexOf + attributeName.length() + OFFSET_ATTRIBUTE_CONTENT_STARTS;
            int indexOf2 = line.indexOf('\"', addonIdFrom);
            attribute = line.substring(addonIdFrom, indexOf2);
        }
        return attribute;
    }
}
