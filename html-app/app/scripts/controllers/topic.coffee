'use strict'

angular.module('gratefulplaceApp')
  .controller 'TopicCtrl', ($scope, $routeParams, Topic) ->
    Topic.get id: $routeParams.id, (topic)->
      $scope.topic = topic
      $scope.firstPost = $scope.topic.posts.shift()
    
    $scope.submitReply = ->
      