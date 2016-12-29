angular.module('singleTranscript', ['ngResource']);

angular.module('singleTranscript').controller('SingleTranscriptController', ['$scope', '$log', 'Transcript',
    function ($scope, $log, Transcript) {
        Transcript.get(function(data){
            $log.log(data);
        });

    }]);


angular.module('singleTranscript').factory('Transcript', function($resource) {
    return $resource('/getTranscript');
});

