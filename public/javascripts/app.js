var myApp = angular.module('app', ['ui.router', 'search', 'singleTranscript']);
myApp.config(function ($stateProvider, $locationProvider, $urlRouterProvider) {

    $stateProvider
        .state('index', {
            url: '/',
            templateUrl: '/assets/modules/index.html'
        })
        .state('search', {
            url: '/search',
            templateUrl: '/assets/modules/searchPage/index.html',
            controller: 'SearchController'
        })
        .state('transcript', {
            url: '/transcript',
            templateUrl: '/assets/modules/singleTranscript/index.html',
            controller: 'SingleTranscriptController'
        });

    $urlRouterProvider.otherwise('/');

    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
});

myApp.controller('MainController', ['$scope', '$log',
    function ($scope, $log) {
        $log.log('MainController');
    }]);