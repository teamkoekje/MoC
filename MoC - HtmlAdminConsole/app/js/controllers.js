/* global angular */

var controllers = angular.module('mocControllers', ['ngCookies']);
controllers.controller('loginController', ['$scope', '$translate', '$cookies', 'user', function ($scope, $translate, $cookies, $user) {
        $scope.changeLanguage = function (langKey) {
            $translate.use(langKey);
            $cookies.language = langKey;
        };
        $scope.changeLanguage($cookies.language);

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
            $scope.selectedCompetition.challenges = $competition.challenges.query({competitionId: competition.id});
            $scope.selectedCompetition.teams = $competition.teams.query({competitionId: competition.id});
        };

        $scope.removeCompetition = function (competition) {
            if (confirm("Are you sure you want to remove " + competition.name + "?")) {
                $competition.all.remove({competitionId: competition.id}, function () {
                    $scope.competitions = new $competition.all.query();
                });

            }
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
            $scope.newCompetition.competitionDate = new Date();
            $scope.newCompetition.endTime = new Date();
            $scope.newCompetition.location = '';
            ngDialog.open({
                template: "popups/addCompetition.html",
                className: 'ngdialog-theme-default',
                scope: $scope
            });
            $scope.save = function () {
                console.log($scope.newCompetition);
                $scope.newCompetition.$save(function () {
                    $scope.competitions = new $competition.all.query();
                });
            };
        };
        $scope.editCompetition = function (i) {
            ngDialog.open({
                template: "popups/editCompetition.html",
                className: 'ngdialog-theme-default',
                scope: $scope
            });
            $scope.currentCompetition = $competition.all.get({competitionId: i}, function () {
                $scope.currentCompetition.challenges = $competition.challenges.query({competitionId: i});
                $scope.currentCompetition.teams = $competition.teams.query({competitionId: i});
            });
            console.log($scope.currentCompetition);
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
        $scope.currentCompetition = $competition.all.get({competitionId: $routeParams.id}, function () {
            $scope.currentCompetition.challenges = $competition.challenges.query({competitionId: $routeParams.id});
            $scope.currentCompetition.currentChallenge = 0;
            $scope.currentCompetition.teams = $competition.teams.query({competitionId: $routeParams.id});
            console.log($scope.currentCompetition);
        });

        $scope.startCompetition = function () {
            console.log("start competition");
            $competition.start.save({competitionId: $routeParams.id});
        };

        $scope.pauseCompetition = function () {
            console.log("pause competition");
            $competition.pause.save({competitionId: $routeParams.id});
        };

        $scope.freezeCompetition = function () {
            console.log("freeze competition");
            $competition.freeze.save({competitionId: $routeParams.id});
        };

        $scope.stopCompetition = function () {
            console.log("stop competition");
            $competition.stop.save({competitionId: $routeParams.id});
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