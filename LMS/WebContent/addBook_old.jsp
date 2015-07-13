<%@include file="main.html"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.service.AdministrativeService"%>
<%@page import="com.gcit.lms.domain.Author"%>
<%@page import="com.gcit.lms.domain.Genre"%>
<%@page import="com.gcit.lms.domain.Publisher"%>
<%
	AdministrativeService adminService = new AdministrativeService();
	List<Author> authors = adminService.readAuthors(0);
	List<Genre> genres = adminService.readGenres(0);
	List<Publisher> publishers  = adminService.readPublishers(0);
%>

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
				<form class="form-horizontal" action="authorView" method="post">
					<fieldset>
						<legend>Book's Details</legend>
						<div class="form-group">
							<label for="inputName" class="col-lg-2 control-label">Title</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" name="title"
									id="inputName" placeholder="Title">
							</div>
						</div>

						<div class="form-group">
							<label for="select" class="col-lg-2 control-label">Author(s)</label>
							<div class="col-lg-10">
								<select multiple="multiple" class="form-control" name="authors">
									<%
										for (Author author : authors) {
									%>
									<option value=<%= author.getAuthorId()%>><%=author.getAuthorName()%></option>
									<%
										}
									%>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="inputPhone" class="col-lg-2 control-label">Publisher</label>
							<div class="col-lg-10">
								<select class="form-control" id="select" name="publisher">
									<%
										for (Publisher pub : publishers) {
									%>

									<option value=<%= pub.getPublisherId() %>><%=pub.getPublisherName()%></option>
									<%
										}
									%>

								</select>

							</div>
						</div>
						<div class="form-group">
							<label for="genres" class="col-lg-2 control-label">Genre(s)</label>
							<div class="col-lg-10">
								<select multiple="multiple" class="form-control" name="genres"
									id="genres">
									<%
										for (Genre genre : genres) {
									%>

									<option value=<%=genre.getGenreId() %>><%=genre.getGenreName()%></option>
									<%
										}
									%>
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
		<div class="col-lg-6 col-centered">
			<div class="well bs-component">
				<table id="selectTable">

				</table>
			</div>

		</div>
	</div>
</div>
