'use strict';
angular.module("gratefulplaceApp").factory "User", ["$resource", ($resource) ->
  $resource '/users/:id/:attribute', id: '@id',
    saveAbout:
      method: 'POST'
      params: {attribute: 'about'}
]