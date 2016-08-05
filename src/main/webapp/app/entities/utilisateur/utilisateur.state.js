(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('utilisateur', {
            parent: 'entity',
            url: '/utilisateur?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Utilisateurs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/utilisateur/utilisateurs.html',
                    controller: 'UtilisateurController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('utilisateur-detail', {
            parent: 'entity',
            url: '/utilisateur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Utilisateur'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/utilisateur/utilisateur-detail.html',
                    controller: 'UtilisateurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Utilisateur', function($stateParams, Utilisateur) {
                    return Utilisateur.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('utilisateur.new', {
            parent: 'utilisateur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/utilisateur/utilisateur-dialog.html',
                    controller: 'UtilisateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                utilisateurId: null,
                                nom: null,
                                prenom: null,
                                email: null,
                                phoneNumber: null,
                                dateDeNaisasnce: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('utilisateur', null, { reload: true });
                }, function() {
                    $state.go('utilisateur');
                });
            }]
        })
        .state('utilisateur.edit', {
            parent: 'utilisateur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/utilisateur/utilisateur-dialog.html',
                    controller: 'UtilisateurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Utilisateur', function(Utilisateur) {
                            return Utilisateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('utilisateur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('utilisateur.delete', {
            parent: 'utilisateur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/utilisateur/utilisateur-delete-dialog.html',
                    controller: 'UtilisateurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Utilisateur', function(Utilisateur) {
                            return Utilisateur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('utilisateur', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
