	with source as (
SELECT
	distinct s.stock_id,
	'Germplasm:' || dbx.accession germplasm_accession,
	dbx.accession germplasm_accession_number,
	s.name stock_name,
	sn.name synonym_name,
	c.name synonym_type
FROM
	stock s JOIN stock_synonym st
		ON
		st.stock_id = s.stock_id JOIN synonym sn
		ON
		sn.synonym_id = st.synonym_id JOIN cvterm c
		ON
		c.cvterm_id = sn.type_id JOIN dbxref dbx
		ON
		s.dbxref_id = dbx.dbxref_id JOIN db
		ON
		db.db_id = dbx.db_id
WHERE
	s.name IN (
	'CS65790' ,
	'CS16609',
	'CS6131',
	'CS934',
	'CS6504',
	'CS5152',
	'CS6700',
	'SALK_139786',
	'CS3734')
ORDER BY
	s.stock_id )
SELECT
	s.stock_id,
	s.germplasm_accession stock_accession,
	s.stock_name,
	s.synonym_name as stock_number_display_name,
	case when (
	s.synonym_type = 'ABRC_stock_number' )
	then 'ABRC' else 'NASC' end as stock_center,
	case when (
	s.synonym_type = 'ABRC_stock_number' )
	then s.germplasm_accession_number else substring(
	s.synonym_name,
	2)
	end as stock_accession_number,
	'Yes' as availability 
FROM
	source s
WHERE
	s.synonym_type IN (
	'ABRC_stock_number',
	'NASC_stock_number');
