(function() {
  'use strict';

  angular.module("gratefulplaceApp").factory("Users", [
    "$http", "$q", function($http, $q) {
      return {
        all: function() {
          var deferred;
          deferred = $q.defer();
          $http.get("data/users.json").then(function(data) {
            return deferred.resolve(data.data);
          });
          return deferred.promise;
        },
        find: function(id) {
          var deferred;
          deferred = $q.defer();
          this.all().then(function(topics) {
            return deferred.resolve(_.find(topics, function(topic) {
              return parseInt(topic.id) === parseInt(id);
            }));
          });
          return deferred.promise;
        }
      };
    }
  ]);

}).call(this);
