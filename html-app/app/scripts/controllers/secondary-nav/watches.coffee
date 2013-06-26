'use strict'

angular.module('gratefulplaceApp')
  .controller 'SecondaryNavWatchesCtrl', ($scope, $routeParams, Watch) ->
    watch = null
    $scope.watching = ->
      watch ||= new Watch($scope.watch())

    $scope.watch = ->
      _.find $scope.secondaryNav.data.watches, (watch)->
        watch['user-id'] == $scope.currentSession.id

    $scope.createWatch = ->
      watch = new Watch('topic-id': $scope.peek.data.id, 'user-id': $scope.currentSession.id)
      watch.$save()

    $scope.destroyWatch = ->
      watch.$delete()