'use strict'

angular.module('gratefulplaceApp')
  .controller 'PeekWatchesCtrl', ($scope, $routeParams, Topic) ->
    $scope.watching = ->
      $scope.watchStatus()
    
    $scope.watchStatus = _.memoize(->
      watch = _.find $scope.peek.data, (watch)->
        watch['user-id'] == $scope.currentSession.id
      if watch
        watch.kind
      else
        false
    )