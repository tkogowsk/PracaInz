var myApp = angular.module('app', ['ui.router', 'ui.bootstrap', 'Controllers', 'filter', 'Repositories', 'Models', 'transcripts', 'Security', 'Admin',
    'LocalStorageModule', 'angularUtils.directives.dirPagination', 'ngCookies']);

myApp.config(function ($stateProvider, $locationProvider, $urlRouterProvider) {

    $stateProvider
        .state('transcript-table', {
            url: '/index/:fakeId',
            templateUrl: '/assets/modules/pages/transcript-table/index.html'
        })
        .state('login', {
            url: '/login',
            templateUrl: '/assets/modules/pages/login/index.html'
        })
        .state('transcript-list', {
            url: '/list',
            templateUrl: '/assets/modules/pages/transcript-list/index.html'
        })
        .state('admin', {
            url: '/admin',
            templateUrl: '/assets/modules/pages/admin/index.html'
        })
        .state('upload', {
            url: '/upload',
            template: ''
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

