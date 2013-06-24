'use strict'

angular.module('gratefulplaceApp')
  .controller 'PeekWatchesCtrl', ($scope, $routeParams, Watch) ->
    $scope.watching = ->
      $scope.watchStatus()
    
    $scope.watchStatus = _.memoize(->
      watch = _.find $scope.peek.data.watches, (watch)->
        watch['user-id'] == $scope.currentSession.id
      if watch
        watch.kind
      else
        false
    )

    $scope.createWatch = ->
      post = new Watch('topic-id': $scope.peek.data.id, 'user-id': $scope.currentSession.id)
      post.$save()