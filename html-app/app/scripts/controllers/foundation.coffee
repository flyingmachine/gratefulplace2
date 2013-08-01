'use strict'

angular.module('gratefulplaceApp').controller 'FoundationCtrl', ($scope, $location, User, CurrentSession, Support) ->

  $scope.showNewTopicForm = true
  refreshSession = ->
    $scope.currentSession = CurrentSession.get()
    console.log $scope.currentSession
  $scope.$on 'auth.logged-in', refreshSession
  refreshSession()

  $scope.support = Support

  $scope.$on "$routeChangeStart", (event, next, current)->
    Support.clear()

  $scope.toggleNewTopicForm = ()->
    $scope.showNewTopicForm = !$scope.showNewTopicForm || $scope.currentSession.newRegistration && !$scope.currentSesssion.hasPosted
    
  $scope.$on 'topic.created', ->
    $scope.showNewTopicForm = false
