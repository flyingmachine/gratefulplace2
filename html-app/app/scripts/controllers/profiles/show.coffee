'use strict'

angular.module('gratefulplaceApp')
  .controller 'ProfilesShowCtrl', ($scope, $location, $routeParams, User, Authorize, Paginator) ->
    Authorize.requireLogin()
    paginator = new Paginator
    $scope.paginationData = paginator.data
    
    $scope.hasAbout = ->
      $scope.user && $scope.user['formatted-about'] && $scope.user['formatted-about'].length
    User.get _.merge({id: $routeParams.id, 'include-posts': true}, $location.search()), (user)->
      $scope.loading = false
      $scope.user = user
      $scope.user.posts = paginator.receive(user.posts)