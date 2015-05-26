SELECT
    s.stock_id,
    s.name, c.name stock_type,
    'Germplasm:' || dbx.accession germplasm_accession,
    s.description,
    V.stock_accession,
    case 
    	when (o.type_id is not null and oc.name='ecotype')
    		then o.common_name
    	else
    		null
    end as accession,
    o.type_id,
    'germplasm' as stock_category,
    VM.mutagen,
    s.uniquename,
    VS.display_name,
    VS.abrc_stock_name,
    VC.comment stock_center_comment,
    coalesce(VMT.is_mutant, 'false') is_mutant,
    coalesce(VT.is_transgene, 'false') is_transgene,
    coalesce(VN.is_natural_variant, 'false') is_natural_variant,
    coalesce(VAC.is_aneploid, 'false') is_aneploid,
    coalesce(VAC.ploidy) ploidy,
    VG.special_growth_conditions,
	VG.duration_of_growth,
	VG.growth_temperature
FROM
    stock s
        JOIN dbxref dbx
        ON
        s.dbxref_id = dbx.dbxref_id JOIN db
        ON
        db.db_id = dbx.db_id 
        join organism o
        on
        o.organism_id = s.organism_id
        join 
        cvterm c 
        on c.cvterm_id = s.type_id
        left join
        cvterm oc
        on oc.cvterm_id = o.type_id
        LEFT JOIN
        (
        select s.stock_id, 'Stock:' || dbx.accession stock_accession, dbx.description from 
	stock s
	join 
        stock_dbxref stb
        ON
        s.stock_id = stb.stock_id JOIN dbxref dbx
        ON
        dbx.dbxref_id = stb.dbxref_id JOIN db
        ON
        dbx.db_id = db.db_id
        ) V
        on V.stock_id = s.stock_id
        left join
        (
        SELECT
    s.stock_id,
    cv.name term_type,
    c.name mutagen
FROM
    stock_cvterm sv JOIN cvterm c
        ON
        c.cvterm_id = sv.cvterm_id JOIN cv
        ON
        cv.cv_id = c.cv_id JOIN stock s
        ON
        s.stock_id = sv.stock_id
WHERE
   cv.name = 'mutagen_type' ) VM
   on VM.stock_id = s.stock_id
 left join 
 (
 SELECT
	s.stock_id,
	s.name
	,
 sn.name abrc_stock_name,
 
 case when (sn.name  is not null)
		then cast ('Germplasm' as text) || cast (' / ' as text) || cast ('Stock: ' as text) || sn.name
		else 
			cast ('Germplasm : ' as text) || s.name
	end as display_name
 
FROM
	stock s JOIN 
	stock_synonym st 
	on st.stock_id = s.stock_id
	JOIN synonym sn
	ON
	sn.synonym_id = st.synonym_id JOIN cvterm c
	ON
	c.cvterm_id = sn.type_id
	where 
	c.name = 'ABRC_stock_number' ) VS
	on VS.stock_id = s.stock_id
	left join
	(
	SELECT
	cv.name vocabulary,
	s.stock_id,
	c.name as comment_type,
	sp.value as comment
FROM
	stock s JOIN stockprop sp
		ON
		s.stock_id = sp.stock_id JOIN cvterm c
		ON
		sp.type_id = c.cvterm_id JOIN cv
		ON
		cv.cv_id = c.cv_id
WHERE
	c.name = 'ABRC_comments')
	VC
	on VC.stock_id = s.stock_id
	left join (
         SELECT
    s.stock_id,
    cv.name term_type,
    c.name mutant_class,
    case 
    	when (not(is_not))
    		then 'true'
    	else
    		'false'
    end as is_mutant
   FROM
    stock_cvterm sv JOIN cvterm c
        ON
        c.cvterm_id = sv.cvterm_id JOIN cv
        ON
        cv.cv_id = c.cv_id JOIN stock s
        ON
        s.stock_id = sv.stock_id
        where c.name = 'mutant' ) 
        VMT on 
        s.stock_id = VMT.stock_id
         left join
        (
          SELECT
    s.stock_id,
    cv.name term_type,
    c.name transgene_class,
    case 
    	when (not(is_not))
    		then 'true'
    	else
    		'false'
    end as is_transgene
   FROM
    stock_cvterm sv JOIN cvterm c
        ON
        c.cvterm_id = sv.cvterm_id JOIN cv
        ON
        cv.cv_id = c.cv_id JOIN stock s
        ON
        s.stock_id = sv.stock_id
        where c.name = 'has_foreign_dna'
                )
                VT
                on VT.stock_id = s.stock_id
                left join
                (
                 SELECT
    s.stock_id,
    cv.name term_type,
    c.name natural_variant_class,
    case 
    	when (not(is_not))
    		then 'true'
    	else
    		'false'
    end as is_natural_variant
   FROM
    stock_cvterm sv JOIN cvterm c
        ON
        c.cvterm_id = sv.cvterm_id JOIN cv
        ON
        cv.cv_id = c.cv_id JOIN stock s
        ON
        s.stock_id = sv.stock_id
        where c.name = 'natural_variant'
                )
                VN
                on VN.stock_id = s.stock_id
                left join
                (
                SELECT
	s.stock_id,
	s.name,
	cv.name term,
	case when (NOT(	sv.is_not))
		then 'true' 
		else 'false' 
		end as is_aneploid,
	cp.name ploidy_term,
	sp.value ploidy
FROM
	stock_cvterm sv JOIN cvterm c
		ON
		c.cvterm_id = sv.cvterm_id JOIN cv
		ON
		cv.cv_id = c.cv_id JOIN stock s
		ON
		s.stock_id = sv.stock_id
		LEFT JOIN
		stockprop sp
		ON
		s.stock_id = sp.stock_id
		LEFT JOIN
		cvterm cp
		ON
		sp.type_id = cp.cvterm_id
		LEFT JOIN
		cv cvp
		ON
		cvp.cv_id = cp.cv_id
WHERE
	cv.name = 'chromosomal_constitution' AND
	cp.name = 'ploidy'
                )
                VAC 
                on 
                VAC.stock_id = s.stock_id
                left join 
                (
                
                SELECT
	cv.name vocabulary,
	s.stock_id,
	s.name,
	coalesce(sp.value) special_growth_conditions,
	coalesce(VD.duration_of_growth) duration_of_growth,
	coalesce(VG.growth_temperature) growth_temperature
	
FROM
	stock s JOIN stockprop sp
		ON
		s.stock_id = sp.stock_id JOIN cvterm c
		ON
		sp.type_id = c.cvterm_id JOIN cv
		ON
		cv.cv_id = c.cv_id
	left join (
	
	SELECT
	s.stock_id,
	s.name,
	c.name as property_type,
	sp.value duration_of_growth
FROM
	stock s JOIN stockprop sp
		ON
		s.stock_id = sp.stock_id JOIN cvterm c
		ON
		sp.type_id = c.cvterm_id JOIN cv
		ON
		cv.cv_id = c.cv_id
WHERE
	c.name = 'duration_of_growth'
	)
VD 
on VD.stock_id = s.stock_id
left join (

SELECT
	s.stock_id,
	s.name,
	c.name as property_type,
	sp.value growth_temperature
FROM
	stock s JOIN stockprop sp
		ON
		s.stock_id = sp.stock_id JOIN cvterm c
		ON
		sp.type_id = c.cvterm_id JOIN cv
		ON
		cv.cv_id = c.cv_id
WHERE
	c.name = 'growth_temperature'

) VG
on VG.stock_id = s.stock_id
WHERE
	c.name = 'special_growth_conditions'
                
                ) VG
                on VG.stock_id = s.stock_id
	       where s.name in ('CS65790' , 'CS16609', 'CS6131', 'CS934'); 
        