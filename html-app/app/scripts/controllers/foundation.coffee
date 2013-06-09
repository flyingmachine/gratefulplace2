'use strict'

angular.module('gratefulplaceApp').controller 'FoundationCtrl', ($scope, $location, User, CurrentSession) ->
  refreshSession = ->
    $scope.currentSession = CurrentSession.get()
    console.log $scope.currentSession
  $scope.$on 'auth.logged-in', refreshSession
  $scope.$on 'auth.logged-out', refreshSession
  refreshSession()
  
  $scope.peek = null
  $scope.peekAt = (toPeekAt)->
    User.get id: toPeekAt.id, (data)->
      $scope.peek = data

  $scope.logout = ->
    CurrentSession.logout()
    $location.path "/"

  $scope.toggleNewTopicForm = ($event)->
    $scope.showNewTopicForm = !$scope.showNewTopicForm
    $event.preventDefault()
    
  $scope.$on 'topic.created', ->
    $scope.showNewTopicForm = false