'use strict'

angular.module('gratefulplaceApp', @App.modules)
  .config ($routeProvider)->
    $routeProvider
      .when '/',
        templateUrl: 'views/topics/index.html',
        controller: 'TopicsCtrl'
      .when '/topics/:id',
        templateUrl: 'views/topics/show.html',
        controller: 'TopicCtrl'
      .when '/login',
        templateUrl: 'views/login.html'
      .when '/profile',
        templateUrl: 'views/profiles/show.html'
        controller: 'ProfileCtrl'
      .otherwise
        redirectTo: '/'
  .directive 'peekable', ($rootScope)->
    restrict: 'A',
    link: ->
      # $rootScope.$broadcast