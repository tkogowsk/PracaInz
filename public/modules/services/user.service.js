angular.module('Repositories').factory('User', function ($resource) {
    return $resource('', null, {
        getSamplesList: {method: 'POST', url: '/getSamplesList', isArray: false}
    });
});
