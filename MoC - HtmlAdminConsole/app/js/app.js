/* global angular */

'use strict';

// Declare app level module which depends on views, and components
var app = angular.module('mocApp', [
    'ngRoute',
    'ngDialog',
    'mocControllers',
    'mocServices',
    'ui.bootstrap.datetimepicker',
    'pascalprecht.translate'
]);

app.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider
                .when('/login', {
                    templateUrl: 'views/login.html',
                    controller: 'loginController', //no need to add loginController because it's defined in index.html
                    caseInsensitiveMatch: true
                })
                .when('/register', {
                    templateUrl: 'views/register.html',
                    controller: 'registerController',
                    caseInsensitiveMatch: true
                })
                .when('/competitionOverview', {
                    templateUrl: 'views/competitionOverview.html',
                    controller: 'competitionOverviewController',
                    caseInsensitiveMatch: true
                })
                .when('/competitionView', {
                    templateUrl: "views/competitionView.html",
                    controller: 'competitionViewController',
                    caseInsensitiveMatch: true
                })
                .when('/addChallenge', {
                    templateUrl: "views/addChallenge.html",
                    controller: 'addChallengeController',
                    caseInsensitiveMatch: true
                })
                .otherwise({redirectTo: '/login'});
    }
]);

app.config(['$translateProvider', function ($translateProvider) {
  // add translation tables
  $translateProvider.translations('en', translationsEN);
  $translateProvider.translations('nl', translationsNL);
  $translateProvider.translations('fi', translationsFI);
  $translateProvider.translations('pl', translationsPL);
  $translateProvider.translations('ru', translationsRU);
  $translateProvider.preferredLanguage('en');
  $translateProvider.fallbackLanguage('en');
  $translateProvider.useSanitizeValueStrategy('escaped');
}]);