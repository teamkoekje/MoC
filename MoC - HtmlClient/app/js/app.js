/* global angular */

'use strict';

// Declare app level module which depends on views, and components
var app = angular.module('mocApp', [
    'ngRoute',
    'mocControllers',
    'mocServices'
]);

app.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider
                .when('/login', {
                    templateUrl: 'views/login.html',
                    controller: 'loginController'
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
                .when('/editor', {
                    templateUrl: 'views/editor.html',
                    controller: 'editorController'
                })
                .when('/apiDemo', {
                    templateUrl: 'views/apiDemo.html',
                    controller: 'demoController'
                })
                .when('/competitionDemo', {
                    templateUrl: 'views/competitionDemo.html',
                    controller: 'competitionController'
                })
                .otherwise({redirectTo: '/login'});
    }
]);
