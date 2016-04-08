
$(document).ready(function() {

  var selectorsByLanguage = {};
  var selectedLanguage = '';

  function addLanguageSelector(language, selector) {
    if (!selectorsByLanguage[language]) {
      selectorsByLanguage[language] = [];
    }
    selectorsByLanguage[language].push(selector)
  }

  function selectLanguage(language, event) {
    // Prevent infinite loop, because every tab select will trigger this method
    if (selectedLanguage === language) {
      return;
    }
    selectedLanguage = language;

    var offsetBefore = event.target.getBoundingClientRect().top;
    $.each(selectorsByLanguage[language], function(index, selector) {
      selector();
    })
    var offsetAfter = event.target.getBoundingClientRect().top;

    // Keep the clicked element in the same position on screen,
    // even when the page length changes above it, because
    // some languages have more compact code examples.
    if (offsetBefore !== offsetAfter) {
      window.scrollBy(0, offsetAfter - offsetBefore);
    }
  }

  $('.example').each(function(exampleIndex, example) {
    var tabs = $.parseHTML('<ul class="tabs"><h1>');
    $(example).children()
      .each(function(snippetIndex, snippet) {
        var language = $(snippet).data('language') || '???';
        var id = 'example-'+exampleIndex+'-'+snippetIndex;
        var tab = $.parseHTML('<li class="tab"><a href="#'+id+'">'+language+'</a></li>');
        $(tabs).append(tab);
        $(snippet).wrap('<div id="'+id+'"></div>');
        addLanguageSelector(language, function() {
            $(tabs).tabs('select_tab', id)
        });
        $(tab).find('a').on('click', function(event) {
          selectLanguage(language, event);
        });
      })
    $(example).prepend(tabs);
    $(tabs).tabs();
  });

  SyntaxHighlighter.defaults['gutter'] = false;
  SyntaxHighlighter.defaults['toolbar'] = false;
  SyntaxHighlighter.all();
});
