/* global angular */

var controllers = angular.module('mocControllers', ['ngCookies']);
controllers.controller('loginController', ['$scope', '$cookies', function ($scope, $cookies) {

        $scope.isLoggedIn = function () {
            return $cookies.user;
        };

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
                location.href = "#/competitions";

                var ws = new WebSocket('ws://localhost:8080/MoC-Service/ws/api');
                ws.onopen = function () {
                    console.log("opening ws connection");
                };
                ws.onmessage = function (msg) {
                    var result = $.parseJSON(msg.data);
                    console.log(result);
                    // TODO: Handle the json result
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
    }
]);
controllers.controller('competitionsController', ['$scope', 'competition',
    function ($scope, $competition) {
        $scope.selectCompetition = function (id) {
            console.log("Select competition with id: " + id);
            $scope.competition = $competition.all.get({competitionId: id});
            $scope.challenges = $competition.challenges.query({competitionId: id});
            $scope.teams = $competition.teams.query({competitionId: id});
        };
        $scope.isSelected = function (competitionId) {
            return $scope.competition.id === competitionId;
        };
        loadData = function () {
            $scope.activeCompetitions = $competition.active.query(function () {
                if (!$scope.competition && $scope.activeCompetitions.length > 0) {
                    $scope.selectCompetition($scope.activeCompetitions[0].id);
                }
            });
            $scope.futureCompetitions = $competition.future.query(function () {
                if (!$scope.competition && $scope.futureCompetitions.length > 0) {
                    $scope.selectCompetition($scope.futureCompetitions[0].id);
                }
            });
        };
        loadData();
    }
]);
controllers.controller('teamsController', ['$scope', '$cookies', 'team', 'user',
    function ($scope, $cookies, $team, $user) {

        $scope.selectTeam = function (teamId) {
            console.log("Select team with id: " + teamId);
            $scope.team = $team.all.get({teamId: teamId});
            $scope.participants = $team.participants.query({teamId: teamId});
        };
        $scope.isSelected = function (teamId) {
            return $scope.team.id === teamId;
        };
        loadData = function () {
            $scope.teams = $team.myTeams.query(function () {
                $scope.selectTeam($scope.teams[0].id);
            });
            //TODO: Don't use hardcoded user
            $scope.invitations = $user.invitations.query({userId: $cookies.user});
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
            $scope.team.$save(function () {
                location.href = "#/teams";
            });
            $scope.team = new $team.all();
        };
        loadData = function () {
            $scope.competitions = $competition.all.query();
            $scope.team = new $team.all();
        };
        loadData();
    }
]);
controllers.controller('inviteUserController', ['$scope', 'team',
    function ($scope, $team) {

        $scope.inviteUser = function () {
            $.ajax({
                type: "POST",
                contentType: 'application/json; charset=UTF-8',
                url: "http://localhost:8080/MoC-Service/api/team/" + window.params().teamid + "/invite",
                data: $("#emailInput").val(),
                xhrFields: {
                    withCredentials: true
                }
            }).success(function (data) {
                console.log($("#emailInput").val());
                console.log("invited succesfully");
                //$cookies.user = undefined;
                location.href = "#/newteammember";
            }).error(function (data) {
                console.log("Error while inviting");
                //$cookies.user = undefined;
                console.log(data);
            });
        };



        loadData = function () {
            $scope.teams = new $team.myTeams.query();
        };
        loadData();

    }
]);


controllers.controller('competitionController', ['$scope', function ($scope) {
        //http://ace.c9.io/#nav=howto
        var editor;
        $(window).ready(function () {
            initEditor("editor");
        });
        /**
         * Create the ace editor
         * @param {String} editorID The element ID of the element that becomes the ace editor
         */
        function initEditor(editorID) {
            // trigger extension
            editor = ace.edit(editorID);
            // we're using java
            editor.session.setMode("ace/mode/java");
            // light theme
            editor.setTheme("ace/theme/tomorrow");
            // dark theme
            // editor.setTheme("ace/theme/monokai");
            // ugly vertical line has to be removed
            editor.setShowPrintMargin(false);
            // false to make it editable
            editor.setReadOnly(false);
            //to fix warning (was shown in console to set to infinity) also infinity is a real global JS variable
            editor.$blockScrolling = Infinity;
            // enable autocompletion and snippets
            editor.setOptions({
                enableBasicAutocompletion: true,
                enableSnippets: true,
                enableLiveAutocompletion: false
            });
            // handle going fullscreen
            $("#wrapperAroundEditor").on(
                    'webkitfullscreenchange mozfullscreenchange fullscreenchange',
                    function () {
                        var isFullScreen = document.fullScreen || document.mozFullScreen || document.webkitIsFullScreen;
                        if (isFullScreen) {
                            editor.setTheme("ace/theme/monokai");
                            $("#wrapperAroundEditor").css({
                                paddingRight: '0px',
                                paddingLeft: '0px'
                            });
                        } else {
                            editor.setTheme("ace/theme/tomorrow");
                            $("#wrapperAroundEditor").css({
                                paddingRight: '15px',
                                paddingLeft: '15px'
                            });
                        }
                        editor.resize();
                    });
        }

        $scope.getTextFromEditor = function getTextFromEditor() {
            return editor.getSession.getValue();
        };
        $scope.getSelectionFromEditor = function getSelectionFromEditor() {
            return editor.session.getTextRange(editor.getSelectionRange());
        };
        function fullScreenElement(elem) {
            if (elem.requestFullscreen) {
                elem.requestFullscreen();
            } else if (elem.msRequestFullscreen) {
                elem.msRequestFullscreen();
            } else if (elem.mozRequestFullScreen) {
                elem.mozRequestFullScreen();
            } else if (elem.webkitRequestFullscreen) {
                elem.webkitRequestFullscreen();
            } else {
                alert("Full screen is not supported, recommended webbrowser to use is Google Chrome");
            }
        }

        $scope.fullScreenEditor = function fullScreenEditor() {
            //Hide the side panels
            $('#wrapperMessages').hide();
            $('#wrapperAroundResults').hide();
            //Make sure the editor will use full width
            $('#wrapperAroundEditor').addClass('col-xs-12').removeClass('col-xs-6');
            //Set the editor full screen
            fullScreenElement($('#wrapperAroundEditor')[0]);
        }

        $scope.toggleMessages = function toggleMessages() {
            if ($('#wrapperMessages').is(":visible")) {
                $('#wrapperAroundEditor').addClass('col-xs-12').removeClass('col-xs-6');
                $('#wrapperMessages').hide();
            } else {
                $('#wrapperAroundEditor').addClass('col-xs-6').removeClass('col-xs-12');
                $('#wrapperMessages').show();
            }
            $('#wrapperAroundResults').hide();
            $('#wrapperTests').hide();
            editor.resize();
        }

        $scope.toggleTests = function toggleTests() {
            if ($('#wrapperTests').is(":visible")) {
                $('#wrapperAroundEditor').addClass('col-xs-12').removeClass('col-xs-6');
                $('#wrapperTests').hide();
            } else {
                $('#wrapperAroundEditor').addClass('col-xs-6').removeClass('col-xs-12');
                $('#wrapperTests').show();
            }
            $('#wrapperAroundResults').hide();
            $('#wrapperMessages').hide();
            editor.resize();
        }

        $scope.toggleResults = function toggleResults() {
            if ($('#wrapperAroundResults').is(":visible")) {
                $('#wrapperAroundEditor').addClass('col-xs-12').removeClass('col-xs-6');
                $('#wrapperAroundResults').hide();
            } else {
                $('#wrapperAroundEditor').addClass('col-xs-6').removeClass('col-xs-12');
                $('#wrapperAroundResults').show();
            }
            $('#wrapperMessages').hide();
            $('#wrapperTests').hide();
            editor.resize();
        }
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