'use strict'

angular.module('gratefulplaceApp')
  .controller 'WatchesQueryCtrl', ($scope, $location, WatchedTopic, Watch, Utils, Paginator) ->
    watches = null
    paginator = new Paginator
    $scope.paginationData = paginator.data

    WatchedTopic.query $location.search(), (data)->
      $scope.loading = false
      $scope.topics = paginator.receive(data)
      Utils.addWatchCountToTopics($scope.topics, watches)

    Watch.query (data)->
      watches = data
      Utils.addWatchCountToTopics($scope.topics, watches)
