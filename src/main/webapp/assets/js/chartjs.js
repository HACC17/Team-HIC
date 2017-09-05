var fiscal_year_data_map =   { "2013": [], "2014": [], "2015": [], "2016": [] };
var fiscal_year_labels_map = { "2013": [], "2014": [], "2015": [], "2016": [] };

var top_5_data_map =   { "ORGANIZATION_AMOUNT": [], "ORGANIZATION_TOTAL_NUMBER_SERVED": [], "ORGANIZATION_NUMBER_NATIVE_HAWAIIANS_SERVED": [],
                         "PROJECT_AMOUNT": [], "PROJECT_TOTAL_NUMBER_SERVED": [], "PROJECT_NUMBER_NATIVE_HAWAIIANS_SERVED": [] };
var top_5_labels_map = { "ORGANIZATION_AMOUNT": [], "ORGANIZATION_TOTAL_NUMBER_SERVED": [], "ORGANIZATION_NUMBER_NATIVE_HAWAIIANS_SERVED": [],
                         "PROJECT_AMOUNT": [], "PROJECT_TOTAL_NUMBER_SERVED": [], "PROJECT_NUMBER_NATIVE_HAWAIIANS_SERVED": [] };

var top_10_data_map =   { "ORGANIZATION_AMOUNT": [], "ORGANIZATION_TOTAL_NUMBER_SERVED": [], "ORGANIZATION_NUMBER_NATIVE_HAWAIIANS_SERVED": [],
                          "PROJECT_AMOUNT": [], "PROJECT_TOTAL_NUMBER_SERVED": [], "PROJECT_NUMBER_NATIVE_HAWAIIANS_SERVED": [] };
var top_10_labels_map = { "ORGANIZATION_AMOUNT": [], "ORGANIZATION_TOTAL_NUMBER_SERVED": [], "ORGANIZATION_NUMBER_NATIVE_HAWAIIANS_SERVED": [],
                          "PROJECT_AMOUNT": [], "PROJECT_TOTAL_NUMBER_SERVED": [], "PROJECT_NUMBER_NATIVE_HAWAIIANS_SERVED": [] };

var top_25_data_map =   { "ORGANIZATION_AMOUNT": [], "ORGANIZATION_TOTAL_NUMBER_SERVED": [], "ORGANIZATION_NUMBER_NATIVE_HAWAIIANS_SERVED": [],
                          "PROJECT_AMOUNT": [], "PROJECT_TOTAL_NUMBER_SERVED": [], "PROJECT_NUMBER_NATIVE_HAWAIIANS_SERVED": [] };
var top_25_labels_map = { "ORGANIZATION_AMOUNT": [], "ORGANIZATION_TOTAL_NUMBER_SERVED": [], "ORGANIZATION_NUMBER_NATIVE_HAWAIIANS_SERVED": [],
                          "PROJECT_AMOUNT": [], "PROJECT_TOTAL_NUMBER_SERVED": [], "PROJECT_NUMBER_NATIVE_HAWAIIANS_SERVED": [] };

var chartColors = [
    'rgb(255, 99, 132)', 'rgb(255, 159, 64)', 'rgb(255, 205, 86)', 'rgb(75, 192, 192)', 'rgb(54, 162, 235)',
    'rgb(142, 94, 162)', 'rgb(58, 140, 192)', 'rgb(196, 88, 80)', 'rgb(232, 195, 185)', 'rgb(60, 186, 159)',
    'rgb(166, 206, 227)', 'rgb(31, 120, 180)', 'rgb(178, 223, 138)', 'rgb(51, 160, 44)', 'rgb(251, 154, 153)',
    'rgb(227, 26, 28)', 'rgb(253, 191, 111)', 'rgb(255, 127, 0)', 'rgb(202, 178, 214)', 'rgb(106, 61, 154)',
    'rgb(255, 255, 153)', 'rgb(177, 89, 40)', 'rgb(161, 195, 211)', 'rgb(253, 189, 103)', 'rgb(80, 198, 197)'
];

var fiscalChart, topChart;

var fiscalChartOptions = {
    responsive: true,
    tooltips: {
        enabled: true,
        mode: 'single',
        callbacks: {
            label: function(tooltipItems, data) {
                var idx = tooltipItems.index;
                return data.labels[idx] + ': ' + accounting.formatMoney(data.datasets[0].data[idx]);
            }
        }
    }
};

var topChartOptions = {
    responsive: true,
    tooltips: {
        enabled: true,
        mode: 'single',
        callbacks: {
            label: function(tooltipItems, data) {
                var idx = tooltipItems.index;
                return data.labels[idx] + ': ' + accounting.formatNumber(data.datasets[0].data[idx]) + ' people';
            }
        }
    }
};

function createFiscalYearPieChart(data, year) {
    var dataset = [];
    var labels = [];

    if (data) {
        var json = JSON.parse(data);
        if (json.length == 0) {
            $(".no-fiscal-year-data").show();
            $(".fiscal-year-chart").hide();
            return;
        } else {
            $(".no-fiscal-year-data").hide();
            $(".fiscal-year-chart").show();
        }
        $.each(json, function(index, value) {
            if (index < 5) {
                dataset.push(json[index].amount);
            }
        });
        fiscal_year_data_map[year] = dataset;
        $.each(json, function(index, value) {
            if (index < 5) {
                labels.push(json[index].organization);
            }
        });
        fiscal_year_labels_map[year] = labels;
    } else {
        dataset = fiscal_year_data_map[year];
        labels = fiscal_year_labels_map[year];
    }

    localStorage.setItem("fiscal-year-dataset", dataset);
    localStorage.setItem("fiscal-year-labels", labels.join(";"));

    fiscalChartOptions.animation = {
        onComplete: function() {
            $("#fiscal-chart-base64").val(fiscalChart.toBase64Image());
        }
    };
    var config = {
        type: 'pie',
        data: {
            datasets: [{
                data: dataset,
                backgroundColor: chartColors.slice(0, dataset.length),
                label: 'Fiscal Year ' + $('#fiscalYearSelect :selected').val()
            }],
            labels: labels
        },
        options: fiscalChartOptions
    };

    $("#fiscalYearChart").remove();
    $("#fiscalYearDiv").html("<label>Total Amount of Money for Fiscal Year " + $("#fiscalYearSelect").val() + "<canvas id='fiscalYearChart' class='fiscal-year-chart' /></label>");
    fiscalChart = new Chart(document.getElementById("fiscalYearChart").getContext('2d'), config);
}

function createTopPieChart(data, cachedData, cachedLabels, n, field, criterion) {
    var dataset = [];
    var labels = [];

    if (data) {
        var json = JSON.parse(data);
        if (json.length == 0) {
            $(".no-top-data").show();
            $(".top-chart").hide();
            return;
        } else {
            $(".no-top-data").hide();
            $(".top-chart").show();
        }

        $.each(json, function(index, value) {
            if (index < chartColors.length) {
                dataset.push(json[index].value);
            }
        });
        var key = field + "_" + criterion;
        if (n == "5") {
            top_5_data_map[key] = dataset;
        } else if (n == "10") {
            top_10_data_map[key] = dataset;
        } else {
            top_25_data_map[key] = dataset;
        }

        $.each(json, function(index, value) {
            if (index < chartColors.length) {
                var lbl = json[index].key;
                labels.push(lbl.length > 50 ? lbl.substring(0, 50).trim() + "..." : lbl);
            }
        });
        if (n == "5") {
            top_5_labels_map[key] = labels;
        } else if (n == "10") {
            top_10_labels_map[key] = labels;
        } else {
            top_25_labels_map[key] = labels;
        }
    } else {
        dataset = cachedData;
        labels = cachedLabels;
    }

    localStorage.setItem("top-dataset", dataset);
    localStorage.setItem("top-labels", labels.join(";"));

    var options = $('#top1_3 :selected').val() === "AMOUNT" ? fiscalChartOptions : topChartOptions;
    options.animation = {
        onComplete: function() {
            $("#top-chart-base64").val(topChart.toBase64Image());
        }
    };
    var config = {
        type: 'pie',
        data: {
            datasets: [{
                data: dataset,
                backgroundColor: chartColors.slice(0, dataset.length),
                label: 'Top ' + n + ' ' + field + ' by ' + criterion
            }],
            labels: labels
        },
        options: options
    };

    $("#topChart").remove();
    $("#topDiv").html("<label>" + $("#top1_1 option:selected").text() + " " + $("#top1_2 option:selected").text() + " by " + $("#top1_3 option:selected").text() + "<canvas id='topChart' class='top-chart' /></label>");
    topChart = new Chart(document.getElementById("topChart").getContext('2d'), config);
}

function chartOrganizationDataOverTime() {
    var url = localStorage.getItem('request') + "charts/time?org=";
    url = url + $("#org1_2").val() + "&criterion=" + $("#org1_1").val();

    $.get(url, function(data, status) {
        var json = JSON.parse(data);
        var dataset = [];
        $.each(json, function(index, value) {
            if (index < chartColors.length) {
                dataset.push(json[index].value);
            }
        });
        var labels = [];
        $.each(json, function(index, value) {
            if (index < chartColors.length) {
                labels.push(json[index].year);
            }
        });
        var yLabel;
        if ($("#org1_1").val() == "AMOUNT") {
            yLabel = "Amount of Money";
        } else if ($("#org1_1").val() == "TOTAL_NUMBER_SERVED") {
            yLabel = "Total Number of People Served";
        } else {
            yLabel = "Number of Native Hawaiians Served";
        }
        var min = 0;
        var max = 5;
        var randomIndex = Math.floor(Math.random() * (max - min + 1)) + min;

        localStorage.setItem("over-time-dataset", dataset);
        localStorage.setItem("over-time-labels", labels);

        var txt = " for ";
        if ($("#org1_1").val() != "AMOUNT") {
            txt = " by ";
        }

        $("#overTimeChart").remove();
        $("#overTimeDiv").html("<label for='overTimeChart'>" + $("#org1_1 option:selected").text() + txt + $("#org1_2 option:selected").text() + " Over Time<canvas id='overTimeChart' class='over-time-chart' /></label>");
        var overTimeChart = new Chart(document.getElementById("overTimeChart").getContext('2d'), {
            type: 'line',
            data: {
                datasets: [{
                    data: dataset,
                    backgroundColor: chartColors[randomIndex],
                    borderColor: chartColors[randomIndex],
                    fill: false,
                    label: $("#org1_2").val()
                }],
                labels: labels
            },
            options: {
                animation: {
                    onComplete: function() {
                        $("#over-time-chart-base64").val(overTimeChart.toBase64Image());
                    }
                },
                responsive: true,
                title: {
                    display: false
                },
                tooltips: {
                    mode: 'index',
                    intersect: false,
                    callbacks: {
                        label: function(tooltipItems, data) {
                            var idx = tooltipItems.index;
                            var isFiscal = $("#org1_1").val() == "AMOUNT";
                            var d = data.datasets[0].data[idx];
                            if (!isFiscal) {
                                if (d == 1) {
                                    return data.labels[idx] + ': 1 person';
                                } else if (d == 0) {
                                    return data.labels[idx] + ': None';
                                }
                            }
                            return data.labels[idx] + ': ' + (isFiscal ? accounting.formatMoney(d) : accounting.formatNumber(d) + " people");
                        }
                    }
                },
                hover: {
                    mode: 'nearest',
                    intersect: true
                },
                scales: {
                    xAxes: [{
                        display: true,
                        scaleLabel: {
                            display: true,
                            labelString: 'Year'
                        }
                    }],
                    yAxes: [{
                        display: true,
                        ticks: {
                            callback: function(label, index, labels) {
                                return $("#org1_1").val() == "AMOUNT" ? accounting.formatMoney(label) : accounting.formatNumber(label);
                            }
                        },
                        scaleLabel: {
                            display: true,
                            labelString: yLabel
                        }
                    }]
                }
            }
        });
    });
}

$(document).ready(function() {
    var baseUrl = localStorage.getItem('request');

    $("#fiscalYearSelect").change(function(event) {
        event.preventDefault();
        var year = $('#fiscalYearSelect :selected').val();
        if (fiscal_year_data_map[year].length > 0) {
            createFiscalYearPieChart(null, year);
        } else {
            $.get(baseUrl + "charts/fiscalYear?year=" + $('#fiscalYearSelect :selected').val(), function(data, status) {
                createFiscalYearPieChart(data, year);
            });
        }
    });

    $("#top1_1, #top1_2, #top1_3").change(function(event) {
        event.preventDefault();
        var n = $('#top1_1 :selected').val();
        var field1 = $('#top1_2 :selected').val();
        var field2 = $('#top1_3 :selected').val();
        var url = baseUrl + "charts/top?top=" + n;
        url = url + "&field1=" + field1;
        url = url + "&field2=" + field2;
        var key = field + "_" + criterion;
        if (n == "5") {
            if (top_5_data_map[key].length > 0) {
                createTopPieChart(null, top_5_data_map[key], top_5_labels_map[key], n, field, criterion);
                return;
            }
        } else if (n == "10") {
            if (top_10_data_map[key].length > 0) {
                createTopPieChart(null, top_10_data_map[key], top_10_labels_map[key], n, field, criterion);
                return;
            }
        } else {
            if (top_25_data_map[key].length > 0) {
                createTopPieChart(null, top_25_data_map[key], top_25_labels_map[key], n, field, criterion);
                return;
            }
        }
        $.get(url, function(data, status) {
            createTopPieChart(data, null, n, field, criterion);
        });
    });

    $("#org1_1").change(function(event) {
        event.preventDefault();
        if ($(this).val() == "AMOUNT") {
            $("#org1_2-label").html("for");
        } else {
            $("#org1_2-label").html("by");
        }
        chartOrganizationDataOverTime();
    });

    $("#org1_2").change(function(event) {
        event.preventDefault();
        chartOrganizationDataOverTime();
    });

    $("#top1_1").trigger("change");
    $("#fiscalYearSelect").trigger("change");
    $("#org1_1").trigger("change");
});
