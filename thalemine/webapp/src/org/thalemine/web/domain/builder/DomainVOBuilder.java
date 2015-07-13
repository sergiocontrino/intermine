package org.thalemine.web.domain.builder;


import org.thalemine.web.domain.DomainVO;
import org.thalemine.web.domain.ValueObject;

public class DomainVOBuilder<E> {
	
	private final ValueObject<E> valueObject;
	
	public DomainVOBuilder() {
		valueObject = new ValueObject<E>();
	}
	
}
