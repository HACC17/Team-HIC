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
                <a class="navbar-brand" href="<c:url value='/' />">OHA Grants</a>
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
                                            <input type="number" min="2013" max="2016" value="2013" class="filter form-control" id="fiscal-year-start" data-key="fiscal-gte">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="fiscal-year-end">end</label>
                                        <div class="input-group">
                                            <div class="input-group-addon"></div>
                                            <input type="number" min="2013" max="2016" value="2016" class="filter form-control" id="fiscal-year-end" data-key="fiscal-lte">
                                        </div>
                                    </div>
                                    <a href="javascript:void(0)" class="clear-filter" onclick="resetDates();"><i class="fa fa-check"></i>clear filter</a>
                                </fieldset>
                                <script type="text/javascript">
                                    $(document).ready(function() {
                                        $("#fiscal-year-start").on("change keyup", function(event) {
                                            if (parseInt($(this).val()) > parseInt($("#fiscal-year-end").val())) {
                                                $(this).val($("#fiscal-year-end").val());
                                            } else {
                                                update();
                                            }
                                        });
                                        $("#fiscal-year-end").on("change keyup", function(event) {
                                            if (parseInt($(this).val()) < parseInt($("#fiscal-year-start").val())) {
                                                $(this).val($("#fiscal-year-start").val());
                                            } else {
                                                update();
                                            }
                                        });
                                    });
                                </script>
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
                                    <a href="javascript:void(0)" class="clear-filter"><i class="fa fa-check"></i>clear filter</a>
                                </fieldset>
                                <script type="text/javascript">
                                    $(document).ready(function() {
                                        $("#min-amount").on("change keyup", function(event) {
                                            if (parseInt($(this).val()) > parseInt($("#max-amount").val())) {
                                                $(this).val($("#max-amount").val());
                                            } else {
                                                update();
                                            }
                                        });
                                        $("#max-amount").on("change keyup", function(event) {
                                            if (parseInt($(this).val()) < parseInt($("#min-amount").val())) {
                                                $(this).val($("#min-amount").val());
                                            } else {
                                                update();
                                            }
                                        });
                                    });
                                </script>
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
                                    <script type="text/javascript">
                                        $(document).ready(function() {
                                            $("#min-total").on("change keyup", function(event) {
                                                if (parseInt($(this).val()) > parseInt($("#max-total").val())) {
                                                    $(this).val($("#max-total").val());
                                                } else {
                                                    update();
                                                }
                                            });
                                            $("#max-total").on("change keyup", function(event) {
                                                if (parseInt($(this).val()) < parseInt($("#min-total").val())) {
                                                    $(this).val($("#min-total").val());
                                                } else {
                                                    update();
                                                }
                                            });
                                        });
                                    </script>
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
                                    <a href="javascript:void(0)" class="clear-filter"><i class="fa fa-check"></i>clear filter</a>
                                    <script type="text/javascript">
                                        $(document).ready(function() {
                                            $("#min-hawaiians").on("change keyup", function(event) {
                                                if (parseInt($(this).val()) > parseInt($("#max-hawaiians").val())) {
                                                    $(this).val($("#max-hawaiians").val());
                                                } else {
                                                    update();
                                                }
                                            });
                                            $("#max-hawaiians").on("change keyup", function(event) {
                                                if (parseInt($(this).val()) < parseInt($("#min-hawaiians").val())) {
                                                    $(this).val($("#min-hawaiians").val());
                                                } else {
                                                    update();
                                                }
                                            });
                                        });
                                    </script>
                                </fieldset>
                            </div>
                        </div>
                        <div class="filter-group">
                            <button id="clear" class="btn btn-primary">Clear Filters</button>
                        </div>
                        <div class="filter-group">
                            <label class="fieldset-label" data-toggle="collapse" data-target="#toggle-chart-settings"><i class="fa fa-pie-chart"></i>Chart Settings<i class="toggle-icon fa fa-angle-left"></i></label>
                            <div id="toggle-chart-settings" class="collapse">
                                <fieldset>
                                    <div class="form-group">
                                        <label for="datatype">Data to display in <strong>all charts</strong></label>
                                        <div class="input-group">
                                            <div class="input-group-addon"><i id="datatype-i" class="fa fa-dollar"></i></div>
                                            <select id="datatype" onchange="updateLabel(); update();" class="form-control">
                                                <option value="AMOUNT" selected>Amount</option>
                                                <option value="TOTAL_NUMBER_SERVED">Total Number of People Served</option>
                                                <option value="NUMBER_NATIVE_HAWAIIANS_SERVED">Number of Native Hawaiians Served</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="drilldown-priority">Configure drilldown for <strong>each pie chart</strong></label>
                                        <div class="input-group">
                                            <div class="input-group-addon"><i class="fa fa-area-chart"></i></div>
                                            <select id="drilldown-priority" class="form-control" data-key="priority" data-chart-title="Strategic Priority">
                                                <option value="">&lt;None&gt;</option>
                                                <option value="result">Strategic Result</option>
                                                <option value="type">Grant Type</option>
                                                <option value="location" selected>Location</option>
                                                <option value="status">Grant Status</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="input-group">
                                            <div class="input-group-addon"><i class="fa fa-line-chart"></i></div>
                                            <select id="drilldown-result" class="form-control" data-key="result" data-chart-title="Strategic Result">
                                                <option value="">&lt;None&gt;</option>
                                                <option value="priority" selected>Strategic Priority</option>
                                                <option value="type">Grant Type</option>
                                                <option value="location">Location</option>
                                                <option value="status">Grant Status</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="input-group">
                                            <div class="input-group-addon"><i class="fa fa-info-circle"></i></div>
                                            <select id="drilldown-type" class="form-control" data-key="type" data-chart-title="Grant Type">
                                                <option value="">&lt;None&gt;</option>
                                                <option value="priority">Strategic Priority</option>
                                                <option value="result">Strategic Results</option>
                                                <option value="location">Location</option>
                                                <option value="status" selected>Grant Status</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="input-group">
                                            <div class="input-group-addon"><i class="fa fa-map-marker"></i></div>
                                            <select id="drilldown-location" class="form-control" data-key="location" data-chart-title="Location">
                                                <option value="" selected>&lt;None&gt;</option>
                                                <option value="priority">Strategic Priority</option>
                                                <option value="result">Strategic Results</option>
                                                <option value="type">Grant Type</option>
                                                <option value="status" >Grant Status</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="input-group">
                                            <div class="input-group-addon"><i class="fa fa-check-circle"></i></div>
                                            <select id="drilldown-status" class="form-control" data-key="status" data-chart-title="Grant Status">
                                                <option value="">&lt;None&gt;</option>
                                                <option value="priority">Strategic Priority</option>
                                                <option value="result" selected>Strategic Results</option>
                                                <option value="type">Grant Type</option>
                                                <option value="location">Location</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="datatype">Configure <strong>top N time series chart</strong></label>
                                        <div class="input-group">
                                            <div class="input-group-addon"><i id="top-n-selector" class="fa fa-line-chart"></i></div>
                                            <select id="top-n-selector" onchange="drawTopOrganizationsSplineChart();" class="form-control">
                                                <option value="5" selected>Show top 5 organizations</option>
                                                <option value="10">Show top 10 organizations</option>
                                            </select>
                                        </div>
                                    </div>
                                </fieldset>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="main-wrapper" class="main-wrapper">
            <div class="panes content-wrapper">
                <div id="grants-table-container">
                    <div class="tab-pane" id="summary-cards">
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
                    <div class="tab-pane tables" id="all">
                        <div class="table-responsive">
                            <%@ include file="/WEB-INF/views/jspf/grants-table.jspf" %>
                        </div>
                    </div>
                </div>
                <div class="tab-pane charts" id="charts">
                    <div class="row">
                        <div class="col-md-4">
                            <div id="priority-pie-chart"></div>
                            <canvas id="priority-pie-chart-canvas" style="display: none"></canvas>
                            <input type="hidden" id="priority-pie-chart-base64" value="" />
                        </div>
                        <div class="col-md-4">
                            <div id="result-pie-chart"></div>
                            <canvas id="result-pie-chart-canvas" style="display: none"></canvas>
                            <input type="hidden" id="result-pie-chart-base64" value="" />
                        </div>
                        <div class="col-md-4">
                            <div id="type-pie-chart"></div>
                            <canvas id="type-pie-chart-canvas" style="display: none"></canvas>
                            <input type="hidden" id="type-pie-chart-base64" value="" />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-8">
                            <div id="location-pie-chart"></div>
                            <canvas id="location-pie-chart-canvas" style="display: none"></canvas>
                            <input type="hidden" id="location-pie-chart-base64" value="" />
                        </div>
                        <div class="col-md-4">
                            <div id="status-pie-chart"></div>
                            <canvas id="status-pie-chart-canvas" style="display: none"></canvas>
                            <input type="hidden" id="status-pie-chart-base64" value="" />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div id="top-5-orgs-column-chart" class="width: 100%; height: 100%"></div>
                        </div>
                        <div class="col-md-6">
                            <div id="top-5-orgs-spline-chart" class="width: 100%; height: 100%"></div>
                        </div>
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

        var keys = ['priority', 'result', 'type', 'location', 'status'];
        localStorage.setItem("keys", JSON.stringify(keys));

        var timer;
        function update() {
            if (timer) {
                clearTimeout(timer);
            }
            var timer = setTimeout(function() {
                updateTable();
                $.each(keys, function(index, value) {
                    $("input[data-key='" + value + "']").first().trigger("change");
                });
                drawTop5OrganizationsBarChart($("#fiscal-year-start").val(), $("#fiscal-year-end").val());
            }, 2000);
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

        $(document).ready(function() {
            $("#grants-table-container").LoadingOverlay("show");

            $("#clear").click(function() {
                $(".filter").each(function() {
                    if ($(this).is(':checkbox')) {
                        $(this).prop('checked', false);
                    } else {
                        $(this).val("");
                    }
                });
                $("#organization").val("");
                $("#fiscal-year-start").val("2013");
                $("#fiscal-year-end").val("2016");
                $("#min-total").val("0");
                $("#max-total").val("100000");
                $("#min-hawaiians").val("0");
                $("#max-hawaiians").val("100000");
                update();
            });
            $(".clear-filter").click(function() {
                $("input[data-key='" + $(this).data("key") + "']").each(function() {
                    $(this).prop('checked', false);
                });
                update();
            });
            $("#organization").change(function() {
                update();
            });
            $.each(keys, function(index, value) {
            	$("#" + value + "-pie-chart").LoadingOverlay("show");
                $("#drilldown-" + value).change(function() {
                    $("#" + value + "-pie-chart").LoadingOverlay("show");
                    var map = {};
                    map["aggregateField"] = $("#datatype").val();
                    map["filters"] = getFilters();
                    map["drilldown"] = $("#drilldown-" + value).val();
                    map["groupBy"] = $(this).data("key");
                    drawChart($(this).data("key"), $(this).data("chart-title"), map);
                });
            });
            drawTop5OrganizationsBarChart($("#fiscal-year-start").val(), $("#fiscal-year-end").val());
        });
    </script>
    <%@ include file="/WEB-INF/views/jspf/grants-modals.jspf" %>
    <%@ include file="/WEB-INF/views/jspf/scripts.jspf" %>
</body>

</html>
