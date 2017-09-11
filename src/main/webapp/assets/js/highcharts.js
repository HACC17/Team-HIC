function getFilters() {
    var filters = {};
    filters["filters"] = [];
    filters["filterValues"] = [];
    $(".filter").each(function() {
        if ($(this).is(':checkbox')) {
            if ($(this).is(':checked')) {
                var array = filters["filters"];
                filters["filters"].push($(this).data("key"));
                var array = filters["filterValues"];
                filters["filterValues"].push($(this).val());
            }
        }
        else if ($(this).val() != null && $(this).val() != '') {
            var array = filters["filters"];
            filters["filters"].push($(this).data("key"));
            var array = filters["filterValues"];
            filters["filterValues"].push($(this).val());
        }
    });
    return filters;
}

var baseUrl = localStorage.getItem("appUrl");

function drawChart(key, title, map) {
    $.ajax({
        type: 'POST',
        url: baseUrl + "charts/totals",
        data: JSON.stringify(map),
        contentType: 'application/json',
        headers: {
            'X-CSRF-TOKEN': localStorage.getItem("csrf.token")
        },
        success: function(response) {
            var json = JSON.parse(response);

            var data = [];
            $.each(json["totals"], function(key, value) {
                var map = {};
                map["name"] = key;
                map["y"] = value;
                map["drilldown"] = key;
                data.push(map);
            });

            var drilldown = [];
            $.each(json, function(key, value) {
                if (key != "totals") {
                    var map = {};
                    map["name"] = key;
                    map["id"] = key;
                    var data = [];
                    $.each(value, function(key2, value2) {
                        var point = [];
                        point.push(key2);
                        point.push(value2);
                        data.push(point);
                    });
                    map["data"] = data;
                    drilldown.push(map);
                }
            });

            var format = '{point.name}: ${point.y}';
            var pointY = '${point.y}'
            if ($("#datatype").val() == "TOTAL_NUMBER_SERVED") {
                pointY = '{point.y} people'
            } else if ($("#datatype").val() == "NUMBER_NATIVE_HAWAIIANS_SERVED") {
                pointY = '{point.y} Native Hawaiians';
            }

            // Create the chart
            var chart = Highcharts.chart(key + '-pie-chart', {
                chart: {
                    type: 'pie',
                    renderTo: key + '-pie-chart',
                    borderColor: "#CCCCCC",
                    borderWidth: 1,
                    backgroundColor: null,
                    events: {
                        load: function() {
                            $("#" + key + "-pie-chart").LoadingOverlay("hide");
                        }
                    }
                },
                colors: [
                    '#7CB5EC', '#434348', '#90ED7D', '#F7A35C', '#8085E9',
                    '#F15C80', '#E4D354', '#2B908F', '#F45B5B', '#91E8E1',
                    '#FF0000', '#FF8000', '#00FF00', '#0000FF', '#BF00FF'
                ],
                title: {
                    text: title
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: false,
                        },
                        showInLegend: true
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                    pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>' + pointY + '</b><br/>'
                },
                series: [{
                    name: title,
                    colorByPoint: true,
                    data: data
                }],
                drilldown: {
                    series: drilldown
                },
            });

            canvg(document.getElementById(key + "-pie-chart-canvas"), chart.getSVG());

            var canvas = document.getElementById(key + "-pie-chart-canvas");
            var img = canvas.toDataURL("image/png");
            $("#" + key + "-pie-chart-base64").val(img.substring(img.indexOf(',') + 1));
        }
    });
}

function drawTop5OrganizationsBarChart(startYear, endYear) {
    var field = $("#datatype").val();

    $.get(baseUrl + "charts/fiscalYearTop?top=5&startYear=" + startYear + "&endYear=" + endYear + "&field="+ field, function(data, status) {
        var json = JSON.parse(data);

        var series = [];
        $.each(json["totals"], function(i, val) {
            var map = {};
            map["name"] = i;
            map["data"] = [ val ];
            series.push(map);
        });

        var pointY = '${point.y}'
        if ($("#datatype").val() == "TOTAL_NUMBER_SERVED") {
            pointY = '{point.y} people'
        } else if ($("#datatype").val() == "NUMBER_NATIVE_HAWAIIANS_SERVED") {
            pointY = '{point.y} Native Hawaiians';
        }

        var label;
        if (field == "AMOUNT") {
            label = "Total Amount of Money";
        } else if (field == "TOTAL_NUMBER_SERVED") {
            label = "Total Number of People Served";
        } else {
            label = "Total Number of Native Hawaiians Served";
        }

        Highcharts.chart('top-5-orgs-column-chart', {
            chart: {
                type: 'column',
                borderColor: "#CCCCCC",
                borderWidth: 1,
                backgroundColor: null
            },
            title: {
                text: 'Top 5 Organizations by ' + label
            },
            xAxis: {
                categories: [ 'Organizations' ]
            },
            yAxis: {
                min: 0,
                title: {
                    text: label
                },
                stackLabels: {
                    enabled: true,
                    formatter: function() {
                        if (field == "AMOUNT") {
                            return "Total: " + accounting.formatMoney(this.total);
                        } else if (field == "TOTAL_NUMBER_SERVED") {
                            return "Total: " + accounting.formatNumber(this.total) + " people";
                        } else {
                            return "Total: " + accounting.formatNumber(this.total) + " Native Hawaiians";
                        }
                    }
                }
            },
            legend: {
                reversed: true
            },
            plotOptions: {
                series: {
                    stacking: 'normal'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:11px">{point.x}</span><br>',
                pointFormat: '<span style="color:{point.color}">{series.name}</span>: <b>' + pointY + '</b><br/>'
            },
            series: series
        });
    });
}

function drawTopOrganizationsSplineChart() {
    var field = $("#datatype").val();
    var topN = $("select#top-n-selector").val();

    $.get(baseUrl + "charts/time?top=" + topN + "&field="+ field, function(data, status) {
        var json = JSON.parse(data);

        var series = [];
        $.each(json, function(index, value) {
            var map = {};
            map["name"] = index;
            map["marker"] = { symbol: 'square' };
            var data = [];
            var year = 2013;
            $.each(value, function(i, v) {
                $.each(v, function(i2, v2) {
                    if (year == i2) {
                        data.push(v2);
                        year++;
                    } else {
                        do {
                            data.push(0);
                            year++;
                        } while (year != i2);
                        data.push(v2);
                    }
                });
            });
            map["data"] = data;
            series.push(map);
        });

        var pointY = '${point.y}'
        if ($("#datatype").val() == "TOTAL_NUMBER_SERVED") {
            pointY = '{point.y} people'
        } else if ($("#datatype").val() == "NUMBER_NATIVE_HAWAIIANS_SERVED") {
            pointY = '{point.y} Native Hawaiians';
        }

        var label;
        if (field == "AMOUNT") {
            label = "Amount of Money";
        } else if (field == "TOTAL_NUMBER_SERVED") {
            label = "Total Number of People Served";
        } else {
            label = "Number of Native Hawaiians Served";
        }

        Highcharts.chart('top-5-orgs-spline-chart', {
            chart: {
                type: 'spline',
                borderColor: "#CCCCCC",
                borderWidth: 1,
                backgroundColor: null
            },
            title: {
                text: 'Top ' + topN + ' Organizations by ' + label + ' from 2013 to 2016'
            },
            xAxis: {
                categories: ['2013', '2014', '2015', '2016']
            },
            yAxis: {
                title: {
                    text: label
                },
                tickamount: 8,
                labels: {
                    formatter: function() {
                        if (field == "AMOUNT") {
                            return accounting.formatMoney(this.value);
                        }
                        return accounting.formatNumber(this.value);
                    }
                }
            },
            tooltip: {
                crosshairs: true,
                shared: true,
                pointFormat: '<span style="color:{point.color}">{series.name}</span>: <b>' + pointY + '</b><br/>'
            },
            plotOptions: {
                spline: {
                    marker: {
                        radius: 4,
                        lineColor: '#666666',
                        lineWidth: 1
                    }
                }
            },
            series: series
        });

    });

}

$(document).ready(function() {

    Highcharts.setOptions({
        lang: {
            thousandsSep: ','
        }
    });

    var baseUrl = localStorage.getItem('request');

    var keys = JSON.parse(localStorage.getItem("keys"));

    var timer;
    $.each(keys, function(index, value) {
        $("input[data-key='" + value + "']").change(function() {
            if (timer) {
                clearTimeout(timer);
            }
            timer = setTimeout(function() {
                var map = {};
                map["aggregateField"] = $("#datatype").val();
                map["filters"] = getFilters();

                $.each(keys, function(i, v) {
                    var element = $("input[data-key='" + v + "']").first();
                    map["drilldown"] = $("#drilldown-" + element.data("key")).val();
                    map["groupBy"] = element.data("key");
                    drawChart(element.data("key"), element.data("chart-title"), map);
                });
            }, 2000);
        });

        $("input[data-key='" + value + "']").first().trigger("change");
    });

    drawTopOrganizationsSplineChart();
});
