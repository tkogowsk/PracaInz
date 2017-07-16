angular.module('transcripts', []);

angular.module('transcripts').controller('TranscriptsTableController', ['$scope', '$rootScope', '$state', '$log', 'Transcript', 'Fields', '$stateParams', 'LocalStorage',
    function ($scope, $rootScope, $state, $log, Transcript, Fields, $stateParams, LocalStorage) {
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
            payload.userName = $rootScope.userName;
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
                                variant_column_id: field.variant_column_id
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
                    userName: $rootScope.userName,
                    sampleFakeId: $stateParams.fakeId
                }, function (response) {
                    prepareTableData(response.data);
                    changeSpinner(false);
                },
                function (error) {
                    console.log("ERROR " + error);
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

        function prepareHeaderColumns() {
            if (!$scope.userColumnsList) {
                var columnList = LocalStorage.getColumnList();
                if (columnList) {
                    $scope.userColumnsList = _.chain(columnList)
                        .map(function (currentItem) {
                            return _.assign(
                                {visible: true},
                                {
                                    variant_column_id: currentItem.id,
                                    column_order: currentItem.id,
                                    sorting: null,
                                    fe_name: currentItem.fe_name,
                                    dataExtractValue: $scope.jsIDPrefix + currentItem.id,
                                    column_name: currentItem.column_name
                                })
                        }).value();
                }
            }
        }

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

        init();

    }]);
