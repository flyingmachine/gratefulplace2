'use strict'

angular.module('gratefulplaceApp')
  .controller 'TopicsNewCtrl', ($rootScope, $scope, $routeParams, $resource, Topic, CurrentSession) ->
    newTopic = ->
      $scope.newTopic =
        visibility: "public"
    newTopic()
    
    $scope.submitNewTopic = ->
      topic = new Topic($scope.newTopic)
      topic.$save (t)->
        $scope.currentSession.hasPostedToday = true
        $scope.newTopicForm.show = false
        newTopic()
        $rootScope.$broadcast('topic.created', t)
