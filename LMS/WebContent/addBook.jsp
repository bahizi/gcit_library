<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.service.AdministrativeService"%>
<%@page import="com.gcit.lms.domain.Author"%>
<%@page import="com.gcit.lms.domain.Genre"%>
<%@page import="com.gcit.lms.domain.Publisher"%>
<%
	AdministrativeService adminService = new AdministrativeService();
	List<Author> authors = adminService.readAuthors(0);
	List<Genre> genres = adminService.readGenres(0);
	List<Publisher> publishers = adminService.readPublishers(0);
%>
<div class="row row-centered">
	<div class="col-lg-12 col-centered">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">×</button>
			<h4 class="modal-title">Add/Edit a Book</h4>
		</div>
		<div class="modal-body">
			<div class="col-lg-6 col-centered">
				<form class="form-horizontal" action="" id="addBookForm"
					method="post">
					<fieldset>
						<legend>Enter book's Details</legend>
						<div class="form-group">
							<label for="inputName" class="col-lg-2 control-label">Title</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" name="title"
									id="inputName" placeholder="Title">
							</div>
						</div>
						<div class="form-group">
							<label for="searchAuthor" class="col-lg-2 control-label">Author(s)</label>
							<div class="col-lg-10" id="searchAuthor">
								<input type="text" class="form-control searchString"
									placeholder="Author Name">
								<textarea class="form-control selectionsDisplay" rows="3"
									id="selectedAuthors" disabled></textarea>
							</div>
							<div class="col-lg-10"></div>
						</div>
						<div class="form-group">
							<label for="searchPublisher" class="col-lg-2 control-label">Publisher</label>
							<div class="col-lg-10" id="searchPublisher">
								<input type="text" class="form-control searchString"
									placeholder="Publisher Name">
							</div>
						</div>

						<div class="form-group">
							<label for="searchGenres" class="col-lg-2 control-label">Genres(s)</label>
							<div class="col-lg-10" id="searchGenre">
								<input type="text" class="form-control searchString"
									placeholder="Genre Name">
								<textarea class="form-control selectionsDisplay" rows="3"
									id="selectedGenres" disabled></textarea>
							</div>
						</div>

						<div class="form-group">
							<div class="col-lg-10 col-lg-offset-2">
								<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
								<button type="submit" class="btn btn-primary">Save Changes</button>
							</div>
						</div>
					</fieldset>
				</form>
			</div>

			<div class="col-lg-6 col-centered">
			<legend>Suggestions</legend>
				<div class="well bs-component">
				
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