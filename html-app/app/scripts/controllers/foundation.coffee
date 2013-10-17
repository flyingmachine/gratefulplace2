'use strict'

angular.module('gratefulplaceApp').controller 'FoundationCtrl', ($scope, $location, $http, User, CurrentSession) ->

  refreshSession = ->
    $scope.currentSession = CurrentSession.get()
    console.log $scope.currentSession
    
  $scope.$on 'auth.logged-in', ->
    refreshSession()
    $location.path("/topics")
    
  refreshSession()

  $scope.$on 'topic.created', ->
    $scope.showNewTopicForm = false

  $http.get("/stats").success (data)->
    $scope.stats = data