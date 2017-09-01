var fiscalYearDataMap =   { "2013": [], "2014": [], "2015": [], "2016": [] };
var fiscalYearLabelsMap = { "2013": [], "2014": [], "2015": [], "2016": [] };

var top5DataMap =    { "ORGANIZATION_AMOUNT": [], "ORGANIZATION_TOTAL_NUMBER_SERVED": [], "ORGANIZATION_NUMBER_NATIVE_HAWAIIANS_SERVED": [],
                       "PROJECT_AMOUNT": [], "PROJECT_TOTAL_NUMBER_SERVED": [], "PROJECT_NUMBER_NATIVE_HAWAIIANS_SERVED": [] };
var top5LabelsMap =  { "ORGANIZATION_AMOUNT": [], "ORGANIZATION_TOTAL_NUMBER_SERVED": [], "ORGANIZATION_NUMBER_NATIVE_HAWAIIANS_SERVED": [],
                       "PROJECT_AMOUNT": [], "PROJECT_TOTAL_NUMBER_SERVED": [], "PROJECT_NUMBER_NATIVE_HAWAIIANS_SERVED": [] };

var top10DataMap =   { "ORGANIZATION_AMOUNT": [], "ORGANIZATION_TOTAL_NUMBER_SERVED": [], "ORGANIZATION_NUMBER_NATIVE_HAWAIIANS_SERVED": [],
                       "PROJECT_AMOUNT": [], "PROJECT_TOTAL_NUMBER_SERVED": [], "PROJECT_NUMBER_NATIVE_HAWAIIANS_SERVED": [] };
var top10LabelsMap = { "ORGANIZATION_AMOUNT": [], "ORGANIZATION_TOTAL_NUMBER_SERVED": [], "ORGANIZATION_NUMBER_NATIVE_HAWAIIANS_SERVED": [],
                       "PROJECT_AMOUNT": [], "PROJECT_TOTAL_NUMBER_SERVED": [], "PROJECT_NUMBER_NATIVE_HAWAIIANS_SERVED": [] };

var top25DataMap =   { "ORGANIZATION_AMOUNT": [], "ORGANIZATION_TOTAL_NUMBER_SERVED": [], "ORGANIZATION_NUMBER_NATIVE_HAWAIIANS_SERVED": [],
                       "PROJECT_AMOUNT": [], "PROJECT_TOTAL_NUMBER_SERVED": [], "PROJECT_NUMBER_NATIVE_HAWAIIANS_SERVED": [] };
var top10LabelsMap = { "ORGANIZATION_AMOUNT": [], "ORGANIZATION_TOTAL_NUMBER_SERVED": [], "ORGANIZATION_NUMBER_NATIVE_HAWAIIANS_SERVED": [],
                       "PROJECT_AMOUNT": [], "PROJECT_TOTAL_NUMBER_SERVED": [], "PROJECT_NUMBER_NATIVE_HAWAIIANS_SERVED": [] };

var chartColors = [
    'rgb(255, 99, 132)', 'rgb(255, 159, 64)', 'rgb(255, 205, 86)', 'rgb(75, 192, 192)', 'rgb(54, 162, 235)'
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
        fiscalYearDataMap[year] = dataset;
        $.each(json, function(index, value) {
            if (index < 5) {
                labels.push(json[index].organization);
            }
        });
        fiscalYearLabelsMap[year] = labels;
    } else {
        dataset = fiscalYearDataMap[year];
        labels = fiscalYearLabelsMap[year];
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
    $("#fiscalYearDiv").html("<canvas id='fiscalYearChart' class='chart' />");
    new Chart(document.getElementById("fiscalYearChart").getContext('2d'), config);
}

function createTopPieChart(data, cachedData, n, field, criterion) {
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
        if (n == "5") {
            top5DataMap[field + "_" + criterion] = dataset;
        } else if (n == "10") {
        	top10DataMap[field + "_" + criterion] = dataset;
        } else {
        	top25DataMap[field + "_" + criterion] = dataset;
        }
        $.each(json, function(index, value) {
            if (index < 5) {
                labels.push(json[index].organization);
            }
        });
        if (n == "5") {
            top5LabelsMap[field + "_" + criterion] = labels;
        } else if (n == "10") {
        	top10LabelsMap[field + "_" + criterion] = labels;
        } else {
        	top25LabelsMap[field + "_" + criterion] = labels;
        }
    } else {
        dataset = cachedData;
        labels = cachedData;
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
    $("#fiscalYearSelect").change(function(event) {
        event.preventDefault();
        var url = localStorage.getItem('request') + "charts/fiscalYear";
        var year = $('#fiscalYearSelect :selected').val();
        if (fiscalYearDataMap[year].length > 0) {
            createFiscalYearPieChart(null, year);
        } else {
            $.get(url + "?year=" + $('#fiscalYearSelect :selected').val(), function(data, status) {
                createFiscalYearPieChart(data, year);
            });
        }
    });
    $("#fiscalYearSelect").trigger("change");

    $("#top1_1, #top1_2, #top1_3").change(function(event) {
        event.preventDefault();
        var url = localStorage.getItem('request') + "charts/top";
        var n = $('#top1_1 :selected').val();
        var field = $('#top1_2 :selected').val();
        var criterion = $('#top1_3 :selected').val();
        url = url + "?top=" + n;
        url = url + "&field=" + field;
        url = url + "&criterion=" + criterion;
        var key = field + "_" + criterion;
        if (n == "5") {
            if (top5DataMap[key].length > 0) {
                createTopPieChart(null, top5DataMap[key], n, field, criterion);
                return;
            }
        } else if (n == "10") {
            if (top10DataMap[key].length > 0) {
                createTopPieChart(null, top10DataMap[key], n, field, criterion);
                return;
            }
        } else {
            if (top25DataMap[key].length > 0) {
                createTopPieChart(null, top25DataMap[key], n, field, criterion);
                return;
            }
        }
        $.get(url, function(data, status) {
        	createTopPieChart(data, null, n, field, criterion);
        });
    });
    $("#top1_1").trigger("change");
});