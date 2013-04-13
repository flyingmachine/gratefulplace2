'use strict';
angular.module("gratefulplaceApp")
  .factory "Topics", ["$http", "$resource", ($http, $resource) ->
    $resource '/topics/:id'
  ]