angular.module('mainPage', []);

angular.module('mainPage').controller('MainPageController', ['$scope', '$rootScope', '$log', 'VariantColumn', 'Transcript',
    function ($scope, $rootScope, $log, VariantColumn, Transcript) {
        $rootScope.showSpinner = true;
        $rootScope.changeSpinner = function(spinnerIndicator) {
            $scope.showSpinner = spinnerIndicator;
        };

        function init() {
            getColumnsList();
        }

        function getColumnsList() {
            VariantColumn.getVariantColumn(function (response) {
                $rootScope.columnsList = response.data;
            })
        }

        $rootScope.getColumnType = function (columnId) {
            console.log(_.chain($rootScope.columnsList).find((elem) => elem.id === columnId).value());
            return _.chain($rootScope.columnsList).find((elem) => elem.id === columnId).value();
        };

        $scope.$on(filterByNameEvent + 'Emit', function (event, name) {
            event.stopPropagation();
            $scope.$broadcast(filterByNameEvent + 'Broadcast', name)
        });
        $scope.$on(getAllTranscriptsEvent + 'Emit', function (event, name) {
            event.stopPropagation();
            $scope.$broadcast(getAllTranscriptsEvent + 'Broadcast', name)
        });

        init();

    }]);
