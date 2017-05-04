angular.module('transcripts', []);

angular.module('transcripts').controller('TranscriptsTableController', ['$scope', '$rootScope', '$log', 'Transcript', 'Fields', 'LocalStorage',
    function ($scope, $rootScope, $log, Transcript, Fields, LocalStorage) {
        $scope.transcriptData = [];
        $scope.userColumnsList = null;

        $scope.tableLimit = 10;
        $scope.tableBegin = 0;
        $scope.currentPage = 1;

        function init() {
            getAll();
            prepareHeaderColumns();
        }

        $scope.$on(filterByNameEvent + 'Broadcast', function (event, name) {
            $rootScope.changeSpinner(true);
            Transcript.getByFilter({formName: name}, function (response) {
                $scope.transcriptData = response.data;
                $rootScope.changeSpinner(false);
            });
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
            Transcript.getAllJDBC(function (response) {
                $scope.transcriptData = _.chain(response.data)
                    .map(function (elem) {
                        var object = {};
                        _.forEach(elem.row, function (item) {
                            object[item.column] = item.value;
                        });
                        return object;
                    }).value();
                $rootScope.changeSpinner(false);
            });
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
