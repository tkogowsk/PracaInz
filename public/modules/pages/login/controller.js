angular.module('security', []);

angular.module('security').controller('LoginController', ['$rootScope', '$log', '$state',
    function ($rootScope, $log, $state) {
        if(!$rootScope.authenticated){
            $rootScope.authenticated = true;
            $log.log('successful login');
            $state.go('index');
        }

    }]);