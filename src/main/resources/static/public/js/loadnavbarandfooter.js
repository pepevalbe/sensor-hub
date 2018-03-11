// Constants
var GET_USERNAME_URL = "/public/username";
var NAVBAR_URL = "/public/navbar.html";
var FOOTER_URL = "/public/footer.html";

// Load Navbar, footer and get username for navbar
$(document).ready(function () {
    $("#navbarId").load(NAVBAR_URL, function () {
        $.ajax({url: GET_USERNAME_URL,
            error: function () {
                $("#LoginItemId").attr("hidden", false);
                $("#SignUpItemId").attr("hidden", false);
                $("#UserItemId").attr("hidden", true);
            },
            success: function (data) {
                $("#UserItemTextId").text("Usuario: " + data);
                $("#LoginItemId").attr("hidden", true);
                $("#SignUpItemId").attr("hidden", true);
                $("#UserItemId").attr("hidden", false);
            }
        });
    });
    $("#footerId").load(FOOTER_URL);
});
