package org.thalemine.web.database.reader.util;

public class IdGenerator {

	static long MIN = 1;
	static long MAX = Integer.MAX_VALUE;
	
	public static long randLong() {

	      return (new java.util.Random().nextLong() % (MAX - MIN)) + MIN;
	    
		}
	
}
