(function() {
  'use strict';

  angular.module('gratefulplaceApp').controller('TopicsCtrl', function($scope, Topics) {
    Topics.all().then(function(data) {
      return $scope.topics = data;
    });
    return $scope.firstPost = function(topic) {
      return topic.posts[0];
    };
  });

}).call(this);
