<%@page import="com.gcit.lms.service.AdministrativeService"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.domain.Author"%>
<%
	AdministrativeService adminService = new AdministrativeService();
	String authorId = request.getParameter("id");
	Author author = adminService.readAuthor(Integer.parseInt(authorId));
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
					<legend><%=author.getAuthorName()%></legend>
					<div class="form-group">
						<label for="inputName" class="col-lg-2 control-label">Name</label>
						<div class="col-lg-10">

							<input type="text" id="inputName" class="form-control"
								name="authorName" value=<%=author.getAuthorName()%>
								placeholder="Full Name"> <input type="text"
								class="form-control" name="authorId" value=<%=authorId%> hidden>

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
