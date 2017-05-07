angular.module('Repositories').factory('Fields', function ($resource) {
    return $resource('', null, {
        getFields: {
            method: 'GET', url: '/getFields/:sample_fake_id/:userName', isArray: false,
            params: {
                sample_fake_id: '@sample_fake_id',
                userName: '@userName'
            }
        }
    });
});
