package org.thalemine.web.query.repository;

import java.util.List;

import org.intermine.pathquery.PathQuery;
import org.thalemine.web.domain.GeneVO;
import org.thalemine.web.injection.RepositorySetter;
import org.thalemine.web.service.Verifiable;

public interface AlleleDAO extends GeneralDAO{

	public QueryResult getGenotype(Object item) throws Exception;
	public QueryResult getGenes(Object item) throws Exception;
	public QueryResult getGenes(String itemId) throws Exception;
	public QueryResult getAllelesByGene(String itemId) throws Exception;
	public QueryResult getGeneModels(String itemId) throws Exception;
	public QueryResult getAlleleSummary(String itemId) throws Exception;	
	
	
}
