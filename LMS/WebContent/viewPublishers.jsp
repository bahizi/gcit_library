<%@page import="com.gcit.lms.service.AdministrativeService"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.domain.Publisher"%>
<%
	AdministrativeService adminService = new AdministrativeService();
	List<Publisher> publishers = adminService.readPublishers(0);
%>
<%@include file="main.html"%>
${result }
<div class="container">
<br />
	<div class="row">
		<div class="col-lg-8 col-sm-offset-2">
		<form action="searchPublishers" method="post">
			<div class="input-group">			
				<input type="text" class="form-control" placeholder="Search publishers"
					autofocus /> <span class="input-group-btn">
					<button class="btn btn-default" type="button">Go</button>
				</span>
			</div>
			</form>
		</div>
	</div>
	<br />

	<table class="table table-striped table-hover">

		<tr>
			<th>Publisher ID</th>
			<th>Name</th>
			<th>Address</th>
			<th>Phone</th>
			<th></th>
		</tr>
		<%
			for (Publisher pub : publishers) {
		%>
		<tr>
			<td>
				<%
					out.println(pub.getPublisherId());
				%>
			</td>
			<td>
				<%
					out.println(pub.getPublisherName());
				%>
			</td>
			<td>
				<%
					out.println(pub.getPublisherAddress());
				%>
			</td>
			<td>
				<%
					out.println(pub.getPublisherPhone());
				%>
			</td>

			<td>
				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#" aria-expanded="false"> Action <span
						class="caret"></span>
				</a>
					<ul class="dropdown-menu">
						<li><a
							href="editPublisher.jsp?pubId=<%=pub.getPublisherId()%>">Edit</a></li>
						<li><a href="deletePublisher?pubId=<%=pub.getPublisherId()%>">Delete</a></li>
					</ul></li>
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