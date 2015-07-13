$( document ).ready(function() {
	window.view = "viewAuthors";
	window.pageCount = 1;
	window.curPage = 1;
	window.searchString = "";
	window.selectedAuthors = [];
	window.selectedPublisher = '';
	window.selectedGenres = [];
	window.allAuthors = [];
	window.allGenres = [];
	window.allPublishers = [];
	
	searchMain("",0);
	$('li[role="presentation"]').click(function(e){
		e.preventDefault();
		$(".active").removeClass("active");		
		$(this).addClass("active");
		view = $(this).find("a").attr("class");
		$("#myTable").empty();
		curPage = 1;
		searchString = "";
		searchMain(searchString,curPage-1);	
	});
	
	
	
	$(document).on('click', '.addPublisher', function () {
		selectedPublisher = ($(this).val());
		$(this).text("Remove");
	    $(this).removeClass("btn-success");
	    $(this).addClass("btn-danger");
	    $(this).removeClass("addPublisher");
	    $(this).addClass("removePublisher");
	    showSelectedPublisher(true);
	});
	
	$(document).on('click', '.removePublisher', function () {
		selectedPublisher = "-1";
	    $("#searchPublisher .searchString").val("");
	    //$("#searchPublisher .searchString").prop('disabled', false);	
	    $(this).text("Select");
	    $(this).removeClass("btn-danger");
	    $(this).addClass("btn-success");
	    $(this).removeClass("removePublisher");
	    $(this).addClass("addPublisher");
	    showSelectedPublisher(false);
	    
	});
	
	$(document).on('click', '.addAuthor', function () {
		selectedAuthors.push($(this).val());
	    $(this).text("Remove");
	    $(this).removeClass("btn-success");
	    $(this).addClass("btn-danger");
	    $(this).removeClass("addAuthor");
	    $(this).addClass("removeAuthor");
	    showSelectedAuthors();
	});
	
	$(document).on('click', '.removeAuthor', function () {		
		var index = selectedAuthors.indexOf($(this).val());
		if (index > -1) {
		    selectedAuthors.splice(index, 1);
		}		
	    $(this).text("Select");
	    $(this).removeClass("btn-danger");
	    $(this).addClass("btn-success");
	    $(this).removeClass("removeAuthor");
	    $(this).addClass("addAuthor");
	    showSelectedAuthors();
	});
	$(document).on('click', '.addGenre', function () {
		selectedGenres.push($(this).val());
		$(this).text("Remove");
		$(this).removeClass("btn-success");
		$(this).addClass("btn-danger");
		$(this).removeClass("addGenre");
		$(this).addClass("removeGenre");
		showSelectedGenres();
		console.log("sasds");
	});
	
	$(document).on('click', '.removeGenre', function () {		
		var index = selectedGenres.indexOf($(this).val());
		if (index > -1) {
			selectedGenres.splice(index, 1);
		}
		$(this).text("Select");
		$(this).removeClass("btn-danger");
		$(this).addClass("btn-success");
		$(this).removeClass("removeGenre");
		$(this).addClass("addGenre");
		showSelectedGenres();
	});	
	
	
//	$("#ssa").prop('disabled', true);
	
	$('#addBookForm').bind('submit', function(event) {
		//alert("Thanks for adding the book");
		event.preventDefault();
		console.log("*****");
		console.log(selectedAuthors);
		console.log(selectedGenres);
		console.log("--------");
		$.ajax({
			  url: "addBook",
			  type: 'POST',
			  data: {
				  title: $("#inputName").val(),
				  authors: selectedAuthors.toString(),
				  publisher: selectedPublisher,
				  genres: selectedGenres.toString()
			  }
			}).done(function(res) {
				window.location.href = "admin.jsp";				
			});	
	});
	
	$('#searchAuthor').bind('input', function() { 
	    console.log($('#searchAuthor .searchString').val());
	    searchString = $('#searchAuthor .searchString').val();
	    view = "viewAuthors";
	    curPage = 1;
	    search(searchString, curPage-1,"searchAuthors",displayAuthors);
	});
	$('#searchGenre').bind('input', function() { 
		view = "viewGenres";
		searchString = $('#searchGenre .searchString').val();	
		curPage = 1;
		search(searchString, curPage-1,"searchGenres",displayGenres);
		console.log(searchString);
	});
	
	$('#searchPublisher').bind('input', function() { 
		searchString = $('#searchPublisher .searchString').val();	
		view = "viewPublishers";
		curPage = 1;
		search(searchString, curPage-1,"searchPublishers",displayPublishers);
		console.log(searchString);
	});
	
	$('#addNewButton').click(function(e){
		event.preventDefault();
		alert("heel");
		
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

function showSelectedAuthors(){
	var txt = "";
	for(i=1; i<selectedAuthors.length + 1; i++){
		txt += i + ". "+allAuthors[selectedAuthors[i-1]]+"\n";
	}
	$("#selectedAuthors").text(txt);
}
function showSelectedPublisher(disable){	
	if(selectedPublisher >0){
		var txt=allPublishers[selectedPublisher]
	}
	else{
		txt = "";
	}
	$("#searchPublisher .searchString").val(txt);
    $("#searchPublisher .searchString").prop('disabled', disable);	
}
function showSelectedGenres(){
	var txt = "";
	for(i=1; i<selectedGenres.length + 1; i++){
		txt += i + ". "+ allGenres[selectedGenres[i-1]]+"\n";
		console.log("Accessing "+selectedGenres[i-1]);
	}
	$("#selectedGenres").text(txt);
}
function search(query, pageNum,searchURL,display){
	console.log("Searching page #" +pageNum);
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
			displayPagination();
			
		});
	
}

function searchMain( query, pageNum){
	if (view === "viewAuthors"){
		  search(query,pageNum,"searchAuthors",displayAuthors);
			
		}
		else if(view ==="viewBooks"){
			search(query,pageNum,"searchBooks",displayBooks);
			$('#searchString').val("");
		}
		
		else if(view ==="viewBorrowers"){
			search(query,pageNum,"searchBorrowers",displayBorrowers);	
		}
		else if(view ==="viewLibraries"){
			search(query,pageNum,"searchLibraries",displayLibraries);
		}
		else if(view ==="viewPublishers"){
			search(query,pageNum,"searchPublishers",displayPublishers);
		}
		else if(view ==="viewGenres"){
			search(query,pageNum,"searchGenres",displayGenres);				
		}	
	
}
function displayAuthors(data){
var table = $("#selectTable");
table.empty();
var header= document.createElement("tr");
header.innerHTML = "<th> Author Name</th><th></th>";
table.append(header);
for(i= 0; i<data.length; i++){
	
	var  id = data[i]["authorId"];
	var name = data[i]["authorName"];
	//console.log(allAuthors);
	if(selectedAuthors.indexOf(id.toString())<0){
		var cls = "btn btn-success addAuthor";
		var txt = "Select";
	}
	else{
		var cls = "btn btn-danger removeAuthor";
		var txt = "Remove";
	}
	
	allAuthors[id]=name;
	var row= document.createElement("tr");
	var inner = "<td>" +name+"</td>";
	inner = inner + "<td> <button class ='btn btn-success "+cls+"' value = '"+id+"'>"+txt+"</button></td>";			
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
	var table = $("#selectTable");
	table.empty();
	var header= document.createElement("tr");
	header.innerHTML = "<th>Name</th><th></th>";
	table.append(header);
	for (i = 0; i <info.length; i++){
		var row= document.createElement("tr");
		var name = info[i]["publisherName"];
		var id = info[i]["publisherId"];
		if(selectedPublisher !== id.toString()){
			var cls = "btn btn-success addPublisher";
			var txt = "Select";
		}
		else{
			var cls = "btn btn-danger removePublisher";
			var txt = "Remove";
		}
		
		
		allPublishers[id] = name;
		var row= document.createElement("tr");
		var inner = "<td>" +name+"</td>";
		inner = inner + "<td> <button class ='btn btn-success "+cls+"' value ='"+id+"'>Select</button></td>";		
		
		row.innerHTML = inner;
		table.append(row);
	}
}
function displayGenres(info){
	var table = $("#selectTable");
	table.empty();
	var header= document.createElement("tr");
	header.innerHTML = "<th>Name</th><th></th>";
	table.append(header);
	allGenres.length = 0;
	for (i = 0; i <info.length; i++){
		
		var row= document.createElement("tr");
		var name = info[i]["genreName"];
		var id = info[i]["genreId"];
		if(selectedGenres.indexOf(id.toString())<0){
			var cls = "btn btn-success addGenre";
			var txt = "Select";
		}
		else{
			var cls = "btn btn-danger removeGenre";
			var txt = "Remove";
		}
		allGenres[id] = name;
		var row= document.createElement("tr");
		var inner = "<td>" +name+"</td>";
		inner = inner + "<td> <button class ='btn btn-success "+cls+"' value ='"+id+"'>"+txt+"</button></td>";		
		row.innerHTML = inner;
		table.append(row);
		
	}
	console.log("All genres"+ allGenres[4]);
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
