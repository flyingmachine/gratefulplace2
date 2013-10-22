'use strict'

angular.module('gratefulplaceApp')
  .controller 'ProfilesShowCtrl', ($scope, $routeParams, User, Authorize) ->
    Authorize.requireLogin()
    $scope.hasAbout = ->
      $scope.user['formatted-about'] && $scope.user['formatted-about'].length
    User.get {id: $routeParams.id, 'include-posts': true}, (user)->
      $scope.user = user