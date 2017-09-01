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

var chartOptions = {
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
        options: chartOptions
    };

    $("#fiscalYearChart").remove();
    $("#fiscalYearDiv").html("<canvas id='fiscalYearChart' class='fiscal-year-chart' />");
    new Chart(document.getElementById("fiscalYearChart").getContext('2d'), config);
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
                labels.push(json[index].key);
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
        options: chartOptions
    };

    $("#topChart").remove();
    $("#topDiv").html("<canvas id='topChart' class='top-chart' />");
    new Chart(document.getElementById("topChart").getContext('2d'), config);
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
        var field = $('#top1_2 :selected').val();
        var criterion = $('#top1_3 :selected').val();
        var url = baseUrl + "charts/top?top=" + n;
        url = url + "&field=" + field;
        url = url + "&criterion=" + criterion;
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

    $("#top1_1").trigger("change");
    $("#fiscalYearSelect").trigger("change");
});
