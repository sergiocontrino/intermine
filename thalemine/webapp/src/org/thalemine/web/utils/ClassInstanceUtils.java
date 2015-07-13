package org.thalemine.web.utils;

import org.apache.log4j.Logger;
import org.thalemine.web.exception.ClassInstantiationException;

public class ClassInstanceUtils {

	protected static final Logger log = Logger.getLogger(ClassInstanceUtils.class);
	

	/**
	 * Convenience method to instantiate a class using its no-arg constructor.
	 * As this method doesn't try to load classes by name, it should avoid
	 * class-loading issues.
	 * @param clazz class to instantiate
	 * @return the new instance
	 * @throws Assertion if the bean cannot be instantiated
	 */
	public static <T> T instantiate(Class<T> clazz) throws ClassInstantiationException {
		
		if (clazz == null){
			throw new ClassInstantiationException(clazz, "Class cannot be null.");
		}
				
		if (clazz.isInterface()) {
			throw new ClassInstantiationException(clazz, "Specified class is an interface");
		}
		try {
			return clazz.newInstance();
		}
		catch (InstantiationException ex) {
			throw new ClassInstantiationException(clazz, "Is it an abstract class?", ex);
		}
		catch (IllegalAccessException ex) {
			throw new ClassInstantiationException(clazz, "Is the constructor accessible?", ex);
		}
	}

}
