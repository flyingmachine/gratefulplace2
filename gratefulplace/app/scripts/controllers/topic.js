(function() {
  'use strict';

  angular.module('gratefulplaceApp').controller('TopicCtrl', function($scope, $routeParams, Topics) {
    return Topics.find($routeParams.id).then(function(topic) {
      console.log(topic);
      return $scope.topic = topic;
    });
  });

}).call(this);
