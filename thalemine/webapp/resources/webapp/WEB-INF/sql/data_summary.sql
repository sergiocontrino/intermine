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
,
gene_agg_source as (
select 
count(*) as gene_count,
d.id dataset_id
from dataset d
join
bioentitiesdatasets bds
on 
d.id = bds.datasets
join
datasource ds 
ON
ds.id = d.datasourceid
join gene g
on g.id = bds.bioentities
join 
organism o
on 
o.id = g.organismid
where o.taxonid = 3702
group by d.id
)
,
pub_agg_feature_source as (
select 
count(distinct bp.publications) feature_count,
d.id dataset_id
from dataset d
join
bioentitiesdatasets bds
on 
d.id = bds.datasets
join
datasource ds 
ON
ds.id = d.datasourceid
join gene g
on g.id = bds.bioentities
join bioentitiespublications bp
on bp.bioentities = bds.bioentities
join publication p
on p.id = bp.publications
join 
organism o
on 
o.id = g.organismid
where o.taxonid = 3702 and d.name = 'PubMed to gene mapping'
group by d.id )
, 

homolog_agg_feature_source as (
select 
count(h.homologueid) as feature_count
,
d.id dataset_id
from dataset d
join
bioentitiesdatasets bds
on 
d.id = bds.datasets
join
datasource ds 
ON
ds.id = d.datasourceid
join gene g
on g.id = bds.bioentities
join 
organism o
on 
o.id = g.organismid
join homologue h 
on g.id = h.geneid
where o.taxonid = 3702 and d.name = 'Panther data set'
group by d.id

)
,
protein_agg_source as (

select 
count(distinct(proteins)) feature_count,
d.id dataset_id
from dataset d
join
bioentitiesdatasets bds
on 
d.id = bds.datasets
join
datasource ds 
ON
ds.id = d.datasourceid
join gene g
on g.id = bds.bioentities
join
genesproteins gp
on gp.genes = g.id
join protein p
on p.id = gp.proteins
join organism o
on o.id = g.organismid
where o.taxonid = 3702 and d.name = 'Genome Annotation' 
and p.uniprotname IS NOT NULL
group by d.id
)

,
go_agg_source as (
SELECT
	count(*) feature_count,
	d.id dataset_id
	from dataset d
join
bioentitiesdatasets bds
on 
d.id = bds.datasets
join
datasource ds 
ON
ds.id = d.datasourceid
join gene g
on g.id = bds.bioentities
join
genegoannotation go
on go.gene = g.id
join organism o
on o.id = g.organismid
where o.taxonid = 3702 and ds.name = 'GO'
group by d.id )

,
po_agg_source as (

SELECT
	count(*) feature_count,
	d.id dataset_id
	from dataset d
join
bioentitiesdatasets bds
on 
d.id = bds.datasets
join
datasource ds 
ON
ds.id = d.datasourceid
join gene g
on g.id = bds.bioentities
join
genepoannotation go
on go.gene = g.id
join organism o
on o.id = g.organismid
where o.taxonid = 3702 and d.name = 'PO Annotation from TAIR'
group by d.id
)
,

generif_agg_source as (
SELECT
	count(distinct(annotation)) feature_count,
	d.id dataset_id
	from dataset d
join
bioentitiesdatasets bds
on 
d.id = bds.datasets
join
datasource ds 
ON
ds.id = d.datasourceid
join gene g
on g.id = bds.bioentities
join
generif gr
on g.id = gr.geneid
join organism o
on o.id = g.organismid
where o.taxonid = 3702 and d.name = 'GeneRIF'
group by d.id

)
,

agg_interactions_source as (

select
count(distinct(interactiondetail)) feature_count,
d.id dataset_id
from dataset d
join
datasetsinteractiondetail dl
on d.id = dl.datasets
group by d.id

)
,
agg_pathways_source as (

select
count(distinct(pathways)) feature_count,
d.id dataset_id
from dataset d
join
bioentitiesdatasets bds
on 
d.id = bds.datasets
join
datasource ds 
ON
ds.id = d.datasourceid
join gene g
on g.id = bds.bioentities
join
genespathways gp
on g.id = gp.genes
join organism o
on o.id = g.organismid
where o.taxonid = 3702 and d.name = 'KEGG pathways data set'
group by 
d.id

)
,
agg_feature_count as 
(

select 
feature_count,
dataset_id
from 
pub_agg_feature_source
UNION
select
feature_count,
dataset_id
from
homolog_agg_feature_source
UNION
select
feature_count,
dataset_id
from
protein_agg_source
UNION
select
feature_count,
dataset_id
from
go_agg_source
UNION
select
feature_count,
dataset_id
from
po_agg_source
UNION
select
feature_count,
dataset_id
from
generif_agg_source
UNION
select
feature_count,
dataset_id
from
agg_interactions_source
UNION
select
feature_count,
dataset_id
from
agg_pathways_source
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
	gs.gene_count,
	aggf.feature_count
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
	left join
	gene_agg_source gs
	on gs.dataset_id = dt.id
	left join 
	agg_feature_count aggf
	on aggf.dataset_id = dt.id
	order by st.sort_order;