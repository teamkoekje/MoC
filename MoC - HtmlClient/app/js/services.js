/* global angular */

'use strict';

/* Services */

var services = angular.module('mocServices', ['ngResource']);

services.config(function ($httpProvider) {
    $httpProvider.defaults.withCredentials = true;
});



var baseUrl = 'http://localhost\\:8080/MoC-Service/api';

services.factory('user', ['$resource',
    function ($resource) {
        return{
            all: $resource(baseUrl + '/user/:userId', {userId: '@userId'}, {remove: {method: 'DELETE'}}),
            teams: $resource(baseUrl + '/user/:userId/teams', {userId: '@userId'})
        };
    }
]);

services.factory('competition', ['$resource',
    function ($resource) {
        return{
            all: $resource(baseUrl + '/competition/:competitionId', {competitionId: '@competitionId'}, {remove: {method: 'DELETE'}}),
            challenges: $resource(baseUrl + '/competition/:competitionId/challenges', {competitionId: '@competitionId'}),
            active: $resource(baseUrl + '/competition/active'),
            future: $resource(baseUrl + '/competition/future'),
            teams: $resource(baseUrl + '/competition/:competitionId/teams', {userId: '@competitionId'})
        };
    }
]);

services.factory('team', ['$resource',
    function ($resource) {
        return{
            all: $resource(baseUrl + '/team/:teamId', {teamId: '@teamId'}, {remove: {method: 'DELETE'}}),
            byUser: $resource(baseUrl + '/eam/user/:userId', {userId: 'userId'}),
            participants: $resource(baseUrl + '/team/:teamId/users', {teamId: 'teamId'})
        };
    }
]);

services.factory('workspace', ['$resource',
    function ($resource) {
        return $resource(baseUrl + '/workspace/:teamId', {teamId: '@teamId'});
    }
]);