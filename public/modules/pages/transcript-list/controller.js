angular.module('Controllers').controller('TranscriptListController', ['$rootScope', '$scope', '$log', '$state',
    function ($rootScope, $scope, $log, $state) {
        console.log('TranscriptListController');

        function init() {
            if (!$rootScope.authenticated) {
                $state.go('login');
            }
        }

        init();

    }]);