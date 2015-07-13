SELECT
	s.name,
	s.stock_id,
	'Germplasm:' || dbx.accession germplasm_accession,
	o.common_name background_accession
FROM
	stock s JOIN reporting.stock_background_accession_mv bg
		ON
		bg.subject_id = s.stock_id JOIN organism o
		ON
		o.organism_id = bg.subject_bg_accession_id JOIN dbxref dbx
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
	order by o.common_name;
