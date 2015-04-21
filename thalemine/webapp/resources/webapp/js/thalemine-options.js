(jQuery(function() { // run when the page has loaded.

    var wrapSpan = function(text){
	return '<span>'+text+'</span>';
    };
    
    var formatLink = function(url, text, target, cls){
	target = target || "_self";
	text = text || url;

	if(cls == 'extlink') {
	    return '<a class="'+cls+'" href="'+url+'" target="'+target+'">'+text+'</a>';
	}else{
	    return '<a href="'+url+'" target="'+target+'">'+text+'</a>';
	}
    };
    
    var formatGeneLink = function(id){	

	var thalemine_url = intermine.options.thalemine_url + '/portal.do?class=Gene&externalids=' + id;
	var phytomine_url = intermine.options.phytomine_url + '/portal.do?class=Gene&externalids=' + id;
	
	if(id.match(/^AT/)){
	    url = thalemine_url;
	    return formatLink(url, wrapSpan(id), "_blank", 'internal');
	}else{
	    url = phytomine_url;
	    return formatLink(url, wrapSpan(id), "_blank", 'extlink');
	}
    };

    if (intermine.options) {   // but only if there is something to do.
        intermine.setOptions({CellPreviewTrigger: 'hover'});
    }

    intermine.setOptions({
	'Gene.name' : function(o){return formatGeneLink(o.get('name'));},
    },'intermine.results.formatsets.genomic');

}));
