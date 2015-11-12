SELECT
	s.name,
	s.stock_id,
	s.germplasm_accession,
	o.common_name background_accession
	FROM
	thalemine_stg.stock_dataset s JOIN reporting.stock_background_accession_mv bg
		ON
		bg.subject_id = s.stock_id JOIN organism o
		ON
		o.organism_id = bg.subject_bg_accession_id
	order by o.common_name;