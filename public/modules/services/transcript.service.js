angular.module('Repositories').factory('Transcript', function ($resource) {
    return $resource('', null, {
        getTranscriptData: {
            method: 'GET', url: '/getTranscriptData/:sampleFakeId', isArray: false,
            params: {
                sampleFakeId: '@sampleFakeId'
            }
        },
        getTranscriptDataWithPagination: {
            method: 'POST', url: '/getTranscriptDataWithPagination/:sampleFakeId/:offset', isArray: true,
            params: {
                sampleFakeId: '@sampleFakeId',
                offset: '@offset'
            }
        },
        getByTab: {method: 'POST', url: '/getByTab', isArray: false}
    });
});
