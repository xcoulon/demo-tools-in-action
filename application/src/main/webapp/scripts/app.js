'use strict';

angular.module('conferences',['ngRoute','ngResource'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/',{templateUrl:'views/landing.html',controller:'LandingPageController'})
      .when('/Conferences',{templateUrl:'views/Conference/search.html',controller:'SearchConferenceController'})
      .when('/Conferences/new',{templateUrl:'views/Conference/detail.html',controller:'NewConferenceController'})
      .when('/Conferences/edit/:ConferenceId',{templateUrl:'views/Conference/detail.html',controller:'EditConferenceController'})
      .when('/Sessions',{templateUrl:'views/Session/search.html',controller:'SearchSessionController'})
      .when('/Sessions/new',{templateUrl:'views/Session/detail.html',controller:'NewSessionController'})
      .when('/Sessions/edit/:SessionId',{templateUrl:'views/Session/detail.html',controller:'EditSessionController'})
      .when('/Speakers',{templateUrl:'views/Speaker/search.html',controller:'SearchSpeakerController'})
      .when('/Speakers/new',{templateUrl:'views/Speaker/detail.html',controller:'NewSpeakerController'})
      .when('/Speakers/edit/:SpeakerId',{templateUrl:'views/Speaker/detail.html',controller:'EditSpeakerController'})
      .otherwise({
        redirectTo: '/'
      });
  }])
  .controller('LandingPageController', function LandingPageController() {
  })
  .controller('NavController', function NavController($scope, $location) {
    $scope.matchesRoute = function(route) {
        var path = $location.path();
        return (path === ("/" + route) || path.indexOf("/" + route + "/") == 0);
    };
  });
