<%@include file="main.html"%>

<div class="container">

	<div class="row row-centered">
		<div class="col-lg-12 col-centered">
			<div class="page-header">
				<h1 id="forms">GCIT Library: Add a Book</h1>
			</div>
		</div>
	</div>


	<div class="row row-centered">
		<div class="col-lg-6 col-centered">
			<div class="well bs-component">
				<form class="form-horizontal" action="viewAuthor" method="post">
					<fieldset>
						<legend>Book's ISBN</legend>
						<div class="form-group">
							<label for="inputAddress" class="col-lg-2 control-label">ISBN</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" name="publisherAddress"
									id="inputAddress" placeholder="ISBN-10 or ISBN-13">

							</div>
						</div>

						<div class="form-group">
							<label class="col-lg-2 control-label" for=authors>Title</label>
							<div class="col-lg-10">
								<input class="form-control" id="authors" type="text"
									placeholder="Book Title" disabled="">
							</div>
						</div>

						<div class="form-group">
							<label class="col-lg-2 control-label" for=authors>Author(s)</label>
							<div class="col-lg-10">
								<input class="form-control" id="authors" type="text"
									placeholder="Authors" disabled="">
							</div>
						</div>

						<div class="form-group">
							<label class="col-lg-2 control-label" for=authors>Genre(s)</label>
							<div class="col-lg-10">
								<input class="form-control" id="authors" type="text"
									placeholder="Genres" disabled="">
							</div>
						</div>

						<div class="form-group">
							<label class="col-lg-2 control-label" for=authors>Publisher</label>
							<div class="col-lg-10">
								<input class="form-control" id="authors" type="text"
									placeholder="Publisher" disabled="">
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

		<div class="col-lg-6 col-centered">
			<div class="well bs-component">
				<form class="form-horizontal" action="addAPublisher" method="post">
					<fieldset>
						<legend>Book's Details</legend>
						<div class="form-group">
							<label for="inputName" class="col-lg-2 control-label">Title</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" name="publisherName"
									id="inputName" placeholder="Title">
							</div>
						</div>
						<div class="form-group">
							<label for="inputAddress" class="col-lg-2 control-label">Author</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" name="publisherAddress"
									id="inputAddress" placeholder="Author">

							</div>
						</div>
						<div class="form-group">
							<label for="inputPhone" class="col-lg-2 control-label">Publisher</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" name="publisherPhone"
									id="inputPhone" placeholder="Publisher">
							</div>
						</div>
						<div class="form-group">
							<label for="inputPhone" class="col-lg-2 control-label">Genre</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" name="publisherPhone"
									id="inputPhone" placeholder="Genre">
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
