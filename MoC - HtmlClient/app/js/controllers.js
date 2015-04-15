/* global angular */

var controllers = angular.module('mocControllers', []);

controllers.controller('demoController', ['$scope', 'user',
    function ($scope, $user) {
        $scope.createUser = function () {
            console.log("Create User");
             $scope.user.$save(function () {
                 console.log("User created!");
            });
            $scope.user = new $user();
        };
        
         $scope.user = new $user();
    }
]);
