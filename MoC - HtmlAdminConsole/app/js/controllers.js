/* global angular */

var controllers = angular.module('mocControllers', ['ngCookies']);
controllers.controller('loginController', ['$scope', '$translate', '$cookies', 'user', function ($scope, $translate, $cookies, $user) {
        $scope.changeLanguage = function (langKey) {
            $translate.use(langKey);
        };

        $scope.isLoggedIn = function () {
            return $cookies.user;
        };

        $scope.admin = undefined;
        $scope.isAdmin = function () {
            if ($scope.isLoggedIn()) {
                console.log("$user.isAdmin()");
                $user.isAdmin.get(function () {
                    console.log('success');
                    $scope.admin = true;
                }, function (error) {
                    console.log('error');
                    console.log(error.data);
                    $scope.admin = false;
                    alert(error.data);
                });
            }
        };
        //$scope.isAdmin();

        $scope.login = function () {
            console.log('login');
            $.ajax({
                type: "POST",
                url: "http://localhost:8080/MoC-Service/api/user/login",
                data: {
                    username: $scope.username,
                    password: $scope.password
                },
                xhrFields: {
                    withCredentials: true
                }
            }).success(function (data) {
                console.log("Logged in succesfully: " + $scope.username);
                $cookies.user = $scope.username;
                location.href = "#/competitionOverview";

                var ws = new WebSocket('ws://localhost:8080/MoC-Service/ws/api');
                ws.onopen = function () {
                    console.log("opening ws connection");
                };
                ws.onmessage = function (msg) {
                    var result = $.parseJSON(msg.data);
                    if (typeof result.hint !== 'undefined') {
                        $scope.newsfeed.push(result.hint.message);
                    }
                };
            }).error(function (data) {
                console.log("Error while logging in");
                console.log(data);
            });
        };
        $scope.logout = function () {
            $.ajax({
                type: "POST",
                url: "http://localhost:8080/MoC-Service/api/user/logout",
                xhrFields: {
                    withCredentials: true
                }
            }).success(function (data) {
                console.log("Logged out succesfully");
                delete $cookies.user;
                location.href = "#/login";
            }).error(function (data) {
                console.log("Error while logging out");
                delete $cookies.user;
                console.log(data);
            });
        };

        $scope.isLoggedInOwner = function (teamOwner) {
            return $cookies.user === teamOwner;
        };

        $scope.newsfeed = [];
        $scope.username = $cookies.user;
    }

]);
controllers.controller('registerController', ['$scope', '$routeParams', 'user', 'team',
    function ($scope, $routeParams, $user, $team) {
        $scope.showDetailsFor = 'user'; // can be 'user' or 'team' depending on what to show
        $scope.users = [
            'luc',
            'astrid',
            'casper',
            'daan',
            'arno',
            'robin'
        ];

        $scope.teams = [
            'team 1',
            'team 2',
            'team 3',
            'team 4',
            'team 5'
        ];

        $scope.currentUser = {
            name: 'Flupke',
            email: 'flupke69@hotmail.be',
            organisation: 'krijg de tyfus BV',
            teams: [
                'ebola FTW',
                'haat aan proftaak'
            ]
        };

        $scope.currentTeam = [
            'user 1',
            'user 2',
            'user 3'
        ];

        $scope.register = function () {
            console.log("Create User");
            $scope.user.$save(function () {
                $scope.showSuccesAlert = true;
                $scope.user = new $user.register();
            }, function (data) {
                console.log(data);
                $scope.showFailedAlert = true;
                $scope.error = data.data;
            });
        };

        if ($routeParams.token) {
            team = $team.byToken.get({token: $routeParams.token}, function () {
                $scope.inviteInfo = "You are invited to " + team.name;
            });
        }
        $scope.user = new $user.register();
    }
]);

controllers.controller('competitionOverviewController', ['$scope', 'ngDialog', 'competition',
    function ($scope, ngDialog, $competition) {
        $scope.competitions = new $competition.all.query();
        $scope.setSelectedCompetition = function (competition) {
            $scope.selectedCompetition = competition;
        };
        //Default info when no competition is selected
        $scope.selectedCompetition = {
            name: "No competition selected",
            startTime: null,
            status: null
        };

        $scope.addCompetition = function () {
            $scope.newCompetition = new $competition.add();
            $scope.newCompetition.name = '';
            $scope.newCompetition.date = new Date();
            ngDialog.open({
                template: "popups/addCompetition.html",
                className: 'ngdialog-theme-default',
                scope: $scope
            });
            $scope.save = function(){
                console.log($scope.newCompetition)
                $scope.newCompetition.$save();
            };
        };
        $scope.editCompetition = function (i) {
            ngDialog.open({
                template: "popups/editCompetition.html?id=" + i,
                className: 'ngdialog-theme-default',
                scope: $scope
            });
        };
        $scope.showServerInfo = function () {
            ngDialog.open({
                template: "popups/serverInfo.html",
                className: 'ngdialog-theme-default',
                scope: $scope
            });
        };
        $scope.servers = [{
                IP: '127.0.0.1',
                CPU: '40%',
                RAM: '8GB/12GB',
                HDD: '200GB/1TB',
                Workspaces: 69
            }, {
                IP: '127.0.0.2',
                CPU: '40%',
                RAM: '8GB/12GB',
                HDD: '200GB/1TB',
                Workspaces: 69
            }, {
                IP: '127.0.0.3',
                CPU: '40%',
                RAM: '8GB/12GB',
                HDD: '200GB/1TB',
                Workspaces: 69
            }];
        $scope.teamSort = 'score';
        $scope.reverseSort = true;
        $scope.teams = [
            {
                name: 'team koekje',
                score: 1337
            },
            {
                name: 'team pannenkoek',
                score: 137
            },
            {
                name: 'team pizza',
                score: 13337
            },
            {
                name: 'team zoute popcorn',
                score: 13337
            },
            {
                name: 'team taart',
                score: 13337
            }
        ];

    }
]);

controllers.controller('competitionViewController', ['$scope', 'competition', '$routeParams', 'ngDialog',
    function ($scope, $competition, $routeParams, ngDialog) {
        $scope.teamSort = 'score';
        $scope.reverseSort = true;
        $scope.description = 'participant';
        $scope.selectedTeam = 0;
        $scope.currentCompetition = {
            name: "selectedCompetitionName",
            date: new Date(),
            status: "not implemented",
            challenges: [{
                    id: 1,
                    name: 'chalName 1',
                    state: 'completed',
                    timeLeft: '00:00',
                    difficulty: 'easy',
                    duration: '1:00',
                    author: {
                        name: 'Luc Kolen',
                        organisation: 'Fontys'
                    },
                    descriptions: {
                        spectator: 'Spectator description',
                        participant: 'Participant description'
                    },
                    hints: [{
                            isReleased: true,
                            releaseTime: '0:50',
                            context: 'hint 1'
                        }, {
                            isReleased: false,
                            releaseTime: '0:30',
                            context: 'hint 2'
                        }, {
                            isReleased: false,
                            releaseTime: '0:10',
                            context: 'hint 3'
                        }]
                }, {
                    id: 2,
                    name: 'chalName 2',
                    state: 'working',
                    timeLeft: '00:01',
                    difficulty: 'hard',
                    duration: '1:00',
                    author: {
                        name: 'Luc Kolen',
                        organisation: 'Fontys'
                    },
                    descriptions: {
                        spectator: 'Spectator description',
                        participant: 'Participant description'
                    },
                    hints: [{
                            isReleased: true,
                            releaseTime: '0:50',
                            context: 'hint 1'
                        }, {
                            isReleased: false,
                            releaseTime: '0:30',
                            context: 'hint 2'
                        }, {
                            isReleased: false,
                            releaseTime: '0:10',
                            context: 'hint 3'
                        }]
                }, {
                    id: 3,
                    name: 'chalName 3',
                    state: 'waiting',
                    timeLeft: '01:00',
                    difficulty: 'hard',
                    duration: '1:00',
                    author: {
                        name: 'Luc Kolen',
                        organisation: 'Fontys'
                    },
                    descriptions: {
                        spectator: 'Spectator description',
                        participant: 'Participant description'
                    },
                    hints: [{
                            isReleased: true,
                            releaseTime: '0:50',
                            context: 'hint 1'
                        }, {
                            isReleased: false,
                            releaseTime: '0:30',
                            context: 'hint 2'
                        }, {
                            isReleased: false,
                            releaseTime: '0:10',
                            context: 'hint 3'
                        }]
                }, {
                    id: 4,
                    name: 'chalName 4',
                    state: 'waiting',
                    timeLeft: '10:00',
                    difficulty: 'hard',
                    duration: '10:00',
                    author: {
                        name: 'Luc Kolen',
                        organisation: 'Fontys'
                    },
                    descriptions: {
                        spectator: 'Spectator description',
                        participant: 'Participant description'
                    },
                    hints: [{
                            isReleased: true,
                            releaseTime: '0:50',
                            context: 'hint 1'
                        }, {
                            isReleased: false,
                            releaseTime: '0:30',
                            context: 'hint 2'
                        }, {
                            isReleased: false,
                            releaseTime: '0:10',
                            context: 'hint 3'
                        }]
                }],
            currentChallenge: 1,
            teams: [
                {
                    name: 'team koekje',
                    score: 1337,
                    members: [
                        'member1',
                        'member2'
                    ]
                },
                {
                    name: 'team pannenkoek',
                    score: 137,
                    members: [
                        'member1',
                        'member3'
                    ]
                },
                {
                    name: 'team pizza',
                    score: 13337,
                    members: [
                        'member2',
                        'member3'
                    ]
                },
                {
                    name: 'team zoute popcorn',
                    score: 13337,
                    members: [
                        'member1',
                        'member2',
                        'member3'
                    ]
                },
                {
                    name: 'team taart',
                    score: 13337,
                    members: [
                        'member2'
                    ]
                }]
        };

        $scope.availableParticipants = [
            'luc',
            'astrid',
            'casper',
            'daan',
            'arno',
            'robin'
        ];

        $scope.startCompetition = function () {
            console.log("start competition");
            $competition.start.save({competitionId: $routeParams.id});
        };

        $scope.editCompetition = function () {
            ngDialog.open({
                template: "popups/editCompetition.html",
                className: 'ngdialog-theme-default',
                scope: $scope
            });
        };

        $scope.editChallenge = function (id) {
            ngDialog.open({
                template: "popups/editChallenge.html?id=" + id,
                className: 'ngdialog-theme-default',
                scope: $scope
            });
        };
        $scope.currentChallenge = {
            name: 'challenge xyz',
            duration: '10:00',
            difficulty: 'hard',
            descriptions: {
                participant: 'Participant description',
                spectator: 'Spectator description'
            },
            hints: [{
                    name: 'hint 1',
                    time: '1:00'
                }, {
                    name: 'hint 2',
                    time: '1:30'
                }, {
                    name: 'hint 3',
                    time: '2:00'
                }]
        };

        $scope.addTeam = function () {
            ngDialog.open({
                template: "popups/addTeam.html",
                className: 'ngdialog-theme-default',
                scope: $scope
            });
        };

        $scope.addMembers = function () {
            ngDialog.open({
                template: "popups/addMember.html",
                className: 'ngdialog-theme-default',
                scope: $scope
            });
        };
    }
]);

controllers.controller('addChallengeController', ['$scope',
    function ($scope) {
        $scope.challenge = {
            name: 'challenge name',
            duration: '10:00',
            author: {
                name: 'henkie',
                organisation: 'ebola 4 life',
                website: 'http://google.com'
            },
            description: {
                spectator: "Spectator description is boring as fuck but for some reason people want to read a description here so I've to type some stuff now.",
                participant: "Guess what? the participant description might be even more boring because those who read it will actually have to do the shit assignment"
            }
        }
    }
]);