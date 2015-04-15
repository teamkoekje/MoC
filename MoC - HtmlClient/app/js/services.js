/* global angular */

'use strict';

/* Services */

var services = angular.module('mocServices', ['ngResource']);

//For credential stuff, activate later
//services.config(function ($httpProvider) {
//    $httpProvider.defaults.withCredentials = true;
//});

var baseUrl = 'http://localhost\\:8080/MoC-Service/api';

services.factory('user', ['$resource',
    function ($resource) {
        return $resource(baseUrl + '/user/:userId', {username: '@userId'});
    }
]);