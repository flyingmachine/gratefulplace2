angular.module("gratefulplaceApp").factory "Page", ->
  title: "Grateful Place"
  setTitle: (newTitle)->
    @title = newTitle