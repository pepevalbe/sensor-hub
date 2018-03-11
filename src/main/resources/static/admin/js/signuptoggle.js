// Constants
var ADMIN_SIGNUPENABLE_URL = "/admin/signupenable";
var ADMIN_SIGNUPDISABLE_URL = "/admin/signupdisable";
var ADMIN_SIGNUPSTATUS_URL = "/admin/signupstatus";

$(document).ready(function () {

    // Load initial switch status
    $.ajax({url: ADMIN_SIGNUPSTATUS_URL,
        error: function () {
            alert("Error obteniendo estado del switch");
        },
        success: function (data) {
            if (data === "1") {
                $("#signupToggleId").bootstrapToggle("on");
            } else if (data === "0") {
                $("#signupToggleId").bootstrapToggle("off");
            } else {
                alert("Error obteniendo estado del switch. Por favor, recarga la página");
            }
        }
    });

    // Activate/Deactivate Switch
    $("#signupToggleId").change(function () {
        var url;
        if ($("#signupToggleId").prop('checked')) {
            url = ADMIN_SIGNUPENABLE_URL;
        } else {
            url = ADMIN_SIGNUPDISABLE_URL;
        }
        $.ajax({url: url,
            error: function () {
                alert("Error cambiando estado. Por favor, recarga la página");
            }
        });
    });
});

