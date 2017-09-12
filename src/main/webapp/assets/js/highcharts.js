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

function getPointY() {
    if ($("#datatype").val() == "TOTAL_NUMBER_SERVED") {
        return '{point.y} people'
    } else if ($("#datatype").val() == "NUMBER_NATIVE_HAWAIIANS_SERVED") {
        return '{point.y} Native Hawaiians';
    }
    return '${point.y}';
}

var colors = [ '#4173CE', '#3D424A', '#4BA4B0', '#BA8F7E', '#E3604A' ];

function drawPieChart(key, title, map) {
    var json = JSON.parse(localStorage.getItem(key));
    var data = [];
    $.each(json["totals"], function(key, value) {
        data.push({ name: key, y: value, drilldown: key });
    });

    var drilldown = [];
    $.each(json, function(key, value) {
        if (key != "totals") {
            var map = { name: key, id: key, data: [] };
            $.each(value, function(k, v) {
                map["data"].push([ k, v ]);
            });
            drilldown.push(map);
        }
    });

    // Create the chart
    var chart = Highcharts.chart(key + '-pie-chart', {
        chart: {
            type: 'pie',
            borderWidth: 0,
            backgroundColor: null
        },
        colors: colors,
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
            headerFormat: '',
            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>' + getPointY() + '</b><br/>'
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

function drawBarChart(key, title, map) {
    var json = JSON.parse(localStorage.getItem(key));
    var series = [];
    var series1 = { name: title, colorByPoint: true, data: [] };
    $.each(json["totals"], function(k, value) {
        series1["data"].push({ name: k, y: value, drilldown: k });
    });
    series.push(series1);

    var drilldown = [];
    $.each(json, function(k1, value) {
        if (k1 != "totals") {
            var series = { name: k1, id: k1, data: [] };
            $.each(value, function(k2, v) {
                series["data"].push([ k2, v ]);
            });
            drilldown.push(series);
        }
    });

    Highcharts.chart(key + '-pie-chart', {
        chart: {
            type: 'bar',
            borderWidth: 0,
            backgroundColor: null
        },
        colors: colors,
        title: {
            text: title
        },
        xAxis: {
            categories: [ title ],
            labels: {
                enabled: false,
            }
        },
        yAxis: {
            min: 0
        },
        plotOptions: {
            bar: {
                dataLabels: {
                    allowOverlap: true,
                    enabled: true,
                    formatter: function() {
                        if ($("#datatype").val() == "AMOUNT") {
                            return accounting.formatMoney(this.point.y);
                        }
                        return accounting.formatNumber(this.point.y);
                    }
                }
            }
        },
        credits: {
            enabled: false
        },
        legend: {
            enabled: false
        },
        tooltip: {
            headerFormat: '',
            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>' + getPointY() + '</b><br/>'
        },
        series: series,
        drilldown: { series: drilldown }
    });
}

function drawChart(chartType, key, title, map) {
    $.ajax({
        type: 'POST',
        url: baseUrl + "charts/totals",
        data: JSON.stringify(map),
        contentType: 'application/json',
        headers: {
            'X-CSRF-TOKEN': localStorage.getItem("csrf.token")
        },
        success: function(response) {
            localStorage.setItem(key, response);

            if (chartType == 'pie') {
                drawPieChart(key, title, map);
            }
            else if (chartType == 'bar') {
                drawBarChart(key, title, map);
            }
        }
    });
}

function drawTop5OrganizationsBarChart(startYear, endYear) {
    var field = $("#datatype").val();

    $.get(baseUrl + "charts/fiscalYearTop?top=5&startYear=" + startYear + "&endYear=" + endYear + "&field="+ field, function(data, status) {
        var json = JSON.parse(data);

        var series = [];
        $.each(json["totals"], function(i, val) {
            series.push({ name: i, data: [ val ] });
        });

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
                borderWidth: 0,
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
                pointFormat: '<span style="color:{point.color}">{series.name}</span>: <b>' + getPointY() + '</b><br/>'
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
            var map = { name: index, marker: { symbol: 'square' }, data: [] };
            var year = 2013;
            $.each(value, function(i, v) {
                $.each(v, function(i2, v2) {
                    if (year == i2) {
                        map["data"].push(v2);
                        year++;
                    } else {
                        do {
                            map["data"].push(0);
                            year++;
                        } while (year != i2);
                        map["data"].push(v2);
                    }
                });
            });
            series.push(map);
        });

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
                borderWidth: 0,
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
                pointFormat: '<span style="color:{point.color}">{series.name}</span>: <b>' + getPointY() + '</b><br/>'
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
