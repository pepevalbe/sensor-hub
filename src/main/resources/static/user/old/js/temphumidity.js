// Constants
var FIND_TEMP_HUMIDITY_URL = "/user/temphumidity/find?date=";

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
        $.getJSON(FIND_TEMP_HUMIDITY_URL + date + "&tz=" + new Date().getTimezoneOffset(), function (measures) {
            if (measures) {
                addHeaderToTable();
                $.each(measures, function (i, measure) {
                    var row = new Object();
                    row.id = measure.id;
                    row.time = new Date(measure.timestamp).toLocaleTimeString();
                    row.temperature = measure.temperature;
                    row.humidity = measure.humidity;
                    addRowToTable(row);
                });
            } else {
                alert("No hay información para el " + date);
            }
        });
    });
});

function addRowToTable(row) {
    var $tr = $('<tr>');
    $tr.attr("id", row.id);
    $tr.append($('<td>').text(row.time));
    $tr.append($('<td>').text(row.temperature));
    $tr.append($('<td>').text(row.humidity));
    $("tbody").append($tr);
}

function addHeaderToTable() {
    var $tr = $('<tr>');
    $tr.append($('<th>').text('Hora'));
    $tr.append($('<th>').text('Temperatura (ºC)'));
    $tr.append($('<th>').text('Humedad (%)'));
    $("thead").append($tr);
}