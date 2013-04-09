'use strict'

angular.module('gratefulplaceApp')
  .controller 'TopicsCtrl', ($scope, Topics) ->
    Topics.all().then (data)->
      $scope.topics = data

    $scope.firstPost = (topic)->
      topic.posts[0]