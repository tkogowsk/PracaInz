angular.module('Repositories').factory('Transcript', function ($resource) {
    return $resource('', null, {
        getTranscriptData: {
            method: 'GET', url: '/getTranscriptData/:sample_fake_id/:userName', isArray: false,
            params: {
                sample_fake_id: '@sample_fake_id',
                userName: '@userName'
            }
        },
        getByFilter: {
            method: 'GET',
            url: '/getByFilter/:formName/1',
            isArray: false,
            params: {
                formName: '@formName',
            }
        },
        getByTab: {method: 'POST', url: '/getByTab', isArray: false},

    });
});
