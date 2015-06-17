/* global angular */

var controllers = angular.module('mocControllers', ['ngCookies']);

controllers.service('newsfeedService', function () {
    var messages = [];
    var hints = [];
    var files = [];

    var onMessage;

    //Subscribe
    var subscribe = function (callback) {
        onMessage = callback;
    };

    var sendMessage = function (msg) {
        onMessage(msg);
    };

    // Messages
    var addMessage = function (message) {
        messages.push(message);
    };

    var clearMessages = function () {
        messages = [];
    };

    var getMessages = function () {
        return messages;
    };

    // Hints
    var addHint = function (hint) {
        hints.push(hint);
    };

    var clearHints = function () {
        hints = [];
    };

    var getHints = function () {
        return hints;
    };

    return {
        addMessage: addMessage,
        clearMessages: clearMessages,
        getMessages: getMessages,
        addHint: addHint,
        clearHints: clearHints,
        getHints: getHints,
        subscribe: subscribe,
        sendMessage: sendMessage
    };

});

controllers.controller('mainController', ['$scope', '$rootScope', '$translate', 'user', 'newsfeedService', '$cookies',
    function ($scope, $rootScope, $translate, $user, newsfeedService, $cookies) {
        $rootScope.loading = false;
        $scope.changeLanguage = function (langKey) {
            $translate.use(langKey);
            $cookies.language = langKey;
        };
        $scope.changeLanguage($cookies.language);

        /**
         * If a user is logged in, returns username, else returns undefined
         * 
         * @returns {String} username
         */
        $scope.isLoggedIn = function () {
            console.log($scope.user);
            return $scope.user.username !== undefined;
        };

        /**
         * Logs in with username and password
         * If login is successful, a websocket is created and the user is redirected to the competitions page
         */
        $scope.login = function (username, password) {
            $rootScope.loading = true;
            $.ajax({
                type: "POST",
                url: "http://localhost:8080/MoC-Service/api/user/login",
                data: {
                    username: username,
                    password: password
                },
                xhrFields: {
                    withCredentials: true
                }
            }).success(function (data) {
                $rootScope.loading = false;
                console.log("Logged in succesfully: " + username);
                location.href = "#/competitions";
                location.reload();
            }).error(function (data) {
                $rootScope.loading = false;
                console.log("Error while logging in");
                console.log(data);
            });
        };

        /**
         * Logs out the current user and, if successful, sends the user to the login page
         */
        $scope.logout = function () {
            $rootScope.loading = true;
            $.ajax({
                type: "POST",
                url: "http://localhost:8080/MoC-Service/api/user/logout",
                xhrFields: {
                    withCredentials: true
                }
            }).success(function (data) {
                $rootScope.loading = false;
                console.log("Logged out succesfully");
                location.href = "#/login";
                location.reload();
            }).error(function (data) {
                $rootScope.loading = false;
                console.log("Error while logging out");
                console.log(data);
            });
        };

        /**
         * Creates a websocket
         */
        openWebsocket = function () {
            var ws = new WebSocket('ws://localhost:8080/MoC-Service/ws/api');

            ws.onopen = function () {
                console.log("opening ws connection");
            };
            ws.onmessage = function (msg) {
                console.log(msg);
                var result = $.parseJSON(msg.data);
                console.log(result);
                if (typeof result.hint !== 'undefined') {
                    newsfeedService.addHint(result.hint.text);
                } else if (typeof result.message !== 'undefined') {
                    newsfeedService.addMessage(result.message.text);
                } else {
                    newsfeedService.sendMessage(result);
                }
            };

            ws.onclose = function () {
                console.log("closing websocket");
                return false;
            };
        };

        openWebsocket();

        $scope.messages = [];
        $scope.hints = [];
        $scope.user = $user.authenticated.get(function () {
            console.log($scope.user);
            $scope.isLoggedIn = $scope.user.username !== undefined;
        });
        // TODO: Remove test data
        newsfeedService.addMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque fermentum, tortor et commodo scelerisque, justo sapien elementum lacus, sed mollis lacus turpis quis mauris");
        newsfeedService.addMessage("Aenean lacus quam, placerat in mi vel, interdum pellentesque nisl. Cras tincidunt cursus eros, vel fermentum lectus fringilla vitae. Donec eget neque faucibus, bibendum orci vel, porta metus. Aliquam odio orci, auctor nec dictum quis, molestie a nisi. Maecenas vitae erat eu sapien fringilla pellentesque eu id velit. Mauris quis mauris tempus, tempor ex et, pharetra justo. Vivamus varius fringilla mauris");
        newsfeedService.addMessage("Fusce ac neque elementum, pharetra ");
        newsfeedService.addMessage("ro nec libero vehicula, in cursus nunc ultrices. Ut turpis metus, porttitor et augue ultricies, imperdiet facilisis est. Etiam molestie sed metus sit amet accumsan. Mauris gravida ultricies molestie. Quisque metus lacus, pharetra eget cursus sed, aliquam quis er");
        newsfeedService.addMessage("ro nec libero vehicula, in cursus nasdfaltrices. Ut turpis metus, porttitor et augue ultricies, imperdiet facilisis est. Etiam molestie sed metus sit amet accumsan. Mauris gravida ultricies molestie. Quisque metus lacus, pharetra eget cursus sed, aliquam quis er");
        newsfeedService.addMessage("ro nec libero vehicula, in cursus aaaUt turpis metus, porttitor et augue ultricies, imperdiet facilisis est. Etiam molestie sed metus sit amet accumsan. Mauris gravida ultricies molestie. Quisque metus lacus, pharetra eget cursus sed, aliquam quis er");
        newsfeedService.addMessage("ro nec libero vehicula, in cursus nungfdsgrttitor et augue ultricies, imperdiet facilisis est. Etiam molestie sed metus sit amet accumsan. Mauris gravida ultricies molestie. Quisque metus lacus, pharetra eget cursus sed, aliquam quis er");
        newsfeedService.addHint("ro nec libero vehicula, in cursus nunc ultrices. Ut turpis metus, porttitor et augue ultricies, imperdiet facilisis est. Etiam molestie sed metus sit amet accumsan. Mauris gravida ultricies molestie. Quisque metus lacus, pharetra eget cursus sed, aliquam quis er");
        newsfeedService.addHint("ro nec libero vehicula, in cursus nasdfaltrices. Ut turpis metus, porttitor et augue ultricies, imperdiet facilisis est. Etiam molestie sed metus sit amet accumsan. Mauris gravida ultricies molestie. Quisque metus lacus, pharetra eget cursus sed, aliquam quis er");
        newsfeedService.addHint("ro nec libero vehicula, in cursus aaaUt turpis metus, porttitor et augue ultricies, imperdiet facilisis est. Etiam molestie sed metus sit amet accumsan. Mauris gravida ultricies molestie. Quisque metus lacus, pharetra eget cursus sed, aliquam quis er");
        newsfeedService.addHint("ro nec libero vehicula, in cursus nungfdsgrttitor et augue ultricies, imperdiet facilisis est. Etiam molestie sed metus sit amet accumsan. Mauris gravida ultricies molestie. Quisque metus lacus, pharetra eget cursus sed, aliquam quis er");
    }

]);
controllers.controller('registerController', ['$scope', '$rootScope', '$routeParams', 'user', 'team',
    function ($scope, $rootScope, $routeParams, $user, $team) {
        /**
         * Registers a user
         */
        $scope.register = function () {
            console.log("Create User");
            $rootScope.loading = true;
            $scope.user.$save(function () {
                $rootScope.loading = false;
                $scope.showSuccesAlert = true;
                $scope.showFailedAlert = false;
                $scope.user = new $user.register();
                setTimeout(function () {
                    location.href = "#/login";
                }, 3000);
            }, function (data) {
                console.log(data);
                $rootScope.loading = false;
                $scope.showFailedAlert = true;
                $scope.showSuccesAlert = false;
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

controllers.controller('competitionsController', ['$scope', '$rootScope', 'competition',
    function ($scope, $rootScope, $competition) {
        /**
         * Load data of comptetition using id
         * @param {int} id
         */
        $scope.selectCompetition = function (id) {
            console.log("Select competition with id: " + id);
            $scope.competition = $competition.all.get({competitionId: id});
            $scope.challenges = $competition.challenges.query({competitionId: id});
            $scope.teams = $competition.teams.query({competitionId: id});
        };

        /**
         * Checks if the selected competition is the same as the loaded competition
         * @param {int} competitionId
         * @returns {Boolean}
         */
        $scope.isSelected = function (competitionId) {
            return $scope.competition.id === competitionId;
        };

        /**
         * Checks if the given competition is active
         * @param {int} competitionId
         * @returns {Boolean}
         */
        $scope.isActive = function (competitionId) {
            for (i = 0; i < $scope.activeCompetitions.length; i++) {
                if ($scope.activeCompetitions[i].id === competitionId) {
                    return true;
                }
            }
            return false;
        };

        /**
         * Loads all active and future competitions
         */
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
controllers.controller('teamsController', ['$scope', '$rootScope', '$cookies', 'team', 'user',
    function ($scope, $rootScope, $cookies, $team, $user) {
        /**
         * Load team and participants using teamId
         * @param {int} teamId
         */
        $scope.selectTeam = function (teamId) {
            $scope.team = $team.all.get({teamId: teamId});
            $scope.participants = $team.participants.query({teamId: teamId});
            $scope.isInvitation = false;
            $scope.invitedParticipants = $team.invitedParticipants.query({teamId: teamId});
        };

        /**
         * Selects given invitation and gets all participants of the team
         * @param {Invitation} invitation
         */
        $scope.selectInvitation = function (invitation) {
            $scope.invitation = invitation;
            $scope.team = invitation.team;
            $scope.participants = $team.participants.query({teamId: invitation.team.id});
            $scope.isInvitation = true;
        };

        /**
         * Checks if the selected team is the same as the loaded team
         * @param {int} teamId
         * @returns {Boolean}
         */
        $scope.isSelected = function (teamId) {
            return $scope.team.id === teamId;
        };

        /**
         * Remove the given user from his/her team
         * @param {User} user
         */
        $scope.leaveTeam = function (user) {
            new $team.leaveTeam({teamId: $scope.team.id, username: user.username}).$save(function () {
                $scope.participants = $team.participants.query({teamId: $scope.team.id});
            });

        };

        /**
         * Accepts the given invitation
         * @param {Invitation} invitation
         */
        $scope.acceptInvitation = function (invitation) {
            console.log("accept: " + invitation.id);
            $team.acceptInvitation.save({invitationId: invitation.id}, function () {
                console.log("invite accepted");
                loadData();
            }, function (data) {
                console.log("error accepting invite");

            });
        };

        /**
         * Declines the given invitation
         * @param {Invitation} invitation
         */
        $scope.declineInvitation = function (invitation) {
            console.log("decline: " + invitation.id);
            $team.declineInvitation.save({invitationId: invitation.id}, function () {
                console.log("invite declined");
                loadData();
            }, function (data) {
                console.log("error declining invite");

            });
        };

        /**
         * Checks if the team owner is logged in
         * @param {String} teamOwner
         * @returns {Boolean}
         */
        $scope.isOwnerLoggedIn = function (teamOwner) {
            return $scope.user.username === teamOwner;
        };

        /**
         * Loads team and invitation data
         */
        loadData = function () {
            $scope.teams = $team.myTeams.query(function () {
                $scope.selectTeam($scope.teams[0].id);
            });
            $scope.invitations = $team.myTeamInvitations.query();
        };
        loadData();
    }
]);
controllers.controller('newTeamController', ['$scope', '$rootScope', 'competition', 'team',
    function ($scope, $rootScope, $competition, $team) {
        /**
         * Creates new team and redirects to team page if successful
         */
        $scope.createTeam = function () {
            console.log("Create Team");
            console.log($scope.team);
            console.log($scope.team.competition.id);
            $rootScope.loading = true;
            $scope.team.$save(function () {
                location.href = "#/teams";
            });
            $scope.team = new $team.all();
            $rootScope.loading = false;
        };

        /**
         * Loads competition and team data
         */
        loadData = function () {
            $scope.competitions = $competition.all.query();
            $scope.team = new $team.all();
        };
        loadData();
    }
]);
controllers.controller('inviteUserController', ['$scope', '$rootScope', 'team', 'user', '$routeParams',
    function ($scope, $rootScope, $team, $user, $routeParams) {
        /**
         * Creates an invite for the given email address
         */
        $scope.inviteUser = function () {
            // TODO: replace following line using Angular (instead of jQuery)
            $rootScope.loading = true;
            console.log($scope.loading)
            var email = $("#emailInput").val();
            $team.invite.save({teamId: $routeParams.teamid}, email, function () {
                $scope.showSuccesAlert = true;
                $scope.showFailedAlert = false;
                $rootScope.loading = false;
                console.log($scope.loading)
            }, function (data) {
                $rootScope.loading = false;
                $scope.showFailedAlert = true;
                $scope.showSuccesAlert = false;
                $scope.error = data.data;
                console.log($scope.loading)
            });
        };

        $scope.searchUser = function () {
            var searchInput = $("#searchInput").val();
            $rootScope.loading = true;
            $scope.foundUsers = $user.search.query({searchInput: searchInput});
            $rootScope.loading = false;
            console.log($scope.foundUsers);
        };


        $scope.inviteExistingUser = function () {
            var email = $("#foundUserInput").val();
            $rootScope.loading = true;
            $team.invite.save({teamId: $routeParams.teamid}, email, function () {
                $scope.showSuccesAlert = true;
            }, function (data) {
                $rootScope.loading = false;
                $scope.showFailedAlert = true;
                $scope.error = data.data;
            });
        };
        loadData = function () {
            $scope.foundUsers = $user.search.query({searchInput: ""});
        };
        loadData();
    }
]);
controllers.controller('competitionController', ['$scope', '$sce', '$rootScope', 'workspace', '$routeParams', 'newsfeedService',
    function ($scope, $sce, $rootScope, $workspace, $routeParams, newsfeedService) {

        //http://ace.c9.io/#nav=howto
        var editor;
        /**
         * Create the ace editor
         * @param {String} editorID The element ID of the element that becomes the ace editor
         */
        initEditor = function (editorID) {
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
                            //editor.setTheme("ace/theme/monokai");
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
        };

        /**
         * Gets the text from the editor
         * @returns {String}
         */
        $scope.getTextFromEditor = function getTextFromEditor() {
            return editor.session.getValue();
        };

        $scope.setTextFromEditor = function setTextFromEditor(text) {
            editor.session.setValue(text);
        };

        /**
         * Gets selected text from the editor
         * @returns {String}
         */
        $scope.getSelectionFromEditor = function getSelectionFromEditor() {
            return editor.session.getTextRange(editor.getSelectionRange());
        };

        /**
         * Sets the given HTML element to fullscreen
         * @param {HTML Element} elem
         */
        fullScreenElement = function (elem) {
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
        };

        /**
         * Sets the style and sets the editor to fullscreen
         */
        $scope.fullScreenEditor = function fullScreenEditor() {
            //Hide the side panels
            $('#wrapperMessages').hide();
            $('#wrapperAroundResults').hide();
            //Make sure the editor will use full width
            $('#wrapperAroundEditor').addClass('col-xs-12').removeClass('col-xs-6');
            //Set the editor full screen
            fullScreenElement($('#wrapperAroundEditor')[0]);
        };

        /**
         * Shows or hides the messages block
         */
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
            $('#wrapperHints').hide();
            editor.resize();
        };

        /**
         * Shows or hides the hints block
         */
        $scope.toggleHints = function toggleHints() {
            if ($('#wrapperHints').is(":visible")) {
                $('#wrapperAroundEditor').addClass('col-xs-12').removeClass('col-xs-6');
                $('#wrapperHints').hide();
            } else {
                $('#wrapperAroundEditor').addClass('col-xs-6').removeClass('col-xs-12');
                $('#wrapperHints').show();
            }
            $('#wrapperAroundResults').hide();
            $('#wrapperTests').hide();
            $('#wrapperMessages').hide();
            editor.resize();
        };

        /**
         * Shows or hides the tests block
         */
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
        };

        /**
         * Shows or hides the results block
         */
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
        };

        /**
         * Saves the file to the workspace
         * @param {Function} onSucces
         */
        save = function (onSucces) {
            var file = new $workspace.update({competitionId: $routeParams.id});
            file.fileContent = $scope.getTextFromEditor();
            //TODO: Get path of current file
            file.filePath = "";
            console.log("content: " + file.fileContent);
            file.$save(onSucces, function (data) {
                console.log(data.data);
            });
        };

        /**
         * Sends compile request
         */
        $scope.compile = function () {
            $rootScope.loading = true;
            var file = new $workspace.compile({competitionId: $routeParams.id});
            file.fileContent = $scope.getTextFromEditor();
            file.filePath = $scope.file.filepath;

            file.$save(function (data) {
                console.log(data.data);
            });
        };

        /**
         * Sends test all request
         */
        $scope.testAll = function () {
            save(function () {
                new $workspace.test({competitionId: $routeParams.id}).$save();
            });
        };

        /**
         * Sends test request
         * @param {String} testFile
         * @param {String} testName
         */
        $scope.test = function (testFile, testName) {
            save(function () {
                new $workspace.test({competitionId: $routeParams.id, testFile: testFile, testName: testName}).$save();
            });
        };

        $scope.selectFile = function (file) {
            $rootScope.loading = true;

            //Save previous selected file
            if ($scope.file !== undefined) {
                var f = new $workspace.update({competitionId: $routeParams.id});
                f.filePath = $scope.file.filepath;
                f.fileContent = $scope.getTextFromEditor();
                f.$save(function (data) {
                    //TODO: SYNTAXERROR UNEXPECTED TOKEN F
                    //console.log(data);
                });
            }

            $scope.file = file;
            editor.setReadOnly(!file.editable);

            //Get file content
            $workspace.file.save({competitionId: $routeParams.id}, file.filepath);
        };

        $scope.isSelected = function (file) {
            return $scope.file.filename === file.filename;
        };

        //Init editor
        initEditor("editor");

        //Subscribe to newsfeed
        newsfeedService.subscribe(function (msg) {
            console.log("hallllooo");
            console.log(msg.type);
            console.log(msg.data);
            switch (msg.type) {
                case "filestructure":
                    $scope.files = msg.data;
                    $scope.selectFile($scope.files[0]);
                    break;
                case "file":
                    $scope.file.filecontent = msg.data.filecontent;
                    $scope.setTextFromEditor(msg.data.filecontent);
                    $rootScope.loading = false;
                    break;
                case "buildresult":
                    console.log("build result");
                    $rootScope.loading = false;
                    //$scope.result = $sce.trustAsHtml(msg.data);
                    $scope.results.push($sce.trustAsHtml(msg.data));
                    break;
            }
            $scope.$apply();
        });

        //Request folderstructure
        $workspace.folderStructure.save({competitionId: $routeParams.id});

        $scope.results = [];
        $scope.messages = newsfeedService.getMessages();
        $scope.hints = newsfeedService.getHints();
    }
]);