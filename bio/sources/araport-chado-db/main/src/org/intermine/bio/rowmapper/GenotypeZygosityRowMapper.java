package org.intermine.bio.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.intermine.bio.domain.source.*;
import org.intermine.bio.jdbc.core.RowMapper;

public class GenotypeZygosityRowMapper implements RowMapper<SourceGenotypeZygosity> {

	@Override
	public SourceGenotypeZygosity mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SourceGenotypeZygosity item = new SourceGenotypeZygosity();
		
		item.setGenotypeUniqueAccession(rs.getString("genotype_unique_accession"));
		item.setAlleleUniqueAccession(rs.getString("genotype_name"));
		item.setGermplasmUniqueAccession(rs.getString("germplasm_unique_accession"));
		item.setZygosity(rs.getString("zygosity"));
					
		return item;
	}

}
