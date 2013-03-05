'use strict'

angular.module('gratefulplaceApp')
  .controller 'FoundationCtrl', ($scope) ->
    $scope.peek =
      username: "John"
    $scope.peekAt = (toPeekAt)->
      $scope.peek = toPeekAt
