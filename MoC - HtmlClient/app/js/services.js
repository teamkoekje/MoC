/* global angular */

'use strict';
/* Services */

var services = angular.module('mocServices', ['ngResource']);
services.config(function ($httpProvider) {
    $httpProvider.defaults.withCredentials = true;
});

var baseUrl = 'http://localhost:8080/MoC-Service/api';

services.factory('user', ['$resource',
    function ($resource) {
        return{
            all: $resource(baseUrl + '/user/:userId', {userId: '@userId'}, {remove: {method: 'DELETE'}}),
            authenticated: $resource(baseUrl + '/user/authenticated'),
            register: $resource(baseUrl + '/user/register/:token', {token: '@token'}),
            teams: $resource(baseUrl + '/user/:userId/teams', {userId: '@userId'}),
            invitations: $resource(baseUrl + '/user/:userId/invitations', {userId: '@userId'}),
            search: $resource(baseUrl + '/user/search/:searchInput', {searchInput: '@searchInput'})
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
            teams: $resource(baseUrl + '/competition/:competitionId/teams', {competitionId: '@competitionId'})
        };
    }
]);
services.factory('team', ['$resource',
    function ($resource) {
        return{
            all: $resource(baseUrl + '/team/:teamId', {teamId: '@teamId'}, {remove: {method: 'DELETE'}}),
            myTeams: $resource(baseUrl + '/team/myTeams'),
            myTeamInvitations: $resource(baseUrl + '/team/myTeamInvitations'),
            acceptInvitation: $resource(baseUrl + '/team/accept/:invitationId', {invitationId: '@invitationId'}),
            declineInvitation: $resource(baseUrl + '/team/decline/:invitationId', {invitationId: '@invitationId'}),
            byToken: $resource(baseUrl + '/team/token/:token', {token: '@token'}),
            participants: $resource(baseUrl + '/team/:teamId/users', {teamId: '@teamId'}),
            invitedParticipants: $resource(baseUrl + '/team/:teamId/findInvited', {teamId: '@teamId'}),
            invite: $resource(baseUrl + '/team/:teamId/invite', {teamId: '@teamId'}),
            leaveTeam: $resource(baseUrl + '/team/:teamId/leave/:username', {teamId: '@teamId', username: '@username'})
        };
    }
]);
services.factory('workspace', ['$resource',
    function ($resource) {
        return{
            update: $resource(baseUrl + '/workspace/:competitionId/update', {competitionId: '@competitionId'}),
            compile: $resource(baseUrl + '/workspace/:competitionId/compile', {competitionId: '@competitionId'}),
            submit: $resource(baseUrl + '/workspace/:competitionId/submit', {competitionId: '@competitionId'}),
            folderStructure: $resource(baseUrl + '/workspace/:competitionId/folderStructure', {competitionId: '@competitionId'}),
            availableTests: $resource(baseUrl + '/workspace/:competitionId/availableTests', {competitionId: '@competitionId'}),
            file: $resource(baseUrl + '/workspace/:competitionId/file', {competitionId: '@competitionId'}),
            test: $resource(baseUrl + '/workspace/:competitionId/test/:testName', {competitionId: '@competitionId', testName: '@testName'})
        };
    }
]);
