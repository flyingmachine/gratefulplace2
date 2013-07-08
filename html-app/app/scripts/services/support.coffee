'use strict'
angular.module("gratefulplaceApp").factory "Support", ()->
  handlerNames = []
  supportHandlers =
    clear: ->
      _.each handlerNames, (h)->
        supportHandlers[h].clear()
        
  defineSupportHandler = (name, rootPath)->
    handlerNames.push name
    handler =
      data: null
      path: null
      include: ->
        if @path
          rootPath + @path + ".html"
      show: (path, data)->
        @path = path
        @data = data
      clear: ->
        @path = null
        @data = null
    supportHandlers[name] = handler

  defineSupportHandler "peek", "views/peeks/"
  defineSupportHandler "secondaryNav", "views/secondary-navs/"

  supportHandlers

  
