<%@page import="com.gcit.lms.service.AdministrativeService"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.domain.Book"%>
<%@page import="com.gcit.lms.domain.Author"%>
<%@page import="com.gcit.lms.domain.Publisher"%>
<%@page import="com.gcit.lms.domain.Genre"%>
<%
	AdministrativeService adminService = new AdministrativeService();
	String id = request.getParameter("id");
	
	Book book = adminService.readBook(Integer.parseInt(id));
	List<Author> authors = book.getAuthors();
	List<Genre> genres = book.getGenres();
	Publisher publisher = book.getPublisher();
	List<Publisher> publishers = adminService.readPublishers(0);
%>
<div class="row row-centered">
	<div class="col-lg-6 col-centered">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">×</button>
			<h4 class="modal-title">Edit Author</h4>
		</div>
		<div class="modal-body">
			<form class="form-horizontal modForm" action="editAuthor"
				method="post">
				<fieldset>
					<legend><%=book.getTitle()%></legend>
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
									<option value = <%= author.getAuthorId()%>><%=author.getAuthorName()%></option>
									<%
										}
									%>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="inputPhone" class="col-lg-2 control-label">Publisher</label>
							<div class="col-lg-10">
							<select class="form-control" id="select" name = "publisher">
							<%
										for (Publisher pub : publishers) {
									%>
							
							<option value = <%= pub.getPublisherId() %>><%=pub.getPublisherName()%></option>
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

									<option value = <%=genre.getGenreId() %>><%=genre.getGenreName()%></option>
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
		<div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			<button type="button" class="btn btn-primary modSave">Save
				changes</button>
		</div>
	</div>
</div>
