angular.module('Repositories').factory('Transcript', function ($resource) {
    return $resource('', null, {
        getAllJDBC: {method: 'GET', url: '/getAllJDBC', isArray: false},
        getByFilter: {
            method: 'GET',
            url: '/getByFilter/:formName/1',
            isArray: false,
            params: {
                formName: '@formName',
            }
        }
    });
});
