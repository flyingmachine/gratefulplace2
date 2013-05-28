angular.module('gratefulplaceApp').directive 'autoExpand', ->
  restrict: 'C'
  link: (scope, el, attrs)->
    $(el).keyup ->
      e = el[0]
      if (e.scrollHeight > e.clientHeight)
        e.style.height = e.scrollHeight + "px"