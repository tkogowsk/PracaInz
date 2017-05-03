angular.module('Repositories').factory('VariantColumn', function($resource) {
    return $resource('', null, {
        getVariantColumn: {method: 'GET', url: '/getVariantColumn', isArray: false}
    });
});
