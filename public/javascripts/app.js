var myApp = angular.module('app', ['ui.router', 'mainPage', 'singleTranscript']);
myApp.config(function ($stateProvider, $locationProvider, $urlRouterProvider) {

    $stateProvider
        .state('index', {
            url: '/',
            templateUrl: '/assets/modules/index.html',
            controller: 'MainPageController'
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
        console.log('MainController')
    }]);