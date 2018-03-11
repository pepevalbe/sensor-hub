// Constants
var GET_SWITCH_STATUS_URL = "/user/get-doorevents-status";
var ACTIVATE_SWITCH_URL = "/user/activate-doorevents";
var DEACTIVATE_SWITCH_URL = "/user/deactivate-doorevents";

$(document).ready(function () {

    // Load initial switch status
    $.ajax({url: GET_SWITCH_STATUS_URL,
        error: function () {
            alert("Error obteniendo estado del switch");
        },
        success: function (data) {
            if (data === "true") {
                $("#doorEventToggleId").bootstrapToggle("on");
            } else if (data === "false") {
                $("#doorEventToggleId").bootstrapToggle("off");
            } else {
                alert("Error obteniendo estado del switch. Por favor, recarga la página");
            }
        }
    });

    // Activate/Deactivate Switch
    $("#doorEventToggleId").change(function () {
        var url;
        if ($("#doorEventToggleId").prop('checked')) {
            url = ACTIVATE_SWITCH_URL;
        } else {
            url = DEACTIVATE_SWITCH_URL;
        }
        $.ajax({url: url,
            error: function () {
                alert("Error cambiando estado. Por favor, recarga la página");
            }
        });
    });
});

