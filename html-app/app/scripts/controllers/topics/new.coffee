'use strict'

angular.module('gratefulplaceApp')
  .controller 'TopicsNewCtrl', ($rootScope, $scope, $routeParams, $resource, Topic, CurrentSession) ->
    $scope.newTopic = {}
    $scope.submitNewTopic = ->
      topic = new Topic($scope.newTopic)
      topic.$save (t)->
        $scope.newTopic = {}
        $rootScope.$broadcast('topic.created', t)