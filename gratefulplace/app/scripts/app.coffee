'use strict'

angular.module('gratefulplaceApp', [])
  .config ($routeProvider) ->
    $routeProvider
      .when '/',
        templateUrl: 'views/topics/index.html'
        controller: 'TopicsCtrl'
      .otherwise
        redirectTo: '/'
