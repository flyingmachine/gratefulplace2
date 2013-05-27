angular.module('gratefulplaceApp').directive 'toggleFormattingHelp', ->
  restrict: 'C'
  link: (scope, el, attrs)->
    helpClass = '.markdown-help'
    $el = $(el)
    $parent = $el.parent()
    $el.click (e)->
      if $parent.children(helpClass).length
        $parent.children(helpClass).remove()
      else
        $parent.append($(helpClass).clone())
        $parent.children(helpClass).removeClass('hidden')
      e.preventDefault()