<div class="row row-centered">
	<div class="col-lg-12 col-centered">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">×</button>
			<h4 class="modal-title">Add/Edit a Library Branch</h4>
		</div>
		<div class="modal-body">
			<div class="well col-lg-8 col-centered col-lg-offset-2">
				<form class="form-horizontal" action="addBranch" method="post">
					<fieldset>
						<legend>Library's details</legend>
						<div class="form-group">
							<label for="inputName" class="col-lg-2 control-label">Name</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" name="borName"
									id="inputName" placeholder="Name">
							</div>
						</div>
						<div class="form-group">
							<label for="inputAddress" class="col-lg-2 control-label">Address</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" name="borAddress"
									id="inputAddress" placeholder="Address">

							</div>
						</div>
						<div class="form-group">
							<div class="col-lg-10 col-lg-offset-2">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">Close</button>
								<button type="submit" class="btn btn-primary">Submit</button>
							</div>
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</div>



