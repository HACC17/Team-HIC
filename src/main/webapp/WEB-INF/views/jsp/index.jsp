<html xmlns="http://www.w3.org/1999/xhtml" lang="en">

	<%@ include file="/WEB-INF/views/jspf/head.jspf" %>

	<body>
		<%@ include file="/WEB-INF/views/jspf/navigation/site-nav.jspf" %>
		<div class="container" id="body-container">
			<%@ include file="/WEB-INF/views/jspf/navigation/app-nav.jspf" %>
			<div class="row nav-row">
				<div class="col-md-4">
					<a href="javascript:void(0)" class="btn btn-lg btn-primary btn-block extra-top-margin" data-tab="all">View Grants</a>
				</div>
				<div class="col-md-4">
					<a href="javascript:void(0)" class="btn btn-lg btn-link btn-block extra-top-margin" data-tab="charts">Charts</a>
				</div>
				<div class="col-md-4">
					<a href="javascript:void(0)" class="btn btn-lg btn-link btn-block extra-top-margin" data-tab="reports">Reports</a>
				</div>
			</div>
			<div class="row extra-top-margin">
				<div class="tab-pane" id="all">
					<%@ include file="/WEB-INF/views/jspf/all-grants.jspf" %>
				</div>
				<div class="tab-pane" id="charts" style="display: none">
					<%@ include file="/WEB-INF/views/jspf/charts.jspf" %>
				</div>
			</div>
		</div>
		<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
	</body>

	<script type="text/javascript">
		$(document).ready(function() {
			$(".nav-row > div > a").on("click", function() {
				$(".nav-row > div > a").removeClass("btn-primary");
				$(".nav-row > div > a").addClass("btn-link");
				$(this).removeClass("btn-link");
				$(this).addClass("btn-primary");
				$(".tab-pane").hide();
				var tab = $(this).data("tab");
				$("#" + tab).show();
			});
		});
	</script>

</html>