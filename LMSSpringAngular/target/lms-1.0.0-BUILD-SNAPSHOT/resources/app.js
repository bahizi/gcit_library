var libraryModule = angular.module('libraryApp', [ 'ngRoute', 'ngCookies' ]);

libraryModule.config([ "$routeProvider", function($routeProvider) {
	return $routeProvider.when("/", {
		redirectTo : "/home"
	}).when("/home", {
		templateUrl : "home.html"
	}).when("/listAuthors", {
		templateUrl : "listAuthors.html"
	}).when("/test", {
		templateUrl : "test.html"
	})
} ]);

libraryModule.controller('authorCtrl', function($scope, $http, $cookieStore) {
	// get all authors and display initially
	$http.get('http://localhost:8080/lms/author/get')
		.success(
			function(data) {
				$scope.authors = data;
				console.log($scope.authors);
			}
		);
	
	$scope.addAuthor = function addAuthor() {
		if($scope.addAuthorFrm.$valid){
		 $http.post('addAuthor/' +$scope.authorName).
		  success(function(data) {
			 alert('Author Added');
			 $scope.authors = data;	
			 showAddAuthor();
		    });
		}
	};
});

libraryModule.controller('bookCtrl', function($scope, $http, $cookieStore) {
	// get all authors and display initially
	$http.get('http://localhost:8080/lms/author/get')
		.success(
			function(data) {
				$scope.authors = data;
				console.log($scope.authors);
			}
		);
	
//	$scope.addAuthor = function addAuthor() {
//		if($scope.addAuthorFrm.$valid){
//		 $http.post('addAuthor/' +$scope.authorName).
//		  success(function(data) {
//			 alert('Author Added');
//			 $scope.authors = data;	
//			 showAddAuthor();
//		    });
//		}
//	};
});

libraryModule.controller('testCtrl', function($scope, $http, $cookieStore) {
	$scope.testVar = $cookieStore.get('sample');
});
