// Constants
var CREATEUSER_URL = "/public/createuser";

function checkPasswords(input) {
    if (input.value !== document.getElementById("passwordId").value) {
        input.setCustomValidity("Las contraseñas no coinciden");
    } else {
        input.setCustomValidity("");
    }
}

function createUser() {
    if ($("#passwordId").val() === $("#passwordConfirmationId").val()) {
        var person = {
            username: $("#usernameId").val(),
            password: $("#passwordId").val(),
            email: $("#emailId").val(),
            firstName: $("#firstNameId").val(),
            lastName: $("#lastNameId").val()
        };
        $.ajax({url: CREATEUSER_URL,
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(person),
            error: function (xhr) {
                if (xhr.status === 409) {
                    $("#errorMessageId").text(xhr.responseText);
                }
                $("#errorModal").modal("show");
            },
            success: function () {
                $("#userCreatedModal").modal("show");
            }
        });
    }
}
