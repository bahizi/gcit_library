<%@include file="main.html"%>
${result}
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
				<li role="presentation" class="active"><a class="viewAuthors"
					href="#">Authors</a></li>
				<li role="presentation"><a class="viewBooks" href="#">Books</a></li>
				<li role="presentation"><a class="viewBorrowers" href="#">Borrowers</a></li>
				<li role="presentation"><a class="viewLibraries" href="#">Libraries</a></li>
				<li role="presentation"><a class="viewPublishers" href="#">Publishers</a></li>
				<li role="presentation"><a class="viewGenres" href="#">Genres</a></li>
				<li role="presentation"><a class="viewLoans" href="#">Loans</a></li>
			</ul>

		</div>

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
<div class="row row-centered">
	<div class="col-lg-6">
		<div id="myModal1" class="modal fade" tabindex="-1" role="dialog"
			aria-labelledby="myLargeModalLabel">
			<div class="modal-dialog modal-lg">
				<div class="modal-content"></div>
			</div>
		</div>
	</div>
</div>



<script type="text/javascript">
	jQuery(function($) {
		$('.panel-heading span.clickable')
				.on(
						"click",
						function(e) {
							if ($(this).hasClass('panel-collapsed')) {
								// expand the panel
								$(this).parents('.panel').find('.panel-body')
										.slideDown();
								$(this).removeClass('panel-collapsed');
								$(this).find('i').removeClass(
										'glyphicon-chevron-down').addClass(
										'glyphicon-chevron-up');
							} else {
								// collapse the panel
								$(this).parents('.panel').find('.panel-body')
										.slideUp();
								$(this).addClass('panel-collapsed');
								$(this).find('i').removeClass(
										'glyphicon-chevron-up').addClass(
										'glyphicon-chevron-down');
							}
						});
	});
</script>

<script src="./assets/js/admin.js"></script>