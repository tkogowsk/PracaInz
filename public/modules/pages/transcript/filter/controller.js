angular.module('filter', []);
angular.module('filter').controller('FilterController', ['$scope', '$rootScope', '$log', 'Form', 'Fields',
    function ($scope, $rootScope, $log, Form, Fields) {

        $scope.groupedData = [];
        $scope.userId = 1;
        $scope.activeTabName = null;

        function init() {
            /*       Form.getUserForms((response) => {
             let data = response.data;
             Object.getOwnPropertyNames(data).forEach(function (val, idx, array) {
             $scope.userForms.push({
             name: val,
             fields: data[val]
             })
             });
             }
             );*/
            getFields();
        }

        function setActiveTab(newName) {
            if ($scope.activeTabName !== newName) {
                $scope.activeTabName = newName;
               /* setActiveFormName(newName);
                if (eventName) {
                    $scope.$emit(eventName + "Emit", newName)
                }*/
            }
        }

        $scope.getAll = function () {
            $scope.$emit(getAllTranscriptsEvent + "Emit", name);
            $scope.activeFormName = null;
        };

        $scope.addNewFilter = function (name) {
            let fields = [];
            console.log($scope.userForms.findIndex(form => form.name === name))
            if ($scope.userForms.findIndex(form => form.name === name) >= 0) {
                console.log('ret');
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
            console.log(newObject);

        };

        $scope.filterTabNameClicked = function (name) {
            setActiveTab(name, filterByNameEvent);
        };

        $scope.saveForm = function (name) {
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
        }

        function getFields() {
            Fields.getFields((response) => {
                    $log.log(response.data);

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
                    $log.log(groupedData);

                    $scope.groupedData = groupedData;
                    $rootScope.changeSpinner(false);
                }
            );
        }

        init();
    }]);
