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
				<li role="presentation" class="active"><a class="searchBooks"  href="#">Books</a></li>
				<li role="presentation"><a class="searchBorrowers" href="#">Library's Details</a></li>
			</ul>
		</div>
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
	</div>
</div>


<script src="./assets/js/admin.js"></script>
<!-- <script src="./assets/js/addBook.js"></script> -->
<script src="datepicker/js/bootstrap-datepicker.js"></script>
<link rel="stylesheet" href="datepicker/css/datepicker.css">

