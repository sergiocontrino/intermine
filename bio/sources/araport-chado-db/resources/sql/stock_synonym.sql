SELECT
	distinct
	s.stock_id,
	'Germplasm:' || dbx.accession germplasm_accession,
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
		c.cvterm_id = sn.type_id
		JOIN dbxref dbx
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
	order by s.stock_id;