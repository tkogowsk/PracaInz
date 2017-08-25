angular.module('Admin').controller('PrivilegesController', ['$scope', '$stateParams', 'User',
    function ($scope, $stateParams, User) {

        $scope.data = [];
        $scope.showFilterSpinner = true;

        User.getSamplesIdsList((response) => {
            $scope.data = _.chain(response.data).map((item) => {
                return {
                    id: item,
                    selected: false
                }
            }).value();
            User.getUserPrivilegesList({userId: $stateParams.userId}, (response) => {
                $scope.showFilterSpinner = false;
                _.forEach(response.data, (elem) => {
                    let selected = _.find($scope.data, (item) => {
                        return item.id === elem;
                    });
                    selected.selected = true;
                })
            })
        });

        $scope.save = function () {
            $scope.showFilterSpinner = true;
            let data = _.chain($scope.data).filter((elem) => {
                return (elem.selected === true)
            }).map(elem => {
                return elem.id
            }).value();

            User.setUserPrivilegesList({userId: $stateParams.userId}, data, (response) => {
                $scope.showFilterSpinner = false;
            })
        }
    }]);