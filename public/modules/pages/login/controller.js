angular.module('Security', []);

angular.module('Security').controller('LoginController', ['$rootScope', '$scope', '$log', '$state', 'Authentication',
    function ($rootScope, $scope, $log, $state, Authentication) {

        $scope.userName = "";
        $scope.password = "";

        console.log($rootScope.authenticated);
        if (!$rootScope.authenticated) {
            $rootScope.authenticated = true;
        }

        $scope.logIn = function () {
            Authentication.logIn({name: $scope.userName, password: $scope.password}, function (response) {
                if (response.data) {
                    $scope.$emit(loggedInEvent + 'Emit');
                    $state.go('transcript-list');
                }
                $scope.message = "Error"
            });

        }
    }]);