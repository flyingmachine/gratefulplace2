'use strict'

angular.module('gratefulplaceApp').controller 'FoundationCtrl', ($scope, $location, User, CurrentSession) ->
  refreshSession = ->
    $scope.currentSession = CurrentSession.get()
    console.log $scope.currentSession
  $scope.$on 'auth.logged-in', refreshSession
  refreshSession()

  # begin secondary handlers
  $scope.secondaryHandlers = []
  defineSecondaryHandler = (name, rootPath)->
    $scope.secondaryHandlers.push name
    handler =
      data: null
      path: null
      include: ->
        if @path
          rootPath + @path + ".html"
      show: (path, data)->
        @path = path
        @data = data
      clear: ->
        @path = null
        @data = null
    $scope[name] = handler

  defineSecondaryHandler "peek", "views/peeks/"
  defineSecondaryHandler "secondaryNav", "views/secondary-navs/"
  # end secondary handlers

  $scope.$on "$routeChangeStart", (event, next, current)->
    _.each $scope.secondaryHandlers, (h)->
      $scope[h].clear()

  $scope.toggleNewTopicForm = ()->
    $scope.showNewTopicForm = !$scope.showNewTopicForm
    
  $scope.$on 'topic.created', ->
    $scope.showNewTopicForm = false