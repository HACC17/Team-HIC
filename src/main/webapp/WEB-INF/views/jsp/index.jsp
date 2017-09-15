<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en">

<%@ include file="/WEB-INF/views/jspf/head.jspf" %>

<style>
    .blue-border {
        border: 1px solid #396FCD;
        margin-bottom: 10px;
        padding: 10px;
    }
</style>

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
                <a class="navbar-brand" href="<c:url value='/' />">OHA Grants</a>
            </div>
            <div class="dropdown admin-nav">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><i class="fa fa-user"></i>Admin Tools<span class="caret"></span></a>
                <ul class="dropdown-menu dropdown-menu-right">
                    <sec:authorize access="isAnonymous()">
                        <li><a href="javascript:void(0);" onclick="$('#login-form-modal').modal('show');"><i class="fa fa-sign-in"></i> Login</a></li>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                        <li><a href="javascript:void(0);" onclick="$('#add-grant-form-modal').modal('show');"><i class="fa fa-plus"></i> Add Grant</a></li>
                        <li><a href="javascript:void(0);" onclick="$('#import-sample-data-modal').modal('show');"><i class="fa fa-file-text"></i> Import Grants</a></li>
                        <li><a href="javascript:void(0);" onclick="$('#push-to-open-data-modal').modal('show');"><i class="fa fa-arrow-up"></i> Export to data.hawaii.gov</a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="<c:url value='/logout'/>"><i class="fa fa-sign-out"></i> Logout</a></li>
                    </sec:authorize>
                </ul>
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
                                            <input class="form-check-input filter" type="checkbox" value="${priority}" onchange="updateTable();" data-key="priority" data-chart-title="Strategic Priority">
                                            <c:out value="${priority}" />
                                        </label>
                                    </c:forEach>
                                    <a href="javascript:void(0)" class="clear-filter" data-key="priority"><i class="fa fa-check"></i>clear filter</a>
                                </fieldset>
                            </div>
                        </div>
                        <div class="filter-group">
                            <label class="fieldset-label" data-toggle="collapse" data-target="#toggle-result"><i class="fa fa-line-chart"></i>Strategic Result<i class="toggle-icon fa fa-angle-left"></i></label>
                            <div  id="toggle-result" class="collapse">
                                <fieldset>
                                    <c:forEach var="result" items="${results}">
                                        <label class="form-check-label">
                                            <input class="form-check-input filter" type="checkbox" value="${result}" onchange="updateTable();" data-key="result" data-chart-title="Strategic Result">
                                            <c:out value="${result}" />
                                        </label>
                                    </c:forEach>
                                    <a href="javascript:void(0)" class="clear-filter" data-key="result"><i class="fa fa-check"></i>clear filter</a>
                                </fieldset>
                            </div>
                        </div>
                        <div class="form-check filter-group">
                            <label class="fieldset-label" data-toggle="collapse" data-target="#grant-type"><i class="fa fa-info-circle"></i>Grant Type<i class="toggle-icon fa fa-angle-left"></i></label>
                            <div id="grant-type" class="collapse">
                                <fieldset>
                                    <c:forEach var="type" items="${types}">
                                        <label class="form-check-label">
                                            <input class="form-check-input filter" type="checkbox" value="${type}" onchange="updateTable();" data-key="type" data-chart-title="Grant Type">
                                            <c:out value="${type}" />
                                        </label>
                                    </c:forEach>
                                    <a href="javascript:void(0)" class="clear-filter" data-key="type"><i class="fa fa-check"></i>clear filter</a>
                                </fieldset>
                            </div>
                        </div>
                        <div class="filter-group">
                            <label class="fieldset-label" data-toggle="collapse" data-target="#location"><i class="fa fa-map-marker"></i>Location<i class="toggle-icon fa fa-angle-left"></i></label>
                            <div  id="location" class="collapse">
                                <fieldset>
                                    <c:forEach var="location" items="${locations}">
                                        <label class="form-check-label">
                                            <input class="form-check-input filter" type="checkbox" value="${location}" onchange="updateTable();" data-key="location" data-chart-title="Location">
                                            <c:out value="${location}" />
                                        </label>
                                    </c:forEach>
                                    <a href="javascript:void(0)" class="clear-filter" data-key="location"><i class="fa fa-check"></i>clear filter</a>
                                </fieldset>
                            </div>
                        </div>
                        <div class="filter-group">
                            <label class="fieldset-label" data-toggle="collapse" data-target="#toggle-organization"><i class="fa fa-home"></i>Organization<i class="toggle-icon fa fa-angle-left"></i></label>
                            <div id="toggle-organization" class="collapse">
                                <fieldset>
                                    <select id="organization" class="filter form-control" data-key="organization" data-chart-title="Organization">
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
                                            <input class="form-check-input filter" type="checkbox" value="${status}" onchange="updateTable();" data-key="status" data-chart-title="Grant Status">
                                            <c:out value="${status}" />
                                        </label>
                                    </c:forEach>
                                    <a href="javascript:void(0)" class="clear-filter" data-key="status"><i class="fa fa-check"></i>clear filter</a>
                                </fieldset>
                            </div>
                        </div>
                        <div class="filter-group">
                            <label class="fieldset-label" data-toggle="collapse" data-target="#year"><i class="fa fa-calendar"></i>Fiscal Year<i class="toggle-icon fa fa-angle-left"></i></label>
                            <div id="year" class="collapse">
                                <fieldset>
                                    <div class="form-group">
                                        <label for="fiscal-year-start">start</label>
                                        <div class="input-group">
                                            <div class="input-group-addon"></div>
                                            <input type="number" min="<c:out value='${startYear}'/>" max="<c:out value='${endYear}'/>" value="<c:out value='${startYear}'/>" class="filter form-control" id="fiscal-year-start" data-key="fiscal-gte">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="fiscal-year-end">end</label>
                                        <div class="input-group">
                                            <div class="input-group-addon"></div>
                                            <input type="number" min="<c:out value='${startYear}'/>" max="<c:out value='${endYear}'/>" value="<c:out value='${endYear}'/>" class="filter form-control" id="fiscal-year-end" data-key="fiscal-lte">
                                        </div>
                                    </div>
                                    <a href="javascript:void(0)" class="clear-filter-fiscal-year" onclick="resetDates();"><i class="fa fa-check"></i>clear filter</a>
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
                                            <input type="number" min="0" max="100000000" value="0" class="filter form-control" id="min-amount" data-key="amount-gte">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="min-amount">max</label>
                                        <div class="input-group">
                                            <div class="input-group-addon">$</div>
                                            <input type="number" min="0" max="100000000" value="10000000" class="filter form-control" id="max-amount" data-key="amount-lte">
                                        </div>
                                    </div>
                                    <a href="javascript:void(0)" class="clear-filter-amount"><i class="fa fa-check"></i>clear filter</a>
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
                                    <a href="javascript:void(0)" class="clear-filter-people"><i class="fa fa-check"></i>clear filter</a>
                                </fieldset>
                            </div>
                        </div>
                        <div class="filter-group">
                            <label class="fieldset-label" data-toggle="collapse" data-target="#toggle-chart-settings"><i class="fa fa-pie-chart"></i>Chart Settings<i class="toggle-icon fa fa-angle-left"></i></label>
                            <div id="toggle-chart-settings" class="collapse">
                                <fieldset>
                                    <div class="form-group">
                                        <label for="datatype">Select chart type</label>
                                        <div class="input-group">
                                            <input type="radio" name="chart-type" value="bar"> Bar&nbsp;&nbsp;&nbsp;<input type="radio" name="chart-type" value="pie"> Pie
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="datatype">Data to display</label>
                                        <div class="input-group">
                                            <div class="input-group-addon"><i id="datatype-i" class="fa fa-dollar"></i></div>
                                            <select id="datatype" onchange="updateLabel(); update();" class="form-control">
                                                <option value="AMOUNT" selected>Amount</option>
                                                <option value="TOTAL_NUMBER_SERVED">Total Number of People Served</option>
                                                <option value="NUMBER_NATIVE_HAWAIIANS_SERVED">Number of Native Hawaiians Served</option>
                                            </select>
                                        </div>
                                    </div>
                                </fieldset>
                            </div>
                        </div>
                        <div class="filter-group">
                            <button id="clear" class="btn btn-md btn-primary"><i class="fa fa-times"></i>Clear Filters</button><a href="javascript:void(0);" class="btn btn-md btn-primary" onclick="showHelpModalDialog();"><i class="fa fa-question-circle"></i>Help</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="main-wrapper" class="main-wrapper" style="background-color: #FFFFFF">
            <div class="panes content-wrapper" style="display: none">
                <div id="grants-table-container">
                    <div class="tab-pane" id="summary-cards" style="max-width: 100%">
                        <div class="row">
                            <div class="col-md-2">
                                <div class="card text-center">
                                    <div class="card-header"></div>
                                    <div class="card-block">
                                        <h4 class="card-title">Grants</h4>
                                        <h1 class="card-text" id="grant-count">0</h1>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-2">
                                <div class="card text-center">
                                    <div class="card-header"></div>
                                    <div class="card-block">
                                        <h4 class="card-title">Strategic Priorities</h4>
                                        <h1 class="card-text" id="priority-count">0</h1>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-2">
                                <div class="card text-center">
                                    <div class="card-header"></div>
                                    <div class="card-block">
                                        <h4 class="card-title">Strategic Results</h4>
                                        <h1 class="card-text" id="results-count">0</h1>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-2">
                                <div class="card text-center">
                                    <div class="card-header"></div>
                                    <div class="card-block">
                                        <h4 class="card-title">Grant Types</h4>
                                        <h1 class="card-text" id="type-count">0</h1>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-2">
                                <div class="card text-center">
                                    <div class="card-header"></div>
                                    <div class="card-block">
                                        <h4 class="card-title">Organizations</h4>
                                        <h1 class="card-text" id="organization-count">0</h1>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-2">
                                <div class="card text-center">
                                    <div class="card-header"></div>
                                    <div class="card-block">
                                        <h4 class="card-title">Locations</h4>
                                        <h1 class="card-text" id="location-count">0</h1>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-3">
                                <div class="card text-center">
                                    <div class="card-header"></div>
                                    <div class="card-block">
                                        <h4 class="card-title"><br />Fiscal Year</h4>
                                        <h1 class="card-text" id="fiscal-year-card">0</h1>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card text-center">
                                    <div class="card-header"></div>
                                    <div class="card-block">
                                        <h4 class="card-title">Number of<br />Native Hawaiians Served</h4>
                                        <h1 class="card-text" id="hawaiians-count">0</h1>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card text-center">
                                    <div class="card-header"></div>
                                    <div class="card-block">
                                        <h4 class="card-title">Total Number<br />of People Served</h4>
                                        <h1 class="card-text" id="total-number-count">0</h1>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card text-center">
                                    <div class="card-header"></div>
                                    <div class="card-block">
                                        <h4 class="card-title"><br />Total Amount</h4>
                                        <h1 class="card-text" id="total-amount">$0.00</h1>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="tab-pane tables" id="all" style="max-width:100%">
                        <div class="table-responsive">
                            <%@ include file="/WEB-INF/views/jspf/grants-table.jspf" %>
                        </div>
                    </div>
                </div>
                <div class="tab-pane charts" id="charts" style="max-width: 100%">
                    <div class="row">
                        <div class="col-md-4 blue-border">
                            <div id="priority-bar-chart"></div>
                            <div id="priority-pie-chart" style="display: none"></div>
                            <canvas id="priority-bar-chart-canvas" style="display: none"></canvas>
                            <canvas id="priority-pie-chart-canvas" style="display: none"></canvas>
                            <input type="hidden" id="priority-bar-chart-base64" value="" />
                            <input type="hidden" id="priority-pie-chart-base64" value="" />
                            <div class="input-group">
                                <div class="input-group-addon" onclick="showHelpModalDialog();"><i class="fa fa-question-circle" style="margin-right: 0px"></i></div>
                                <select id="drilldown-priority" class="form-control" data-key="priority" data-chart-title="Strategic Priority">
                                    <option value="">&lt;None&gt;</option>
                                    <option value="result">Strategic Result</option>
                                    <option value="type">Grant Type</option>
                                    <option value="location" selected>Location</option>
                                    <option value="status">Grant Status</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-4 blue-border">
                            <div id="result-bar-chart"></div>
                            <div id="result-pie-chart" style="display: none"></div>
                            <canvas id="result-bar-chart-canvas" style="display: none"></canvas>
                            <canvas id="result-pie-chart-canvas" style="display: none"></canvas>
                            <input type="hidden" id="result-bar-chart-base64" value="" />
                            <input type="hidden" id="result-pie-chart-base64" value="" />
                            <div class="input-group">
                                <div class="input-group-addon" onclick="showHelpModalDialog();"><i class="fa fa-question-circle" style="margin-right: 0px"></i></div>
                                <select id="drilldown-result" class="form-control" data-key="result" data-chart-title="Strategic Result">
                                    <option value="">&lt;None&gt;</option>
                                    <option value="priority" selected>Strategic Priority</option>
                                    <option value="type">Grant Type</option>
                                    <option value="location">Location</option>
                                    <option value="status">Grant Status</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-4 blue-border">
                            <div id="type-bar-chart"></div>
                            <div id="type-pie-chart" style="display: none"></div>
                            <canvas id="type-bar-chart-canvas" style="display: none"></canvas>
                            <canvas id="type-pie-chart-canvas" style="display: none"></canvas>
                            <input type="hidden" id="type-bar-chart-base64" value="" />
                            <input type="hidden" id="type-pie-chart-base64" value="" />
                            <div class="input-group">
                                <div class="input-group-addon" onclick="showHelpModalDialog();"><i class="fa fa-question-circle" style="margin-right: 0px"></i></div>
                                <select id="drilldown-type" class="form-control" data-key="type" data-chart-title="Grant Type">
                                    <option value="">&lt;None&gt;</option>
                                    <option value="priority">Strategic Priority</option>
                                    <option value="result">Strategic Results</option>
                                    <option value="location">Location</option>
                                    <option value="status" selected>Grant Status</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-8 blue-border">
                            <div id="location-bar-chart"></div>
                            <div id="location-pie-chart" style="display: none"></div>
                            <canvas id="location-bar-chart-canvas" style="display: none"></canvas>
                            <canvas id="location-pie-chart-canvas" style="display: none"></canvas>
                            <input type="hidden" id="location-bar-chart-base64" value="" />
                            <input type="hidden" id="location-pie-chart-base64" value="" />
                            <div class="input-group">
                                <div class="input-group-addon" onclick="showHelpModalDialog();"><i class="fa fa-question-circle" style="margin-right: 0px"></i></div>
                                <select id="drilldown-location" class="form-control" data-key="location" data-chart-title="Location">
                                    <option value="" selected>&lt;None&gt;</option>
                                    <option value="priority">Strategic Priority</option>
                                    <option value="result">Strategic Results</option>
                                    <option value="type">Grant Type</option>
                                    <option value="status" >Grant Status</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-4 blue-border">
                            <div id="status-bar-chart"></div>
                            <div id="status-pie-chart" style="display: none"></div>
                            <canvas id="status-bar-chart-canvas" style="display: none"></canvas>
                            <canvas id="status-pie-chart-canvas" style="display: none"></canvas>
                            <input type="hidden" id="status-bar-chart-base64" value="" />
                            <input type="hidden" id="status-pie-chart-base64" value="" />
                            <div class="input-group">
                                <div class="input-group-addon" onclick="showHelpModalDialog();"><i class="fa fa-question-circle" style="margin-right: 0px"></i></div>
                                <select id="drilldown-status" class="form-control" data-key="status" data-chart-title="Grant Status">
                                    <option value="">&lt;None&gt;</option>
                                    <option value="priority">Strategic Priority</option>
                                    <option value="result" selected>Strategic Results</option>
                                    <option value="type">Grant Type</option>
                                    <option value="location">Location</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 blue-border">
                            <div id="top-5-orgs-column-chart" class="width: 100%; height: 100%"></div>
                            <div class="input-group">
                                <div class="input-group-addon" onclick="showHelpModalDialog();"><i class="fa fa-question-circle" style="margin-right: 0px"></i></div>
                                <select id="column-top-n" class="form-control">
                                    <option value="5" selected>Show top 5 organizations</option>
                                    <option value="10">Show top 10 organizations</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6 blue-border">
                            <div id="top-5-orgs-spline-chart" class="width: 100%; height: 100%"></div>
                            <div class="input-group">
                                <div class="input-group-addon" onclick="showHelpModalDialog();"><i class="fa fa-question-circle" style="margin-right: 0px"></i></div>
                                <select id="spline-top-n" onchange="drawTopOrganizationsSplineChart();" class="form-control">
                                    <option value="5" selected>Show top 5 organizations</option>
                                    <option value="10">Show top 10 organizations</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <footer style="display: none">
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

        var keys = ['priority', 'result', 'type', 'location', 'status'];

        var timer;
        function update() {
            $("input[data-key='priority']").first().trigger("change");
            drawTopOrganizationsBarChart($("#column-top-n").val(), $("#fiscal-year-start").val(), $("#fiscal-year-end").val());
            drawTopOrganizationsSplineChart();
        }

        function updateLabel() {
            if ($("#datatype").val() == "AMOUNT") {
                $("#datatype-i").removeClass("fa-users");
                $("#datatype-i").addClass("fa-dollar");
            } else {
                $("#datatype-i").addClass("fa-users");
                $("#datatype-i").removeClass("fa-dollar");
            }
        }

        function getMap(key) {
            var map = {};
            map["aggregateField"] = $("#datatype").val();
            map["filters"] = getFilters();
            map["drilldown"] = $("#drilldown-" + key).val();
            map["groupBy"] = key;
            return map;
        }

        function showHelpModalDialog() {
            $("#help-modal").modal("show");
        }

        function addListeners(id1, id2) {
            $("#" + id1).on("change keyup", function(event) {
                var min = $(this).val().replace(/^0+/, '');
                var max = $("#" + id2).val().replace(/^0+/, '');
                if (parseInt(min) > parseInt(max)) {
                    $(this).val(max);
                } else {
                    $(this).val(min);
                    update();
                }
            });
            $("#" + id2).on("change keyup", function(event) {
                var max = $(this).val().replace(/^0+/, '');
                var min = $("#" + id1).val().replace(/^0+/, '');
                if (parseInt(max) < parseInt(min)) {
                    $(this).val(min);
                } else {
                    $(this).val(max);
                    update();
                }
            });
        }

        $(document).ready(function() {
            $("#main-wrapper > div").hide();
            $("#main-wrapper").append("<div id='temp-div' style='background-color: white; height: 100%; width: 100%'></div>");
            $("#main-wrapper").LoadingOverlay("show", { color : "rgba(255, 255, 255, 1.0)", zIndex : 1 });
            localStorage.setItem("init", true);

            $("input[value='bar']").prop("checked", true);

            $("input[name='chart-type']").change(function() {
                $.each(keys, function(index, value) {
                    var type = $("input[name='chart-type']:checked").val();
                    if (type == 'bar') {
                        $("#" + value + "-pie-chart").hide();
                        $("#" + value + "-bar-chart").show();
                    } else {
                        $("#" + value + "-bar-chart").hide();
                        $("#" + value + "-pie-chart").show();
                    }
                });
            });

            Highcharts.setOptions({
                lang: {
                    thousandsSep: ','
                }
            });

            $("#column-top-n").change(function() {
                drawTopOrganizationsBarChart($(this).val(), $("#fiscal-year-start").val(), $("#fiscal-year-end").val());
            });

            var timer;
            $.each(keys, function(index, value) {
                $("input[data-key='" + value + "']").change(function() {
                    $.each(keys, function(i, v) {
                        var element = $("input[data-key='" + v + "']").first();
                        var key = element.data("key");
                        var title = element.data("chart-title");
                        var map = getMap(element.data("key"));
                        drawChart($("input[name='chart-type']:checked").val(), key, title, map);
                    });
                });
            });

            addListeners("fiscal-year-start", "fiscal-year-end");
            addListeners("min-amount", "max-amount");
            addListeners("min-total", "max-total");
            addListeners("min-hawaiians", "max-hawaiians");

            $("#clear").click(function() {
                if (!confirm("Are you sure you want to clear ALL filters?")) {
                    return;
                }
                $(".filter").each(function() {
                    if ($(this).is(':checkbox')) {
                        $(this).prop('checked', false);
                    } else {
                        $(this).val("");
                    }
                });
                $("#organization").val("");
                $("#fiscal-year-start").text("<c:out value='${startYear}'/>");
                $("#fiscal-year-start").val("<c:out value='${startYear}'/>");
                $("#fiscal-year-end").text("<c:out value='${endYear}'/>");
                $("#fiscal-year-end").val("<c:out value='${endYear}'/>");
                $("#min-amount").text(0);
                $("#min-amount").val(0);
                $("#max-amount").text(10000000);
                $("#max-amount").val(10000000);
                $("#min-total").text(0);
                $("#min-total").val(0);
                $("#max-total").text(0);
                $("#max-total").val(100000);
                $("#min-hawaiians").text(0);
                $("#min-hawaiians").val(0);
                $("#max-hawaiians").text(100000);
                $("#max-hawaiians").val(100000);
                update();
            });
            $(".clear-filter").click(function() {
                $("input[data-key='" + $(this).data("key") + "']").each(function() {
                    $(this).prop('checked', false);
                });
                update();
            });
            $(".clear-filter-fiscal-year").click(function() {
                $("#fiscal-year-start").text("<c:out value='${startYear}'/>");
                $("#fiscal-year-start").val("<c:out value='${startYear}'/>");
                $("#fiscal-year-end").text("<c:out value='${endYear}'/>");
                $("#fiscal-year-end").val("<c:out value='${endYear}'/>");
                update();
            });
            $(".clear-filter-amount").click(function() {
                $("#min-amount").text(0);
                $("#min-amount").val(0);
                $("#max-amount").text(10000000);
                $("#max-amount").val(10000000);
                update();
            });
            $(".clear-filter-people").click(function() {
                $("#min-total").text(0);
                $("#min-total").val(0);
                $("#max-total").text(0);
                $("#max-total").val(100000);
                $("#min-hawaiians").text(0);
                $("#min-hawaiians").val(0);
                $("#max-hawaiians").text(100000);
                $("#max-hawaiians").val(100000);
                update();
            });
            $("#organization").change(function() {
                update();
            });
            $.each(keys, function(index, value) {
                $("#drilldown-" + value).change(function() {
                    var map = getMap($(this).data("key"));
                    drawChart($("input[name='chart-type']:checked").val(), $(this).data("key"), $(this).data("chart-title"), map);
                });
            });

            update();
        });
    </script>
    <%@ include file="/WEB-INF/views/jspf/grants-modals.jspf" %>
    <%@ include file="/WEB-INF/views/jspf/scripts.jspf" %>
    <%@ include file="/WEB-INF/views/jspf/help-modal.jspf" %>
    <sec:authorize access="isAnonymous()">
        <%@ include file="/WEB-INF/views/jspf/login-form-modal.jspf" %>
    </sec:authorize>
    <sec:authorize access="hasRole('ROLE_ADMIN')">
        <%@ include file="/WEB-INF/views/jspf/admin/add-grant-form-modal.jspf" %>
        <%@ include file="/WEB-INF/views/jspf/admin/import-sample-data-modal.jspf" %>
        <%@ include file="/WEB-INF/views/jspf/admin/push-to-open-data-modal.jspf" %>
    </sec:authorize>
</body>

</html>
