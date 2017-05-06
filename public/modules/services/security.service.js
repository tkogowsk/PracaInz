angular.module('Repositories').factory('Authentication', function ($resource) {
    return $resource('', null, {
        logIn: {method: 'POST', url: '/logIn', isArray: false}
    });
});
