<html xmlns="http://www.w3.org/1999/xhtml" lang="en">

	<%@ include file="/WEB-INF/views/jspf/head.jspf" %>

	<body>
		<%@ include file="/WEB-INF/views/jspf/navigation/site-nav.jspf" %>
		<div class="container" id="body-container">
			<%@ include file="/WEB-INF/views/jspf/navigation/app-nav.jspf" %>
			<div class="row nav-row">
				<c:if test="${not empty pageContext.request.userPrincipal}">
					<div class="col-md-4">
						<a href="<c:url value='/admin' />" class="btn btn-lg btn-link btn-block extra-top-margin" data-tab="add">Add Grant</a>
					</div>
					<div class="col-md-4">
						<a href="<c:url value='/' />" class="btn btn-lg btn-link btn-block extra-top-margin" data-tab="all">View Grants</a>
					</div>
					<div class="col-md-4">
						<a href="<c:url value='/charts' />" class="btn btn-lg btn-primary btn-block extra-top-margin" data-tab="charts">Charts</a>
					</div>
				</c:if>
				<c:if test="${empty pageContext.request.userPrincipal}">
					<div class="col-md-6">
						<a href="<c:url value='/' />" class="btn btn-lg btn-link btn-block extra-top-margin" data-tab="all">View Grants</a>
					</div>
					<div class="col-md-6">
						<a href="<c:url value='/charts' />" class="btn btn-lg btn-primary btn-block extra-top-margin" data-tab="charts">Charts</a>
					</div>
				</c:if>
			</div>
			<div class="row extra-top-margin">
				<div class="tab-pane" id="charts">
					<%@ include file="/WEB-INF/views/jspf/charts.jspf" %>
				</div>
			</div>
		</div>
		<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
	</body>

</html>
