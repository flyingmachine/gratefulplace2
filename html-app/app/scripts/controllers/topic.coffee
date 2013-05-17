'use strict'

angular.module('gratefulplaceApp').controller 'TopicCtrl', ($scope, $routeParams, Topics) ->
  Topics.get(id: $routeParams.id, (topic)->
    console.log(topic)
    $scope.topic = topic
  )