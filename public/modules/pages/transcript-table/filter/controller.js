angular.module('filter', []);
angular.module('filter').controller('FilterController', ['$scope', '$rootScope', '$state', '$log', 'Fields', '$stateParams', 'LocalStorage',
    function ($scope, $rootScope, $state, $log, Fields, $stateParams, LocalStorage) {

        $scope.groupedData = [];
        $scope.userId = 1;
        $scope.activeTabName = null;

        function init() {
            /*         if (!$rootScope.authenticated) {
             $state.go('login');
             } else {*/
            getFields();
            setColumnList();
            //       }
        }

        function setActiveTab(newName) {
            if ($scope.activeTabName !== newName) {
                $scope.activeTabName = newName;
            }
        }

        function setColumnList() {
            $scope.columnsList = LocalStorage.getColumnList();
        }

        $scope.$on(variantColumnsFetchedEvent + 'Broadcast', function (event, name) {
            setColumnList();
        });

        $scope.getAll = function () {
            $scope.$emit(getAllTranscriptsEvent + "Emit", name);
            $scope.activeFormName = null;
        };


        $scope.filterTabNameClicked = function (name) {
            setActiveTab(name);
        };

        function getFields() {
            Fields.getFields({
                    userName: $rootScope.userName,
                    sample_fake_id: $stateParams.fakeId
                }, (response) => {
                    let groupedData = _.chain(response.data)
                        .groupBy('tab_name')
                        .toPairs()
                        .map(function (currentItem) {
                            return _.zipObject(['tabName', 'items'], currentItem)
                        })
                        .value();

                    _.forEach(groupedData, function (item) {
                        item.items = _.chain(item.items)
                            .groupBy('filterName')
                            .toPairs()
                            .map(function (currentItem) {
                                return _.zipObject(['filterName', 'items'], currentItem)
                            }).value();

                    });

                    $scope.groupedData = groupedData;
                    $rootScope.changeSpinner(false);
                }
            );
        }

        $scope.filter = function (tab) {
            $scope.$emit(filterTabEvent + "Emit", tab);
        };

        $scope.getColumnName = function (columnId) {
            if ($scope.columnsList) {
                var elem = _.find($scope.columnsList, function (elem) {
                    return elem.id === columnId;
                });
                return elem.fe_name;
            }
            return "";
        };

        /* $scope.isActive = function (tabName) {
         return viewLocation === $location.path();
         };
         */
        init();
    }]);
