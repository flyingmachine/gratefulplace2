'use strict';
angular.module("gratefulplaceApp").factory "Topic", ["$resource", ($resource) ->
  $resource '/topics/:id'
]