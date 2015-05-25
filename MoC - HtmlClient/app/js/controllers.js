/* global angular */

var controllers = angular.module('mocControllers', []);

controllers.controller('loginController', ['$scope', function ($scope) {

        $scope.login = function () {
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
                //$cookies.user = $scope.username;
                location.href = "#/competitions";

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
                //$cookies.user = undefined;
                location.href = "#/login";
            }).error(function (data) {
                console.log("Error while logging out");
                //$cookies.user = undefined;
                console.log(data);
            });
        };
    }
]);

controllers.controller('registerController', ['$scope', 'user',
    function ($scope, $user) {
        $scope.register = function () {
            console.log("Create User");
            $scope.user.$save(function () {
                loadData();
            });
            $scope.user = new $user();
            
            location.href = "#/login";
        };
        
        $scope.user = new $user();
        $scope.user.email = "robin@robin.nl";
        $scope.user.password = "welkom123";
        $scope.user.username = "Memphizx";
        $scope.user.name = "Robin van der Avoort";
        $scope.user.organisation = "Fontys";
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

        $scope.refreshTeams = function () {
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


        var ws = new WebSocket('ws://localhost:8080/MoC-Service/ws/api');
        ws.onopen = function () {
            console.log("opening ws connection");
        };
        ws.onmessage = function (msg) {
            console.log(msg);
        };
    }
]);

controllers.controller('competitionsController', ['$scope', 'competition',
    function ($scope, $competition) {
        $scope.selectCompetition = function (id) {
            console.log("Select competition with id: " + id);
            $scope.competition = $competition.get({competitionId: id});
        };

        $scope.isSelected = function (competitionId) {
            return $scope.competition.id === competitionId;
        };

        loadData = function () {
            $scope.competitions = $competition.query();

            //TODO: Get the first competition in the list (don't use hard-coded id)
            $scope.competition = $competition.get({competitionId: 4});
        };

        loadData();
    }
]);

controllers.controller('teamsController', ['$scope', 'user', 'team',
    function ($scope, $user, $team) {

        $scope.selectTeam = function (competitionId, teamId) {
            console.log("Select team with id: " + teamId);
            $scope.team = $team.get({competitionId: competitionId, teamId: teamId});
        };

        $scope.isSelected = function (teamId) {
            return $scope.team.id === teamId;
        };

        loadData = function () {
            $scope.user = $user.get({userId: 'Strike'});

            //TODO: Get the first team in the list (don't use hard-coded id)
            $scope.team = $team.get({competitionId: 1, teamId: 1});
        };

        loadData();
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
     