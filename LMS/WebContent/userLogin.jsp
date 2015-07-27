<div class="row row-centered">
	<div class="col-lg-12 col-centered">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">×</button>
			<h4 class="modal-title">Library User Login</h4>
		</div>

		<div class="modal-body">
			<div class="well col-lg-8 col-centered col-lg-offset-2">
				<form class="form-horizontal" action="borrowerLogin" method="post">
					<fieldset>
						<legend>Enter your Card Number</legend>
						<div class="form-group">
							<label for="inputCardNo" class="col-lg-2 control-label">Card#</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" name="borName"
									id="inputCardNo" placeholder="Card Number">
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
