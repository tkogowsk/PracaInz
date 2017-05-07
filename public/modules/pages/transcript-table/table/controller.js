angular.module('transcripts', []);

angular.module('transcripts').controller('TranscriptsTableController', ['$scope', '$rootScope', '$state', '$log', 'Transcript', 'Fields', '$stateParams', 'LocalStorage',
    function ($scope, $rootScope, $state, $log, Transcript, Fields, $stateParams, LocalStorage) {
        $scope.transcriptData = [];
        $scope.userColumnsList = null;

        $scope.tableLimit = 10;
        $scope.tableBegin = 0;
        $scope.currentPage = 1;

        function init() {
            /*            if (!$rootScope.authenticated) {
                $state.go('login');
             } else {*/
                getAll();
                prepareHeaderColumns();
            //          }
        }

        $scope.$on(filterTabEvent + 'Broadcast', function (event, data) {
            $rootScope.changeSpinner(true);

            console.log(data);
            var payload = {};
            var list = [];

            _.forEach(data.items, function (filter) {
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

            payload["filters"] = list;
            console.log(payload);
            $rootScope.changeSpinner(false);

            Transcript.getByTab(payload, (response) => {
                console.log(response);
                $scope.transcriptData = [];
                prepareTableData(response.data);
                $rootScope.changeSpinner(false);
            })

            /*Transcript.getByTab({formName: name}, function (response) {
             $scope.transcriptData = response.data;
             $rootScope.changeSpinner(false);
             });*/
        });


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
            Transcript.getTranscriptData({
                userName: $rootScope.userName,
                sample_fake_id: $stateParams.fakeId
            }, function (response) {
                prepareTableData(response.data);
                $rootScope.changeSpinner(false);
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
                                    fe_name: currentItem.fe_name,
                                    //TODO
                                    user_id: 1
                                })
                        }).value();
                }
            }
        }

        init();

    }]);
