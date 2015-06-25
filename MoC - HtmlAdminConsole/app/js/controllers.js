/* global angular */

var controllers = angular.module('mocControllers', ['ngCookies']);
controllers.controller('mainController', ['$rootScope', 'ngDialog', 'workspace', 'competition', 'user',
    function ($rootScope, ngDialog, $workspace, $competition, $user) {
        /*
         YES THIS LOOKS LIKE SHIT, 
         BUT EASIEST WAY I'VE FOUND TO BE ABLE TO USE THE POPUPS ON EVERY PAGE WITHOUT HAVING TO EDIT IT IN EVERY CONTROLLER
         
         -Luc
         */
        $rootScope.showServerInfo = function ($scope) {
            ngDialog.open({
                template: "popups/serverInfo.html",
                className: 'ngdialog-theme-default',
                scope: $scope
            });
            $scope.servers = $workspace.sysInfo.save();
            //TODO GET SERVER INFO OVER WEBSERVICE
        };
        $rootScope.addCompetition = function ($scope) {
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

        $rootScope.editCompetition = function (i, $scope) {
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

        $rootScope.editChallenge = function (id, $scope) {
            ngDialog.open({
                template: "popups/editChallenge.html?id=" + id,
                className: 'ngdialog-theme-default',
                scope: $scope
            });
        };

        $rootScope.addTeam = function ($scope) {
            ngDialog.open({
                template: "popups/addTeam.html",
                className: 'ngdialog-theme-default',
                scope: $scope
            });
            $scope.availableParticipants = $user.allUsers.query();
            console.log($scope.availableParticipants);
        };

        $rootScope.addMembers = function ($scope) {
            ngDialog.open({
                template: "popups/addMember.html",
                className: 'ngdialog-theme-default',
                scope: $scope
            });
        };
    }]);

controllers.controller('loginController', ['$scope', '$rootScope', '$translate', '$cookies', 'user',
    function ($scope, $rootScope, $translate, $cookies, $user) {
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
controllers.controller('registerController', ['$scope', 'rootScope', '$routeParams', 'user', 'team',
    function ($scope, $rootScope, $routeParams, $user, $team) {
        $scope.showDetailsFor = 'user'; // can be 'user' or 'team' depending on what to show
        $scope.users = $user.allUsers.query();
        $scope.teams = $team.allTeams.query();

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

controllers.controller('competitionOverviewController', ['$scope', '$rootScope', 'ngDialog', 'competition', 'workspace',
    function ($scope, $rootScope, ngDialog, $competition, $workspace) {
        $scope.competitions = new $competition.all.query();
        $scope.setSelectedCompetition = function (competition) {
            $scope.selectedCompetition = competition;
            /*
             Team has:
             @XmlElement
             private Competition competition;
             So we can't have:
             @XmlElement
             public List<Team> getTeams() {
             return teams;
             }
             To prevent any cyclic references
             */
            $scope.selectedCompetition.teams = new $competition.teams.query({competitionId: competition.id});
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
            name: null,
            startTime: null,
            status: null
        };

        $scope.addCompetition = function () {
            $rootScope.addCompetition($scope);
        };
        $scope.editCompetition = function (id) {
            $rootScope.editCompetition(id, $scope);
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

        $scope.showServerInfo = function () {
            $rootScope.showServerInfo($scope);
        };
    }
]);

controllers.controller('competitionViewController', ['$scope', '$rootScope', 'competition', '$routeParams', 'ngDialog',
    function ($scope, $rootScope, $competition, $routeParams, ngDialog) {
        $scope.teamSort = 'score';
        $scope.reverseSort = true;
        $scope.description = 'participant';
        $scope.selectedTeam = 0;
        $scope.currentCompetition = $competition.all.get({competitionId: $routeParams.id}, function () {
            $scope.currentRound = 0;
            $scope.currentCompetition.teams = $competition.teams.query({competitionId: $routeParams.id});
            console.log($scope.currentCompetition);
        });

        $scope.startCompetition = function () {
            console.log("start competition");
            $competition.start.save({competitionId: $routeParams.id}, function () {
                alert("Started the competition");
            }, function (response) {
                console.log(response);
                alert(response.data);
            });
        };

        $scope.pauseCompetition = function () {
            console.log("pause competition");
            $competition.pause.save({competitionId: $routeParams.id}, function () {
                alert("Paused the competition");
            }, function (response) {
                console.log(response);
                alert(response.data);
            });
        };

        $scope.freezeCompetition = function () {
            console.log("freeze competition");
            $competition.freeze.save({competitionId: $routeParams.id}, function () {
                alert("Froze the competition");
            }, function (response) {
                console.log(response);
                alert(response.data);
            });
        };

        $scope.stopCompetition = function () {
            console.log("stop competition");
            $competition.stop.save({competitionId: $routeParams.id}, function () {
                alert("Stopped the competition");
            }, function (response) {
                console.log(response);
                alert(response.data);
            });
        };

        $scope.editCompetition = function () {
            $rootScope.editCompetition($routeParams.id, $scope);
        };

        $scope.editChallenge = function (id) {
            $rootScope.editChallenge(id, $scope);
        };

        $scope.addTeam = function () {
            $rootScope.addTeam($scope);
        };

        $scope.addMembers = function () {
            $rootScope.addMembers($scope);
        };
    }
]);

controllers.controller('addChallengeController', ['$scope', '$rootScope', 'challenge', '$routeParams',
    function ($scope, $rootScope, $challenge, $routeParams) {
        $scope.fileName = null;
        $scope.currentChallenge = {};
        $scope.currentCompetition = $routeParams.compId;

        $scope.upload = function () {
            $rootScope.loading = true;
            //Lazy async task in JavaScript 
            setTimeout(function () {
                var formData = new FormData();
                formData.append('fileContent', $scope.base64File);
                formData.append('fileName', $scope.fileName);
                console.log(formData);
                $challenge.create.save(formData, function () {
                    $rootScope.loading = false;
                    updateAvailable();
                });
            }, 100);
        };

        updateAvailable();
        function updateAvailable() {
            $rootScope.loading = true;
            $scope.availableChallenges = $challenge.available.query(function () {
                $rootScope.loading = false;
            });
        }


        $scope.challengeInfo = function (challengeName) {
            $challenge.challengeInfo.get({challengeName: challengeName}, function (data) {
                $scope.currentChallenge = data;
                console.log(data);
            }, function (data) {
                console.log('error');
                console.log(data);
            });
        };

        $scope.addToCompetition = function () {
            if (Object.keys($scope.currentChallenge).length < 1) { //http://stackoverflow.com/questions/679915/how-do-i-test-for-an-empty-javascript-object
                alert("Please select a challenge first");
                return;
            }
            console.log($scope.currentCompetition);
            var challenge = new $challenge.addToCompetition();
            challenge.name = $scope.currentChallenge.challengeName;
            challenge.difficulty = $scope.currentChallenge.difficulty;
            challenge.suggestedDuration = 1; //TODO
            //releaseTime
            challenge.hints = [];
            for (var i = 0; i < $scope.currentChallenge.hints.length; i++) {
                challenge.hints.push({
                    content: $scope.currentChallenge.hints[i].content,
                    time: $scope.currentChallenge.hints[i].delay
                });
            }
            challenge.$save({
                competitionId: $scope.currentCompetition
            });
        };

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
        };

        var handleFileSelect = function (evt) {
            $scope.loading = true;
            var files = evt.target.files;
            var file = files[0];

            $scope.fileName = file.name;
            $scope.$apply();

            if (files && file) {
                var reader = new FileReader();

                reader.onload = function (readerEvt) {
                    var binaryString = readerEvt.target.result;
                    $scope.base64File = btoa(binaryString);
                    console.log(2);
                    $scope.loading = false;
                };

                reader.readAsBinaryString(file);
            }
        };

        if (window.File && window.FileReader && window.FileList && window.Blob) {
            document.getElementById('newChallengeUpload').addEventListener(
                    'change',
                    handleFileSelect,
                    false
                    );
        } else {
            alert('The File APIs are not fully supported in this browser.');
        }
    }
]);