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

        function setFieldValue(currentItem) {
            if (_.isNil(currentItem.value) === true && _.isNil(currentItem.default_value) === false) {
                currentItem.value = currentItem.default_value;
            }
        }

        function getFields() {
            Fields.getFields({
                    userName: $rootScope.userName,
                    sample_fake_id: $stateParams.fakeId
                }, (response) => {
                let data = _.chain(response.data)
                    .map(function (currentItem) {
                        setFieldValue(currentItem);
                        return currentItem;
                    }).value();

                let groupedData = _.chain(data)
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

        $scope.count = function (tab) {
            $rootScope.changeSpinner(true);
            var payload = {};
            var list = [];
            _.forEach(tab.items, function (filters) {
                _.forEach(filters.items, function (field) {
                    if (field.value) {
                        list.push({
                            filterName: field.filterName,
                            relation: field.relation,
                            value: field.value,
                            variant_column_id: field.variant_column_id
                        })
                    }
                })
            });

            payload["counters"] = list;
            payload.tabName = tab.tabName;
            payload.sampleFakeId = parseInt($stateParams.fakeId);
            Fields.count(payload, (response) => {
                for (var i = 0; i < $scope.groupedData.length; ++i) {
                    if ($scope.groupedData[i].tabName === $scope.activeTabName) {
                        $scope.groupedData[i].items.forEach(function (filter) {
                            filter.countValue = null;
                            response.data.forEach(function (elem) {
                                if (elem.filterName === filter.filterName) {
                                    filter.countValue = elem.value
                                }
                            })
                        })
                    }
                }

                $rootScope.changeSpinner(false);
            });
        };

        $scope.saveUserFields = function (tab) {
            //$rootScope.changeSpinner(true);
            var payload = [];
            _.forEach(tab.items, function (filters) {
                _.forEach(filters.items, function (field) {
                    payload.push({
                        tab_id: field.tab_id,
                        field_id: field.field_id,
                        filter_id: field.filter_id,
                        value: field.value || '',
                        sample_fake_id: parseInt($stateParams.fakeId)
                    })
                })
            });

            Fields.save({
                userName: $rootScope.userName,
            }, payload, (response) => {
                console.log("ELLLO");
                console.log(response);

                $rootScope.changeSpinner(false);
            });
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

        init();
    }]);
