<%@include file="main.html"%>
<div class="container">
	<div class="well bs-component">
		<div class="row">
			<div class="col-lg-8 col-sm-offset-2">
				<form id="searchBox" action="/search" method="post">
					<div class="input-group">
						<input id="searchString" type="text" class="form-control"
							placeholder="Search" autofocus /> <span class="input-group-btn">
							<button id="searchButton" class="btn btn-default" type="button">
								<span class="glyphicon glyphicon-search"></span> Search
							</button>
						</span>
					</div>
				</form>
			</div>
		</div>
		<br />
		<div class="bs-example" data-example-id="simple-nav-justified">
			<ul class="nav nav-tabs nav-justified">
				<li role="presentation" class="active"><a class="searchAuthors"
					href="#">Authors</a></li>
				<li role="presentation"><a class="searchBooks" href="#">Books</a></li>
				<li role="presentation"><a class="searchBorrowers" href="#">Borrowers</a></li>
				<li role="presentation"><a class="searchLibraries" href="#">Libraries</a></li>
				<li role="presentation"><a class="searchPublishers" href="#">Publishers</a></li>
				<li role="presentation"><a class="searchGenres" href="#">Genres</a></li>
				<li role="presentation"><a class="viewLoans" href="#">Loans</a></li>
			</ul>
		</div>
		<a id="addNewAuthor" data-toggle='modal' data-target="#myModal1"
			href="addAuthor.jsp?id=null" style="display: none"></a> <a
			id="addNewBook" data-toggle='modal' data-target="#myModal1"
			href="addBook.jsp?id=null" style="display: none"></a> <a
			id="addNewBorrower" data-toggle='modal' data-target="#myModal1"
			href="addBorrower.jsp?id=null" style="display: none"></a> <a
			id="addNewBranch" data-toggle='modal' data-target="#myModal1"
			href="addBranch.jsp?id=null" style="display: none"></a> <a
			id="addNewPublisher" data-toggle='modal' data-target="#myModal1"
			href="addPublisher.jsp?id=null" style="display: none"></a> <a
			id="addNewGenre" data-toggle='modal' data-target="#myModal1"
			href="addGenre.jsp?id=null" style="display: none"></a>

		<div id="searchResult">
			<span class="input-group-btn">
				<button id="addNewButton" class="btn btn-primary" type="button">
					<span class="glyphicon glyphicon-plus"></span> Add New
				</button>
			</span> <br>
			<table id="myTable" class="table table-striped table-hover">
			</table>
			<div class="row">
				<div class="col-lg-4 col-lg-offset-4">
					<div class="bs-component">
						<ul id="pagination" class="pagination">

						</ul>
					</div>
				</div>
			</div>

		</div>
		<div id="loanManager" class="row" hidden>
			<div class="col-lg-6 col-centered">
				<form class="form-horizontal" action="" id="cardNoForm"
					method="post">
					<div class="form-group">
						<label for="inputName" class="col-lg-2 control-label">CardNo</label>
						<div id="inputName" class="col-lg-10">
							<input type="text" class="form-control" id = "loanLib" name="title"
								placeholder="Press 'enter' to submit"> <span> </span>
						</div>

					</div>
				</form>
				<form class="form-horizontal" action="" method="post" id="loanForm">
					<div class="form-group">
						<label for="inputLib" class="col-lg-2 control-label">Library</label>
						<div class="col-lg-10">
							<input type="text" class="form-control" name="title"
								id="inputLib" placeholder="Search Library" >
						</div>
					</div>
					<div class="form-group">
						<label for="inputLib" class="col-lg-2 control-label">Book</label>
						<div class="col-lg-10">
							<input type="text" class="form-control" name="title"
								id="inputLib" placeholder="Search Book" >
						</div>
					</div>
					<div class="form-group">
						<label for="datetimepicker1" class="col-lg-2 control-label">Due
							on:</label>
						<div class='col-lg-10 date'	id="datetimepicker1">
							<input type="text" class="span2 form-control" value=""
								placeholder="Due date" id="dp1" data-date-end-date="7d">
						</div>
					</div>
					<div class="form-group">
						<div class="col-lg-10 col-lg-offset-2">
							<button type="submit" class="btn btn-primary">Save Changes</button>
						</div>
					</div>
				</form>
			</div>
			<div class="col-lg-6 col-centered">
			<div class="bs-component">				
					<table id="selectTable" class="table table-striped table-hover">

					</table>
					<div class="row">
						<div class="col-lg-4 col-lg-offset-4">
							<div class="bs-component">
								<ul id="pagination" class="pagination">
								</ul>
							</div>
						</div>
					</div>
				</div>			
			</div>
		</div>
	</div>
</div>

<script src="./assets/js/admin.js"></script>
<!-- <script src="./assets/js/addBook.js"></script> -->
<script src="datepicker/js/bootstrap-datepicker.js"></script>
<link rel="stylesheet" href="datepicker/css/datepicker.css">

