'use strict'

angular.module('gratefulplaceApp').controller 'TopicsCtrl', ($scope, Topic) ->
  Topic.query (data)->
    $scope.topics = data

  $scope.firstPost = (topic)->
    topic.posts[0]