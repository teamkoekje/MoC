/* global angular */

var controllers = angular.module('mocControllers', ['ngCookies']);
controllers.controller('loginController', ['$scope', '$cookies', 'user', function ($scope, $cookies, $user) {

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
        $scope.isAdmin();

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
                $cookies.user = $scope.username;
                location.href = "#/demo";

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
        $scope.register = function () {
            console.log("Create User");
            $scope.user.$save(function () {
                $scope.showSuccesAlert = true;
                $scope.user = new $user.register();
                setTimeout(function () {
                    location.href = "#/login";
                }, 3000);
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
        $scope.user = new $user.register({token: $routeParams.token});
        $scope.user.email = "luc@luc.nl";
        $scope.user.password = "luc";
        $scope.user.username = "luc";
        $scope.user.name = "Luc Kolen";
        $scope.user.organisation = "FHICT";
    }
]);

/**
 * Get the parameters from the URL and put them in a map
 * @returns Map with URL parameters
 */
window.params = function () {
    var result = {};
    var searchString = location.search.substring(1, location.search.length);
    var pairs = searchString.split("&");
    for (var i in pairs) {
        var pair = pairs[i].split("=");
        result[pair[0]] = pair[1];
    }
    return result;
};

controllers.controller('competitionOverviewController', ['$scope',
    function ($scope) {
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
                name: 'fuck deze proftaak',
                score: 13337
            },
            {
                name: 'fuck deze proftaak 2',
                score: 13337
            },
            {
                name: 'waarom is angular kut?',
                score: 13337
            }
        ];
    }
]);