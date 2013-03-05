(function() {
  'use strict';

  angular.module("gratefulplaceApp").factory("Peek", [
    "$http", "$q", function($http, $q) {
      return {
        one: "two"
      };
    }
  ]);

}).call(this);
