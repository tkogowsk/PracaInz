angular.module('singleTranscript', ['ngResource']);

angular.module('singleTranscript').controller('SingleTranscriptController', ['$scope', '$log', 'Transcript', 'UserFilter',
    function ($scope, $log, Transcript, UserFilter) {

       // $log.log(TranscriptsTableModel);
        Transcript.get(function(data){
            $log.log(data);
        });

        UserFilter.get(function(data){
            $log.log(data);
        });
    }]);


angular.module('singleTranscript').factory('Transcript', function($resource) {
    return $resource('/getTranscript');
});

angular.module('singleTranscript').factory('UserFilter', function($resource) {
    return $resource('/getUserFilter');
});