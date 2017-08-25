angular.module('Controllers').controller('TranscriptListController', ['$rootScope', '$scope', '$log', '$state', 'User',
    function ($rootScope, $scope, $log, $state, User) {

        function init() {
            if (!$rootScope.isAuthenticated()) {
                $state.go('login');
            } else {
                User.getUserSamplesList(function (response) {
                    $scope.samples = response.data;
                })
            }
        }

        $scope.elemClicked = function (id) {
            $state.go('transcript-table', {fakeId: id})
        };

        init();

    }]);