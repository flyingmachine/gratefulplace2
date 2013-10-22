'use strict'

angular.module('gratefulplaceApp', @App.modules)
  .config ($routeProvider)->
    $routeProvider
      .when '/',
        templateUrl: 'views/topics/index.html',
        controller: 'TopicsQueryCtrl'
      .when '/topics',
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

      .when '/credential-recovery/forgot-username',
        templateUrl: 'views/credential-recovery/forgot-username.html',
        controller: 'CredentialRecoveryForgotUsernameCtrl'
      .when '/credential-recovery/forgot-password',
        templateUrl: 'views/credential-recovery/forgot-password.html',
        controller: 'CredentialRecoveryForgotPasswordCtrl'
      .when '/credential-recovery/reset-password',
        templateUrl: 'views/credential-recovery/reset-password.html',
        controller: 'CredentialRecoveryResetPasswordCtrl'

      .when '/admin/users',
        templateUrl: 'views/admin/users/index.html',
        controller: 'AdminUsersQueryCtrl'
        
      .when '/welcome',
        templateUrl: 'views/welcome.html'
      .otherwise
        redirectTo: '/topics'
