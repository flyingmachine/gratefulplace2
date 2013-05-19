'use strict'

angular.module('gratefulplaceApp').controller 'FoundationCtrl', ($scope, $location, Users, CurrentSession) ->
  refreshSession = ->
    $scope.currentSession = CurrentSession.get()
    console.log $scope.currentSession
  $scope.$on 'auth.logged-in', refreshSession
  $scope.$on 'auth.logged-out', refreshSession
  refreshSession()
  
  $scope.peek = null
  $scope.peekAt = (toPeekAt)->
    Users.get id: toPeekAt.id, (data)->
      $scope.peek = data
      console.log $scope.peek

  $scope.logout = ->
    CurrentSession.logout()
    $location.path "/"

  $scope.showNewTopic = ($event)->
    $scope.newTopic = {}
    $event.preventDefault()