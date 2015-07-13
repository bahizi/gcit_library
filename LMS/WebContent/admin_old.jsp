<%@include file="main.html"%>
${result}
<div class="container">
	<div class="well bs-component">
	
	
		
		<div class="bs-example" data-example-id="simple-nav-justified">
    <ul class="nav nav-tabs nav-justified">
      <li role="presentation" class="active"><a class = "viewAuthors" href="#">Authors</a></li>
      <li role="presentation"><a class = "viewBooks" href="#">Books</a></li>
      <li role="presentation"><a class = "viewBorrowers" href="#">Borrowers</a></li>
      <li role="presentation"><a class = "viewPublishers" href="#">Publishers</a></li>
      <li role="presentation"><a class = "viewGenres" href="#">Genres</a></li>
      <li role="presentation"><a class = "viewLoans" href="#">Loans</a></li>
    </ul>
    <br>    
  </div>
		
<div class="jumbotron">
		
			<div class="row row-centered">
				<div class="col-lg-3">
					<div class="well bs-component">

						<div class="form-group">
							<h3>Books</h3>
							<a href="addBook.jsp">Add a Book</a><br /> <a
								href="viewBooks.jsp">View Books</a><br />
						</div>

					</div>
				</div>
				<div class="col-lg-3">
					<div class="well bs-component">
						<div class="form-group">
							<h3>Authors</h3>
							<a href="addAuthor.jsp">Add an Author</a><br /> <a
								href="viewAuthors.jsp">View Authors</a> <br />
						</div>
					</div>
				</div>
				<div class="col-lg-3">
					<div class="well bs-component">
						<div class="form-group">
							<h3>Publishers</h3>
							<a href="addPublisher.jsp">Add a Publisher</a><br /> <a
								href="viewPublishers.jsp">View Publisher</a><br />
						</div>

					</div>
				</div>
				<div class="col-lg-3">
					<div class="well bs-component">
						<div class="form-group">
							<h3>Borrowers</h3>
							<a href="addBorrower.jsp">Add a Borrower</a><br /> <a
								href="viewBorrowers.jsp">View Borrowers</a><br />
						</div>
					</div>
				</div>
			</div>


			<div class="row">
				<div class="col-lg-6 col-centered">
					<div class="well bs-component">
						<form class="form-horizontal" action="addBook" method="post">
							<fieldset>
								<legend>Admin Tools</legend>
								<div class="form-group">
									<label for="entity" class="col-lg-2 control-label">Entity</label>
									<div class="col-lg-10">
										<select class="form-control" id="entity" name="domain">
											<option value="empty">(Select)</option>
											<option value="author">Author</option>
											<option value="book">Book</option>
											<option value="borrower">Borrower</option>
											<option value="publisher">Publisher</option>
											<option value="Genre">Genre</option>
										</select>

									</div>
								</div>


								<div class="form-group">
									<label for="action" class="col-lg-2 control-label">Action</label>
									<div class="col-lg-10">
										<select class="form-control" id="action" name="action">
											
											<option value="empty">(Select)</option>
										</select>

									</div>
								</div>

								<div class="form-group">
									<div class="col-lg-10 col-lg-offset-2">
										<button type="reset" class="btn btn-default">Cancel</button>
										<button type="submit" class="btn btn-primary">Submit</button>
									</div>
								</div>
							</fieldset>
						</form>
					</div>
				</div>


			</div>

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