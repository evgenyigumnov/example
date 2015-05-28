angular.module('com.igumnov.common.example', ['ui.bootstrap', 'ngResource'])
    .factory('User', ['$resource', function ($resource) {
        return $resource('/rest/user', {}, {
            list: {
                method: 'GET',
                cache: false,
                isArray: true
            },
            add: {
                method: 'POST',
                cache: false,
                isArray: false
            },
            delete: {
                method: 'DELETE',
                cache: false,
                isArray: false
            }
        });
    }])
    .controller('UserCtrl', function ($scope, User) {

        $scope.users = User.list({});
        $scope.addUser = function (user) {
            User.add({},user,function (data) {
                $scope.users = User.list({});   
            }, function (err) {
                alert(err.data.message);
            });
        }
        $scope.deleteUser = function (user) {
            User.delete({"userName" : user.userName},user,function (data) {
                $scope.users = User.list({});
            }, function (err) {
                alert(err.data.message);
            });
        }

    });