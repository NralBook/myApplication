(function() {
    'use strict';
    angular
        .module('myApp')
        .factory('ModificationUtilisateur', ModificationUtilisateur);

    ModificationUtilisateur.$inject = ['$resource'];

    function ModificationUtilisateur ($resource) {
        var resourceUrl =  'api/sendItems';

        return $resource(resourceUrl, {}, {
            'update': { method:'PUT' }
        });
    }
})();