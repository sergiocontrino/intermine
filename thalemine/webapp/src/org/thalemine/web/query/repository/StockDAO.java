package org.thalemine.web.query.repository;

import org.intermine.metadata.Model;
import org.intermine.pathquery.Constraints;
import org.intermine.pathquery.PathQuery;
import org.thalemine.web.injection.RepositorySetter;
import org.thalemine.web.service.Verifiable;

public interface StockDAO extends GeneralDAO {

	public QueryResult getGenotype(Object item) throws Exception;
	public QueryResult getGrowthRequirements(Object item) throws Exception;
	public QueryResult getStockPhenotypes(Object item) throws Exception;
	public QueryResult getMutagenChromosomalConstitution(Object item) throws Exception;
	public QueryResult getNaturalAccession(Object item) throws Exception;
	public QueryResult getStockGenotypeGeneticContext(String stockId, String genotypeId) throws Exception;
	public QueryResult getStocksByGeneticItem(String geneId, String itemClass) throws Exception;
	public QueryResult getBackgroundAccessions(String itemId) throws Exception;
	public QueryResult getPhenotypesbyGeneticItem(String itemId, String stockId, String itemClass) throws Exception;
	public QueryResult getPhenotypeGeneticContext(String itemId) throws Exception;
	public QueryResult getStockAvailability(String itemId) throws Exception;
	
}
