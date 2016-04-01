
$(document).ready(function() {

  var selectorsByLanguage = {};
  var selectedLanguage = '';

  function addLanguageSelector(language, selector) {
    if (!selectorsByLanguage[language]) {
      selectorsByLanguage[language] = [];
    }
    selectorsByLanguage[language].push(selector)
  }

  function selectLanguage(language) {
    if (selectedLanguage === language) {
      return; // prevent infinite loop, because every tab select will trigger this method
    }
    selectedLanguage = language;
    $.each(selectorsByLanguage[language], function(index, selector) {
      selector();
    })
  }

  $('div.example').each(function(exampleIndex, example) {
    var tabs = $.parseHTML('<ul class="tabs"><h1>');
    $(example).children()
      .each(function(codeIndex, code) {
        var language = $(code).data('language') || '???';
        var id = 'example-'+exampleIndex+'-'+codeIndex;
        var tab = $.parseHTML('<li class="tab"><a href="#'+id+'">'+language+'</a></li>');
        $(tabs).append(tab);
        $(code).wrap('<div id="'+id+'"></div>');
        addLanguageSelector(language, function() {
            $(tabs).tabs('select_tab', id)
        });
        $(tab).find('a').on('click', function() {
          selectLanguage(language);
        });
      })
    $(example).prepend(tabs);
    $(tabs).tabs();
  });

  SyntaxHighlighter.defaults['gutter'] = false;
  SyntaxHighlighter.defaults['toolbar'] = false;
  SyntaxHighlighter.all();
});
