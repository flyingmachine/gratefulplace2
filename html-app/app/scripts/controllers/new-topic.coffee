'use strict'

angular.module('gratefulplaceApp')
  .controller 'NewTopicCtrl', ($scope, $routeParams, $resource, Topic) ->
    $scope.submitNewTopic = ->
      topic = new Topic($scope.newTopic)
      console.log $scope.newTopic
      topic.$save()
      