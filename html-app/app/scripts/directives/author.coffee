'use strict'

angular.module('gratefulplaceApp').directive 'author', ->
  restrict: 'E'
  scope:
    author: '=model'
  controller: ['$scope', 'User', 'Support', ($scope, User, Support)->
    $scope.peekAtAuthor = (author)->
      User.get id: author.id, (data)->
        Support.peek.show("user", data)
  ]
  template: """
  <div class="author">
    <img ng-src="{{author.gravatar}}" class="gravatar"/>
    <a href="" ng-click="peekAtAuthor(author)">
      {{author.username}}
    </a>
  </div>
  """
  replace: true
