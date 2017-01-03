angular.module('transcripts', []);

angular.module('transcripts').controller('TranscriptsTableController', ['$scope', '$log', 'Transcript', 'TranscriptsTableModel',
    function ($scope, $log, Transcript, TranscriptsTableModel) {
        $scope.transcriptData = [];
        $scope.columnsList = TranscriptsTableModel.columnsList;
        $scope.showSpinner = true;

        $scope.tableLimit = 10;
        $scope.tableBegin = 0;
        $scope.currentPage = 1;

        Transcript.getAll(function (response) {
            $scope.transcriptData = response.data;
            changeSpinner(false);
        });

        $scope.$on(filterByNameEvent + 'Broadcast', function (event, name) {
            changeSpinner(true);
            Transcript.getByFilter({formName: name}, function (response) {
                $scope.transcriptData = response.data;
                changeSpinner(false);
            });
        });

        function changeSpinner(spinnerIndicator) {
            $scope.showSpinner = spinnerIndicator;
        }

        $scope.pageChanged = function () {
            $scope.tableBegin = $scope.tableLimit * ($scope.currentPage - 1);
        }

    }]);
