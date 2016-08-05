(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('UtilisateurDetailController', UtilisateurDetailController);

    UtilisateurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Utilisateur'];

    function UtilisateurDetailController($scope, $rootScope, $stateParams, entity, Utilisateur) {
        var vm = this;

        vm.utilisateur = entity;

        var unsubscribe = $rootScope.$on('myApp:utilisateurUpdate', function(event, result) {
            vm.utilisateur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
