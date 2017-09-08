<html xmlns="http://www.w3.org/1999/xhtml" lang="en">

	<%@ include file="/WEB-INF/views/jspf/head.jspf" %>

	<body>
		<%@ include file="/WEB-INF/views/jspf/navigation/site-nav.jspf" %>
		<div class="container" id="body-container">
			<%@ include file="/WEB-INF/views/jspf/navigation/app-nav.jspf" %>
			<div class="row nav-row">
				<div class="col-md-4">
					<a href="javascript:void(0)" class="btn btn-lg btn-primary btn-block extra-top-margin" data-tab="add">Add Grant</a>
				</div>
				<div class="col-md-4">
					<a href="<c:url value='/' />" class="btn btn-lg btn-link btn-block extra-top-margin" data-tab="all">View Grants</a>
				</div>
				<div class="col-md-4">
					<a href="<c:url value='/charts' />" class="btn btn-lg btn-link btn-block extra-top-margin" data-tab="charts">Charts</a>
				</div>
			</div>
			<div class="row extra-top-margin">
				<div class="tab-pane" id="add">
					<%@ include file="/WEB-INF/views/jspf/admin/add-grant-form.jspf" %>
				</div>
			</div>
			<div class="row extra-extra-top-margin" id="message" style="display: none">
				<div class="alert alert-danger" style="display: none">
					A problem was encountered during the import process: <span id="problem"></span>
				</div>
				<div class="alert alert-warning" style="display: none">
					Importing grants data... This may take a few minutes.
				</div>
				<div class="alert alert-success" style="display: none">
					Successfully imported grants data from CSV file!
				</div>
			</div>
			<div class="row extra-extra-top-margin top-border">
				<div class="col-md-4 col-md-push-4 extra-extra-top-margin">
					<button id="import-csv" type="button" class="btn btn-lg btn-block btn-primary">Import Grants Data from CSV File</button>
					<button id="import-excel" type="button" class="btn btn-lg btn-block btn-primary">Import Grants Data from Excel File</button>
				</div>
			</div>
		</div>
		<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
	</body>

	<script type="text/javascript">
		$(document).ready(function() {
			$("#import-csv").click(function(event) {
				event.preventDefault();
				$("#message").show();
				$(".alert-warning").show();
				$.get("<c:url value='/admin/import?ext=csv' />", function(data) {
					console.log(data);
				}).done(function() {
					$(".alert-warning").hide();
					$(".alert-success").show();
					$("#import-csv").attr("disabled", "disabled");
					$("#import-excel").attr("disabled", "disabled");
				}).fail(function(data, textStatus, xhr) {
					$(".alert-warning").hide();
					$("#problem").html(xhr);
					$(".alert-danger").show();
				});
			});
			$("#import-excel").click(function(event) {
				event.preventDefault();
				$("#message").show();
				$(".alert-warning").show();
				$.get("<c:url value='/admin/import?ext=excel' />", function(data) {
					console.log(data);
				}).done(function() {
					$(".alert-warning").hide();
					$(".alert-success").show();
					$("#import-csv").attr("disabled", "disabled");
					$("#import-excel").attr("disabled", "disabled");
				}).fail(function(data, textStatus, xhr) {
					$(".alert-warning").hide();
					$("#problem").html(xhr);
					$(".alert-danger").show();
				});
			});
		});
	</script>

</html>
