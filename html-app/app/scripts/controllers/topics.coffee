'use strict'

angular.module('gratefulplaceApp').controller 'TopicsCtrl', ($scope, Topics) ->
  Topics.query (data)->
    $scope.topics = data

  $scope.firstPost = (topic)->
    topic.posts[0]