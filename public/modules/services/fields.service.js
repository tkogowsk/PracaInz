angular.module('Repositories').factory('Fields', function ($resource) {
    return $resource('', null, {
        getFields: {
            method: 'GET', url: '/getFields/:sample_fake_id', isArray: false,
            params: {
                sample_fake_id: '@sample_fake_id'
            }
        },
        count: {method: 'POST', url: '/count', isArray: false},
        save: {
            method: 'POST', url: '/saveUserFields', isArray: false
        }
    });
});
