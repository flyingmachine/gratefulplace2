'use strict';
angular.module("gratefulplaceApp")
  .factory "Topics", ["$resource", ($resource) ->
    $resource '/topics/:id'
  ]