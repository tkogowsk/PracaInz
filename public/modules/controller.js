angular.module('Controllers', []);

angular.module('Controllers').controller('MainPageController', ['$scope', '$rootScope', '$log', '$state', 'VariantColumn', 'LocalStorage',
    function ($scope, $rootScope, $log, $state, VariantColumn, LocalStorage) {
        $rootScope.showSpinner = true;

        $rootScope.changeSpinner = function(spinnerIndicator) {
            $scope.showSpinner = spinnerIndicator;
        };

        function init() {
            if (!$rootScope.authenticated) {
                $state.go('login');
            } else {
                getColumnsList();
            }
        }

        function getColumnsList() {
            VariantColumn.getVariantColumn(function (response) {
                LocalStorage.setColumnList(response.data);
                $scope.$broadcast(variantColumnsFetchedEvent + 'Broadcast', name)
            })
        }

        $rootScope.getColumnType = function (columnId) {
            return _.chain($rootScope.columnsList).find((elem) => elem.id === columnId).value();
        };

        $scope.$on(filterTabEvent + 'Emit', function (event, data) {
            event.stopPropagation();
            $scope.$broadcast(filterTabEvent + 'Broadcast', data)
        });

        $scope.$on(getAllTranscriptsEvent + 'Emit', function (event, name) {
            event.stopPropagation();
            $scope.$broadcast(getAllTranscriptsEvent + 'Broadcast', name)
        });

        $scope.$on(loggedInEvent + 'Emit', function (event, data) {
            event.stopPropagation();
            getColumnsList();
        });

        init.bind(this)();

    }]);
