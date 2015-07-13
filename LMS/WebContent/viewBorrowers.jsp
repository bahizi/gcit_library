<%@page import="com.gcit.lms.service.AdministrativeService"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.domain.Borrower"%>
<%
	AdministrativeService adminService = new AdministrativeService();
	List<Borrower> borrowers = adminService.readBorrowers(0);
%>
<%@include file="main.html"%>
${result }
<div class="container">
<br />
	<div class="row">
		<div class="col-lg-8 col-sm-offset-2">
		<form action="searchBorrowers" method="post">
			<div class="input-group">
				<input type="text" class="form-control" placeholder="Search borrowers"
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
			<th>Borrower ID</th>
			<th>Name</th>
			<th>Address</th>
			<th>Phone</th>
			<th></th>
		</tr>
		<%
			for (Borrower bor : borrowers) {
		%>
		<tr>
			<td>
				<%
					out.println(bor.getCardNo());
				%>
			</td>
			<td>
				<%
					out.println(bor.getName());
				%>
			</td>
			<td>
				<%
					out.println(bor.getAddress());
				%>
			</td>
			<td>
				<%
					out.println(bor.getPhone());
				%>
			</td>

			<td>
				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#" aria-expanded="false"> Action <span
						class="caret"></span>
				</a>
					<ul class="dropdown-menu">
						<li><a
							href="editBorrower.jsp?borId=<%=bor.getCardNo()%>">Edit</a></li>
						<li><a href="deleteBorrower?borId=<%=bor.getCardNo()%>">Delete</a></li>
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