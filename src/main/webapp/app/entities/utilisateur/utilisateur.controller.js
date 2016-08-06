(function() {
	'use strict';

	angular.module('myApp').controller('UtilisateurController',
			UtilisateurController);

	UtilisateurController.$inject = [ '$scope', '$state', 'Utilisateur',
			'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants','ModificationUtilisateur','$window' ];

	function UtilisateurController($scope, $state, Utilisateur, ParseLinks,
			AlertService, pagingParams, paginationConstants, ModificationUtilisateur,$window) {
		var vm = this;

		vm.loadPage = loadPage;
		vm.predicate = pagingParams.predicate;
		vm.reverse = pagingParams.ascending;
		vm.transition = transition;
		vm.itemsPerPage = paginationConstants.itemsPerPage;
		vm.remove = remove;
		vm.add = add;
		vm.save = save;

		loadAll();

		function loadAll() {
			Utilisateur.query({
				page : pagingParams.page - 1,
				size : vm.itemsPerPage,
				sort : sort()
			}, onSuccess, onError);
			function sort() {
				var result = [ vm.predicate + ','
						+ (vm.reverse ? 'asc' : 'desc') ];
				if (vm.predicate !== 'id') {
					result.push('id');
				}
				return result;
			}
			function onSuccess(data, headers) {
				vm.links = ParseLinks.parse(headers('link'));
				vm.totalItems = headers('X-Total-Count');
				vm.queryCount = vm.totalItems;
				// Données initiales
				if (data) {
					for (var i = 0; i < data.length; i++) {
						data[i].added = false;
						data[i].removed = false;
					}
				}
				vm.utilisateurs = data;
				vm.page = pagingParams.page;
			}
			function onError(error) {
				AlertService.error(error.data.message);
			}
		}

		function loadPage(page) {
			vm.page = page;
			vm.transition();
		}

		function transition() {
			$state.transitionTo($state.$current, {
				page : vm.page,
				sort : vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
				search : vm.currentSearch
			});
		}

		function add(nomField, prenomField) {

			vm.utilisateurs.push({
				nom : nomField,
				prenom : prenomField,
				added : true,
				removed : false
			})
			console.log(nomField, prenomField, "added");
		}

		function remove(index) {
			if(vm.utilisateurs[index].added==false){
				vm.utilisateurs[index].removed = !vm.utilisateurs[index].removed;
			}else{
				vm.utilisateurs.splice(index,1);
			}
			console.log(index, "removed");
		}
		
		function onSaveSuccess (result) {
			console.log(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
		
		function save(){
			ModificationUtilisateur.update(vm.utilisateurs);
			AlertService.success("Modifications enregistrées");
			console.log("modification saved");
			$window.location.reload();
		}
	}
})();
