'use strict'

angular.module('gratefulplaceApp')
  .controller 'WatchedTopicsNavCtrl', ($rootScope, $scope, Watch) ->
    watches = {}
    Watch.query (data)->
      _.each data, (watch)->
        watches[watch['topic-id']] = watch
      console.log watches
      updateCount()

    updateCount = ->
      $scope.watchedTopicCount = _.reduce _.values(watches), (sum, w)->
        sum + w['unread-count']
      , 0
    
    $rootScope.$on "view.topic", (e, topic)->
      watches[topic.id]['unread-count'] = 0
      updateCount()
      
