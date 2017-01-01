angular.module('mainPage', []);

angular.module('mainPage').controller('MainPageController', ['$scope', '$log', 'Transcript', 'TranscriptsTableModel',
    function ($scope, $log, Transcript, TranscriptsTableModel) {
        $scope.transcriptData = [];
        $scope.columnsList = TranscriptsTableModel.columnsList;

        Transcript.getByFilter(function (response) {
            $scope.transcriptData = response.data;
        });

    }]);
