<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en">

<%@ include file="/WEB-INF/views/jspf/head.jspf" %>

<body class="sidebar-fixed">
    <div class="wrapper" id="wrapper">
        <nav class="navbar navbar-oha navbar-fixed-top">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">OHA Grants</a>
            </div>
        </nav>
        <div class="collapse navbar-collapse" id="navbar-collapse-1">

        </div>
        <div id="main-wrapper" class="main-wrapper">
            <div class="panes content-wrapper">
                <div class="tab-pane col-md-12">
                    <%@ include file="/WEB-INF/views/jspf/admin/add-grant-form.jspf" %>
                </div>
            </div>
            <footer>
                <div class="content-wrapper">
                    <span class="copyright">
                        &copy; <script>document.write(new Date().getFullYear());</script> <a href="https://www.oha.org/">Office of Hawaiian Affairs</a>
                    </span>
                </div>
            </footer>
        </div>
    </div>
    <!-- .wrapper -->

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

</body>

</html>
