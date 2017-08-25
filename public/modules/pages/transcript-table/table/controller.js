angular.module('transcripts', []);

angular.module('transcripts').controller('TranscriptsTableController', ['$scope', '$rootScope', '$state', '$log', 'Transcript', 'Fields', '$stateParams', 'LocalStorage', 'User', '$uibModal', '$document',
    function ($scope, $rootScope, $state, $log, Transcript, Fields, $stateParams, LocalStorage, User, $uibModal, $document) {
        $scope.transcriptData = [];
        $scope.userColumnsList = null;

        $scope.dataLength = null;
        $scope.tableLimit = 300;
        $scope.tableBegin = 0;
        $scope.currentPage = 1;
        $scope.search = {};

        $scope.showTableSpinner = true;
        $scope.transcriptSortReverse = true;
        $scope.transcriptSortPropertyName = null;
        $scope.jsIDPrefix = "id_";
        $scope.showHideColumnsModal = false;

        function changeSpinner(spinnerIndicator) {
            $scope.showTableSpinner = spinnerIndicator;
        }

        function init() {
            if (!$rootScope.isAuthenticated()) {
                $state.go('login');
            } else {
                getAll();
                prepareHeaderColumns();
            }
        }

        $scope.$on(filterTabEvent + 'Broadcast', function (event, data) {
            changeSpinner(true);
            var payload = {};
            var list = prepareFiltersList(data.items);
            payload["filters"] = list;
            payload.sampleFakeId = parseInt($stateParams.fakeId);
            Transcript.getByTab(payload, (response) => {
                $scope.transcriptData = [];
                prepareTableData(response.data);
                changeSpinner(false);
            })
        });

        function prepareFiltersList(items) {
            var list = [];
            _.forEach(items, function (filter) {
                if (!filter.inactive) {
                    _.forEach(filter.items, function (field) {
                        if (field.value) {
                            list.push({
                                relation: field.relation,
                                value: field.value,
                                variantColumnId: field.variantColumnId
                            })
                        }
                    })
                }
            });
            return list;
        }

        $scope.$on(variantColumnsFetchedEvent + 'Broadcast', function (event, name) {
            prepareHeaderColumns();
        });

        $scope.$on(getAllTranscriptsEvent + 'Broadcast', function (event, name) {
            getAll();
        });

        $scope.pageChanged = function () {
            $scope.tableBegin = $scope.tableLimit * ($scope.currentPage - 1);
        };

        function getAll() {
            changeSpinner(true);
            Transcript.getTranscriptData({
                    sampleFakeId: $stateParams.fakeId
                }, function (response) {
                    prepareTableData(response.data);
                    changeSpinner(false);
                },
                function (error) {
                    changeSpinner(false);
                    console.log("ERROR " + error.data);
                });
        }

        function prepareTableData(data) {
            $scope.transcriptData = _.chain(data)
                .map(function (elem) {
                    var object = {};
                    _.forEach(elem.row, function (item) {
                        object[$scope.jsIDPrefix + item.id] = item.value;
                    });
                    return object;
                }).value();
        }

        function setVisibilityFromDatabase(data, currentItemId) {
            return (_.find(data, (responseElem) => {
                return responseElem.variantColumnId === currentItemId
            }) !== undefined)
        }

        function prepareHeaderColumns() {
            if (!$scope.userColumnsList) {
                $scope.columnList = LocalStorage.getColumnList();
                if ($scope.columnList) {
                    User.getVisibleColumns((response => {
                            var getVisibility = (response.data && response.data.length > 0) ? setVisibilityFromDatabase : () => {
                                return true;
                            };
                            $scope.userColumnsList = _.chain($scope.columnList)
                                .map(function (currentItem) {
                                    return _.assign(
                                        {visible: getVisibility(response.data, currentItem.id)},
                                        {
                                            variantColumnId: currentItem.id,
                                            column_order: currentItem.id,
                                            sorting: null,
                                            feName: currentItem.feName,
                                            dataExtractValue: $scope.jsIDPrefix + currentItem.id,
                                            columnName: currentItem.columnName
                                        })
                                }).value();
                        }
                    ))
                }
            }
        }

        $scope.saveVisibleColumns = function () {
            let payload = _.chain($scope.userColumnsList).filter((elem) => {
                return (elem.visible === true)
            }).map((elem) => {
                return {id: elem.variantColumnId}
            }).value();
            User.saveVisibleColumns(payload,
                (response) => {
                },
                (error) => {
                    console.log(error);
                });
        };

        $scope.changeSorting = function (header) {
            if ($scope.transcriptSortPropertyName === header.dataExtractValue) {
                $scope.transcriptSortReverse = !$scope.transcriptSortReverse;
            } else {
                $scope.transcriptSortPropertyName = header.dataExtractValue;
                $scope.transcriptSortReverse = true;
            }
        };

        $scope.getSorting = function (header) {
            if ($scope.transcriptSortPropertyName === header.dataExtractValue) {
                if ($scope.transcriptSortReverse) {
                    return 'glyphicon glyphicon-chevron-up';
                } else {
                    return 'glyphicon glyphicon-chevron-down';
                }
            } else {
                return 'glyphicon glyphicon-chevron-right';
            }
        };

        $scope.openShowHideColumnsModal = function () {
            var parentElem = angular.element($document[0].querySelector('.modal-root'));
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'myModalContent.html',
                controller: 'ModalInstanceCtrl',
                controllerAs: '$ctrl',
                appendTo: parentElem,
                resolve: {
                    items: function () {
                        return $scope.userColumnsList;
                    },
                    saveVisibleColumns: function () {
                        return $scope.saveVisibleColumns
                    }
                }
            });
        };

        init();

    }]);

angular.module('transcripts').controller('ModalInstanceCtrl', function ($uibModalInstance, items, saveVisibleColumns) {
    var $ctrl = this;
    $ctrl.columns = items;
    $ctrl.save = function () {
        saveVisibleColumns();
        $uibModalInstance.close($ctrl.items);
    };

    $ctrl.cancel = function () {
        $uibModalInstance.close(true);
    };
});
