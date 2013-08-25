'use strict'

angular.module('gratefulplaceApp', @App.modules)
  .config ($routeProvider)->
    $routeProvider
      .when '/',
        templateUrl: 'views/topics/index.html',
        controller: 'TopicsQueryCtrl'
      .when '/topics/:id',
        templateUrl: 'views/topics/show.html',
        controller: 'TopicsShowCtrl'
      .when '/login',
        templateUrl: 'views/login.html'
      .when '/manage-profile/about',
        templateUrl: 'views/manage-profile/about.html'
        controller: 'ManageProfileAboutCtrl'
      .when '/manage-profile/password',
        templateUrl: 'views/manage-profile/password.html'
        controller: 'ManageProfilePasswordCtrl'
      .when '/manage-profile/email',
        templateUrl: 'views/manage-profile/email.html'
        controller: 'ManageProfileEmailCtrl'
      .when '/profiles/:id',
        templateUrl: 'views/profiles/show.html'
        controller: 'ProfilesShowCtrl'
      .when '/watched-topics',
        templateUrl: 'views/watched-topics.html',
        controller: 'WatchesQueryCtrl'
      .when '/welcome',
        templateUrl: 'views/welcome.html'
      .otherwise
        redirectTo: '/'
