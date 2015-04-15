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
                .when('/apiDemo', {
                    templateUrl: 'views/apiDemo.html',
                    controller: 'demoController'
                })
                .otherwise({redirectTo: '/view1'});
    }
]);
