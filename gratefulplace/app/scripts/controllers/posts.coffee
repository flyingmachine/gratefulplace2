'use strict'

angular.module('gratefulplaceApp')
  .controller 'PostsCtrl', ($scope, Posts) ->
    Posts.load().then ()->
      $scope.posts = Posts.data()
      console.log $scope.posts