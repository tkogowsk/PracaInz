angular.module('mainPage', []);

angular.module('mainPage').controller('MainPageController', ['$scope', '$log', 'Transcript','TranscriptsTableModel',
    function ($scope, $log, Transcript, TranscriptsTableModel) {
        $scope.transcriptData = [];
        $scope.columnsList = TranscriptsTableModel.columnsList;
        Transcript.get(function(response){
            console.log(response);
            $scope.transcriptData = response.data;
        });

    }]);
