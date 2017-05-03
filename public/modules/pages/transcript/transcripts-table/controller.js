angular.module('transcripts', []);

angular.module('transcripts').controller('TranscriptsTableController', ['$scope','$rootScope', '$log', 'Transcript', 'Fields',
    function ($scope, $rootScope, $log, Transcript, Fields) {
        $scope.transcriptData = [];
        $scope.columnsList = null;

        $scope.tableLimit = 10;
        $scope.tableBegin = 0;
        $scope.currentPage = 1;

        function init() {

        }

        $scope.$on(filterByNameEvent + 'Broadcast', function (event, name) {
            $rootScope.changeSpinner(true);
            Transcript.getByFilter({formName: name}, function (response) {
                $scope.transcriptData = response.data;
                $rootScope.changeSpinner(false);
            });
        });

        $scope.$on(getAllTranscriptsEvent + 'Broadcast', function (event, name) {
            getAll();
        });

        $scope.pageChanged = function () {
            $scope.tableBegin = $scope.tableLimit * ($scope.currentPage - 1);
        };

        function getAll() {
            Transcript.getAll(function (response) {
                $scope.transcriptData = response.data;
                $rootScope.changeSpinner(false);
            });
        }

        init();

    }]);
