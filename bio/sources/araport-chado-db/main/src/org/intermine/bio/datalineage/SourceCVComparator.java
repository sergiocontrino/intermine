package org.intermine.bio.datalineage;

import java.util.Comparator;

import org.intermine.bio.dataflow.config.SourceCV;

public class SourceCVComparator  implements Comparator<SourceCV> {

	public SourceCVComparator() {
		super();
	}

public int compare(SourceCV keyA, SourceCV keyB) {
		
		Comparable valueA = (Comparable) keyA.getPriority();
		Comparable valueB = (Comparable) keyB.getPriority();
		
		return valueA.compareTo(valueB);
	}

}
