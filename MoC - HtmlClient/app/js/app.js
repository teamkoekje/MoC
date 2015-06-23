/* global angular */

'use strict';

// Declare app level module which depends on views, and components
var app = angular.module('mocApp', [
    'ngRoute',
    'mocControllers',
    'mocServices',
    'pascalprecht.translate'
]);

app.config(['$routeProvider', 
    function ($routeProvider) {
        $routeProvider
                .when('/login', {
                    templateUrl: 'views/login.html'
                })
                .when('/register', {
                    templateUrl: 'views/register.html',
                    controller: 'registerController'
                })
                .when('/competitions', {
                    templateUrl: 'views/competitions.html',
                    controller: 'competitionsController'
                })
                .when('/teams', {
                    templateUrl: 'views/teams.html',
                    controller: 'teamsController'
                })
                .when('/newteam', {
                    templateUrl: 'views/newteam.html',
                    controller: 'newTeamController'
                })
                .when('/apiDemo', {
                    templateUrl: 'views/apiDemo.html',
                    controller: 'demoController'
                })
                .when('/competitionDemo', {
                    templateUrl: 'views/competitionDemo.html',
                    controller: 'competitionController'
                })
                .when('/competition', {
                    templateUrl: 'views/competition.html',
                    controller: 'competitionController'
                })
                .when('/newteammember', {
                    templateUrl: 'views/newteammember.html',
                    controller: 'inviteUserController'
                })
                .when('/roundResult', {
                    templateUrl: 'views/roundResult.html',
                    controller: 'roundResultController'
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
