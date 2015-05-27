select g.genotype_id, c.name genotype_type, g.name,  g.uniquename genotype_unique_name, cf.name genotype_to_feature_association_type, f.name feature_name, 
 ft.name feature_type , f.feature_id
 from feature_genotype fg
join genotype g
on fg.genotype_id = g.genotype_id
join cvterm c
on c.cvterm_id = g.type_id
join cvterm cf
on cf.cvterm_id = fg.cvterm_id
join feature f on
f.feature_id = fg.feature_id
join
cvterm ft
on ft.cvterm_id = f.type_id
where fg.genotype_id in (9)
order by fg."rank";