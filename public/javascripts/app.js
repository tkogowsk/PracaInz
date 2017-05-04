var myApp = angular.module('app', ['ui.router', 'ui.bootstrap', 'mainPage', 'filter', 'Repositories', 'Models', 'transcripts', 'security', 'LocalStorageModule']);
myApp.config(function ($stateProvider, $locationProvider, $urlRouterProvider) {

    $stateProvider
        .state('index', {
            url: '/',
            templateUrl: '/assets/modules/pages/transcript/index.html'
        })
        .state('login', {
            url: '/login',
            templateUrl: '/assets/modules/pages/login/index.html'
        });

    $urlRouterProvider.otherwise('/login');

    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
});

/*myApp.config(['$qProvider', function ($qProvider) {
    $qProvider.errorOnUnhandledRejections(false);
}]);*/

