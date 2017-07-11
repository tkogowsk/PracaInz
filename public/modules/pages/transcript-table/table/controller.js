angular.module('transcripts', []);

angular.module('transcripts').controller('TranscriptsTableController', ['$scope', '$rootScope', '$state', '$log', 'Transcript', 'Fields', '$stateParams', 'LocalStorage',
    function ($scope, $rootScope, $state, $log, Transcript, Fields, $stateParams, LocalStorage) {
        $scope.transcriptData = [];
        $scope.userColumnsList = null;

        $scope.dataLength = null;

        $scope.tableLimit = 10;
        $scope.tableBegin = 0;
        $scope.currentPage = 1;
        $scope.search = {};

        function init() {
            /*            if (!$rootScope.authenticated) {
             $state.go('login');
             } else {*/
            countAll();
            getAll();
            prepareHeaderColumns();
            //          }
        }

        $scope.$on(filterTabEvent + 'Broadcast', function (event, data) {
            $rootScope.changeSpinner(true);
            var payload = {};
            var list = prepareFiltersList(data.items);
            payload["filters"] = list;
            payload.sampleFakeId = parseInt($stateParams.fakeId);
            payload.userName = $rootScope.userName;
            $rootScope.changeSpinner(false);

            Transcript.getByTab(payload, (response) => {
                $scope.transcriptData = [];
                prepareTableData(response.data);
                $rootScope.changeSpinner(false);
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

        function countAll() {
            Transcript.countAll({sampleFakeId: $stateParams.fakeId},
                function (response) {
                    console.log(response);
                    $scope.dataLength = response.data;
                },
                function (error) {
                    console.log("ERROR " + error);
                    alert("ERROR " + error);
                })
        }

        function getAll() {
            Transcript.getTranscriptData({
                    userName: $rootScope.userName,
                    sampleFakeId: $stateParams.fakeId
                }, function (response) {
                    prepareTableData(response.data);
                    $rootScope.changeSpinner(false);
                },
                function (error) {
                    console.log("ERROR " + error);
                    alert("ERROR " + error);
                });
        }

        function prepareTableData(data) {
            $scope.transcriptData = _.chain(data)
                .map(function (elem) {
                    var object = {};
                    _.forEach(elem.row, function (item) {
                        object[item.column] = item.value;
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
                                    column_name: currentItem.column_name,
                                    //TODO
                                    user_id: 1
                                })
                        }).value();
                }
            }
        }

        $scope.changeSorting = function (header) {
            if (header.sorting === "ASC") {
                header.sorting = "DESC"
            } else if (header.sorting === "DESC") {
                header.sorting = null;
            }
            else {
                header.sorting = "ASC"
            }

        };

        $scope.getSorting = function (header) {
            if (header.sorting === "ASC") {
                return 'glyphicon glyphicon-chevron-up';
            } else if (header.sorting === "DESC") {
                return 'glyphicon glyphicon-chevron-down';
            }
            else {
                return 'glyphicon glyphicon-chevron-right';
            }
        };

        function getData() {

        }

        init();

    }]);
