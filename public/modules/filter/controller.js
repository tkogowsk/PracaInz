angular.module('filter', []);

angular.module('filter').controller('FilterController', ['$scope', '$log', 'Form', 'Fields',
    function ($scope, $log, Form, Fields) {
        $log.log('FilterController');
        $scope.fields = [];
        $scope.userForms = [];
        $scope.userId = null;
        $scope.activeFormName = null;
        Form.getUserForms((response) => {
                let data = response.data;
                Object.getOwnPropertyNames(data).forEach(function (val, idx, array) {
                    $scope.userForms.push({
                        name: val,
                        fields: data[val]
                    })
                });
                $scope.activeFormName = $scope.userForms[0].name;
                $scope.userId = $scope.userForms[0].fields[0].userId;
            }
        );

        Fields.getFields((response) => {
                $scope.fields = response.data
            }
        );

        $scope.addNewFilter = function (name) {
            let fields = [];
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
        };

        $scope.userFormNameClicked = function (name) {
            if ($scope.activeFormName != name) {
                $scope.activeFormName = name;
                $scope.$emit("ActiveFormChangedEmit", name)
            }
        }

        $scope.saveForm = function (name) {
            var form = $scope.userForms.find((elem) => {
                return elem.name == name
            })
            Form.saveForm(form, (response) => {

            })
        };

        $scope.editForm = function (name) {
            var form = $scope.userForms.find((elem) => {
                return elem.name == name
            });
            Form.editForm(form, (response) => {
            })
        }
    }]);
