'use strict'

angular.module('gratefulplaceApp')
  .controller 'WatchedTopicsCtrl', ($scope, WatchedTopic) ->
    WatchedTopic.query (data)->
      $scope.topics = data
