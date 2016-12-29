angular.module('Repositories').factory('Transcript', function($resource) {
    return $resource('', null, {
        getAll: {method: 'GET', url: '/getTranscript', isArray: false},
        getByFilter: {method: 'GET', url: '/getByFilter/1/1', isArray: false}
    });
});
