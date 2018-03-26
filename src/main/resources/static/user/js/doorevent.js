// Constants
var FIND_DOOR_EVENT_URL = "/user/doorevent/find?date=";

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
        $.getJSON(FIND_DOOR_EVENT_URL + date, function (events) {
            if (events) {
                addHeaderToTable();
                $.each(events, function (i, event) {
                    var row = new Object();
                    row.id = event.id;
                    row.time = new Date(event.timestamp).toLocaleTimeString();
                    if (i < events.length - 1) {
                        row.timeToNext = new Date(events[i + 1].timestamp - events[i].timestamp).getMinutes();
                    } else {
                        row.timeToNext = "-";
                    }
                    addRowToTable(row);
                });
                $("html, body").animate({scrollTop: $(document).height()}, 1000);
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
    $tr.append($('<td>').text(row.timeToNext));
    $("tbody").append($tr);
}

function addHeaderToTable() {
    var $tr = $('<tr>');
    $tr.append($('<th>').text('Hora del Acceso (hh:mm:ss)'));
    $tr.append($('<th>').text('Tiempo hasta siguiente acceso (min)'));
    $("thead").append($tr);
}
