<%@include file="main.html"%>

<div class="container">

	<div class="row row-centered">
		<div class="col-lg-12 col-centered">
			<div class="page-header">
				<h1 id="forms">GCIT Library: Add Genre</h1>
			</div>
		</div>
	</div>


	<div class="row row-centered">
		<div class="col-lg-6 col-centered">
			<div class="well bs-component">
				<form class="form-horizontal" action="addAuthor" method="post">
					<fieldset>
						<legend>Genre's details</legend>
						<div class="form-group">
							<label for="inputName" class="col-lg-2 control-label">Name</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" name="authorName"
									id="inputName" placeholder="Genre name">
							</div>
						</div>
						<div class="form-group">
							<div class="col-lg-10 col-lg-offset-2">
								<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
								<button type="submit" class="btn btn-primary">Submit</button>
							</div>
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>

<div class="dropdown" hidden>
                  <a class="dropdown-toggle" data-toggle="dropdown" href="#" aria-expanded="false">
                    Author <span class="caret"></span>
                  </a>
                  <ul class="dropdown-menu">
                    <li><a href="#">Add new author</a></li>
                    <li><a href="#">Edit author</a></li>
                    <li><a href="#">Delete an author</a></li>
                    <li class="divider"></li>
                    <li><a href="#">View All</a></li>
                  </ul>
                </div>



	<form action="addAuthor" method="post" hidden>


		<p>
			Enter Author Name: <input type="text" name="authorName" />
		</p>
		<input type="submit">
	</form>

</div>
