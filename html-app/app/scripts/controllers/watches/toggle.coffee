'use strict'
angular.module('gratefulplaceApp')
  .controller 'WatchesToggleCtrl', ($scope, $routeParams, Watch) ->
    $scope.watch = null
    deleted = false
    
    $scope.watching = ->
      if $scope.watch
        $scope.watch
      else if !deleted && watchData = findWatch()
        $scope.watch = new Watch(watchData)

    findWatch = ->
      if $scope.topic
        _.find $scope.topic.watches, (watch)->
          watch['user-id'] == $scope.currentSession.id

    $scope.createWatch = ->
      $scope.watch = new Watch
        'topic-id': $scope.topic.id
        'user-id': $scope.currentSession.id
        
      $scope.watch.$save (watchData)->
        $scope.watch =  new Watch(watchData)

    $scope.destroyWatch = ->
      $scope.watch.$delete()
      $scope.watch = null
      deleted = true
