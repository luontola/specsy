
$(document).ready(function() {

  $('div.example').each(function(exampleIndex, example) {
    var tabs = $.parseHTML('<ul class="tabs"><h1>');

    $(example).children()
      .each(function(variantIndex, variant) {
        var label = $(variant).attr('data-label') || '???';
        var id = 'example-'+exampleIndex+'-'+variantIndex;
        $(tabs).append('<li class="tab"><a href="#'+id+'">'+label+'</a></li>');
        $(variant).wrap('<div id="'+id+'"></div>');
      })

    $(example).prepend(tabs);
    $(tabs).tabs();
  });

  SyntaxHighlighter.defaults['gutter'] = false;
  SyntaxHighlighter.defaults['toolbar'] = false;
  SyntaxHighlighter.all();
});
