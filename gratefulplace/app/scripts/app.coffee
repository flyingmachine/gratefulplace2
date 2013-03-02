'use strict'

angular.module('gratefulplaceApp', [])
  .config ($routeProvider) ->
    $routeProvider
      .when '/',
        templateUrl: 'views/posts/index.html'
        controller: 'PostsCtrl'
      .otherwise
        redirectTo: '/'
