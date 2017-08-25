angular.module('Repositories').factory('Fields', function ($resource) {
    return $resource('', null, {
        getFields: {
            method: 'GET', url: '/getFields/:sampleFakeId', isArray: false,
            params: {
                sampleFakeId: '@sampleFakeId'
            }
        },
        count: {method: 'POST', url: '/count', isArray: false},
        save: {
            method: 'POST', url: '/saveUserFields', isArray: false
        }
    });
});
