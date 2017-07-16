angular.module('Admin', []);

angular.module('Admin').controller('AdminController', ['$rootScope', '$scope', '$log', '$state', 'User',
    function ($rootScope, $scope, $log, $state, User) {

        function init() {
            if (!$rootScope.isAuthenticated() || !$rootScope.isAdmin()) {
                $state.go('transcript-list');
            } else {
                User.getUsersList(function (response) {
                    $scope.users = response.data;
                })
            }
        }

        init();

    }]);