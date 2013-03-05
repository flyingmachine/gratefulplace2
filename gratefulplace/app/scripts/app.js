(function() {
  'use strict';

  angular.module('gratefulplaceApp', []).config(function($routeProvider) {
    return $routeProvider.when('/', {
      templateUrl: 'views/topics/index.html',
      controller: 'TopicsCtrl'
    }).when('/topics/:id', {
      templateUrl: 'views/topics/show.html',
      controller: 'TopicCtrl'
    }).otherwise({
      redirectTo: '/'
    });
  }).directive('peekable', function($rootScope) {
    return {
      restrict: 'A',
      link: function() {}
    };
  });

}).call(this);
