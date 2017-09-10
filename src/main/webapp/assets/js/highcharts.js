function getFilters() {
    var filters = {};
    $(".filter").each(function() {
        if ($(this).is(':checkbox')) {
            if ($(this).is(':checked')) {
                var array = filters["filters"];
                if (array == null) {
                    filters["filters"] = [];
                }
                filters["filters"].push($(this).data("key"));
                var array = filters["filterValues"];
                if (array == null) {
                    filters["filterValues"] = [];
                }
                filters["filterValues"].push($(this).val());
            }
        }
        else if ($(this).val() != null && $(this).val() != '') {
            var array = filters["filters"];
            if (array == null) {
                filters["filters"] = [];
            }
            filters["filters"].push($(this).data("key"));
            var array = filters["filterValues"];
            if (array == null) {
                filters["filterValues"] = [];
            }
            filters["filterValues"].push($(this).val());
        }
    });
    return filters;
}

$(document).ready(function() {

    Highcharts.setOptions({
        lang: {
            thousandsSep: ','
        }
    });

    var baseUrl = localStorage.getItem('request');

    $("input[data-key='priority']").change(function() {
        var key = $(this).data("key");
        var title = $(this).data("chart-title");

        var map = {};
        map["aggregateField"] = "AMOUNT";
        map["filters"] = getFilters();
        map["groupBy"] = key;

        $.ajax({
            type: 'POST',
            url: localStorage.getItem("appUrl") + "charts/totals",
            data: JSON.stringify(map),
            contentType: 'application/json',
            headers: {
                'X-CSRF-TOKEN': localStorage.getItem("csrf.token")
            },
            success: function(response) {
                var json = JSON.parse(response);
                var series = [];
                $.each(json["totals"], function(key, value) {
                    var map = {};
                    map["name"] = key;
                    map["colorByPoint"] = true;
                    map["data"] = value;
                    series.push(map);
                });
                console.log(series);
                var format = '{point.name}: ${point.y}';
                var pointY = '${point.y}'
                var isFiscal = true;
                // Create the chart
                var chart = Highcharts.chart(key + '-pie-chart', {
                    chart: {
                        type: 'pie'
                    },
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
                    series: series
                });

                canvg(document.getElementById(key + "-pie-chart-canvas"), chart.getSVG());

                var canvas = document.getElementById(key + "-pie-chart-canvas");
                var img = canvas.toDataURL("image/png");
                $("#" + key + "-pie-chart-base64").val(img.substring(img.indexOf(',') + 1));
            }
        });

        /*$.get(baseUrl + "charts/totals?aggregateField=AMOUNT&filter=" + field, function(data, status) {
            var json = JSON.parse(data);



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

        });*/
    });

});