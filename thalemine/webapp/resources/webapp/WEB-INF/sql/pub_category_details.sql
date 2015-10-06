WITH 
pub_category_details as (
select 
cast('details' as text) as row_type,
(select d.id from dataset d where d.name = 'PubMed to gene mapping' limit 1) as parent_dataset_id,
cast('Publications' as text) as category_name,
9 sort_order,
999999999 datasource_id,
cast('TAIR' as text) as datasource_name,
cast('https://www.arabidopsis.org/' as text) as datasource_url,
cast('TAIR' as text) as datasource_description,
cast('TAIR Publications' as text)  dataset_description,
9999999 dataset_id,
cast('TAIR Publications' as text) dataset_name,
cast('https://www.arabidopsis.org/download/index-auto.jsp?dir=%2Fdownload_files%2FPublic_Data_Releases%2FTAIR_Data_20140630' as text) dataset_url,
cast('' as text) pubmed_id,
cast('' as text) as authors,
cast(2008 as int) as year,
cast ('09/30/2015' as text) dataset_version,
0 as gene_count,
0 as feature_count
UNION
select 
cast('details' as text) as row_type,
(select d.id from dataset d where d.name = 'PubMed to gene mapping' limit 1) as parent_dataset_id,
cast('Publications' as text) as category_name,
9 sort_order,
999999999 datasource_id,
cast('UniProt' as text) as datasource_name,
cast('http://www.uniprot.org/' as text) as datasource_url,
cast('UniProt' as text) as datasource_description,
cast('UniProt Publications' as text)  dataset_description,
9999999 dataset_id,
cast('UniProt Publications' as text) dataset_name,
cast('http://www.uniprot.org/' as text) dataset_url,
cast('' as text) pubmed_id,
cast('' as text) as authors,
cast(2008 as int) as year,
cast ('2015_09' as text) dataset_version,
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
cast(0 as text) as gene_count,
cast(0 as text) as feature_count,
NULL units
from
pub_category_details