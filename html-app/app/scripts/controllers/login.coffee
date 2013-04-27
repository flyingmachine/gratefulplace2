'use strict'

angular.module('gratefulplaceApp')
  .controller 'LoginCtrl', ($scope, Session) ->
    Topics.query (data)->
      $scope.topics = data

    $scope.firstPost = (topic)->
      topic.posts[0]