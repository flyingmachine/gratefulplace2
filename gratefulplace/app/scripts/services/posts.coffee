'use strict';
angular.module("gratefulplaceApp")
  .factory "Posts", ["$http", ($http) ->
    posts = []
    loadCall = undefined
    
    # Public API here
    load: ->
      return loadCall  if loadCall
      loadCall = $http.get("data/posts.json").then (data)->
        console.log(data)
        posts = data.data
    data: ->
      posts
  ]