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
        return $resource(baseUrl + '/user/:userId', {userId: '@userId'}, {
            remove: {method: 'DELETE'}
        });
    }
]);

services.factory('competition', ['$resource',
    function ($resource) {
        return $resource(baseUrl + '/competition/:competitionId', {competitionId: '@competitionId'}, {
            remove: {method: 'DELETE'}
        });
    }
]);

services.factory('competitions', ['$resource',
    function ($resource) {
        return $resource(baseUrl + '/competition/type/:type', {type: '@type'}, {
        });
    }
]);

services.factory('team', ['$resource',
    function ($resource) {
        return{
            all: $resource(baseUrl + '/team/:teamId', {teamId: '@teamId'}, {remove: {method: 'DELETE'}}),
            byUser: $resource(baseUrl + '/team/user/:userId', {userId: 'userId'}),
            participants: $resource(baseUrl + '/user/team/:teamId', {teamId: 'teamId'})
        };
    }
]);

services.factory('workspace', ['$resource',
    function ($resource) {
        return $resource(baseUrl + '/workspace/:teamId', {teamId: '@teamId'});
    }
]);