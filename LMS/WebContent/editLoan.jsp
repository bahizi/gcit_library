
<div class="row row-centered">
	<div class="col-lg-12 col-centered">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">×</button>
			<h4 class="modal-title">Overwrite Book Loan's due date</h4>
		</div>

		<div class="modal-body">
				<div class="col-lg-12 col-centered">
				<div class="modal-body">
			<div class="well col-lg-6 col-centered">
			<form class="form-horizontal" action="" id="cardNoForm"
					method="post">
					<div class="form-group">
						<label for="inputName" class="col-lg-2 control-label">CardNo</label>
						<div id="inputName" class="col-lg-10">
							<input type="text" class="form-control" name="title"
								placeholder="User Card Number">
								<span> </span>
						</div>

					</div>
				</form>
				<form class="form-horizontal" action="" method="post" id= "loanForm">
					<div class="form-group">
						<label for="inputLib" class="col-lg-2 control-label">Library</label>
						<div class="col-lg-10">
							<input type="text" class="form-control" name="title"
								id="inputLib" placeholder="Library Name" disabled>
						</div>
					</div>
					<div class="form-group">
						<label for="inputLib" class="col-lg-2 control-label">Book</label>
						<div class="col-lg-10">
							<input type="text" class="form-control" name="title"
								id="inputLib" placeholder="Book Title" disabled>
						</div>
					</div>
					<div class="form-group">
						<label for="datetimepicker1" class="col-lg-2 control-label">Due
							on:</label>
						<div class='col-lg-10 date'data-date-format="dd-mm-yyyy" id="datetimepicker1">							
							<input type="text" class="span2 form-control" value="" placeholder = "Due date" id="dp1">
						</div>
					</div>

				</form>			
			</div>

			<div class="well col-lg-6 col-centered">
			<fieldset>
				<legend> Suggestions</legend>
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
				</fieldset>
			</div>
		</div>	
			</div>
		</div>
	</div>
</div>
<script src="datepicker/js/bootstrap-datepicker.js"></script>
<link rel="stylesheet" href="datepicker/css/datepicker.css">	
