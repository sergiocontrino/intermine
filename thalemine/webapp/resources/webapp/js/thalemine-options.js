(jQuery(function() { // run when the page has loaded.
    if (intermine.options) {   // but only if there is something to do.
        intermine.setOptions({CDN: {'server': 'https://cdn.araport.org', 'resources': intermine.options.CDN.resources}});
        intermine.setOptions({CellPreviewTrigger: 'hover'});
    }
}));
