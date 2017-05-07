angular.module('Controllers').controller('TranscriptListController', ['$rootScope', '$scope', '$log', '$state', 'User',
    function ($rootScope, $scope, $log, $state, User) {


        function init() {
            if (!$rootScope.authenticated) {
                $state.go('login');
            } else {
                User.getSamplesList({name: $rootScope.userName, password: ""}, function (response) {
                    $scope.samples = response.data;
                })
            }
        }

        $scope.elemClicked = function (id) {
            $state.go('transcript-table', {fakeId: id})
        };

        init();

    }]);