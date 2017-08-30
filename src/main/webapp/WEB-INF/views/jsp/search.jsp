<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en">

	<%@ include file="/WEB-INF/views/jspf/head.jspf" %>

	<body>
		<%@ include file="/WEB-INF/views/jspf/navigation/site-nav.jspf" %>
		<div class="container" id="body-container">
			<%@ include file="/WEB-INF/views/jspf/navigation/app-nav.jspf" %>
			<c:set var="path" value="search/query" scope="request" />
			<%@ include file="/WEB-INF/views/jspf/admin/search.jspf" %>
		</div>
		<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
	</body>

</html>