<html xmlns="http://www.w3.org/1999/xhtml" lang="en">

	<%@ include file="/WEB-INF/views/jspf/head.jspf" %>

	<body>
		<%@ include file="/WEB-INF/views/jspf/navigation/site-nav.jspf" %>
		<div class="container" id="body-container">
			<%@ include file="/WEB-INF/views/jspf/navigation/app-nav.jspf" %>
			<div class="row nav-row">
				<div class="col-md-3">
					<a href="javascript:void(0)" class="btn btn-lg btn-primary btn-block extra-top-margin" data-tab="add">Add Grant</a>
				</div>
				<div class="col-md-3">
					<a href="<c:url value='/' />" class="btn btn-lg btn-link btn-block extra-top-margin" data-tab="all">View Grants</a>
				</div>
				<div class="col-md-3">
					<a href="<c:url value='/charts' />" class="btn btn-lg btn-link btn-block extra-top-margin" data-tab="charts">Charts</a>
				</div>
				<div class="col-md-3">
					<a href="<c:url value='/reports' />" class="btn btn-lg btn-link btn-block extra-top-margin" data-tab="reports">Reports</a>
				</div>
			</div>
			<div class="row extra-top-margin">
				<div class="tab-pane" id="add">
					<%@ include file="/WEB-INF/views/jspf/admin/add-grant-form.jspf" %>
				</div>
			</div>
		</div>
		<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
	</body>

</html>
