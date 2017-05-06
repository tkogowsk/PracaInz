var myApp = angular.module('app', ['ui.router', 'ui.bootstrap', 'Controllers', 'filter', 'Repositories', 'Models', 'transcripts', 'Security', 'LocalStorageModule']);
myApp.config(function ($stateProvider, $locationProvider, $urlRouterProvider) {

    $stateProvider
        .state('index', {
            url: '/index',
            templateUrl: '/assets/modules/pages/transcript-table/index.html'
        })
        .state('login', {
            url: '/login',
            templateUrl: '/assets/modules/pages/login/index.html'
        })
        .state('transcript-list', {
            url: '/list',
            templateUrl: '/assets/modules/pages/transcript-list/index.html'
        });

    $urlRouterProvider.otherwise('/login');

    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
});

myApp.config(['$qProvider', function ($qProvider) {
    $qProvider.errorOnUnhandledRejections(false);

}]);

