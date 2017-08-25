angular.module('Admin', []);


angular.module('Admin').controller('AdminController', ['$rootScope', '$scope', '$log', '$state', 'User',
    function ($rootScope, $scope, $log, $state, User) {
        $scope.showFilterSpinner = false;
        function init() {
            if (!$rootScope.isAuthenticated() || !$rootScope.isAdmin()) {
                $state.go('transcript-list');
            } else {
                $scope.showFilterSpinner = true;
                User.getUsersList(function (response) {
                    $scope.showFilterSpinner = false;
                    $scope.users = response.data;
                })
            }
        }

        init();

    }]);