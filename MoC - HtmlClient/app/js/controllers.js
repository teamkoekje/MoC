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

controllers.controller('competitionsController', ['$scope', 'competition', 'competitions',
    function ($scope, $competition, $competitions) {
        $scope.selectCompetition = function (id) {
            console.log("Select competition with id: " + id);
            $scope.competition = $competition.get({competitionId: id});
        };

        $scope.isSelected = function (competitionId) {
            return $scope.competition.id === competitionId;
        };

        loadData = function () {
            $scope.competitions = $competition.query();
            $scope.activeCompetitions = $competitions.query({type: "active"});
            $scope.futureCompetitions = $competitions.query({type: "future"});

            //TODO: Get the first competition in the list (don't use hard-coded id)
            $scope.competition = $competition.get({competitionId: 4});
        };

        loadData();
    }
]);

controllers.controller('teamsController', ['$scope', 'team',
    function ($scope, $team) {

        $scope.selectTeam = function (competitionId, teamId) {
            console.log("Select team with id: " + teamId);
            $scope.team = $team.all.get({competitionId: competitionId, teamId: teamId});
            $scope.participants = $team.participants.query({teamId: teamId});
        };

        $scope.isSelected = function (teamId) {
            return $scope.team.id === teamId;
        };

        loadData = function () {
            //$scope.user = $user.get({userId: 'Strike'});
            $scope.teams = $team.byUser.query({userId: 'Strike'});

            //TODO: Get the first team in the list (don't use hard-coded id)
            $scope.team = $team.all.get({competitionId: 1, teamId: 1});
        };

        loadData();
    }
]);

controllers.controller('newTeamController', ['$scope', 'competition', 'team',
    function ($scope, $competition, $team) {

        $scope.createTeam = function () {
            console.log("Create Team");
            console.log($scope.team);
            console.log($scope.team.competition.id);
            $scope.team.$save({competitionId: $scope.team.competition.id}, function () {
                location.href = "#/teams";
            });
            $scope.team = new $team();
        };

        loadData = function () {
            $scope.competitions = $competition.query();
            $scope.team = new $team();
        };

        loadData();
    }
]);