(jQuery(function() { // run when the page has loaded.
    if (intermine) {   // but only if there is something to do.
        intermine.setOptions({CDN: { server: 'https://cdn.araport.org' }});
    }
});
