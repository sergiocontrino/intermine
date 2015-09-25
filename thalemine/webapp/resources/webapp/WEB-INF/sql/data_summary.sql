WITH datacategory as (

SELECT
	dt.id dataset_id,
	ds.id datasource_id,
	dt.name dataset_name,
	case 
		when (dt.name = 'Genome Annotation' OR 	dt.name = 'GeneRIF' or dt.name = 'Genome Assembly' or dt.name = 'Coding Sequence FASTA')
		then 'Genes'
	   when (	
			dt.name = 'Swiss-Prot data set' OR
			dt.name = 'TrEMBL data set' OR
			dt.name = 'UniProt FASTA dataset' OR
			dt.name = 'UniProt keywords data set' OR
			dt.name = 'Protein Sequence FASTA' or
			dt.name = 'InterPro data set'
			)
		then 'Proteins'
		when (dt.name = 'Panther data set')
	   	then 'Homology'
	   	when (ds.name = 'GO' or dt.name = 'InterPro GO Annotation data set')
	   	then 'Gene Ontology'
	   	when (dt.name = 'IntAct interactions data set' or dt.name = 'BioGRID interaction data set')
	   		then 'Interactions'
	   	when (dt.name = 'atgenexp_hormone' or 
	   		dt.name = 'atgenexp' or 
	   		dt.name = 'atgenexp_pathogen' or 
	   		dt.name = 'atgenexp_plus' or 
	   		dt.name = 'light_series' or
	   		dt.name = 'atgenexp_stress' or
	   		dt.name = 'root' or
	   		dt.name = 'seed_db' or
	   		dt.name = 'affydb')
	   	then 'Expression'	
	   	when (dt.name = 'PubMed to gene mapping')
		then 'Publications'
		when (dt.name = 'KEGG pathways data set')
		then 'Pathways'
		when (dt.name = 'PO Annotation from TAIR')
		then 'Plant Ontology'
		else ds.name
	   	
	 end as category_name
FROM
	datasource ds JOIN dataset dt
		ON
		ds.id = dt.datasourceid
) 

,
sort as (

select
	dataset_id,
	datasource_id,
	dataset_name,
	category_name,
	case 
		when (category_name = 'Genes')
			then 1
		when (category_name = 'Proteins')
			then 2
		when (category_name = 'Homology')
			then 3
		when (category_name = 'Homology')
			then 4
		when (category_name = 'Gene Ontology')
			then 5
		when (category_name = 'Plant Ontology')
			then 6
		when (category_name = 'Interactions')
			then 7
		when (category_name = 'Expression')
			then 8
		when (category_name = 'Publications')
			then 9
		when (category_name = 'Pathways')
			then 10
		else
			999
	end as sort_order
from
datacategory dc )
,
publication_source as (
SELECT
	p.id,
	p.pubmedid pubmed_id,
	p.title,
	p.intermine_year as year,
	string_agg(a.name, ', ' order by lower(a.name)) as author_list
FROM
	publication p
	join
	authorspublications ap
	on 
	ap.publications = p.id
	join 
	author a
	on a.id = ap.authors
	group by p.id, p.pubmedid, p.title, p.intermine_year


)

SELECT
	st.category_name,
	st.sort_order,
	ds.name datasource_name,
	ds.id datasource_id,
	ds.url datasource_url,
	ds.description datasource_description,
	dt.description dataset_description,
	dt.id dataset_id,
	dt.name dataset_name,
	dt.version dataset_version,
	dt.url dataset_url,
	dt.description dataset_description,
	p.pubmed_id,
	p.author_list as authors,
	p.year,
	0 gene_count,
	0 feature_count
	FROM
	datasource ds JOIN dataset dt
		ON
		ds.id = dt.datasourceid
		join
		sort st 
		on st.dataset_id = dt.id and st.datasource_id = ds.id
		left join
	publication_source p
	on p.id = dt.publicationid
	order by st.sort_order;