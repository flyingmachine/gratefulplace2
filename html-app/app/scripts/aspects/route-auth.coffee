angular.module('gratefulplaceApp').run ($rootScope, CurrentSession)->
  authRoutes = ['/profile']

  isAuthRoute = (route)->
    _.any(authRoutes, (authRoute)-> (new RegExp("^#{authRoute}")).test(route))

  routeFromURL = (url)->
    match = /\#(.*)/.exec(url)
    match && match[1] || ""
  
  $rootScope.$on '$locationChangeStart', (event, newURL, currentURL)->
    route  = routeFromURL(newURL)
    if !CurrentSession.get() && isAuthRoute(route)
      event.preventDefault()
