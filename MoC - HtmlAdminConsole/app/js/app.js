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
                .otherwise({redirectTo: '/login'});
    }
]);
