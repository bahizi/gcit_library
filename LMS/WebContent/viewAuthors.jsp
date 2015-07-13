<%@page import="com.gcit.lms.service.AdministrativeService"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.domain.Author"%>
<%
	AdministrativeService adminService = new AdministrativeService();
	List<Author> authors = adminService.readAuthors(0);
%>
<%@include file="main.html"%>

<div class="container">
	${result } <br />
	<div class="row">
		<div class="col-lg-8 col-sm-offset-2">
		<form action ="/searchAuthors" method = "post">
			<div class="input-group">
				<input type="text" class="form-control" placeholder="Search authors"
					autofocus /> <span class="input-group-btn">
					<button class="btn btn-default" type="submit">Go</button>
				</span>
			</div>
			</form>
		</div>
	</div>
	<br />
	<table class="table table-striped table-hover">

		<tr>
			<th>Author ID</th>
			<th>Author Name</th>
			<th></th>
		</tr>
		<%
			for (Author a : authors) {
		%>
		<tr>
			<td>
				<%
					out.println(a.getAuthorId());
				%>
			</td>
			<td>
				<%
					out.println(a.getAuthorName());
				%>
			</td>
			<td><ul><li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#" aria-expanded="false"> Action <span
						class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="editAuthor.jsp?authorId=<%=a.getAuthorId()%>">Edit</a></li>
						<li><a href="deleteAuthor?authorId=<%=a.getAuthorId()%>">Delete</a></li>
						
					</ul></li>
					</ul></td>
		</tr>
		<%
			}
		%>
	</table>
	<div class="row">
		<div class="col-lg-4 col-sm-offset-4">
			<div class="bs-component">
				<ul class="pagination">
					<li class="disabled"><a href="#">«</a></li>
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