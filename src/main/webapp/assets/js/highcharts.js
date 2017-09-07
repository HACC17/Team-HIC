$(document).ready(function() {

    Highcharts.setOptions({
        lang: {
            thousandsSep: ','
        }
    });

    var baseUrl = localStorage.getItem('request');

    $("#fiscalYearSelect2, #drilldownField").change(function() {
        var fiscalYear = $("#fiscalYearSelect2").val();
        var field = $("#drilldownField").val();

        $.get(baseUrl + "charts/locations?aggregateField=" + field + "&filter=fiscal&filterValue="+ fiscalYear, function(data, status) {
            var json = JSON.parse(data);

            var series = [];
            $.each(json["totals"], function(key, value) {
                var map = {};
                map["name"] = key;
                map["y"] = value;
                map["drilldown"] = key;
                series.push(map);
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

            localStorage.setItem("totals", JSON.stringify(series));
            localStorage.setItem("drilldown", JSON.stringify(drilldown));

            var format = '{point.name}: ${point.y}';
            var pointY = '${point.y}'
            var isFiscal = true;

            if (field != "AMOUNT") {
                format = '{point.name}: {point.y}';
                pointY = '{point.y}';
                isFiscal = false;
            }

            localStorage.setItem("isFiscal", isFiscal);

            var title = $("#drilldownField option:selected").text() + ' for Each Location (' + fiscalYear + ')';
            localStorage.setItem("title", title);

            // Create the chart
            var chart = Highcharts.chart('locations-pie-chart', {
                chart: {
                    type: 'pie'
                },
                title: {
                    text: title
                },
                subtitle: {
                    text: 'Click on a slice to view the location in more detail.'
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
                    name: 'Locations',
                    colorByPoint: true,
                    data: series
                }],
                drilldown: {
                    series: drilldown
                }
            });

            canvg(document.getElementById("locations-pie-chart-canvas"), chart.getSVG());

            var canvas = document.getElementById("locations-pie-chart-canvas");
            var img = canvas.toDataURL("image/png");
            $("#locations-pie-chart-base64").val(img.substring(img.indexOf(',') + 1));

        });
    });

    $("#fiscalYearSelect2").trigger("change");

    $("#fiscalYearSelect3, #stackedBarChartFilter1, #stackedBarChartFilter2").change(function() {
        var year = $("#fiscalYearSelect3").val();
        var location = $("#stackedBarChartFilter1").val();
        var field = $("#stackedBarChartFilter2").val();

        $.get(baseUrl + "charts/fiscalYearTop?top=5&year=" + year + "&location=" + location + "&field="+ field, function(data, status) {
            var json = JSON.parse(data);
            console.log(json);

            var categories = [], values = [];
            $.each(json["totals"], function(i, val) {
                categories.push(i);
                values.push(val);
            });

            var filter;
            if ($("#stackedBarChartFilter2").val() == "AMOUNT") {
                filter = "Amount of Money";
            } else if ($("#stackedBarChartFilter2").val() == "TOTAL_NUMBER_SERVED") {
                filter = "Number of People Served";
            } else {
                filter = "Number of Native Hawaiians Served";
            }

            Highcharts.chart('top-5-orgs-chart', {
                chart: {
                    type: 'bar'
                },
                title: {
                    text: 'Top 5 Organizations in ' + year + ' by ' + filter + ' (' + location + ')'
                },
                xAxis: {
                    categories: categories
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: filter
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
                series: [{
                    name: filter,
                    data: values
                }]
            });

        });
    });

    $("#fiscalYearSelect3").trigger("change");

});