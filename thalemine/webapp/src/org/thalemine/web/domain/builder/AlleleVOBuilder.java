package org.thalemine.web.domain.builder;

import java.util.List;

import org.apache.log4j.Logger;
import org.intermine.api.results.ResultElement;
import org.thalemine.web.domain.AlleleVO;
import org.thalemine.web.domain.DomainVO;
import org.thalemine.web.domain.StockAnnotationVO;

public class AlleleVOBuilder extends DomainVO {

	protected static final Logger log = Logger.getLogger(AlleleVOBuilder.class);
	private final AlleleVO valueObject;

	public AlleleVOBuilder() {
		valueObject = new AlleleVO();
	}

	public AlleleVO build(List<ResultElement> element) {

		initByResultList(element);
		format();

		return this.valueObject;

	}
	
	public AlleleVO buildbyObjectList(List<Object> element) {

		initByObjectList(element);
		format();

		return this.valueObject;

	}

	private void initByResultList(List<ResultElement> element) {

		log.debug("Value Object:" + this.valueObject);
		this.valueObject.setObjectId(createElement(element, 0));
		log.debug("Value Object:" + this.valueObject);
		this.valueObject.setPrimaryIdentifier(createElement(element, 1));
		String name = createElement(element, 2);
		log.debug("Allele Builder Name:" + name);
		this.valueObject.setName(createElement(element, 2));
		log.debug("Value Object:" + this.valueObject);
		this.valueObject.setMutagen(formatValue("mutagen", createElement(element, 3)));
		this.valueObject.setSequenceAlterationType(formatValue("sequenceAlterationType", createElement(element, 4)));
		this.valueObject.setMutationSite(formatValue("mutationSite", createElement(element, 5)));
		this.valueObject.setInheritanceType(formatValue("inheritanceType", createElement(element, 6)));
		this.valueObject.setAlleleClass(formatValue("alleleClass", createElement(element, 7)));

		log.debug("Value Object:" + this.valueObject);

	}

	private void format() {

		if (this.valueObject != null && valueObject.getName() != null) {
			this.valueObject.setName(this.valueObject.getName().toLowerCase());
		}

	}

	private void initByObjectList(List<Object> element) {

		log.debug("Value Object:" + this.valueObject);
		this.valueObject.setObjectId(getElement(element, 0));
		log.debug("Value Object:" + this.valueObject);
		this.valueObject.setPrimaryIdentifier(getElement(element, 1));
		String name = getElement(element, 2);
		log.debug("Allele Builder Name:" + name);
		this.valueObject.setName(getElement(element, 2));
		log.debug("Value Object:" + this.valueObject);
		this.valueObject.setMutagen(formatValue("mutagen", getElement(element, 3)));
		this.valueObject.setSequenceAlterationType(formatValue("sequenceAlterationType", getElement(element, 4)));
		this.valueObject.setMutationSite(formatValue("mutationSite", getElement(element, 5)));
		this.valueObject.setInheritanceType(formatValue("inheritanceType", getElement(element, 6)));
		this.valueObject.setAlleleClass(formatValue("alleleClass", getElement(element, 7)));

	}

}
