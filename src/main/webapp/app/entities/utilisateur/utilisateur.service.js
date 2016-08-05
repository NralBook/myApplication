(function() {
    'use strict';
    angular
        .module('myApp')
        .factory('Utilisateur', Utilisateur);

    Utilisateur.$inject = ['$resource', 'DateUtils'];

    function Utilisateur ($resource, DateUtils) {
        var resourceUrl =  'api/utilisateurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateDeNaisasnce = DateUtils.convertDateTimeFromServer(data.dateDeNaisasnce);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
