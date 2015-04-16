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
        
        $scope.deleteUser = function (userId) {
            console.log("Delete User");
            $user.delete({userId:userId}, function () {
                loadData();
            });
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
