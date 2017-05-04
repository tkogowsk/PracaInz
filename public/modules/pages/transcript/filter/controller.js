angular.module('filter', []);
angular.module('filter').controller('FilterController', ['$scope', '$rootScope', '$log', 'Form', 'Fields', 'LocalStorage',
    function ($scope, $rootScope, $log, Form, Fields, LocalStorage) {

        $scope.groupedData = [];
        $scope.userId = 1;
        $scope.activeTabName = null;

        function init() {
            getFields();
            setColumnList()
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

        $scope.addNewFilter = function (name) {
            let fields = [];
            if ($scope.userForms.findIndex(form => form.name === name) >= 0) {
                return
            }

            $scope.fields.forEach((elem) => {
                fields.push({
                    value: '',
                    fieldFk: elem.id,
                    userId: $scope.userId,
                    name: name
                })
            });
            let newObject = {name: name, fields: fields};
            $scope.userForms.push(newObject);
            setActiveFormName(name);
        };

        $scope.filterTabNameClicked = function (name) {
            setActiveTab(name);
        };

        /*    $scope.saveForm = function (name) {
            let form = getFormByName(name);
            Form.saveForm(form, (response) => {
                $scope.$emit(filterByNameEvent + "Emit", name)
            })
        };

        $scope.editForm = function (name) {
            let form = getFormByName(name);
            Form.editForm(form, (response) => {
                $scope.$emit(filterByNameEvent + "Emit", name)
            })
        };

        function getFormByName(name) {
            return $scope.userForms.find((elem) => {
                return elem.name === name
            });
         }*/

        function getFields() {
            Fields.getFields((response) => {
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

        init();
    }]);
