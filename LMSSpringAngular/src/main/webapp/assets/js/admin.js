$( document ).ready(function() {
	window.view = "searchAuthors";
	window.select = "author";
	window.pageCount = 1;
	window.curPage = 1;
	window.searchString = "";
	window.user = -1;
	window.mode = "editDomain";
	searchDomain("",0);
	window.addDomain = $("#addNewAuthor");
	$('li[role="presentation"]').click(function(e){
		e.preventDefault();
		$(".active").removeClass("active");		
		$(this).addClass("active");
		view = $(this).find("a").attr("class");
		console.log("view :"+view);
		$("#myTable").empty();
		curPage = 1;
		searchString = "";
		if(view ==="viewLoans"){
			$("#searchResult").hide();
			$("#loanManager").show();
			
		}
		else{
			searchDomain(searchString,curPage-1);
		}
		
		
	});
	$('#dp1').datepicker({
	    startDate: '+0d',
	    endDate: '+7d',
	    todayHighlight: true
	})
	$('#addNewButton').click(function(e){
		event.preventDefault();
		console.log("fetching from "+addDomain.attr("href"));
		addDomain.click();		
	});

	$('#searchButton').click(function(e){
		event.preventDefault();
		$("#searchBox").submit();

	});
	$('body').on('hidden.bs.modal', '.modal', function () {
		$(this).removeData('bs.modal');
	});

	$("#searchBox").submit(function( event ) {
		event.preventDefault();
		searchString = $("#searchString").val();
		curPage = 1;
		searchDomain(searchString,curPage-1);

	});
	$("#cardNoForm").submit(function( event ) {
		event.preventDefault();
		alert("hello there");

	});
	
	
	$("#pagination").on("click","#left",function(){
		event.preventDefault();
		var classList = this.className
		if(!classList.includes("disabled")){
			curPage -= 1;
			searchDomain(searchString,curPage-1);

		}
	});
	$("#pagination").on("click","#right",function(){
		event.preventDefault();
		var classList = this.className;
		if(!classList.includes("disabled")){
			curPage += 1;
			searchDomain(searchString,curPage-1);

		}
	});
	$("#pagination").on("click",".pageNumber",function(){
		event.preventDefault();
		curPage = parseInt($(this).text(), 10);
		searchDomain(searchString,curPage-1);
	});

});

function search(query,pageNum,searchURL,handleData){
	var res = [];
	$.ajax({
		url: searchURL,
		type: 'POST',
		data: {
			pageNum: pageNum,
			searchString: query
		}
	}).done(function(res) {
		var JSONData = JSON.parse(res);
		console.log(JSONData);
		handleData(JSONData);
	});
	
}

function searchDomain( query, pageNum, displayMode){
	$("#loanManager").hide();
	if (view === "searchAuthors"){
		addDomain = $("#addNewAuthor");	
		search(query,pageNum,view,displayAuthors);		
	}
	else if(view ==="searchBooks"){
		addDomain = $("#addNewBook");	
		search(query,pageNum,view,displayBooks);
	}

	else if(view ==="searchBorrowers"){
		addDomain = $("#addNewBorrower");	
		search(query,pageNum,view,displayBorrowers);
	}
	else if(view ==="searchLibraries"){
		addDomain = $("#addNewBranch");	
		search(query,pageNum,view,displayLibraries);
	}
	else if(view ==="searchPublishers"){
		addDomain = $("#addNewPublisher");	
		search(query,pageNum,view,displayPublishers);
	}

	else if(view ==="searchGenres"){
		addDomain = $("#addNewGenre");	
		search(query,pageNum,view,displayGenres);
	}
	
}

function getDisplayText(info,field){
	if(info.length>0){
		var res = info[0][field];
		var j = 1;
		while(j < info.length){
			if (j == info.length-1){
				res +=" and ";
			}
			else{
				res+=", ";
			}
			res += info[j][field];
			j += 1;
		}
		return res;
	}
	else{
		return "---";
	}	
}


function displayAuthors(info){
	var	data = info [0];
	pageCount = info[1];
	$("#myTable").empty();
	$("#searchResult").show();
	var table = $("#myTable");
	var header= document.createElement("tr");
	header.innerHTML = "<th>Author Name</th><th></th>";
	table.append(header);
	for(i= 0; i<data.length; i++){
		var  id = data[i]["authorId"];
		var name = data[i]["authorName"];
		var row= document.createElement("tr");
		var inner = "<td>" +name+"</td>";
				
		inner = inner + "<td>" +
		"<ul>" +
		"<li class='dropdown'><a class='dropdown-toggle' data-toggle='dropdown' href='#' aria-expanded='false'> Action <span class='caret'></span></a>	" +
		"<ul class='dropdown-menu'>" +
		"<li><a data-toggle='modal' data-target='#myModal1' href='addAuthor.jsp?id="+id+"'>Edit</a></li>" +
		"<li><a href='deleteAuthor.jsp?id="  +id+"'>Delete</a></li>" +
		"</ul></li></ul>" +
		"</td>";			
		row.innerHTML = inner;
		
		table.append(row);
	}
	displayPagination();
}

function displayBooks(info){
	var	data = info [0];
	pageCount = info[1];
	$("#myTable").empty();
	$("#searchResult").show();
	$("#addNew").attr('href', "addBook.jsp");
	var table = $("#myTable");
	var header= document.createElement("tr");
	header.innerHTML = "<th> Title</th><th>Author</th><th>Publisher</th><th>Genre</th><th><th></th>";
	table.append(header);

	for(i= 0; i<data.length; i++){
		
		var id = data[i]["bookId"];
		var title = data[i]["title"];
		var publisher = data[i]["publisher"];
		var authors = data[i]["authors"];
		var genres = data[i] ["genres"];		
		var pubName = "---";

		if(publisher!==null){
			pubName = publisher["publisherName"];
		}
		var authorName = getDisplayText(authors,"authorName");
		var genre = getDisplayText(genres,"genreName");


		var row= document.createElement("tr");
		var inner = "<td>" +title+"</td><td>"+authorName+"</td><td>"+pubName+"</td><td>"+genre+"</td>";
		inner = inner + "<td>" +
		"<ul>" +
		"<li class='dropdown'><a class='dropdown-toggle' data-toggle='dropdown' href='#' aria-expanded='false'> Action <span class='caret'></span></a>	" +
		"<ul class='dropdown-menu'>" +
		"<li><a data-toggle='modal' data-target='#myModal1' href='addBook.jsp?id="+id+"'>Edit</a></li>" +
		"<li><a href='deleteBook.jsp?id="  +id+"'>Delete</a></li>" +
		"</ul></li></ul>" +
		"</td>";
		row.innerHTML = inner;
		table.append(row);
	}
	displayPagination();
}

function displayBorrowers(data){
	var info = data [0];
	pageCount = data[1];
	$("#myTable").empty();
	$("#searchResult").show();
	var table = $("#myTable");
	var header= document.createElement("tr");
	header.innerHTML = "<th>Name</th><th>Address</th><th>Phone</th><th><th></th>";
	table.append(header);
	for (i = 0; i <info.length; i++){
		var row= document.createElement("tr");
		var name = info[i]["name"];
		var phone = info[i]["phone"];
		var address = info[i]["address"];
		var id = info[i]["cardNo"];

		var row= document.createElement("tr");
		var inner = "<td>" +name+"</td><td>"+address+"</td><td>"+phone+"</td>";
		inner = inner + "<td>" +
		"<ul>" +
		"<li class='dropdown'><a class='dropdown-toggle' data-toggle='dropdown' href='#' aria-expanded='false'> Action <span class='caret'></span></a>	" +
		"<ul class='dropdown-menu'>" +
		"<li><a data-toggle='modal' data-target='#myModal1' href='addBorrower.jsp?id="+id+"'>Edit</a></li>" +
		"<li><a href='deleteBorrower.jsp?id="  +id+"'>Delete</a></li>" +
		"</ul></li></ul>" +
		"</td>";

		row.innerHTML = inner;
		table.append(row);
	}	
	displayPagination();
}
function displayLibraries(data){
	var info = data [0];
	pageCount = data[1];
	$("#myTable").empty();
	$("#searchResult").show();	
	var table = $("#myTable");
	var header= document.createElement("tr");
	header.innerHTML = "<th>Name</th><th>Address</th><th></th>";
	table.append(header);
	for (i = 0; i <info.length; i++){
		var row= document.createElement("tr");
		var name = info[i]["branchName"];
		var address =info[i]["address"];
		var id = info[i]["branchId"];

		var row= document.createElement("tr");
		var inner = "<td>" +name+"</td><td>"+address+"</td>";
		inner = inner + "<td>" +
		"<ul>" +
		"<li class='dropdown'><a class='dropdown-toggle' data-toggle='dropdown' href='#' aria-expanded='false'> Action <span class='caret'></span></a>	" +
		"<ul class='dropdown-menu'>" +
		"<li><a data-toggle='modal' data-target='#myModal1' href='addBranch.jsp?id="+id+"'>Edit</a></li>" +
		"<li><a href='deleteBorrower.jsp?id="  +id+"'>Delete</a></li>" +
		"</ul></li></ul>" +
		"</td>";

		row.innerHTML = inner;
		table.append(row);
	}	
	displayPagination();
}
function displayPublishers(data){
	var info = data[0];
	pageCount = data[1];
	$("#myTable").empty();
	$("#searchResult").show();
	var table = $("#myTable");
	var header= document.createElement("tr");
	header.innerHTML = "<th>Name</th><th>Address</th><th>Phone</th><th></th>";
	table.append(header);
	for (i = 0; i <info.length; i++){
		var row= document.createElement("tr");
		var id = info[i]["publisherId"];
		var name = info[i]["publisherName"];
		var address =info[i]["publisherAddress"];
		var phone = info [i]["publisherPhone"];

		var row= document.createElement("tr");
		var inner = "<td>" +name+"</td><td>"+address+"</td><td>"+phone+"</td>";
		inner = inner + "<td>" +
		"<ul>" +
		"<li class='dropdown'><a class='dropdown-toggle' data-toggle='dropdown' href='#' aria-expanded='false'> Action <span class='caret'></span></a>	" +
		"<ul class='dropdown-menu'>" +
		"<li><a data-toggle='modal' data-target='#myModal1' href='addPublisher.jsp?id="+id+"'>Edit</a></li>" +
		"<li><a href='deletePublisher.jsp?id="  +id+"'>Delete</a></li>" +
		"</ul></li></ul>" +
		"</td>";

		row.innerHTML = inner;
		table.append(row);
		displayPagination();
	}
}
function displayGenres(data){
	var info  = data[0];
	pageCount = data[1];
	$("#myTable").empty();
	$("#searchResult").show();
	var table = $("#myTable");
	var header= document.createElement("tr");
	header.innerHTML = "<th>Name</th><th></th>";
	table.append(header);
	for (i = 0; i <info.length; i++){

		var row= document.createElement("tr");
		var id = info[i]["genreId"];
		var name = info[i]["genreName"];

		var row= document.createElement("tr");
		var inner = "<td>" +name+"</td>";
		inner = inner + "<td>" +
		"<ul>" +
		"<li class='dropdown'><a class='dropdown-toggle' data-toggle='dropdown' href='#' aria-expanded='false'> Action <span class='caret'></span></a>	" +
		"<ul class='dropdown-menu'>" +
		"<li><a data-toggle='modal' data-target='#myModal1' href='addGenre.jsp?id="+id+"'>Edit</a></li>" +
		"<li><a href='deleteGenre.jsp?id="  +id+"'>Delete</a></li>" +
		"</ul></li></ul>" +
		"</td>";

		row.innerHTML = inner;
		table.append(row);
	}
}
function displayLoanManagement(info){}
function displayPagination(){
	var pagination = $("#pagination");
	pagination.empty();
	var left= document.createElement("li");
	left.innerHTML = "<a href='#'> - </a>";
	if (curPage == 1){

		left.className = "disabled";
	}
	left.setAttribute("id", "left");
	var range= Math.min(4,pageCount);
	pagination.append(left);
	var center =curPage;	
	var start = Math.max(1,center - Math.max(0, Math.floor((range-1)/2)));
	var limit = Math.min(pageCount,start+range);
	while(start<limit+1){
		console.log(start);
		var page = document.createElement("li");
		page.innerHTML = "<a href='#'>"+start+"</a>";
		var classname = "pageNumber";
		if (curPage == start){
			classname += " active";
		}
		page.className= classname;
		start += 1;		
		pagination.append(page);
	}
	var right= document.createElement("li");
	right.innerHTML = "<a href='#'> + </a>";
	if (curPage == pageCount || curPage == 5){
		right.className = "disabled";
	}
	right.setAttribute("id", "right");
	pagination.append(right);	
}
