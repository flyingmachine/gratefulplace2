'use strict';
angular.module("gratefulplaceApp").factory "WatchedTopic", ["$resource", ($resource) ->
  $resource '/watched-topics/:id', id: '@id'
]