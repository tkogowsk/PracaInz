angular.module('Repositories').factory('Transcript', function($resource) {
    return $resource('/getTranscript');
});
