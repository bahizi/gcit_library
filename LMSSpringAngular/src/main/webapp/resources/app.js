var libraryModule = angular.module('libraryApp', [ 'ngRoute', 'ngCookies','ngTable' ]);
libraryModule.filter("sanitize", ['$sce', function($sce) {
	return function(htmlCode){
		return $sce.trustAsHtml(htmlCode);		
	}	

}]);
libraryModule.config([ "$routeProvider", function($routeProvider) {
	return $routeProvider.when("/", {
		redirectTo : "/home"
	}).when("/admins", {
		redirectTo : "/admin/listAuthors"
	}).when("/home", {
		templateUrl : "home.html"
	}).when("/listAuthors", {
		templateUrl : "listAuthors.html"
	}).when("/admin", {
		templateUrl : "listAuthors.html"
	}).when("/admin/listAuthors", {
		templateUrl : "listAuthors.html"
	}).when("/admin/listBooks", {
		templateUrl : "listBooks.html"
	}).when("/admin/listBorrowers", {
		templateUrl : "listBorrowers.html"
	}).when("/admin/listBranches", {
		templateUrl : "listBranches.html"
	}).when("/admin/listPublishers", {
		templateUrl : "listPublishers.html"
	}).when("/admin/listGenres", { 
		templateUrl : "listGenres.html"
	}).when("/admin/manageLoans", {
		templateUrl : "loanManager.html"
	}).when("/previewBook/:bookId/:branchId/:cardNo", {
		templateUrl : "viewBook.html"
	}).when("/userLogin", {
		templateUrl : "userLogin.html"
	}).when("/librarianLogin", {
		templateUrl : "libraryManagement.html"
	})

} ]);

libraryModule.controller('authorCtrl', function($scope, $http, $cookieStore){
	angular.element(document).ready(function () {
		$("#myModal").addClass("modal-sm");

		$scope.search($scope.searchQuery,$scope.curPage);        
	});	
	$scope.searchQuery = "*";
	$scope.curPage = 0;
	$scope.nameIn = "";
	$scope.curDomain = {};
	$scope.authorName ="";
	$scope.showModal = false;
	//$scope.pageCount=1;
	$scope.submit = function submit(){		
		$scope.searchQuery = $("#searchBox").val();			 
		$scope.search($scope.searchQuery,$scope.curPage);
	}
	$scope.search = function search(query,pageNum){
		if(query ==""){
			query = "*";
		}
		if(pageNum<0){
			pageNum = 0;
		}
		$http.get('http://localhost:8080/lms/authors/search/'+query+'/'+pageNum)
		.success(
				function(data) {
					$scope.info = extractInfo(data);
				}
		);
	}
	$scope.manageAuthor = function manageAuthor(author){
		$scope.nameIn = "";
		$scope.action =  add;
		$scope.curDomain = author;
		if(author!=null){ 
			$scope.nameIn =author["authorName"];
			$("#inputName").val($scope.nameIn);
			$scope.modelTitle ="Edit Author";
			$scope.action =  edit;
		}		
		$scope.toggleModal();
	};
	function add(){
		var name = $("#inputName").val(); 
		var toAdd = {"authorId": null, "authorName": name, "books" : []};
		var res = $http.post('http://localhost:8080/lms/authors/add',toAdd);
		//console.log(toAdd);
		res.success(function(data) {
			if(data["ok"]==true){
				var id = data["id"];
				$scope.toggleModal();
				$scope.info[id] = toAdd;
			}
		});	
	}
	function edit(){		
		$scope.curDomain["authorName"] = $("#inputName").val();
		console.log($scope.curDomain);
		//$scope.info[$scope.curDomain["id"]] = $scope.curDomain;
		console.log($scope.info);
		var res = $http.post('http://localhost:8080/lms/author/edit',$scope.curDomain);
		res.success(function(data) {
			if(data["ok"]==true){
				$scope.info[$scope.curDomain["authorId"]] = $scope.curDomain;
				$scope.toggleModal();
			}
		});		
	}
	$scope.del = function del(id){
		$http.get('http://localhost:8080/lms/author/delete/'+id)
		.success(
				function(data) {
					if(data=="true"){						
						console.log("Deleted "+$scope.info[id]["authorName"]);
						delete $scope.info[id];
					}					
				}
		);

	}

	//helper methods
	function extractInfo(data){
		var res = {};
		for (a in data){
			var id= data[a]["authorId"];
			var value = data[a];
			res[id]= value;			
		}
		console.log(res);	
		return res;
	}


	$scope.toggleModal = function(){
		$scope.showModal = !$scope.showModal;
		if($scope.showModal==false){//reset the modal
			$scope.nameIn ="";
			$("#inputName").val($scope.nameIn);
			$scope.modelTitle ="New Author";
			$("#save").prop('disabled', true);
		} 	
	};		


	$scope.checkInput = function checkInput(){		
		$scope.nameIn = $("#inputName").val();
		if($scope.nameIn.length<1){			
			$("#save").prop('disabled', true);
		}
		else{
			$("#save").prop('disabled', false);
		}		
	}
});

libraryModule.controller('bookCtrl', function($scope, $http, $cookieStore) {
	angular.element(document).ready(function () {
		$scope.search($scope.searchQuery,$scope.curPage);     
		//$("#myModal").addClass("modal-lg");
	});	
	$scope.searchQuery = "*";
	$scope.curPage = 0;
	$scope.showModal = false;
	//$scope.pageCount=1;

	//controller specific
	$scope.nameIn = "";
	$scope.curDomain = {};
	$scope.authorName ="";

	$scope.submit = function submit(){		
		$scope.searchQuery = $("#searchBox").val();			 
		$scope.search($scope.searchQuery,$scope.curPage);
	}
	$scope.search = function search(query,pageNum){
		if(query ==""){
			query = "*";
		}
		if(pageNum<0){
			pageNum = 0;
		}
		$http.get('http://localhost:8080/lms/books/search/'+query+'/'+pageNum)
		.success(
				function(data) {
					$scope.info = extractInfo(data);
				}
		);
	}
	$scope.manageDomain = function manageDomain(domain){
		$scope.nameIn = "";
		$scope.action =  add;
		$scope.curDomain = domain;
		$scope.modelTitle ="New Book";
		if(domain!=null){ 
			$scope.nameIn =domain["title"];
			$("#inputName").val($scope.nameIn);
			$("#inputISBN").val(domain["isbn"]);
			$("#inputAuthor").val(delimit(domain["authors"],"authorName"));
			(domain["publisher"]!=null) && ($("#inputPublisher").val(domain["publisher"]["publisherName"]));
			$("#inputGenre").val(delimit(domain["genres"],"genreName"));
			$scope.modelTitle ="Edit Book";
			$scope.action =  edit;
		}		
		$scope.toggleModal();
	};
	function add(){
		var name = $("#inputName").val(); 
		var address = $("#inputAddress").val(); 
		var phone = $("#inputPhone").val(); 
		var toAdd = {"cardNo": null, "name": name, "address" : address, "phone" :phone };
		console.log(toAdd);
		var res = $http.post('http://localhost:8080/lms/borrowers/add',toAdd);

		res.success(function(data) { 	
			if(data["ok"]==true){				
				var id = data["id"];
				$scope.toggleModal();
				$scope.info[id] = toAdd;
			}
		});	
	}	
	function getGID(isbn, next){
		console.log(isbn);
		$http.get('https://www.googleapis.com/books/v1/volumes?q=isbn:'+isbn)
		.success(
				function(data) {
					console.log(data);
					if(data["totalItems"]!="0"){						
						console.log("Updated ISBN and GID");
						console.log($scope.domain);
						$scope.curDomain["isbn"] = isbn
						$scope.curDomain["gid"]= data["items"][0]["id"];
						next();
					}					
				}
		);
	}
	function editGID(then){
		getGID($("#inputISBN").val(), then);
		
	}
	function updateBook(){
		$scope.curDomain["title"] = $("#inputName").val();
		if($("#inputAuthor").val() !== delimit($scope.curDomain["authors"], "authorName")){
			$scope.curDomain["authors"] = [];
			var inputAuthors = $("#inputAuthor").val().split(",");
			for (i = 0;i<inputAuthors.length; i++){
				var genre ={"authorId": null, "authorName": inputAuthors[i]};
				$scope.curDomain["authors"].push(genre);
			}

		}
		$scope.curDomain["publisher"] = {"publisherId":null, "publisherName":$("#inputPublisher").val(), "publisherAddress":null,"publisherPhone":null};
		if($scope.curDomain["publisher"]!=null){
			if($scope.curDomain["publisher"]["publisherName"]!=$("#inputPublisher").val()){
				$scope.curDomain["publisher"] = {"publisherId":null, "publisherName":$("#inputPublisher").val(), "publisherAddress":null,"publisherPhone":null};			
			}
		}
		if($("#inputGenre").val() !== delimit($scope.curDomain["genres"], "genreName")){
			$scope.curDomain["genres"] = [];
			var inputGenres = $("#inputGenre").val().split(",");
			for (i = 0;i<inputGenres.length; i++){
				var genre ={"genreId": null, "genreName": inputGenres[i]};
				$scope.curDomain["genres"].push(genre);
			}

		}	


		console.log("After: ");
		console.log($scope.curDomain);
		var res = $http.post('http://localhost:8080/lms/book/edit',$scope.curDomain);
		res.success(function(data) {
		if(data["ok"]==true){
		$scope.info[$scope.curDomain["bookId"]] = $scope.curDomain;
		$scope.toggleModal();
		}
		});
		
	}
	function edit(){
		console.log("Before: ");
		console.log($scope.curDomain);
		if($("#inputISBN").val() !== $scope.curDomain["isbn"]){
			editGID(updateBook);
		}
		else{
			updateBook();
		}
				
	}
	$scope.del = function del(id){		
		$http.get('http://localhost:8080/lms/books/delete/'+id)
		.success(
				function(data) {
					console.log(data);
					if(data["ok"]==true){						
						console.log("Deleted "+$scope.info[id]["authorName"]);
						delete $scope.info[id];
					}					
				}
		);

	}

	//helper methods
	function extractInfo(data){
		var res = {};
		for (a in data){
			var id= data[a]["bookId"];
			var value = data[a];
			res[id]= value;			
		}
		console.log(res);	
		return res;
	}


	$scope.toggleModal = function(){
		$scope.showModal = !$scope.showModal;
		if($scope.showModal==false){//reset the modal
			$scope.nameIn ="";
			$("#inputName").val($scope.nameIn);
			$scope.modelTitle ="New Borrower";
			$("#save").prop('disabled', true);
		} 	
	};		


	$scope.checkInput = function checkInput(){		
		$scope.nameIn = $("#inputName").val();
		if($scope.nameIn.length<1){			
			$("#save").prop('disabled', true);
		}
		else{
			$("#save").prop('disabled', false);
		}		
	}

	$scope.getDisplayText = function getDisplayText(info, field) {	
		if(info.length>0){
			var res = info[0][field];
			var j = 1;
			while(j < info.length && j<3){
				if (j == info.length-1 || j ==2){
					res +=" and ";
				}
				else{
					res+=", ";
				}
				if( j == 2){
					res += "more";
				}
				else{
					res += info[j][field];						
				}
				j += 1;
			}
			return res;
		}
		else{
			return "---";
		}	
	};
	function delimit(info, field) {
		var j = 0;
		var res="";
		while(j<info.length){
			res+= info[j][field];

			if(j!== info.length-1){
				res+=",";
			}
			j+=1;
		}
		return res;
	}
	function getDomainList(str){
		return str.split(",");
	}


});
libraryModule.controller('borrowerCtrl', function($scope, $http, $cookieStore) {
	angular.element(document).ready(function () {
		$scope.search($scope.searchQuery,$scope.curPage);        
	});	
	$scope.searchQuery = "*";
	$scope.curPage = 0;
	$scope.showModal = false;
	//$scope.pageCount=1;

	//controller specific
	$scope.nameIn = "";
	$scope.curDomain = {};
	$scope.authorName ="";

	$scope.submit = function submit(){		
		$scope.searchQuery = $("#searchBox").val();			 
		$scope.search($scope.searchQuery,$scope.curPage);
	}
	$scope.search = function search(query,pageNum){
		if(query ==""){
			query = "*";
		}
		if(pageNum<0){
			pageNum = 0;
		}
		$http.get('http://localhost:8080/lms/borrowers/search/'+query+'/'+pageNum)
		.success(
				function(data) {
					$scope.info = extractInfo(data);
				}
		);
	}
	$scope.manageDomain = function manageDomain(domain){
		$scope.nameIn = "";
		$scope.action =  add;
		$scope.curDomain = domain;
		$scope.modelTitle ="New Borrower";
		if(domain!=null){ 
			$scope.nameIn =domain["name"];
			$("#inputName").val($scope.nameIn);
			$("#inputAddress").val(domain["address"]);
			$("#inputPhone").val(domain["phone"]);
			$scope.modelTitle ="Edit Borrower";
			$scope.action =  edit;
		}		
		$scope.toggleModal();
	};
	function add(){
		var name = $("#inputName").val(); 
		var address = $("#inputAddress").val(); 
		var phone = $("#inputPhone").val(); 
		var toAdd = {"cardNo": null, "name": name, "address" : address, "phone" :phone };
		console.log(toAdd);
		var res = $http.post('http://localhost:8080/lms/borrowers/add',toAdd);

		res.success(function(data) {
			if(data["ok"]==true){				
				var id = data["id"];
				$scope.toggleModal();
				$scope.info[id] = toAdd;
			}
		});	
	}	

	function edit(){		
		var name = $("#inputName").val(); 
		var address = $("#inputAddress").val(); 
		var phone = $("#inputPhone").val(); 
		var toEdit = {"cardNo": $scope.curDomain["cardNo"], "name": name, "address" : address, "phone" :phone };
		var res = $http.post('http://localhost:8080/lms/borrowers/edit',toEdit);
		res.success(function(data) {
			if(data["ok"]==true){
				$scope.info[$scope.curDomain["cardNo"]] = toEdit;
				$scope.toggleModal();
			}
		});		
	}
	$scope.del = function del(id){		
		$http.get('http://localhost:8080/lms/borrowers/delete/'+id)
		.success(
				function(data) {
					console.log(data);
					if(data["ok"]==true){						
						console.log("Deleted "+$scope.info[id]["authorName"]);
						delete $scope.info[id];
					}					
				}
		);

	}

	//helper methods
	function extractInfo(data){
		var res = {};
		for (a in data){
			var id= data[a]["cardNo"];
			var value = data[a];
			res[id]= value;			
		}
		console.log(res);	
		return res;
	}


	$scope.toggleModal = function(){
		$scope.showModal = !$scope.showModal;
		if($scope.showModal==false){//reset the modal
			$scope.nameIn ="";
			$("#inputName").val($scope.nameIn);
			$scope.modelTitle ="New Borrower";
			$("#save").prop('disabled', true);
		} 	
	};		


	$scope.checkInput = function checkInput(){		
		$scope.nameIn = $("#inputName").val();
		if($scope.nameIn.length<1){			
			$("#save").prop('disabled', true);
		}
		else{
			$("#save").prop('disabled', false);
		}		
	}
});
libraryModule.controller('branchCtrl', function($scope, $http, $cookieStore) {
	angular.element(document).ready(function () {
		$scope.search($scope.searchQuery,$scope.curPage);        
	});	
	$scope.searchQuery = "*";
	$scope.curPage = 0;
	$scope.showModal = false;
	//$scope.pageCount=1;

	//controller specific
	$scope.nameIn = "";
	$scope.curDomain = {};
	$scope.authorName ="";

	$scope.submit = function submit(){		
		$scope.searchQuery = $("#searchBox").val();			 
		$scope.search($scope.searchQuery,$scope.curPage);
	}
	$scope.search = function search(query,pageNum){
		if(query ==""){
			query = "*";
		}
		if(pageNum<0){
			pageNum = 0;
		}
		$http.get('http://localhost:8080/lms/branches/search/'+query+'/'+pageNum)
		.success(
				function(data) {
					$scope.info = extractInfo(data);
				}
		);
	}
	$scope.manageDomain = function manageDomain(domain){
		$scope.nameIn = "";
		$scope.action =  add;
		$scope.curDomain = domain;
		$scope.modelTitle ="New Branch";
		if(domain!=null){ 
			$scope.nameIn =domain["branchName"];
			$("#inputName").val($scope.nameIn);
			$("#inputAddress").val(domain["address"]);
			$scope.modelTitle ="Edit Branch";
			$scope.action =  edit;
		}		
		$scope.toggleModal();
	};
	function add(){
		var name = $("#inputName").val(); 
		var address = $("#inputAddress").val(); 
		var toAdd = {"branchId": null, "branchName": name, "address" : address };
		console.log(toAdd);
		var res = $http.post('http://localhost:8080/lms/branches/add',toAdd);

		res.success(function(data) {
			if(data["ok"]==true){				
				var id = data["id"];
				$scope.toggleModal();
				$scope.info[id] = toAdd;
			}
		});	
	}	

	function edit(){		
		var name = $("#inputName").val(); 
		var address = $("#inputAddress").val();
		var toEdit = {"branchId": $scope.curDomain["branchId"], "branchName": name, "address" : address };
		var res = $http.post('http://localhost:8080/lms/branches/edit',toEdit);
		res.success(function(data) {
			if(data["ok"]==true){
				$scope.info[$scope.curDomain["branchId"]] = toEdit;
				$scope.toggleModal();
			}
		});		
	}
	$scope.del = function del(id){		
		$http.get('http://localhost:8080/lms/branches/delete/'+id)
		.success(
				function(data) {
					console.log(data);
					if(data["ok"]==true){						
						console.log("Deleted "+$scope.info[id]["branchName"]);
						delete $scope.info[id];
					}					
				}
		);

	}

	//helper methods
	function extractInfo(data){
		var res = {};
		for (a in data){
			var id= data[a]["branchId"];
			var value = data[a];
			res[id]= value;			
		}
		console.log(res);	
		return res;
	}


	$scope.toggleModal = function(){
		$scope.showModal = !$scope.showModal;
		if($scope.showModal==false){//reset the modal
			$scope.nameIn ="";
			$("#inputName").val($scope.nameIn);
			$("#inputAddress").val("");
			$scope.modelTitle ="New Borrower";
			$("#save").prop('disabled', true);
		} 	
	};		


	$scope.checkInput = function checkInput(){		
		$scope.nameIn = $("#inputName").val();
		if($scope.nameIn.length<1){			
			$("#save").prop('disabled', true);
		}
		else{
			$("#save").prop('disabled', false);
		}		
	}
});

libraryModule.controller('publisherCtrl', function($scope, $http, $cookieStore) {
	angular.element(document).ready(function () {
		$scope.search($scope.searchQuery,$scope.curPage);        
	});	
	$scope.searchQuery = "*";
	$scope.curPage = 0;
	$scope.showModal = false;
	//$scope.pageCount=1;

	//controller specific
	$scope.nameIn = "";
	$scope.curDomain = {};
	$scope.authorName ="";

	$scope.submit = function submit(){		
		$scope.searchQuery = $("#searchBox").val();			 
		$scope.search($scope.searchQuery,$scope.curPage);
	}
	$scope.search = function search(query,pageNum){
		if(query ==""){
			query = "*";
		}
		if(pageNum<0){
			pageNum = 0;
		}
		$http.get('http://localhost:8080/lms/publishers/search/'+query+'/'+pageNum)
		.success(
				function(data) {
					$scope.info = extractInfo(data);
				}
		);
	}
	$scope.manageDomain = function manageDomain(domain){
		$scope.nameIn = "";
		$scope.action =  add;
		$scope.curDomain = domain;
		$scope.modelTitle ="New Publisher";
		if(domain!=null){ 
			console.log(domain);
			$scope.nameIn =domain["publisherName"];
			$("#inputName").val($scope.nameIn);
			$("#inputAddress").val(domain["publisherAddress"]);
			$("#inputPhone").val(domain["publisherPhone"]);
			$scope.modelTitle ="Edit Publisher";
			$scope.action =  edit;
		}		
		$scope.toggleModal();
	};
	function add(){
		var name = $("#inputName").val(); 
		var address = $("#inputAddress").val(); 
		var phone = $("#inputPhone").val();
		var toAdd =  {"publisherId": null, "publisherName": name, "publisherAddress" : address, "publisherPhone" : phone };
		console.log(toAdd);
		var res = $http.post('http://localhost:8080/lms/publishers/add',toAdd);

		res.success(function(data) {
			if(data["ok"]==true){				
				var id = data["id"];
				$scope.toggleModal();
				$scope.info[id] = toAdd;
			}
		});	
	}	

	function edit(){		
		var name = $("#inputName").val(); 
		var address = $("#inputAddress").val();
		var phone = $("#inputPhone").val();
		var toEdit = {"publisherId": $scope.curDomain["publisherId"], "publisherName": name, "publisherAddress" : address, "publisherPhone" : phone }; 
		var res = $http.post('http://localhost:8080/lms/publishers/edit',toEdit);
		res.success(function(data) {
			if(data["ok"]==true){
				$scope.info[$scope.curDomain["publisherId"]] = toEdit;
				$scope.toggleModal();
			}
		});		
	}
	$scope.del = function del(id){		
		$http.get('http://localhost:8080/lms/publishers/delete/'+id)
		.success(
				function(data) {
					console.log(data);
					if(data["ok"]==true){						
						console.log("Deleted "+$scope.info[id]["publisherName"]);
						delete $scope.info[id];
					}					
				}
		);

	}

	//helper methods
	function extractInfo(data){
		var res = {};
		for (a in data){
			var id= data[a]["publisherId"];
			var value = data[a];
			res[id]= value;			
		}
		console.log(res);	
		return res;
	}


	$scope.toggleModal = function(){
		$scope.showModal = !$scope.showModal;
		if($scope.showModal==false){//reset the modal
			$scope.nameIn ="";
			$("#inputName").val($scope.nameIn);
			$("#inputAddress").val("");
			$("#inputPhone").val("");
			$scope.modelTitle ="New Borrower";
			$("#save").prop('disabled', true);
		} 	
	};		


	$scope.checkInput = function checkInput(){		
		$scope.nameIn = $("#inputName").val();
		if($scope.nameIn.length<1){			
			$("#save").prop('disabled', true);
		}
		else{
			$("#save").prop('disabled', false);
		}		
	}
});
libraryModule.controller('genreCtrl', function($scope, $http, $cookieStore) {
	angular.element(document).ready(function () {
		$scope.search($scope.searchQuery,$scope.curPage);        
	});	
	$scope.searchQuery = "*";
	$scope.curPage = 0;
	$scope.showModal = false;
	//$scope.pageCount=1;

	//controller specific
	$scope.nameIn = "";
	$scope.curDomain = {};
	$scope.authorName ="";

	$scope.submit = function submit(){		
		$scope.searchQuery = $("#searchBox").val();			 
		$scope.search($scope.searchQuery,$scope.curPage);
	}
	$scope.search = function search(query,pageNum){
		if(query ==""){
			query = "*";
		}
		if(pageNum<0){
			pageNum = 0;
		}
		$http.get('http://localhost:8080/lms/genres/search/'+query+'/'+pageNum)
		.success(
				function(data) {
					$scope.info = extractInfo(data);
				}
		);
	}
	$scope.manageDomain = function manageDomain(domain){
		$scope.nameIn = "";
		$scope.action =  add;
		$scope.curDomain = domain;
		$scope.modelTitle ="New Genre";
		if(domain!=null){ 
			console.log(domain);
			$scope.nameIn =domain["genreName"];
			$("#inputName").val($scope.nameIn);
			$scope.modelTitle ="Edit Genre";
			$scope.action =  edit;
		}		
		$scope.toggleModal();
	};
	function add(){
		var name = $("#inputName").val(); 
		var toAdd =  {"genreId": null, "genreName": name};
		console.log(toAdd);
		var res = $http.post('http://localhost:8080/lms/genres/add',toAdd);

		res.success(function(data) {
			if(data["ok"]==true){				
				var id = data["id"];
				$scope.toggleModal();
				$scope.info[id] = toAdd;
			}
		});	
	}	

	function edit(){		
		var name = $("#inputName").val(); 
		var toEdit = {"genreId": $scope.curDomain["genreId"], "genreName": name};
		var res = $http.post('http://localhost:8080/lms/genres/edit',toEdit);
		res.success(function(data) {
			if(data["ok"]==true){
				$scope.info[$scope.curDomain["genreId"]] = toEdit;
				$scope.toggleModal();
			}
		});		
	}
	$scope.del = function del(id){		
		$http.get('http://localhost:8080/lms/genreId/delete/'+id)
		.success(
				function(data) {
					console.log(data);
					if(data["ok"]==true){						
						console.log("Deleted "+$scope.info[id]["genreName"]);
						delete $scope.info[id];
					}					
				}
		);

	}

	//helper methods
	function extractInfo(data){
		var res = {};
		for (a in data){
			var id= data[a]["genreId"];
			var value = data[a];
			res[id]= value;			
		}
		console.log(res);	
		return res;
	}


	$scope.toggleModal = function(){
		$scope.showModal = !$scope.showModal;
		if($scope.showModal==false){//reset the modal
			$scope.nameIn ="";
			$("#inputName").val($scope.nameIn);
			$scope.modelTitle ="New Borrower";
			$("#save").prop('disabled', true);
		} 	
	};		


	$scope.checkInput = function checkInput(){		
		$scope.nameIn = $("#inputName").val();
		if($scope.nameIn.length<1){			
			$("#save").prop('disabled', true);
		}
		else{
			$("#save").prop('disabled', false);
		}		
	}
});
libraryModule.controller('loanCtrl', function($scope, $http, $cookieStore) {
	$scope.cardNo = 5;
	$scope.branchId = -1;
	$scope.bookId = -1;
	$scope.tableParams = [];

	angular.element(document).ready(function () {
		$('#dp1').datepicker({
			startDate: '+0d',
			endDate: '+7d',
			todayHighlight: true
		})

	});
});

libraryModule.controller('previewCtrl', function($scope, $http, $cookieStore,$rootScope, $routeParams, $route) {
	//"https://www.googleapis.com/books/v1/volumes?q=isbn:0440245923"
	$scope.bookId = $routeParams.bookId;
	$scope.branchId = $routeParams.branchId;
	$scope.cardNo = $routeParams.cardNo;
	$scope.title ="*missing title*";
	$scope.stars = 0;
	$scope.pageCount = "--";
	$scope.description = "**No description available for this book. Sorry**";
	$scope.authors = "--";
	$scope.publisher = "--";
	$scope.genres = "--";
	$scope.thumbnail = "http://www.sylviaday.com/WP/wp-content/uploads/2014/09/temp.jpg";


	$http.get("http://localhost:8080/lms/books/get/"+$scope.bookId)
	.success(
			function(info) {				
				if(info!==null){
					(info["title"]!==null) && ($scope.title = info["title"]);
					(info["publisher"]!=null) && ($scope.publisher = info["publisher"]["publisherName"]);
					(info["authors"].length>0) && ( $scope.authors = info["authors"][0]["authorName"]);
					(info["genres"].length>0) && ($scope.genres = info["genres"][0]["genreName"]);
					if(info["gid"]!=null){
						$scope.gId =info["gid"];
						$http.get("https://www.googleapis.com/books/v1/volumes/"+$scope.gId)
						.success(
								function(gdata) {									
									if(gdata["volumeInfo"]!==null){
										var volumeInfo = gdata["volumeInfo"];
										(volumeInfo["averageRating"]!==null) && ($scope.stars = volumeInfo["averageRating"]);
										(volumeInfo["pageCount"]!==null) && ($scope.pageCount = volumeInfo["pageCount"]);
										(volumeInfo["description"]!==null) && ($scope.description = volumeInfo["description"]);						
										(volumeInfo["imageLinks"]!==null) && ($scope.thumbnail = volumeInfo["imageLinks"]["thumbnail"]);
										(info["publisher"]==null) && ((volumeInfo["publisher"]!==null) && ($scope.publisher = volumeInfo["publisher"]+"*"));
										(info["authors"].length==0) && ((volumeInfo["authors"]!==null) && ($scope.authors = volumeInfo["authors"][0]+"*"));
										(info["genres"].length==0) && ((volumeInfo["categories"]!==null) && ($scope.genres = volumeInfo["categories"][0]+"*"));


									}				
								});	
					}


				}	

			});	


	$http.get("http://localhost:8080/lms/checkLoan/"+$scope.bookId+"/"+$scope.branchId+"/"+$scope.cardNo)
	.success(
			function(info) {
				if(info!==null){
					$scope.canBorrow = info["canBorrow"];
					$scope.canReturn= info["canReturn"];
				}				
			});

	$scope.getStarCount = function getStarCount(cur, total){
		var res = "";
		if(cur>total){
			res =  "glyphicon glyphicon-star-empty";
		}		
		else{
			res =  "glyphicon glyphicon-star";
		}
		return res;
	} ;

	$scope.getDescription = function getDescription(cur, total){
		console.log("getting description");
		$sce.trustAsHtml($scope.description);
		return $scope.description;	
	} ;
	function extractInfo(info,field, idx,def){
		if(info.hasOwnProperty(field)){
			return info[field];
		}
		else{

		}
	}
	angular.element(document).ready(function () {


	});
});
libraryModule.directive('modal', function () {
	return {
		template: '<div class="modal fade">' + 
		'<div id="myModal" class="modal-dialog">' + 
		'<div class="modal-content">' + 
		'<div class="modal-header">' + 
		'<button type="button" ng-click = "toggleModal()"class="close" aria-hidden="true">&times;</button>' + 
		'<h4 class="modal-title">Admin</h4>' + 
		'</div>' + 
		'<div class="modal-body" ng-transclude></div>' +

		'<div class="modal-footer">'+
		'<button type="button" id = "save" ng-click= "action()"class="btn btn-primary modSave" disabled>Save changes</button>'+
		'<button type="button" ng-click = "toggleModal()" class="btn btn-default" >Close</button>'+
		'</div>' +
		'</div>' + 
		'</div>' + 
		'</div>',
		restrict: 'E',
		transclude: true,
		replace:true,
		scope:true,
		link: function postLink(scope, element, attrs) {
			scope.title = attrs.title;

			scope.$watch(attrs.visible, function(value){
				if(value == true)
					$(element).modal('show');
				else
					$(element).modal('hide');
			});

			$(element).on('shown.bs.modal', function(){
				scope.$apply(function(){
					scope.$parent[attrs.visible] = true;
				});
			});

			$(element).on('hidden.bs.modal', function(){
				$(this).removeData('bs.modal');
				scope.$apply(function(){

					scope.$parent[attrs.visible] = false;
				});
			});
		}
	};
});

