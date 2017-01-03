angular.module('transcripts', []);

angular.module('transcripts').controller('TranscriptsTableController', ['$scope', '$log', 'Transcript', 'TranscriptsTableModel',
    function ($scope, $log, Transcript, TranscriptsTableModel) {
        $scope.transcriptData = [];
        $scope.columnsList = TranscriptsTableModel.columnsList;
        $scope.showSpinner = true;
        Transcript.getAll(function (response) {
            $scope.transcriptData = response.data;
            $scope.showSpinner = false;
        });
        $scope.$on(filterByNameEvent +'Broadcast',  function (event, name) {
            $scope.showSpinner = true;
            Transcript.getByFilter({formName: name}, function (response) {
                $scope.transcriptData = response.data;
                $scope.showSpinner = false;
            });
        })
    }]);
