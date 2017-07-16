angular.module('Repositories').factory('User', function ($resource) {
    return $resource('', null, {
        getSamplesList: {method: 'GET', url: '/getSamplesList', isArray: false},
        getUsersList: {method: 'GET', url: '/getUsersList', isArray: false}
    });
});
