'use strict'

angular.module('gratefulplaceApp').controller 'FoundationCtrl', ($scope, $location, User, CurrentSession) ->
  refreshSession = ->
    $scope.currentSession = CurrentSession.get()
    console.log $scope.currentSession
  $scope.$on 'auth.logged-in', refreshSession
  refreshSession()
  
  $scope.peek = null
  $scope.peekAt = (peekType, peekData)->
    $scope.peekInclude = "views/peeks/#{peekType}.html"
    $scope.peek = peekData
  $scope.clearPeek = ->
    $scope.peek = null

  $scope.toggleNewTopicForm = ($event)->
    $scope.showNewTopicForm = !$scope.showNewTopicForm
    $event.preventDefault()
    
  $scope.$on 'topic.created', ->
    $scope.showNewTopicForm = false