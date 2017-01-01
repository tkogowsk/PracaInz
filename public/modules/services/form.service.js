angular.module('Repositories').factory('Form', function($resource) {
    return $resource('', null, {
        getUserForms: {method: 'GET', url: '/getUserForms/1', isArray: false},
        editForm: {method: 'POST', url: '/editForm', isArray: false}
    });
});
