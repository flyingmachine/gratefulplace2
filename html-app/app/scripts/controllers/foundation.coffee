'use strict'

angular.module('gratefulplaceApp').controller 'FoundationCtrl', ($scope, Users, CurrentSession) ->
  $scope.currentSession = CurrentSession.get()
  $scope.peek = null
  $scope.peekAt = (toPeekAt)->
    Users.find(toPeekAt.id).then (data)->
      $scope.peek = data
