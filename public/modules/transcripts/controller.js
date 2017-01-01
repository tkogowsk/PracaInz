angular.module('transcripts', []);

angular.module('transcripts').controller('TranscriptsTableController', ['$scope', '$log', 'Transcript', 'TranscriptsTableModel',
    function ($scope, $log, Transcript, TranscriptsTableModel) {
        $scope.transcriptData = [];
        $scope.columnsList = TranscriptsTableModel.columnsList;

        Transcript.getAll(function (response) {
            $scope.transcriptData = response.data;
        });
        $scope.$on('ActiveFormChangedBroadcast', function (event, name) {
            Transcript.getByFilter({formName: name}, function (response) {
                $scope.transcriptData = response.data;
            });
        })
    }]);
