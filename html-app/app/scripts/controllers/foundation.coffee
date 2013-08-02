'use strict'

angular.module('gratefulplaceApp').controller 'FoundationCtrl', ($scope, $location, User, CurrentSession, Support) ->

  refreshSession = ->
    $scope.currentSession = CurrentSession.get()
    console.log $scope.currentSession
  $scope.$on 'auth.logged-in', refreshSession
  refreshSession()

  $scope.support = Support

  $scope.$on "$routeChangeStart", (event, next, current)->
    Support.clear()
    
  $scope.$on 'topic.created', ->
    $scope.showNewTopicForm = false
