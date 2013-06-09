'use strict'

angular.module('gratefulplaceApp')
  .controller 'ProfileCtrl', ($scope, User, CurrentSession) ->

    $scope.editing = false
    
    User.get id: CurrentSession.get().id, (user)->
      $scope.user = user

    $scope.showEdit = ->
      true

    $scope.toggleEdit = ($event)->
      $scope.editing = !$scope.editing
      $event && $event.preventDefault()
    