angular.module('Controllers', []);

angular.module('Controllers').controller('MainPageController', ['$scope', '$rootScope', '$log', '$state', 'VariantColumn', 'LocalStorage', '$location', '$cookies',
    function ($scope, $rootScope, $log, $state, VariantColumn, LocalStorage, $location, $cookies) {
        function init() {
            if (!$rootScope.isAuthenticated()) {
                $state.go('login');
            } else {
                getColumnsList();
            }
        }

        function getColumnsList() {
            VariantColumn.getVariantColumn(function (response) {
                LocalStorage.setColumnList(response.data);
                $scope.$broadcast(variantColumnsFetchedEvent + 'Broadcast', name)
            })
        }

        $rootScope.getColumnType = function (columnId) {
            return _.chain($rootScope.columnsList).find((elem) => elem.id === columnId).value();
        };

        $rootScope.logout = function () {
            $rootScope.authenticated = false;
            $rootScope.userName = "";
            deleteCookie('authenticated');
            deleteCookie('PLAY_SESSION');
            $location.url('/logout');
        };

        function deleteCookie(name) {
            $cookies.remove(name);
            document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
        }

        $rootScope.isAuthenticated = function () {
            return ($cookies.get('authenticated') === "true")
        };

        $rootScope.isAdmin = function () {
            return ($cookies.get('role') === "admin")
        };

        $scope.$on(filterTabEvent + 'Emit', function (event, data) {
            event.stopPropagation();
            $scope.$broadcast(filterTabEvent + 'Broadcast', data)
        });

        $scope.$on(getAllTranscriptsEvent + 'Emit', function (event, name) {
            event.stopPropagation();
            $scope.$broadcast(getAllTranscriptsEvent + 'Broadcast', name)
        });

        $scope.$on(loggedInEvent + 'Emit', function (event, data) {
            event.stopPropagation();
            getColumnsList();
        });

        init.bind(this)();
    }]);

angular.module('Controllers').controller('HeaderController', ['$scope', '$location',
    function ($scope, $location) {
        $scope.isActive = function (viewLocation) {
            return viewLocation === $location.path();
        };
    }
]);