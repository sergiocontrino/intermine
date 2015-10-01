WITH datacategory as (

SELECT
	dt.id dataset_id,
	ds.id datasource_id,
	dt.name dataset_name,
	ds.name datasource_name,
	case 
		when (ds.name = 'GO')
			then true
		else
			false
	end as hide_empty_rows,
	case 
		when (dt.name = 'Genome Assembly')
		then 'Genome Assembly'
		when (dt.name = 'Genome Annotation' or dt.name = 'Coding Sequence FASTA')
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
	   		dt.name = 'affydb' or
	   		dt.name = 'arabidopsis_ecotypes'
	   		)
	   	then 'Expression'	
	   	when (dt.name = 'PubMed to gene mapping' OR dt.name = 'GeneRIF')
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
	hide_empty_rows,
	case 
		when (category_name = 'Genome Assembly')
			then 0
		when (category_name = 'Genes')
			then 1
		when (category_name = 'Proteins')
			then 2
		when (category_name = 'Proteins Domains')
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
datacategory dc 
where dataset_name not in  ('BAR Annotations Lookup', 'Coding Sequence FASTA', 'Protein Sequence FASTA', 'UniProt FASTA dataset', 'UniProt keywords data set')
and dc.datasource_name not in ('BAR', 'InterPro')
)
,
publication_source as (
SELECT
	p.id,
	p.pubmedid pubmed_id,
	p.title,
	p.intermine_year as year,
	p.firstauthor  as author_list
FROM
	publication p
),

gene_pub_source as (
SELECT
	distinct
	g.primaryidentifier,
	p.pubmedid,
	(select d.id from dataset d where d.name = 'PubMed to gene mapping' limit 1) dataset_id
FROM
	gene g JOIN bioEntitiespublications bp
		ON
		bp.bioentities = g.id JOIN publication p
		ON
		p.id = bp.publications
		join organism o on 
		g.organismid = o.id and o.taxonid = 3702
		)	
,
gene_rif_pub_agg_source as (
select 
count(distinct(annotation)) as feature_count,
(select d.id from dataset d where d.name = 'GeneRIF' limit 1) dataset_id
from generif gr
join
gene g
on g.id = gr.geneid
join
publication p
on gr.publicationid = p.id
join organism o on 
g.organismid = o.id and o.taxonid = 3702
)
,

ncbi_pub_agg_source as (
select 
count(*) feature_count,
dataset_id
from 
gene_pub_source gs 
group by dataset_id )
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
where o.taxonid = 3702 and d.name not in  ('BAR Annotations Lookup', 'Coding Sequence FASTA', 'Protein Sequence FASTA', 'UniProt FASTA dataset', 'UniProt keywords data set')
group by d.id
)
,
pub_agg_feature_source as (
select 
feature_count,
dataset_id
from 
gene_rif_pub_agg_source
UNION
select
feature_count,
dataset_id
from 
ncbi_pub_agg_source
 )
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
where o.taxonid = 3702 and ds.name = 'UniProt' 
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

),

genome_assembly_agg_source as (
select count(*) feature_count, 
d.id dataset_id
from dataset d
join
bioentitiesdatasets ds
on 
d."id" = ds.datasets
join sequencefeature g
on g."id" = ds.bioentities
where d.name = 'Genome Assembly'
group by d.id
),

cds_agg_source as (
select count(*) feature_count, 
d.id dataset_id
from dataset d
join
bioentitiesdatasets ds
on 
d."id" = ds.datasets
join cds c
on c.id = ds.bioentities
join
gene g
on g.id = c.geneid
group by d.id)

,
agg_feature_count_helper as 
(
select
feature_count,
dataset_id
from
cds_agg_source
UNION
select 
feature_count,
dataset_id
from 
genome_assembly_agg_source
UNION
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
,

agg_feature_count as (
select
feature_count,
dataset_id,
ds.id datasource_id
from
agg_feature_count_helper
join
dataset dt
on dataset_id = dt.id
join
datasource ds 
on ds.id = dt.datasourceid
)
,
feature_data_source_agg as
(
select
sum(feature_count) feature_count,
datasource_id
from
agg_feature_count
group by 
datasource_id
)
,
gene_data_source_agg as (
select 
count(*) as gene_count,
ds.id datasource_id
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
group by ds.id )
,
data_summary_source as(
SELECT
	distinct
	cast('summary' as text) as row_type,
	0 as parent_dataset_id,
	st.category_name,
	st.sort_order,
	st.hide_empty_rows,
	ds.name datasource_name,
	ds.id datasource_id,
	ds.url datasource_url,
	ds.description datasource_description,
	case 
		when (ds.name = 'GO' or ds.name = 'BAR') 
			then
				ds.description
		 else dt.description
	end as dataset_description,
	case 
		when (ds.name = 'GO' or ds.name = 'BAR') 
			then
				ds.id
		 else dt.id
	end as dataset_id,
	case 
		when (ds.name = 'GO' or ds.name = 'BAR') 
			then
				ds.name
		 else dt.name
	end as dataset_name,
	dt.version dataset_version,
	case 
		when (ds.name = 'GO' or ds.name = 'BAR') 
			then
				ds.url
		 else dt.url
	end as dataset_url,
	p.pubmed_id,
	p.author_list as authors,
	p.year,
	case 
		when (ds.name = 'GO') 
			then
				gds.gene_count
		 else gs.gene_count
	end as gene_count,
	gds.gene_count data_source_gene_count,
	case 
		when (ds.name = 'GO') 
			then
				agg_f_ds.feature_count
		 else aggf.feature_count
	end as feature_count,
	agg_f_ds.feature_count data_source_feature_count
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
	left join
	gene_data_source_agg gds
	on gds.datasource_id = ds.id
	left join
	feature_data_source_agg agg_f_ds
	on agg_f_ds.datasource_id = ds.id
	where dt.name <> 'BAR Annotations Lookup'
	order by st.sort_order )
,

protein_domain_summary_helper as (

select
cast('summary' as text) as row_type,
0 as parent_dataset_id,
cast('Protein Domains' as text) as category_name,
3 as sort_order,
ds.id datasource_id,
ds.name datasource_name,
ds.url datasource_url,
ds.description as datasource_description,
d.description dataset_description,
d.id dataset_id,
d.name dataset_name,
d.url dataset_url,
p.pubmed_id,
p.author_list as authors,
p.year,
d.version dataset_version,
count(distinct g.id) gene_count,
count(distinct ptd.proteindomains) as feature_count
from
genesproteins gp
join
gene g
on g.id = gp.genes
join
protein pt
on pt.id = gp.proteins
join
proteindomainsproteins ptd
on ptd.proteins = pt.id
join
proteindomain pd
on pd.id = ptd.proteindomains
join
bioentitiesdatasets bds
on bds.bioentities = pd.id
join
dataset d
on d.id = bds.datasets
join
datasource ds 
ON
ds.id = d.datasourceid
join
organism o
on
o.id = g.organismid
left join
	publication_source p
	on p.id = d.publicationid
where o.taxonid = 3702 and pt.uniprotname IS NOT NULL and ds.name = 'InterPro'
group by d.id, ds.id, ds.name, d.name, d.description, ds.description, d.version, ds.url, d.url, p.pubmed_id, p.author_list, p.year

)
,
expression_datasource as
(
select 
ds.id datasource_id,
ds.name datasource_name,
ds.url datasource_url,
ds.description as datasource_description,
ds.description dataset_description,
d.id dataset_id,
ds.description dataset_name,
ds.url dataset_url,
p.pubmed_id,
p.author_list as authors,
p.year,
d.version dataset_version
from dataset d
join
datasource ds
on d.datasourceid = ds.id
left join
publication_source p
on p.id = d.publicationid
where ds.name = 'BAR'
)
,
expression_summary as (
select 
cast('summary' as text) as row_type,
0 as parent_dataset_id,
cast ('Expression' as text) category_name,
8 sort_order,
eds.datasource_id,
eds.datasource_name,
eds.datasource_url,
eds.datasource_description,
eds.dataset_description,
eds.dataset_id,
eds.dataset_name,
eds.dataset_url,
eds.dataset_version,
eds.pubmed_id,
eds.authors,
eds.year,
24005 as gene_count,
2952615 as feature_count
from 
expression_datasource eds
limit 1
)
,
phytomozome_summary as (
select 
cast('summary' as text) as row_type,
0 as parent_dataset_id,
cast('Homology' as text) as category_name,
4 sort_order,
999999999 datasource_id,
cast('Phytozome' as text) as datasource_name,
cast('http://phytozome.jgi.doe.gov/phytomine' as text) as datasource_url,
cast('Phytozome Homologs Generated with InParanoid' as text) as datasource_description,
cast('Phytozome Homologs Generated with InParanoid' as text)  dataset_description,
999 dataset_id,
cast('Phytozome' as text) dataset_name,
cast('http://phytozome.jgi.doe.gov/phytomine' as text) dataset_url,
cast('22110026' as text) pubmed_id,
cast('Goodstein' as text) as authors,
cast(2012 as int) as year,
cast ('' as text) dataset_version,
0 as gene_count,
0 as feature_count

)
,

atgen_express_summary as (
select 
cast('summary' as text) as row_type,
0 as parent_dataset_id,
cast('Expression' as text) category_name,
8 sort_order,
999999999 datasource_id,
cast('ATTED-II' as text) as datasource_name,
cast('http://atted.jp/' as text) as datasource_url,
cast('provides co-regulated gene relationships to estimate gene functions' as text) as datasource_description,
cast('provides co-regulated gene relationships to estimate gene functions' as text)  dataset_description,
9999999 dataset_id,
cast('ATTED-II' as text) dataset_name,
cast('http://atted.jp/' as text) dataset_url,
cast('' as text) pubmed_id,
cast('' as text) as authors,
cast(2008 as int) as year,
cast ('' as text) dataset_version,
0 as gene_count,
0 as feature_count

)

select 
distinct
row_type,
parent_dataset_id,
category_name,
sort_order,
datasource_id,
datasource_name,
datasource_url,
datasource_description,
dataset_description,
dataset_id,
dataset_name,
dataset_url,
dataset_version,
pubmed_id,
authors,
year,
cast(gene_count as text) as gene_count,
cast(feature_count as text) as feature_count
from 
data_summary_source
UNION
select
distinct
row_type,
parent_dataset_id,
category_name,
sort_order,
datasource_id,
datasource_name,
datasource_url,
datasource_description,
dataset_description,
dataset_id,
dataset_name,
dataset_url,
dataset_version,
pubmed_id,
authors,
year,
cast(gene_count as text) as gene_count,
cast(feature_count as text) as feature_count
from
protein_domain_summary_helper
UNION
select
distinct
row_type,
parent_dataset_id,
category_name,
sort_order,
datasource_id,
datasource_name,
datasource_url,
datasource_description,
dataset_description,
dataset_id,
dataset_name,
dataset_url,
dataset_version,
pubmed_id,
authors,
year,
cast(gene_count as text) as gene_count,
cast(feature_count as text) as feature_count
from
expression_summary
UNION
select
distinct 
row_type,
parent_dataset_id,
category_name,
sort_order,
datasource_id,
datasource_name,
datasource_url,
datasource_description,
dataset_description,
dataset_id,
dataset_name,
dataset_url,
dataset_version,
pubmed_id,
authors,
year,
cast('real-time' as text) as gene_count,
cast('real-time' as text) as feature_count
from
phytomozome_summary
UNION
select
row_type,
parent_dataset_id,
category_name,
sort_order,
datasource_id,
datasource_name,
datasource_url,
datasource_description,
dataset_description,
dataset_id,
dataset_name,
dataset_url,
dataset_version,
pubmed_id,
authors,
year,
cast('real-time' as text) as gene_count,
cast('real-time' as text) as feature_count
from
atgen_express_summary
order by sort_order, datasource_id;