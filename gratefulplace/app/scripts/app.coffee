'use strict'

angular.module('gratefulplaceApp', [])
  .config ($routeProvider) ->
    $routeProvider
      .when '/',
        templateUrl: 'views/topics/index.html',
        controller: 'TopicsCtrl'
      .when '/topics/:id',
        templateUrl: 'views/topics/show.html',
        controller: 'TopicCtrl'
      .otherwise
        redirectTo: '/'
