angular.module('mainPage', []);

angular.module('mainPage').controller('MainPageController', ['$scope', '$log',
    function ($scope, $log) {
        $scope.$on(filterByNameEvent + 'Emit', function (event, name) {
           event.stopPropagation();
            $scope.$broadcast(filterByNameEvent + 'Broadcast', name)
        })
        $scope.$on(getAllTranscriptsEvent + 'Emit', function (event, name) {
            event.stopPropagation();
            $scope.$broadcast(getAllTranscriptsEvent + 'Broadcast', name)
        })
    }]);
