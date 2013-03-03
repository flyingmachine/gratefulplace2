'use strict';
angular.module("gratefulplaceApp")
  .factory "Topics", ["$http", ($http) ->
    posts = []
    loadCall = undefined
    
    # Public API here
    load: ->
      return loadCall  if loadCall
      loadCall = $http.get("data/topics.json").then (data)->
        console.log(data)
        posts = data.data
    data: ->
      posts
  ]