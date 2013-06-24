'use strict'

angular.module('gratefulplaceApp')
  .controller 'WatchedTopicsCtrl', ($scope, WatchedTopic, User) ->
    WatchedTopic.query (data)->
      $scope.topics = data
