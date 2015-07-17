$( document ).ready(function() {
	window.view = "viewAuthors";
	window.pageCount = 1;
	window.curPage = 1;
	window.searchString = "";
	searchMain("",0);
	$('li[role="presentation"]').click(function(e){
		e.preventDefault();
		$(".active").removeClass("active");		
		$(this).addClass("active");
		view = $(this).find("a").attr("class");
		console.log("view :"+view);
		$("#myTable").empty();
		curPage = 1;
		searchString = "";
		searchMain(searchString,curPage-1);	
		alert($("#addNew").attr("href"));		
		
	});
	
	
	
	
	$('#addNewButton').click(function(e){
		event.preventDefault();
		//goToAddPage();
		$('#addNew').click();
		
	});
	
	$('#searchButton').click(function(e){
		event.preventDefault();
		$("#searchBox").submit();
		
	});
	
	$("#searchBox").submit(function( event ) {
		event.preventDefault();
		searchString = $("#searchString").val();
		curPage = 1;
		searchMain(searchString,curPage-1);
		  
		});
	
	$("#pagination").on("click","#left",function(){
		event.preventDefault();
		var classList = this.className
		if(!classList.includes("disabled")){
			curPage -= 1;
			searchMain(searchString,curPage-1);
			
		}
	});
	$("#pagination").on("click","#right",function(){
		event.preventDefault();
		var classList = this.className;
		if(!classList.includes("disabled")){
			curPage += 1;
			searchMain(searchString,curPage-1);
			
		}
	});
	$("#pagination").on("click",".pageNumber",function(){
		event.preventDefault();
		curPage = parseInt($(this).text(), 10);
		searchMain(searchString,curPage-1);
	});
	
});
function search(query, pageNum,searchURL,display){
//	console.log("Searching page #" +pageNum);
	$.ajax({
		  url: searchURL,
		  type: 'POST',
		  data: {
			  pageNum: pageNum,
			  searchString: query
		  }
		}).done(function(res) {
			var JSONData = JSON.parse(res);
			pageCount = JSONData[1];
			$("#myTable").empty();
			display(JSONData[0]);
			console.log("updated pagecount to "+pageCount);
			//displayPagination();
			console.log("Query: '"+query +"' in "+ view +"; "+pageCount+"  pages. CurPage = "+curPage );
		});
	
}
function goToAddPage(){
	if (view === "viewAuthors"){
		window.location.href = "addAuthor.jsp";
			
		}
		else if(view ==="viewBooks"){
			window.location.href = "addBook.jsp";
		}
		
		else if(view ==="viewBorrowers"){
			window.location.href = "addBorrower.jsp";	
		}
		else if(view ==="viewLibraries"){
			window.location.href = "addBranch.jsp";
		}
		else if(view ==="viewPublishers"){
			window.location.href = "addPublisher.jsp";
		}
		else if(view ==="viewGenres"){
			window.location.href = "addGenre.jsp";				
		}
	
}
function searchMain( query, pageNum){
	if (view === "viewAuthors"){
		$("#addNew").attr('href', "addAuthor.jsp");
		  search(query,pageNum,"searchAuthors",displayAuthors);
			
		}
		else if(view ==="viewBooks"){
			$("#addNew").attr('href', "addBook.jsp");
			search(query,pageNum,"searchBooks",displayBooks);
			$('#searchString').val("");
		}
		
		else if(view ==="viewBorrowers"){
			$("#addNew").attr('href', "addBorrower.jsp");
			search(query,pageNum,"searchBorrowers",displayBorrowers);	
		}
		else if(view ==="viewLibraries"){
			$("#addNew").attr('href', "addBranch.jsp");
			search(query,pageNum,"searchLibraries",displayLibraries);
		}
		else if(view ==="viewPublishers"){
			$("#addNew").attr('href', "addPublisher.jsp");
			search(query,pageNum,"searchPublishers",displayPublishers);
		}
		else if(view ==="viewGenres"){
			$("#addNew").attr('href', "addGenre.jsp");
			search(query,pageNum,"searchGenres",displayGenres);				
		}	
	
}
function displayAuthors(data){
$("#addNew").attr('href', "addAuthor.jsp");

var table = $("#myTable");
var header= document.createElement("tr");
header.innerHTML = "<th>Author Id</th><th> Author Name</th><th></th>";
table.append(header);
for(i= 0; i<data.length; i++){
	var  id = data[i]["authorId"];
	var name = data[i]["authorName"];
	var row= document.createElement("tr");
	var inner = "<td>" +id+"</td><td>" +name+"</td>";
	inner = inner + "<td>" +
			"<ul>" +
			"<li class='dropdown'><a class='dropdown-toggle' data-toggle='dropdown' href='#' aria-expanded='false'> Action <span class='caret'></span></a>	" +
			"<ul class='dropdown-menu'>" +
			"<li><a data-toggle='modal' data-target='#myModal1' href='addBook.jsp?id="+id+"'>Edit</a></li>" +
			"<li><a href='deleteAuthor.jsp?id="  +id+"'>Delete</a></li>" +
			"</ul></li></ul>" +
			"</td>";			
	row.innerHTML = inner;
	table.append(row);
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
function displayBooks(data){
	$("#addNew").attr('href', "addBook.jsp");
	var table = $("#myTable");
	var header= document.createElement("tr");
	header.innerHTML = "<th>Id</th><th> Title</th><th>Author</th><th>Publisher</th><th>Genre</th><th><th></th>";
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
		var inner = "<td>" +id+"</td><td>" +title+"</td><td>"+authorName+"</td><td>"+pubName+"</td><td>"+genre+"</td>";
		inner = inner + "<td>" +
				"<ul>" +
				"<li class='dropdown'><a class='dropdown-toggle' data-toggle='dropdown' href='#' aria-expanded='false'> Action <span class='caret'></span></a>	" +
				"<ul class='dropdown-menu'>" +
				"<li><a href='editBook.jsp?id="+id+"'>Edit</a></li>" +
				"<li><a href='deleteBook.jsp?id="  +id+"'>Delete</a></li>" +
				"</ul></li></ul>" +
				"</td>";		
			
			
		row.innerHTML = inner;
		table.append(row);
	}
	}

function displayBorrowers(info){
	//$("#addNew").href = "addAuthor.jsp";
	$("#addNew").attr('href', "addBorrower.jsp");
	var table = $("#myTable");
	var header= document.createElement("tr");
	header.innerHTML = "<th>ID</th><th>Name</th><th>Address</th><th>Phone</th><th><th></th>";
	table.append(header);
	for (i = 0; i <info.length; i++){
		var row= document.createElement("tr");
		var name = info[i]["name"];
		var phone = info[i]["phone"];
		var address = info[i]["address"];
		var id = info[i]["cardNo"];
		
		var row= document.createElement("tr");
		var inner = "<td>" +id+"</td><td>" +name+"</td><td>"+address+"</td><td>"+phone+"</td>";
		inner = inner + "<td>" +
		"<ul>" +
		"<li class='dropdown'><a class='dropdown-toggle' data-toggle='dropdown' href='#' aria-expanded='false'> Action <span class='caret'></span></a>	" +
		"<ul class='dropdown-menu'>" +
		"<li><a href='editBorrower.jsp?id="+id+"'>Edit</a></li>" +
		"<li><a href='deleteBorrower.jsp?id="  +id+"'>Delete</a></li>" +
		"</ul></li></ul>" +
		"</td>";
		
		row.innerHTML = inner;
		table.append(row);
	}	
}
function displayLibraries(info){
	$("#addNew").attr('href', "addBranch.jsp");
	var table = $("#myTable");
	var header= document.createElement("tr");
	header.innerHTML = "<th>ID</th><th>Name</th><th>Address</th><th></th>";
	table.append(header);
	for (i = 0; i <info.length; i++){
		var row= document.createElement("tr");
		var name = info[i]["branchName"];
		var address =info[i]["address"];
		var id = info[i]["branchId"];
		
		var row= document.createElement("tr");
		var inner = "<td>" +id+"</td><td>" +name+"</td><td>"+address+"</td>";
		inner = inner + "<td>" +
		"<ul>" +
		"<li class='dropdown'><a class='dropdown-toggle' data-toggle='dropdown' href='#' aria-expanded='false'> Action <span class='caret'></span></a>	" +
		"<ul class='dropdown-menu'>" +
		"<li><a href='editLibrary.jsp?id="+id+"'>Edit</a></li>" +
		"<li><a href='deleteBorrower.jsp?id="  +id+"'>Delete</a></li>" +
		"</ul></li></ul>" +
		"</td>";
		
		row.innerHTML = inner;
		table.append(row);
	}	
}
function displayPublishers(info){
	$("#addNew").attr('href', "addPublisher.jsp");
	var table = $("#myTable");
	var header= document.createElement("tr");
	header.innerHTML = "<th>ID</th><th>Name</th><th>Address</th><th>Phone</th><th></th>";
	table.append(header);
	for (i = 0; i <info.length; i++){
		var row= document.createElement("tr");
		var id = info[i]["publisherId"];
		var name = info[i]["publisherName"];
		var address =info[i]["publisherAddress"];
		var phone = info [i]["publisherPhone"];
		
		var row= document.createElement("tr");
		var inner = "<td>" +id+"</td><td>" +name+"</td><td>"+address+"</td><td>"+phone+"</td>";
		inner = inner + "<td>" +
		"<ul>" +
		"<li class='dropdown'><a class='dropdown-toggle' data-toggle='dropdown' href='#' aria-expanded='false'> Action <span class='caret'></span></a>	" +
		"<ul class='dropdown-menu'>" +
		"<li><a href='editPublisher.jsp?id="+id+"'>Edit</a></li>" +
		"<li><a href='deletePublisher.jsp?id="  +id+"'>Delete</a></li>" +
		"</ul></li></ul>" +
		"</td>";
		
		row.innerHTML = inner;
		table.append(row);
	}
}
function displayGenres(info){
	$("#addNew").attr('href', "addGenre.jsp");
	var table = $("#myTable");
	var header= document.createElement("tr");
	header.innerHTML = "<th>ID</th><th>Name</th><th></th>";
	table.append(header);
	for (i = 0; i <info.length; i++){
		
		var row= document.createElement("tr");
		var id = info[i]["genreId"];
		var name = info[i]["genreName"];
		
		var row= document.createElement("tr");
		var inner = "<td>" +id+"</td><td>" +name+"</td>";
		inner = inner + "<td>" +
		"<ul>" +
		"<li class='dropdown'><a class='dropdown-toggle' data-toggle='dropdown' href='#' aria-expanded='false'> Action <span class='caret'></span></a>	" +
		"<ul class='dropdown-menu'>" +
		"<li><a href='editGenre.jsp?id="+id+"'>Edit</a></li>" +
		"<li><a href='deleteGenre.jsp?id="  +id+"'>Delete</a></li>" +
		"</ul></li></ul>" +
		"</td>";
		
		row.innerHTML = inner;
		table.append(row);
	}
}
function displayPagination(){
	console.log("pagination");
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
