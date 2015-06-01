SELECT
	g.name entity_name,
	g.uniquename entity_unique_name,
	'Genotype:' || dbx.accession entity_unique_accession,
	p.value phenotype_description,
	p.name phenotype_name,
	p.uniquename phenotype_unique_accession,
	p.phenotype_id,
	'genotype' as type
FROM
	phenstatement phst JOIN genotype g
		ON
		g.genotype_id = phst.genotype_id JOIN phenotype p
		ON
		p.phenotype_id = phst.phenotype_id JOIN phendesc phd
		ON
		phd.genotype_id = phst.genotype_id JOIN dbxref dbx
		ON
		dbx.dbxref_id = g.dbxref_id
UNION
SELECT
	f.name entity_name,
	f.uniquename entity_unique_name,
	'Polyallele:' || dbx.accession entity_unique_accession,
	p.uniquename phenotype_unique_accession,
	p.name phenotype_name,
	p.value as phenotype_description,
	p.phenotype_id,
	'allele' as type
FROM
	phenotype p JOIN feature_phenotype fp
		ON
		fp.phenotype_id = p.phenotype_id JOIN feature f
		ON
		f.feature_id = fp.feature_id JOIN dbxref dbx
		ON
		dbx.dbxref_id = f.dbxref_id JOIN cvterm c
		ON
		c.cvterm_id = f.type_id
order by entity_name, phenotype_id