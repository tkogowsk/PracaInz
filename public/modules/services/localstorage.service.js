angular.module('LocalStorageModule', []);
angular.module('LocalStorageModule').factory('LocalStorage', function () {

    function getColumnList() {
        return JSON.parse(localStorage.getItem("columnList"));
    }

    function setColumnList(data) {
        localStorage.setItem("columnList", JSON.stringify(data));
    }

    return {
        getColumnList: getColumnList,
        setColumnList: setColumnList
    }
});
