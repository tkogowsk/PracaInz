angular.module('Repositories').factory('Fields', function($resource) {
    return $resource('', null, {
        getFields: {method: 'GET', url: '/getFields', isArray: false}
    });
});
