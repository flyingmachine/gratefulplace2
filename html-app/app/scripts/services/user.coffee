'use strict';
angular.module("gratefulplaceApp").factory "User", ["$resource", ($resource) ->
  $resource '/users/:id/:attribute', id: '@id',
    saveAbout:
      method: 'POST'
      params: {attribute: 'about'}
    saveEmail:
      method: 'POST'
      params: {attribute: 'email'}
    changePassword:
      method: 'POST'
      params: {attribute: 'password'}
]