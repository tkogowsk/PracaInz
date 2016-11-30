var search = angular.module('search', ['ngResource']);

search.controller('SearchController', ['$scope', '$log',
    function ($scope, $log) {
        $log.log('SearchController');
    }]);
