angular.module('transcripts', []);

angular.module('transcripts').controller('TranscriptsTableController', ['$scope', '$log', 'Transcript', 'VariantColumn', 'Fields',
    function ($scope, $log, Transcript, VariantColumn, Fields) {
        $scope.transcriptData = [];
        $scope.columnsList = null;
        $scope.showSpinner = true;

        $scope.tableLimit = 10;
        $scope.tableBegin = 0;
        $scope.currentPage = 1;

        function init() {
            getColumnsList();
            getAll();
            Fields.getFields((response) => {
                $log.log(response);
            })
        }

        $scope.$on(filterByNameEvent + 'Broadcast', function (event, name) {
            changeSpinner(true);
            Transcript.getByFilter({formName: name}, function (response) {
                $scope.transcriptData = response.data;
                changeSpinner(false);
            });
        });

        $scope.$on(getAllTranscriptsEvent + 'Broadcast', function (event, name) {
            getAll();
        });

        function changeSpinner(spinnerIndicator) {
            $scope.showSpinner = spinnerIndicator;
        }

        $scope.pageChanged = function () {
            $scope.tableBegin = $scope.tableLimit * ($scope.currentPage - 1);
        };

        function getAll() {
            Transcript.getAll(function (response) {
                $scope.transcriptData = response.data;
                changeSpinner(false);
            });
        }

        function getColumnsList() {
            VariantColumn.getVariantColumn(function (response) {
                $scope.columnsList = response.data;
                $log.log(response);
            })
        }
        init();

    }]);
