'use strict'

angular.module('gratefulplaceApp')
  .controller 'TopicCtrl', ($scope, $routeParams, Topic, Post, User) ->
    Topic.get id: $routeParams.id, (topic)->
      $scope.topic = topic
      $scope.firstPost = $scope.topic.posts.shift()
        
    $scope.submitReply = ->
      post = new Post($scope.reply)
      post['topic-id'] = $scope.topic.id
      post.$save((p)->
        $scope.topic.posts.push(p)
      , (res)->
        $scope.errors = res.data.errors
      )
      $scope.reply = {}
