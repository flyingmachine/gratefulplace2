'use strict'

angular.module('gratefulplaceApp')
  .controller 'WatchedTopicsCtrl', ($scope, WatchedTopic, Watch) ->
    watches = null

    addWatchCounts = ->
      if $scope.topics && watches
        _.each watches, (watch)->
          _.each $scope.topics, (topic)->
            if watch['topic-id'] == topic.id && watch['unread-count']
              topic['unread-count'] = watch['unread-count']
              false

    WatchedTopic.query (data)->
      $scope.topics = data
      addWatchCounts()

    Watch.query (data)->
      watches = data
      addWatchCounts()
