'use strict'

angular.module('gratefulplaceApp').controller 'TopicsCtrl', ($scope, Topic) ->
  $scope.$on 'topic.created', (e, topic)->
    console.log "got event"
    console.log topic
    $scope.topics.unshift topic
  
  Topic.query (data)->
    $scope.topics = data
    console.log data

  $scope.firstPost = (topic)->
    topic.posts[0]