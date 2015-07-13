package org.thalemine.web.domain.builder;

import java.util.List;

import org.thalemine.web.domain.DomainVO;
import org.thalemine.web.domain.StockAnnotationVO;

public class StockAnnotationVOBuilder extends DomainVO {

	private final StockAnnotationVO valueObject;
	
	public StockAnnotationVOBuilder(){
		this.valueObject = new StockAnnotationVO();
	};
	
			
	public StockAnnotationVO buildByMutagenChromosomalConstitution(List<Object> element){
		
		initByMutagenChromosomalConstitution(element);
		return this.valueObject;
		
	}
	
	private void initByMutagenChromosomalConstitution(List<Object> element){
		
		this.valueObject.setObjectId(getElement(element, 0));
		this.valueObject.setStockPrimaryIdentifier(getElement(element, 1));
		this.valueObject.setMutagen(formatValue("mutagen", getElement(element, 2)));
		this.valueObject.setAneploid(getElement(element, 3));
		this.valueObject.setPloidy(getElement(element, 4));
		
		log.debug("Value Object:" + this.valueObject);
		
	}
}
