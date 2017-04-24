angular.module('filter', []);
angular.module('filter').controller('FilterController', ['$scope', '$log', 'Form', 'Fields',
    function ($scope, $log, Form, Fields) {

        $scope.fields = [];
        $scope.userForms = [];
        $scope.userId = 1;
        $scope.activeFormName = null;


        function init(){
            Form.getUserForms((response) => {
                    let data = response.data;
                    Object.getOwnPropertyNames(data).forEach(function (val, idx, array) {
                        $scope.userForms.push({
                            name: val,
                            fields: data[val]
                        })
                    });
                }
            );

            Fields.getFields((response) => {
                    $scope.fields = response.data
                }
            );
        }

        function setActiveFormName(newName, eventName) {
            if ($scope.activeFormName != newName) {
                $scope.activeFormName = newName;
                setActiveFormName(newName);
                if(eventName){
                    $scope.$emit(eventName + "Emit", newName)
                }
            }
        }

        $scope.getAll = function () {
            $scope.$emit(getAllTranscriptsEvent + "Emit", name);
            $scope.activeFormName = null;
        };

        $scope.addNewFilter = function (name) {
            let fields = [];
            console.log($scope.userForms.findIndex(form => form.name == name))
            if($scope.userForms.findIndex(form => form.name == name) >= 0){
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

        $scope.userFormNameClicked = function (name) {
            setActiveFormName(name, filterByNameEvent);
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
                return elem.name == name
            });
        }
    }]);
