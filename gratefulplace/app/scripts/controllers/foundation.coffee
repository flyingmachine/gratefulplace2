'use strict'

angular.module('gratefulplaceApp')
  .controller 'FoundationCtrl', ($scope) ->
    $scope.peek = null
    $scope.peekAt = (toPeekAt)->
      $scope.peek = toPeekAt
