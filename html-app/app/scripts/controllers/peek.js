(function() {
  'use strict';

  angular.module('gratefulplaceApp').controller('PeekCtrl', function($scope, $rootScope, $routeParams, Peek) {
    return $rootScope.$on("peek.new")(function(event, payload) {
      return $scope.peek = payload;
    });
  });

}).call(this);
