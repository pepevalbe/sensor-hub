// Constants
var GET_USERPROFILE_URL = "/public/userprofile";
var NAVBAR_URL = "/public/navbar.html";
var FOOTER_URL = "/public/footer.html";

// Load Navbar, footer and get username for navbar
$(document).ready(function () {
    $("#navbarId").load(NAVBAR_URL, function () {
        $.ajax({url: GET_USERPROFILE_URL,
            error: function () {
                $("#LoginItemId").attr("hidden", false);
                $("#SignUpItemId").attr("hidden", false);
                $("#UserItemId").attr("hidden", true);
            },
            success: function (data) {
                $("#UserItemTextId").text("Usuario: " + data.username);
                $("#LoginItemId").attr("hidden", true);
                $("#SignUpItemId").attr("hidden", true);
                $("#UserItemId").attr("hidden", false);
            }
        });
    });
    $("#footerId").load(FOOTER_URL);
});
