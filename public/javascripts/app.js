var myApp = angular.module('app', ['ui.router', 'ui.bootstrap', 'mainPage', 'filter', 'Repositories', 'Models', 'ngAnimate', 'transcripts']);
myApp.config(function ($stateProvider, $locationProvider, $urlRouterProvider) {

    $stateProvider
        .state('index', {
            url: '/',
            templateUrl: '/assets/modules/index.html'
        })

    $urlRouterProvider.otherwise('/');

    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
});


