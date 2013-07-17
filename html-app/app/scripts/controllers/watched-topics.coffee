'use strict'

angular.module('gratefulplaceApp')
  .controller 'WatchedTopicsCtrl', ($scope, WatchedTopic, Watch, Utils) ->
    watches = null

    WatchedTopic.query (data)->
      $scope.topics = data
      Utils.addWatchCountToTopics($scope.topics, watches)

    Watch.query (data)->
      watches = data
      Utils.addWatchCountToTopics($scope.topics, watches)
