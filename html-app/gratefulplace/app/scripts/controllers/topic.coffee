'use strict'

angular.module('gratefulplaceApp')
  .controller 'TopicCtrl', ($scope, $routeParams, Topics) ->
    Topics.find($routeParams.id).then (topic)->
      console.log(topic)
      $scope.topic = topic