SELECT
distinct 
    p.pub_id,
	p.pub_type,
	p.pub_title,
	p.pub_uniquename,
	p.pub_unique_accession,
	p.pub_accession_number,
	p.pub_db_name,
	s.uniquename entity_name,
	s.germplasm_accession entity_unique_accession,
	'germplasm' as genetic_feature_type
	from thalemine_stg.publication_features p
