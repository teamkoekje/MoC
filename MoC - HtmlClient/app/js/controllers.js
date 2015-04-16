/* global angular */

var controllers = angular.module('mocControllers', []);

controllers.controller('demoController', ['$scope', 'user',
    function ($scope, $user) {
        $scope.createUser = function () {
            console.log("Create User");
            $scope.user.$save(function () {
                loadData();
            });
            $scope.user = new $user();
        };
        
        $scope.compileWorkspace = function () {
            console.log("Compile workspace");

        };

        loadData = function () {
            $scope.users = $user.query();
        };

        loadData();
        $scope.user = new $user();
    }
]);
