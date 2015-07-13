package org.thalemine.web.utils;

import org.apache.log4j.Logger;
import org.thalemine.web.context.ContextLoader;

public class ClassUtils {

	protected static final Logger log = Logger.getLogger(ContextLoader.class);

	public static Class<?> determineContextClass(String className) throws Exception {

		Class clazz = null;
		Exception exception = null;

		try {
			clazz = Class.forName(className);

			return clazz;

		} catch (ClassNotFoundException e) {

			exception = e;

		} catch (Exception e) {
			exception = e;
		} finally {

			if (exception != null) {
				log.error("Error occured: " + exception.getMessage() + " Message:" + exception.getCause());
				exception.printStackTrace();
				throw exception;
			}

		}

		return clazz;

	}
}
