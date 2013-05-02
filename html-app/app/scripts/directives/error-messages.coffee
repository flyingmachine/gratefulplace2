angular.module('gratefulplaceApp').directive 'errorMessages',  ->
  restrict: 'A'
  replace: false
  scope:
    messages: '=errorMessages'
  template: '<ul><li ng-repeat="message in messages">{{message}}</li></ul>'