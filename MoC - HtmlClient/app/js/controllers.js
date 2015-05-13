/* global angular */

var controllers = angular.module('mocControllers', []);

controllers.controller('loginController', ['$scope', '$cookies',
    function ($scope, $cookies) {
        $scope.logout = function () {
            $.ajax({
                type: "POST",
                url: "http://localhost:8080/MoC-Service/api/user/logout",
                xhrFields: {
                    withCredentials: true
                }
            }).success(function (data) {
                console.log("Logged out succesfully");
                $cookies.user = undefined;
                location.href = "#/login";
            }).error(function (data) {
                console.log("Error while logging out");
                $cookies.user = undefined;
                console.log(data);
            });
        };


        $scope.login = function () {
            $.ajax({
                type: "POST",
                url: "http://localhost:8080/KwebbelBackEnd/api/users/login",
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
                location.href = "#/personal";

            }).error(function (data) {
                console.log("Error while logging in");
                console.log(data);
            });
        };
    }
]);

controllers.controller('demoController', ['$scope', 'user', 'competition', 'team',
    function ($scope, $user, $competition, $team) {
        $scope.createUser = function () {
            console.log("Create User");
            $scope.user.$save(function () {
                loadData();
            });
            $scope.user = new $user();
        };

        $scope.createTeam = function () {
            console.log("Create Team");
            console.log($scope.team);
            console.log($scope.competition.id);
            $scope.team.$save({competitionId: $scope.competition.id}, function () {
                loadData();
            });
            $scope.team = new $team();
        };

        $scope.deleteUser = function (userId) {
            console.log("Delete User");
            $user.delete({userId: userId}, function () {
                loadData();
            });
        };

        $scope.deleteTeam = function (teamId) {
            console.log("Delete Team");
            $team.delete({competitionId: $scope.selected_competition.id, teamId: teamId}, function () {
                loadData();
            });
        };

        loadData = function () {
            console.log("loading data");
            $scope.competitions = $competition.query();
            $scope.users = $user.query();
            if ($scope.selected_competition !== undefined) {
                $scope.teams = $team.query({competitionId: $scope.selected_competition.id});
            }
        };
        
        $scope.refreshTeams = function() {
            if ($scope.selected_competition !== undefined) {
                $scope.teams = $team.query({competitionId: $scope.selected_competition.id});
            }
        }

        loadData();
        $scope.team = new $team();
        $scope.user = new $user();
        $scope.user.email = "robin@robin.nl";
        $scope.user.password = "welkom123";
        $scope.user.username = "Memphizx";
        $scope.user.name = "Robin van der Avoort";
        $scope.user.organisation = "Fontys";
    }
]);

controllers.controller('competitionController', ['$scope', 'competition',
    function ($scope, $competition) {
        $scope.createCompetition = function () {
            console.log("Create Competition");
            $scope.competition.competitionDate = new Date($scope.competitionDate);
            $scope.competition.startTime = new Date($scope.competitionDate + " " + $scope.startTime);
            $scope.competition.endTime = new Date($scope.competitionDate + " " + $scope.endTime);
            $scope.competition.$save(function () {
                loadData();
            });
            $scope.competition = new $competition();
        };

        $scope.deleteCompetition = function (competitionId) {
            console.log("Delete Competition: " + competitionId);
            $competition.delete({competitionId: competitionId}, function () {
                loadData();
            });
        };

        loadData = function () {
            $scope.competitions = $competition.query();
        };
        loadData();
        $scope.competition = new $competition();
        $scope.competition.name = "TestCompetition";
        $scope.competitionDate = "2015-04-23";
        $scope.startTime = "13:37";
        $scope.endTime = "17:00";
        $scope.competition.location = "CookieJar - Fontys";
    }
]);
     