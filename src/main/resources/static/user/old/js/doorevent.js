// Constants
var FIND_DOOR_EVENT_URL = "/user/doorevent/find?date=";
var GET_SWITCH_STATUS_URL = "/user/get-doorevents-status";
var ACTIVATE_SWITCH_URL = "/user/activate-doorevents";
var DEACTIVATE_SWITCH_URL = "/user/deactivate-doorevents";

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
    $tr.append($('<th>').text('Hora del Acceso'));
    $tr.append($('<th>').text('Tiempo hasta siguiente acceso (min)'));
    $("thead").append($tr);
}

// Add siwtch action
$(document).ready(function () {
    $.ajax({url: GET_SWITCH_STATUS_URL,
        type: "GET",
        error: function () {
            alert("Error obteniendo estado");
        },
        success: function (data) {
            if (data === "1") {
                $('#input1').prop("checked", true);
                $('#switchText').text("Activado");
            } else if (data === "0") {
                $('#input1').prop("checked", false);
                $('#switchText').text("Desactivado");
            } else {
                alert("Error obteniendo estado");
            }
        }
    });

    $('#input1').change(function () {
        if (this.checked) {
            $.ajax({url: ACTIVATE_SWITCH_URL,
                type: "GET",
                error: function () {
                    alert("Error Activando");
                    $('#input1').prop("checked", false);
                },
                success: function () {
                    $('#switchText').text("Activado");
                }
            });
        } else {
            $.ajax({url: DEACTIVATE_SWITCH_URL,
                type: "GET",
                error: function () {
                    alert("Error Desactivando");
                    $('#input1').prop("checked", true);
                },
                success: function () {
                    $('#switchText').text("Desactivado");
                }
            });
        }
    });
});
