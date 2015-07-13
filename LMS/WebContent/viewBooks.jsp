<%@page import="com.gcit.lms.service.AdministrativeService"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.domain.Book"%>
<%@page import="com.gcit.lms.domain.Publisher"%>
<%@page import="com.gcit.lms.domain.Author"%>
<%@page import="com.gcit.lms.domain.Genre"%>
<%
	AdministrativeService adminService = new AdministrativeService();
	List<Book> books = adminService.readBooks(0);
%>
<%@include file="main.html"%>
${result }
<div class="container">
	<br />
	<div class="row">
	<div class="bs-component">
		<div class="col-lg-8 col-sm-offset-2">
			<form action="searchBooks" method="post">
				<div class="input-group">
					<input type="text" class="form-control" placeholder="Search books"
						autofocus /> <span class="input-group-btn">
						<button class="btn btn-default" type="submit">Go</button>
					</span>
				</div>
			</form>
		</div>
		</div>
	</div>
	<br />



	<table class="table table-striped table-hover">

		<tr>
			<th>Book ID</th>
			<th>Title</th>
			<th>Author</th>
			<th>Publisher</th>
			<th>Genre</th>
			<th></th>
		</tr>
		<%
			for (Book book : books) {
		%>
		<tr>
			<td>
				<%
					out.println(book.getBookId());
				%>
			</td>
			<td>
				<%
					out.println(book.getTitle());
				%>
			</td>
			<td>
				<%
					List<Author> auth = book.getAuthors();
						String authDisplay = "---";
						if (auth != null && auth.size() > 0) {
							authDisplay = "" + auth.get(0);
							if (auth.size() == 2) {
								authDisplay += " & " + auth.get(1);
							} else if (auth.size() > 2) {
								authDisplay += ", " + auth.get(1) + " and more";
							}
						}
						out.println(authDisplay);
				%>
			</td>
			<td>
				<%
					Publisher pub = book.getPublisher();
						String pubDisplay = "---";
						if (pub != null) {
							pubDisplay = pub.toString();
						}
						out.println(pubDisplay);
				%>
			</td>

			<td>
				<%
					List<Genre> gnrs = book.getGenres();
						String genreDisplay = "---";
						if (gnrs != null && gnrs.size() > 0) {
							genreDisplay = "" + gnrs.get(0);
							if (gnrs.size() == 2) {
								genreDisplay += " & " + gnrs.get(1);
							} else if (gnrs.size() > 2) {
								genreDisplay += ", " + gnrs.get(1) + " and more";
							}
						}
						out.println(genreDisplay);
				%>
			</td>

			<td>
				<ul>
					<li class="dropdown">
						<a class="dropdown-toggle" data-toggle="dropdown" href="#" aria-expanded="false"> Action <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="editBook.jsp?bookId=<%= book.getBookId()%>">Edit</a></li>
							<li><a href="deleteBook?bookId=<%= book.getBookId()%>">Delete</a></li>
							<% System.out.println(book); %>							
						</ul>
					</li>
				</ul>
			</td>
		</tr>
		<%
			}
		%>
	</table>
	<div class="row">
		<div class="col-lg-4 col-sm-offset-4">
			<div class="bs-component">
				<ul class="pagination">
					<li class="disabled"><a aayouebahref="#">«</a></li>
					<li class="active"><a href="#">1</a></li>
					<li><a href="#">2</a></li>
					<li><a href="#">3</a></li>
					<li><a href="#">4</a></li>
					<li><a href="#">5</a></li>
					<li><a href="#">»</a></li>
				</ul>
			</div>
		</div>
	</div>
	
	<div id="myModal1" class="modal fade" tabindex="-1" role="dialog"
		aria-labelledby="myLargeModalLabel">
		<div class="modal-dialog modal-lg">
			<div class="modal-content"></div>
		</div>
	</div>

</div>