angular.module('Repositories').factory('User', function ($resource) {
    return $resource('', null, {
        getUserSamplesList: {method: 'GET', url: '/getUserSamplesList', isArray: false},
        getUsersList: {method: 'GET', url: '/getUsersList', isArray: false},
        getSamplesIdsList: {method: 'GET', url: '/getSamplesIdsList', isArray: false},
        setUserPrivilegesList: {
            method: 'POST', url: '/userPrivilegesList/:userId', isArray: false,
            params: {userId: '@userId'}
        },
        getUserPrivilegesList: {
            method: 'GET', url: '/userPrivilegesList/:userId', isArray: false,
            params: {userId: '@userId'}
        },
        getVisibleColumns:  {method: 'GET', url: '/getVisibleColumns', isArray: false},
        saveVisibleColumns:  {method: 'POST', url: '/saveVisibleColumns', isArray: false}
    });
});
