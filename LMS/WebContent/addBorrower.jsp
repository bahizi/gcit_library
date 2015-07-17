<div class="row row-centered">
	<div class="col-lg-6 col-centered">
		<div class="well bs-component">
			<form class="form-horizontal" action="addBorrower" method="post">
				<fieldset>
					<legend>Borrower's details</legend>
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
						<label for="inputPhone" class="col-lg-2 control-label">Phone</label>
						<div class="col-lg-10">
							<input type="text" class="form-control" name="borPhone"
								id="inputPhone" placeholder="Phone">
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