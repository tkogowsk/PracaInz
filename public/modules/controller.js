angular.module('mainPage', []);

angular.module('mainPage').controller('MainPageController', ['$scope', '$log',
    function ($scope, $log) {
        $scope.$on('ActiveFormChangedEmit', function (event, name) {
           event.stopPropagation();
            $scope.$broadcast("ActiveFormChangedBroadcast", name)
        })
    }]);
