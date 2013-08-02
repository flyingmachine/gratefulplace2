'use strict'

angular.module('gratefulplaceApp')
  .controller 'TopicsNewCtrl', ($rootScope, $scope, $routeParams, $resource, Topic, CurrentSession) ->
    $scope.newTopic = {}

    $scope.submitNewTopic = ->
      topic = new Topic($scope.newTopic)
      topic.$save (t)->
        $scope.currentSession.hasPostedToday = true
        $scope.newTopicForm.show = false
        $scope.newTopic = {}
        $rootScope.$broadcast('topic.created', t)
