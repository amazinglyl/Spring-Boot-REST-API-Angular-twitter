(function(angular){
    'use strict';
var app = angular.module('myApp', []);
app.controller('myCtr', function($scope, $http) {
    $scope.createUser = function(){
        $http.post("http://10.20.20.141:8080/users",{
            "name": $scope.name,
            "profile": $scope.profile,
            "tweets": 0,
            "followers": 0,
            "followings": 0
        }).then(function(){})
    }
    
    $scope.showUsers=function(){
        $http.get("http://10.20.20.141:8080/users").then(function (response) {
        $scope.myData = response.data;
        });
    }
  
});
})(window.angular);