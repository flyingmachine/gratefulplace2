'use strict'

angular.module('gratefulplaceApp')
  .controller 'TopicCtrl', ($scope, $routeParams, Topics) ->
    $scope.firstPost = (topic)->
      topic.posts[0]