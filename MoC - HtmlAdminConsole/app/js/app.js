/* global angular */

'use strict';

// Declare app level module which depends on views, and components
var app = angular.module('mocApp', [
    'ngRoute',
    'ngDialog',
    'mocControllers',
    'mocServices',
    'ui.bootstrap.datetimepicker'
]);

app.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider
                .when('/login', {
                    templateUrl: 'views/login.html',
                    controller: '', //no need to add loginController because it's defined in index.html
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
