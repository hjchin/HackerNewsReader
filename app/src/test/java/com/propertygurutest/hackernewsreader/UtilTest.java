package com.propertygurutest.hackernewsreader;

import com.propertygurutest.hackernewsreader.util.Util;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;
/**
 * Created by HJ Chin on 11/11/2017.
 */

public class UtilTest {

    @Test public void getPrettyTimeTest(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date result = cal.getTime();
        assertEquals("1 month ago",Util.getPrettyTime(result.getTime()/1000));
    }

    @Test
    public void getHostNameTest(){
        assertEquals("", Util.getHostname(null));
        assertEquals("", Util.getHostname(""));
        assertEquals("", Util.getHostname("hello world"));
        assertEquals("google.com", Util.getHostname("http://www.google.com"));
        assertEquals("plus.google.com", Util.getHostname("http://plus.google.com"));
    }

}
