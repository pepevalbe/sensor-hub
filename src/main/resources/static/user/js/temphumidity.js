// Constants
var FIND_TEMP_HUMIDITY_URL = "/user/temphumidity/find";
var chartGlobalVar;

// Add datepicker
$(document).ready(function () {
    $('#input0').val($.datepicker.formatDate('yy-mm-dd', new Date()));
    $('#input0').datepicker({
        dateFormat: 'yy-mm-dd', // Set apropiate format for web service
        maxDate: new Date(), // Prevent selection of date older than today
        firstDay: 1             // Start with Monday
    });
});

// Add button action
$(document).ready(function () {
    $('#button0').click(function () {
        $("thead").empty();
        $("tbody").empty();
        var date = $("#input0").val();
        var tz = new Date().getTimezoneOffset();
        var aux = $("#input1").val().split(':');
        var minutes = (+aux[0]) * 60 + (+aux[1]);
        $.getJSON(FIND_TEMP_HUMIDITY_URL + "?date=" + date + "&tz=" + tz + "&minutes=" + minutes, function (measures) {
            if (measures) {
                var temperaturePoints = [];
                var humidityPoints = [];
                createMinMaxTable(measures);
                addHeaderToTable();
                $.each(measures, function (i, measure) {
                    var row = new Object();
                    var temperaturePoint = new Object();
                    var humidityPoint = new Object();
                    row.id = measure.id;
                    row.time = new Date(measure.timestamp).toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
                    row.temperature = measure.temperature;
                    row.humidity = measure.humidity;
                    temperaturePoint.x = new Date(measure.timestamp);
                    temperaturePoint.y = row.temperature;
                    humidityPoint.x = new Date(measure.timestamp);
                    humidityPoint.y = row.humidity;
                    temperaturePoints.push(temperaturePoint);
                    humidityPoints.push(humidityPoint);
                    addRowToTable(row);
                });

                var ctx = document.getElementById('tempHumidityChart').getContext('2d');
				if (chartGlobalVar) chartGlobalVar.destroy();
                chartGlobalVar = new Chart(ctx, {
                    // The type of chart we want to create
                    type: 'scatter',

                    // The data for our dataset
                    data: {
                        datasets: [{
                                label: "Temperatura",
                                yAxisID: 'temp',
                                borderColor: 'rgb(255,140,0)',
                                data: temperaturePoints
                            },
                            {
                                label: "Humedad",
                                yAxisID: 'hum',
                                borderColor: 'rgb(0,0,139)',
                                data: humidityPoints
                            }]
                    },

                    // Configuration options go here
                    options: {
                        scales: {
                            xAxes: [{
                                    type: 'time',
                                    time: {
                                        displayFormats: {
                                            'millisecond': 'HH:mm',
                                            'second': 'HH:mm',
                                            'minute': 'HH:mm',
                                            'hour': 'HH:mm',
                                            'day': 'HH:mm',
                                            'week': 'HH:mm',
                                            'month': 'HH:mm',
                                            'quarter': 'HH:mm',
                                            'year': 'HH:mm'
                                        }
                                    },
                                    position: 'bottom',
                                    distribution: 'linear'
                                }],
                            yAxes: [{
                                    id: 'temp',
                                    scaleLabel: {
                                        display: true,
                                        labelString: 'Temperatura'
                                    },
                                    ticks: {
                                        callback: function (value, index, values) {
                                            return value.toFixed(2) + ' ºC';
                                        }
                                    }
                                },
                                {
                                    id: 'hum',
                                    position: 'right',
                                    scaleLabel: {
                                        display: true,
                                        labelString: 'Humedad'
                                    },
                                    ticks: {
                                        callback: function (value, index, values) {
                                            return value.toFixed(2) + ' %';
                                        }
                                    }
                                }]
                        },
                        tooltips: {
                                enabled: true,
                                mode: 'index',
                                position: 'nearest',
                                callbacks: {
									title: function (tooltipItems, data) {
                                        var hour = tooltipItems[0].xLabel.getHours();
                                        var minutes = tooltipItems[0].xLabel.getMinutes();
                                        return 'Hora: ' + hour + ':' + minutes;
									},
                                    label: function (tooltipItems, data) {
										if (tooltipItems.datasetIndex  === 0) {
											return 'Temperatura: ' + tooltipItems.yLabel + ' ºC';
										} else if (tooltipItems.datasetIndex  === 1) {
											return 'Humedad: ' + tooltipItems.yLabel + ' %';
										}
                                    }
                                }
                        }
                    }
                });

            } else {
                $('#noDataModal').modal();
            }
        });
    });
});

function addRowToTable(row) {
    var $tr = $('<tr>');
    $tr.attr("id", row.id);
    $tr.append($('<td>').text(row.time));
    $tr.append($('<td>').text(row.temperature.toFixed(2)));
    $tr.append($('<td>').text(row.humidity.toFixed(2)));
    $("#tableTempHumidity tbody").append($tr);
}

function addHeaderToTable() {
    var $tr = $('<tr>');
    $tr.append($('<th>').text('Hora (hh:mm)'));
    $tr.append($('<th>').text('Temperatura (ºC)'));
    $tr.append($('<th>').text('Humedad (%)'));
    $("#tableTempHumidity thead").append($tr);
}

// Create table with maximum, minimum and last tempereature registry
function createMinMaxTable(measures) {
    var minmax = getMinAndMax(measures);

    var $tr = $('<tr>');
    $tr.append($('<th>').text($("#input0").val()));
    $tr.append($('<th>').text('Temperatura (ºC)'));
    $tr.append($('<th>').text('Hora (hh:mm)'));
    $("#tableMinMax thead").append($tr);

    $tr = $('<tr>');
    $tr.append($('<td>').attr("style", "font-weight:bold").text("Máxima"));
    $tr.append($('<td>').text(minmax.tempMaxRow.temperature));
    $tr.append($('<td>').text(new Date(minmax.tempMaxRow.timestamp).toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'})));
    $("#tableMinMax tbody").append($tr);

    $tr = $('<tr>');
    $tr.append($('<td>').attr("style", "font-weight:bold").text("Mínima"));
    $tr.append($('<td>').text(minmax.tempMinRow.temperature));
    $tr.append($('<td>').text(new Date(minmax.tempMinRow.timestamp).toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'})));
    $("#tableMinMax tbody").append($tr);

    $tr = $('<tr>');
    $tr.append($('<td>').attr("style", "font-weight:bold").text("Última"));
    $tr.append($('<td>').text(measures[measures.length - 1].temperature));
    $tr.append($('<td>').text(new Date(measures[measures.length - 1].timestamp).toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'})));
    $("#tableMinMax tbody").append($tr);
}

// Get Maximum and Minimum values for temperature and humidity
function getMinAndMax(measures) {
    var returnValue = new Object();
    returnValue.tempMaxRow = measures[0];
    returnValue.tempMinRow = measures[0];
    returnValue.humMaxRow = measures[0];
    returnValue.humMinRow = measures[0];

    measures.forEach(function (measure) {
        if (measure.temperature > returnValue.tempMaxRow.temperature) {
            returnValue.tempMaxRow = measure;
        }
        if (measure.temperature < returnValue.tempMinRow.temperature) {
            returnValue.tempMinRow = measure;
        }
        if (measure.humidity > returnValue.humMaxRow.humidity) {
            returnValue.humMaxRow = measure;
        }
        if (measure.humidity < returnValue.humMinRow.humidity) {
            returnValue.humMinRow = measure;
        }
    });
    return returnValue;
}