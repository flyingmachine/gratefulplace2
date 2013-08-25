'use strict'

angular.module('gratefulplaceApp').directive 'author', ->
  restrict: 'E'
  scope:
    author: '=model'
  template: """
  <div class="author">
    <img ng-src="{{author.gravatar}}" class="gravatar"/>
    <a href="/#/profiles/{{author.id}}">
      {{author.username}}
    </a>
  </div>
  """
  replace: true
