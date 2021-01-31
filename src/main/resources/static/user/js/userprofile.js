// Constants
var GET_USER_PROFILE_URL = "/public/userprofile";
var REGENERATE_TOKEN_URL = "/user/regeneratepersonaltoken";

$(document).ready(function () {

    $.ajax({url: GET_USER_PROFILE_URL,
        error: function () {
            alert("Error obteniedo perfil de usuario");
        },
        success: function (data) {
            $("#userProfileUsernameId").text(data.username);
            $("#userProfileEmailId").text(data.email);
            $("#userProfileFirstNameId").text(data.firstName);
            $("#userProfileLastNameId").text(data.lastName);
            $("#userProfileTokenId").text(data.token);
        }
    });

    $("#regenerateTokenButtonId").click(function () {
        $.ajax({url: REGENERATE_TOKEN_URL,
            method: "POST",
            error: function () {
                alert("Error regenerando token");
                location.reload();
            },
            success: function () {
                alert("Token regenerado correctamente");
                location.reload();
            }
        });
    });
});
