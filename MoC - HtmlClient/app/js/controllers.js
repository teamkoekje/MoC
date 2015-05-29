/* global angular */

var controllers = angular.module('mocControllers', ['ngCookies']);
controllers.controller('loginController', ['$scope', '$cookies', function ($scope, $cookies) {

        $scope.isLoggedIn = function() {
            return $cookies.user;
        }

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
    }
]);
controllers.controller('registerController', ['$scope', '$routeParams', 'user',
    function ($scope, $routeParams, $user) {
        $scope.register = function () {
            console.log("Create User");
            $scope.user.$save(function () {
                location.href = "#/login";
            });
        };
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
controllers.controller('teamsController', ['$scope', 'team', 'user',
    function ($scope, $team, $user) {

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
            $scope.invitations = $user.invitations.query({userId: 'Strike'});
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
controllers.controller('inviteUserController', ['$scope', 'email', 'team',
    function ($scope, $email, $team) {

        $scope.inviteUser = function () {
            $.ajax({
                type: "POST",
                url: "http://localhost:8080/MoC-Service/api/team/" + $team + "/invite",
                data: {
                    email: $email
                },
                xhrFields: {
                    withCredentials: true
                }
            }).success(function (data) {
                console.log("invited succesfully");
                //$cookies.user = undefined;
                location.href = "#/login";
            }).error(function (data) {
                console.log("Error while inviting");
                //$cookies.user = undefined;
                console.log(data);
            });
        }
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