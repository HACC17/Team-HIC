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
            <div class="left-sidebar" id="left-sidebar">
                <div class="sidebar-scroll">
                    <div id="filters-panel" class="filters-panel">
                        <h3 class="sr-only">Filters</h3>
                        <div class="filter-group">
                            <label class="fieldset-label" data-toggle="collapse" data-target="#toggle-priority"><i class="fa fa-area-chart"></i>Strategic Priority<i class="toggle-icon fa fa-angle-left"></i></label>
                            <div  id="toggle-priority" class="collapse">
                                <fieldset>
                                    <c:forEach var="priority" items="${priorities}">
                                        <label class="form-check-label">
                                            <input class="form-check-input filter" type="checkbox" value="${priority}" onchange="updateTable();" data-key="priority" data-chart-title="Strategic Priorities">
                                            <c:out value="${priority}" />
                                        </label>
                                    </c:forEach>
                                    <a href="#" class="clear-filter"><i class="fa fa-check"></i>clear filter</a>
                                </fieldset>
                            </div>
                        </div>
                        <div class="filter-group">
                            <label class="fieldset-label" data-toggle="collapse" data-target="#toggle-result"><i class="fa fa-line-chart"></i>Strategic Result<i class="toggle-icon fa fa-angle-left"></i></label>
                            <div  id="toggle-result" class="collapse">
                                <fieldset>
                                    <c:forEach var="result" items="${results}">
                                        <label class="form-check-label">
                                            <input class="form-check-input filter" type="checkbox" value="${result}" onchange="updateTable();" data-key="result">
                                            <c:out value="${result}" />
                                        </label>
                                    </c:forEach>
                                    <a href="#" class="clear-filter"><i class="fa fa-check"></i>clear filter</a>
                                </fieldset>
                            </div>
                        </div>
                        <div class="form-check filter-group">
                            <label class="fieldset-label" data-toggle="collapse" data-target="#grant-type"><i class="fa fa-info-circle"></i>Grant Type<i class="toggle-icon fa fa-angle-left"></i></label>
                            <div id="grant-type" class="collapse">
                                <fieldset>
                                    <c:forEach var="type" items="${types}">
                                        <label class="form-check-label">
                                            <input class="form-check-input filter" type="checkbox" value="${type}" onchange="updateTable();" data-key="type">
                                            <c:out value="${type}" />
                                        </label>
                                    </c:forEach>
                                    <a href="#" class="clear-filter"><i class="fa fa-check"></i>clear filter</a>
                                </fieldset>
                            </div>
                        </div>
                        <div class="filter-group">
                            <label class="fieldset-label" data-toggle="collapse" data-target="#location"><i class="fa fa-map-marker"></i>Location<i class="toggle-icon fa fa-angle-left"></i></label>
                            <div  id="location" class="collapse">
                                <fieldset>
                                    <c:forEach var="location" items="${locations}">
                                        <label class="form-check-label">
                                            <input class="form-check-input filter" type="checkbox" value="${location}" onchange="updateTable();" data-key="location">
                                            <c:out value="${location}" />
                                        </label>
                                    </c:forEach>
                                    <a href="#" class="clear-filter"><i class="fa fa-check"></i>clear filter</a>
                                </fieldset>
                            </div>
                        </div>
                        <div class="filter-group">
                            <label class="fieldset-label" data-toggle="collapse" data-target="#toggle-organization"><i class="fa fa-home"></i>Organization<i class="toggle-icon fa fa-angle-left"></i></label>
                            <div id="toggle-organization" class="collapse">
                                <fieldset>
                                    <select id="organization" class="filter form-control" onchange="updateTable();" data-key="organization">
                                        <option value="">All</option>
                                        <c:forEach var="organization" items="${organizations}">
                                            <option value="${organization}">
                                                <c:out value="${organization}" />
                                            </option>
                                        </c:forEach>
                                    </select>
                                </fieldset>
                            </div>
                        </div>
                        <div class="filter-group">
                            <label class="fieldset-label" data-toggle="collapse" data-target="#status"><i class="fa fa-check-circle"></i>Grant Status<i class="toggle-icon fa fa-angle-left"></i></label>
                            <div id="status" class="collapse">
                                <fieldset>
                                    <c:forEach var="status" items="${statuses}">
                                        <label class="form-check-label">
                                            <input class="form-check-input filter" type="checkbox" value="${status}" onchange="updateTable();" data-key="status">
                                            <c:out value="${status}" />
                                        </label>
                                    </c:forEach>
                                    <a href="#" class="clear-filter"><i class="fa fa-check"></i>clear filter</a>
                                </fieldset>
                            </div>
                        </div>
                        <div class="filter-group">
                            <label class="fieldset-label" data-toggle="collapse" data-target="#year"><i class="fa fa-calendar"></i>Fiscal Year<i class="toggle-icon fa fa-angle-left"></i></label>
                            <div id="year" class="collapse">
                                <fieldset>
                                    <span id="ex18-label-2a" class="hidden">Example low value</span>
                                    <span id="ex18-label-2b" class="hidden">Example high value</span>
                                    <input id="ex18b" type="text"/>
                                </fieldset>
                            </div>
                        </div>
                        <div class="filter-group">
                            <label class="fieldset-label" data-toggle="collapse" data-target="#amount"><i class="fa fa-dollar"></i>Grant Amount<i class="toggle-icon fa fa-angle-left"></i></label>
                            <div id="amount" class="collapse">
                                <fieldset>
                                    <div class="form-group">
                                        <label for="min-amount">min</label>
                                        <div class="input-group">
                                            <div class="input-group-addon">$</div>
                                            <input type="number" min="0" max="10000000" value="0" class="filter form-control" id="min-amount" data-key="amount-gte">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="min-amount">max</label>
                                        <div class="input-group">
                                            <div class="input-group-addon">$</div>
                                            <input type="number" min="0" max="10000000" value="10000000" class="filter form-control" id="max-amount" data-key="amount-lte">
                                        </div>
                                    </div>
                                    <a href="#" class="clear-filter"><i class="fa fa-check"></i>clear filter</a>
                                </fieldset>
                            </div>
                        </div>
                        <div class="filter-group">
                            <label class="fieldset-label" data-toggle="collapse" data-target="#people"><i class="fa fa-users"></i>People Served<i class="toggle-icon fa fa-angle-left"></i></label>
                            <div id="people" class="collapse">
                                <fieldset>
                                    <label for="min-total">Total</label>
                                    <fieldset>
                                        <div class="form-group">
                                            <label for="max-total">min</label>
                                            <input type="number" min="0" max="10000000" value="0" class="filter form-control" id="min-total" data-key="total-gte">
                                        </div>
                                        <div class="form-group">
                                            <label for="max-total">max</label>
                                            <input type="number" min="0" max="10000000" value="100000" class="filter form-control" id="max-total" data-key="total-lte">
                                        </div>
                                    </fieldset>
                                    <label for="min-hawaiians">Native Hawaiians</label>
                                    <fieldset>
                                        <div class="form-group">
                                            <label for="max-total">min</label>
                                            <input type="number" min="0" max="10000000" value="0" class="filter form-control" id="min-hawaiians" data-key="hawaiians-gte">
                                        </div>
                                        <div class="form-group">
                                            <label for="max-hawaiians">max</label>
                                            <input type="number" min="0" max="10000000" value="100000" class="filter form-control" id="max-hawaiians" data-key="hawaiians-lte">
                                        </div>
                                    </fieldset>
                                    <a href="#" class="clear-filter"><i class="fa fa-check"></i>clear filter</a>
                                </fieldset>
                            </div>
                        </div>
                        <div class="filter-group">
                            <button id="clear" class="btn btn-primary">Clear Filters</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="main-wrapper" class="main-wrapper">
            <div class="panes content-wrapper">
                <div class="tab-pane tables" id="all">
                    <div class="table-responsive">
                        <%@ include file="/WEB-INF/views/jspf/grants-table.jspf" %>
                    </div>
                </div>
                <div class="tab-pane charts" id="charts">
                    <div class="demo-chart">
                        <div id="priority-pie-chart" class="width: 100%; height: 100%"></div>
                        <canvas id="priority-pie-chart-canvas" style="display: none"></canvas>
                        <input type="hidden" id="priority-pie-chart-base64" value="" />
                    </div>
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
    <c:set var="req" value="${pageContext.request}" />
    <c:set var="url" value="${req.scheme}://${req.serverName}:${req.serverPort}" />
    <script type="text/javascript">
        localStorage.setItem("appUrl", "<c:out value='${url}' /><c:url value='/' />");
        localStorage.setItem("csrf.token", "<c:out value='${_csrf.token}' />");
    </script>
    <%@ include file="/WEB-INF/views/jspf/grants-modals.jspf" %>
    <%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
</body>

</html>
