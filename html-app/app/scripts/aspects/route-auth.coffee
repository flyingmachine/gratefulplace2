angular.module('gratefulplaceApp').run ($rootScope, CurrentSession)->
  authRoutes = ['/profile']

  isAuthRoute = (route)->
    _.any(authRoutes, (authRoute)-> (new RegExp("^#{authRoute}")).test(route))
  
  $rootScope.$on '$locationChangeStart', (event, newURL, currentURL)->
    route  = /\#(.*)/.exec(newURL)[1]
    if !CurrentSession.get() && isAuthRoute(route)
      event.preventDefault()