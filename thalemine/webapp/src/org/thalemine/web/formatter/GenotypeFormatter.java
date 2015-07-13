package org.thalemine.web.formatter;

import org.apache.log4j.Logger;

public class GenotypeFormatter {

	protected static final Logger log = Logger.getLogger(GenotypeFormatter.class);

	String genotypeName;

	public GenotypeFormatter() {

	}

	public GenotypeFormatter(String genotypeName) {
		this.genotypeName = genotypeName;
	}

	public static String getAllelesNames(final String name) {

		String result = null;
		Exception exception = null;

		try {
			if (name != null) {
				int beginIndex = name.indexOf("(");
				int endIndex = name.indexOf(")");

				if ((beginIndex > 0) && (endIndex > 0)) {
					result = name.substring(beginIndex + 1, endIndex);
				}

				if (result != null) {
					result = result.replaceAll(",", "");
				}

				if (result != null) {
					result = result.toLowerCase();
				}

			}
		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {
				log.error("Error ocurred while retriveing allele names for a genotype." + "Genotype Name:" + name);
			}
		}
		
		log.debug("Genotype Alleles Name:" + result);
		return result;

	}

	public static String getGenesNames(final String name) {

		String result = null;
		Exception exception = null;

		try {
			if (name != null) {
				int endIndex = name.indexOf(";");

				if (endIndex > 0) {
					result = name.substring(0, endIndex);
				}

				if (result != null) {
					result = result.replaceAll(",", " ");
				}

				if (result != null) {
					result = result.replaceAll("  ", " ");
				}

				if (result != null) {
					result = "(" + result + ")";
				}

			}
		} catch (Exception e) {
			exception = e;
		} finally {
			if (exception != null) {
				log.error("Error ocurred while retriveing gene names for a genotype." + "Genotype Name:" + name);
			}
		}

		log.debug("Genotype Genes Name:" + result);
		
		return result;

	}

	public static String getFormattedName(final String name) {

		String result = name;

		if (name != null) {

			log.error("Error ocurred. Name cannot be null!");

			return null;

		}

		StringBuilder builder = new StringBuilder();

		if (getAllelesNames(name) != null) {
			builder.append(getAllelesNames(name));
		}

		if (getGenesNames(name) != null) {

			builder.append(" ");
			builder.append(getGenesNames(name));
		}

		if (builder.length() > 0) {
			result = builder.toString();
		}

		log.debug("Genotype Formatted Name:" + result);

		return result;

	}

}
