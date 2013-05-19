'use strict'

angular.module('gratefulplaceApp')
  .controller 'TopicCtrl', ($scope, $routeParams, Topic) ->
    Topic.get(id: $routeParams.id, (topic)->
      console.log(topic)
      $scope.topic = topic
    )