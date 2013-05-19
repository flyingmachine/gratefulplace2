'use strict'

angular.module('gratefulplaceApp')
  .controller 'NewTopicCtrl', ($rootScope, $scope, $routeParams, $resource, Topic, CurrentSession) ->
    $scope.newTopic = {}
    $scope.submitNewTopic = ->
      topic = new Topic($scope.newTopic)
      topic.$save (t, x)->
        $scope.newTopic = {}
        $rootScope.$broadcast('topic.created', t)