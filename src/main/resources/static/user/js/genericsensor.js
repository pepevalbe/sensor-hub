// Constants
var FIND_SENSOR_READING_URL = "/user/genericsensor/find?date=";

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
        $.getJSON(FIND_SENSOR_READING_URL + date + "&tz=" + new Date().getTimezoneOffset(), function (measures) {
            if (measures) {
                addHeaderToTable();
                $.each(measures, function (i, measure) {
                    var row = new Object();
                    row.id = measure.id;
                    row.time = new Date(measure.timestamp).toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
                    row.value1 = measure.value1;
                    if (measure.value2 !== null) {
                        row.value2 = measure.value2;
                    }
                    if (measure.value3 !== null) {
                        row.value3 = measure.value3;
                    }
                    addRowToTable(row);
                });
                $("html, body").animate({scrollTop: $(document).height()}, 1000);
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
    $tr.append($('<td>').text(row.value1.toFixed(2)));
    $tr.append($('<td>').text(row.value2.toFixed(2)));
    $tr.append($('<td>').text(row.value3.toFixed(2)));
    $("tbody").append($tr);
}

function addHeaderToTable() {
    var $tr = $('<tr>');
    $tr.append($('<th>').text('Hora (hh:mm)'));
    $tr.append($('<th>').text('Valor 1'));
    $tr.append($('<th>').text('Valor 2'));
    $tr.append($('<th>').text('Valor 3'));
    $("thead").append($tr);
}
