'use strict'

angular.module('gratefulplaceApp').controller 'FoundationCtrl', ($scope, $location, Users, CurrentSession) ->
  refreshSession = ->
    $scope.currentSession = CurrentSession.get()
  $scope.$on 'auth.logged-in', refreshSession
  $scope.$on 'auth.logged-out', refreshSession
  refreshSession()
  
  $scope.peek = null
  $scope.peekAt = (toPeekAt)->
    Users.find(toPeekAt.id).then (data)->
      $scope.peek = data

  $scope.logout = ->
    CurrentSession.logout()
    $location.path "/"