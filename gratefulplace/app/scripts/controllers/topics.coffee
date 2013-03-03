'use strict'

angular.module('gratefulplaceApp')
  .controller 'TopicsCtrl', ($scope, Topics) ->
    Topics.load().then ()->
      $scope.topics = Topics.data()

    $scope.firstPost = (topic)->
      topic.posts[0]