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
                    controller: '' //no need to add loginController because it's defined in index.html
                })
                .when('/register', {
                    templateUrl: 'views/register.html',
                    controller: 'registerController'
                })
                .when('/competitionOverview', {
                    templateUrl: 'views/competitionOverview.html',
                    controller: 'competitionOverviewController'
                })
                .when('/competitionView', {
                    templateUrl: "views/competitionView.html",
                    controller: 'competitionViewController'
                })
                .otherwise({redirectTo: '/login'});
    }
]);
