var myApp = angular.module('app', ['ui.router', 'ui.bootstrap', 'Controllers', 'filter', 'Repositories', 'Models', 'transcripts', 'Security', 'Admin',
    'LocalStorageModule', 'angularUtils.directives.dirPagination', 'ngCookies', 'isteven-multi-select']);

myApp.config(function ($stateProvider, $locationProvider, $urlRouterProvider) {

    $stateProvider
        .state('transcript-table', {
            url: '/list/:fakeId',
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
            templateUrl: '/assets/modules/pages/admin/upload/index.html'
        })
        .state('privileges', {
            url: '/privileges/:userId',
            templateUrl: '/assets/modules/pages/admin/privileges/index.html'
        });

    $urlRouterProvider.otherwise('/list');

    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });

});

myApp.config(['$qProvider', function ($qProvider) {
    $qProvider.errorOnUnhandledRejections(false);
}]);

