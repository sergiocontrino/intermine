package org.thalemine.web.formatter;

import org.apache.log4j.Logger;

public class AlleleFormatter {

	protected static final Logger log = Logger.getLogger(AlleleFormatter.class);
	private static final String NA = "N/A";

	String name;

	public AlleleFormatter() {

	}

	public AlleleFormatter(String genotypeName) {
		this.name = genotypeName;
	}

	public static String getFormattedName(final String name) {

		String result = null;

		Exception exception = null;

		try {
			if (name != null) {

				result = name.toLowerCase();
			}

		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {
				log.error("Error ocurred while formatting allele name." + "Allele Name:" + name);
			}
		}

		log.debug("Name:" + result);
		return result;

	}

	public static String getFormattedAttributeName(final String type, final String name) {

		String result = null;

		Exception exception = null;

		try {
			if (name != null && !name.isEmpty()) {

				if (type.equals("sequenceAlterationType") && name.equalsIgnoreCase("T-dna Insertion")) {
					result = "insertion (T-DNA Insertion)";
				}

				if (type.equals("mutagen") && name.equalsIgnoreCase("T Dna Insertion")) {
					result = "T-DNA Insertion";
				}

				if (name.equalsIgnoreCase("UNKNOWN")) {
					result = NA;
				}

			} else {
				result = NA;
			}

		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {
				log.error("Error ocurred while formatting allele attribute name." + "Allele Name:" + name);
			}
		}

		log.debug("Name:" + result);
		return result;

	}

}
