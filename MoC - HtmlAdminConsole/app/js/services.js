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
            allUsers: $resource(baseUrl + '/user'),
            all: $resource(baseUrl + '/user/:userId', {userId: '@userId'}, {remove: {method: 'DELETE'}}),
            register: $resource(baseUrl + '/user/register/:token', {token: '@token'}),
            teams: $resource(baseUrl + '/user/:userId/teams', {userId: '@userId'}),
            invitations: $resource(baseUrl + '/user/:userId/invitations', {userId: '@userId'}),
            isAdmin: $resource(baseUrl + '/user/isAdmin')
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
            teams: $resource(baseUrl + '/competition/:competitionId/teams', {competitionId: '@competitionId'}),
            start: $resource(baseUrl + '/competition/:competitionId/start', {competitionId: '@competitionId'}),
            pause: $resource(baseUrl + '/competition/:competitionId/pause', {competitionId: '@competitionId'}),
            freeze: $resource(baseUrl + '/competition/:competitionId/freeze', {competitionId: '@competitionId'}),
            stop: $resource(baseUrl + '/competition/:competitionId/stop', {competitionId: '@competitionId'}),
            add: $resource(baseUrl + '/competition')
        };
    }
]);
services.factory('team', ['$resource',
    function ($resource) {
        return{
            allTeams: $resource(baseUrl + '/team'),
            all: $resource(baseUrl + '/team/:teamId', {teamId: '@teamId'}, {remove: {method: 'DELETE'}}),
            myTeams: $resource(baseUrl + '/team/myTeams'),
            byToken: $resource(baseUrl + '/team/token/:token', {token: '@token'}),
            participants: $resource(baseUrl + '/team/:teamId/users', {teamId: '@teamId'})
        };
    }
]);

services.factory('workspace', ['$resource',
    function ($resource) {
        return{
            all: $resource(baseUrl + '/workspace/:teamId', {teamId: '@teamId'}),
            myTeams: $resource(baseUrl + '/team/myTeams'),
            sysInfo: $resource(baseUrl + '/workspace/sysinfo'),
            byToken: $resource(baseUrl + '/team/token/:token', {token: '@token'}),
            participants: $resource(baseUrl + '/team/:teamId/users', {teamId: '@teamId'})
        };
    }
]);

services.factory('challenge', ['$resource',
    function ($resource) {
        return {
            create: $resource(baseUrl + '/challenge')
        };
    }
]);