// Constants
var GET_CONFIGVARIABLES_URL = "/configVariables";
var GET_USERS_URL = "/persons";
var GET_TEMPORARYTOKENS_URL = "/temporaryTokens";
var GET_TEMPHUMIDITIES_URL = "/tempHumidities";
var GET_DOOREVENTS_URL = "/doorEvents";
var GET_SENSORREADINGS_URL = "/sensorReadings";

var configTable;
var usersTable;
var tokensTable;
var tempHumiditiesTable;
var doorEventsTable;
var sensorReadingsTable;

$(document).ready(function () {

    $("#configNav").click(function () {     // Create table for confi variables
        if (!configTable) {
            configTable = $("#configTableId").DataTable({
                searching: false,
                ajax: {
                    "url": GET_CONFIGVARIABLES_URL,
                    "dataSrc": "_embedded.configVariables"
                },
                columns: [
                    {data: function (row) {
                            return row._links.self.href.split('/').pop();
                        }},
                    {data: 'varKey'},
                    {data: 'varValue'}
                ]
            });
            addCustomEvents(configTable);
        }
    });

    $("#usersNav").click(function () {     // Create table for users
        if (!usersTable) {
            usersTable = $("#usersTableId").DataTable({
                serverSide: true,
                searching: false,
                ajax: function (data, callback) {
                    var columns = ["id", "activated", "username", "email", "firstName", "lastName", "creationTimestamp"];
                    datatablesToSpringRest(data, GET_USERS_URL, columns, callback);
                },
                columns: [
                    {data: function (row) {
                            return row._links.self.href.split('/').pop();
                        }},
                    {data: 'activated'},
                    {data: 'username'},
                    {data: 'email'},
                    {data: 'firstName'},
                    {data: 'lastName'},
                    {data: function (row) {
                            return row.creationTimestamp.split("T")[0];
                        }},
                    {data: function (row) {
                            var aux = "<a class=\"removeRow\" id=\"{id}\" href=\"#\">Borrar</a>";
                            return aux.replace("{id}", row._links.self.href);
                        }}
                ]
            });
            addCustomEvents(usersTable);
        }
    });

    $("#tokensNav").click(function () { // Create temporary tokens table
        if (!tokensTable) {
            tokensTable = $("#tokensTableId").DataTable({
                searching: false,
                ajax: {
                    "url": GET_TEMPORARYTOKENS_URL,
                    "dataSrc": "_embedded.temporaryTokens"
                },
                columns: [
                    {data: function (row) {
                            return row._links.self.href.split('/').pop();
                        }},
                    {data: function (row) {
                            return row._links.person.href;
                        }},
                    {data: function (row) {
                            return row.expirationTimestamp.split("+")[0];
                        }},
                    {data: 'token'},
                    {data: function (row) {
                            var aux = "<a class=\"removeRow\" id=\"{id}\" href=\"#\">Borrar</a>";
                            return aux.replace("{id}", row._links.self.href);
                        }}
                ]
            });
            addCustomEvents(tokensTable);
        }
    });

    $("#tempHumiditiesNav").click(function () { // Create temperature table
        if (!tempHumiditiesTable) {
            tempHumiditiesTable = $("#tempHumiditiesTableId").DataTable({
                lengthMenu: [20, 50, 100, 200],
                serverSide: true,
                ajax: function (data, callback) {
                    var columns = ["id", "user", "timestamp", "temperature", "humidity"];
                    datatablesToSpringRest(data, GET_TEMPHUMIDITIES_URL, columns, callback);
                },
                columns: [
                    {data: function (row) {
                            return row._links.self.href.split('/').pop();
                        }},
                    {data: function (row) {
                            return row._links.owner.href;
                        }},
                    {data: function (row) {
                            return row.timestamp.split("+")[0];
                        }},
                    {data: 'temperature'},
                    {data: 'humidity'},
                    {data: function (row) {
                            var aux = "<a class=\"removeRow\" id=\"{id}\" href=\"#\">Borrar</a>";
                            return aux.replace("{id}", row._links.self.href);
                        }}
                ]
            });
            addCustomEvents(tempHumiditiesTable);
        }
    });

    $("#doorEventsNav").click(function () { // Create door events table
        if (!doorEventsTable) {
            doorEventsTable = $("#doorEventsTableId").DataTable({
                lengthMenu: [20, 50, 100, 200],
                serverSide: true,
                ajax: function (data, callback) {
                    var columns = ["id", "user", "timestamp"];
                    datatablesToSpringRest(data, GET_DOOREVENTS_URL, columns, callback);
                },
                columns: [
                    {data: function (row) {
                            return row._links.self.href.split('/').pop();
                        }},
                    {data: function (row) {
                            return row._links.owner.href;
                        }},
                    {data: function (row) {
                            return row.timestamp.split("+")[0];
                        }},
                    {data: function (row) {
                            var aux = "<a class=\"removeRow\" id=\"{id}\" href=\"#\">Borrar</a>";
                            return aux.replace("{id}", row._links.self.href);
                        }}
                ]
            });
            addCustomEvents(doorEventsTable);
        }
    });

    $("#sensorReadingsNav").click(function () { // Create generic sensor table
        if (!sensorReadingsTable) {
            sensorReadingsTable = $("#sensorReadingsTableId").DataTable({
                lengthMenu: [20, 50, 100, 200],
                search: false,
                serverSide: true,
                ajax: function (data, callback) {
                    var columns = ["id", "user", "timestamp", "value1", "value2", "value3"];
                    datatablesToSpringRest(data, GET_SENSORREADINGS_URL, columns, callback);
                },
                columns: [
                    {data: function (row) {
                            return row._links.self.href.split('/').pop();
                        }},
                    {data: function (row) {
                            return row._links.owner.href;
                        }},
                    {data: function (row) {
                            return row.timestamp.split("+")[0];
                        }},
                    {data: 'value1'},
                    {data: 'value2'},
                    {data: 'value3'},
                    {data: function (row) {
                            var aux = "<a class=\"removeRow\" id=\"{id}\" href=\"#\">Borrar</a>";
                            return aux.replace("{id}", row._links.self.href);
                        }}
                ]
            });
            addCustomEvents(sensorReadingsTable);
        }
    });
});

// Calculate paging for spring rest, make ajax call to server, prepare response for datatables and call callback function
function datatablesToSpringRest(data, baseUrl, columns, fnCallback) {
    var page = (data.start / data.length).toString();
    var size = data.length.toString();
    var sort = columns[data.order[0].column] + "," + data.order[0].dir;
    if (data.search.value !== "") {
        var url = baseUrl + "/search/findByUsername?username=" + data.search.value + "&page=" + page + "&size=" + size + "&sort=" + sort;
    } else {
        var url = baseUrl + "?page=" + page + "&size=" + size + "&sort=" + sort;
    }
    console.log("Get data url:" + url);
    $.ajax({
        url: url,
        success: function (responseData) {
            var dataToReturn = new Object();
            dataToReturn.draw = data.draw;
            dataToReturn.length = responseData.page.size;
            dataToReturn.recordsTotal = responseData.page.totalElements;
            dataToReturn.recordsFiltered = responseData.page.totalElements;
            dataToReturn.data = responseData._embedded[baseUrl.split("/")[1]];
            fnCallback(dataToReturn);
        }
    });
}

function addCustomEvents(table) {
    table.on('draw', function () {
        // Get usernames in second column in case they start with http
        table.column(1).nodes().each(function (i) {
            if ($(i).text().startsWith("http://")) {
                $.ajax({
                    url: $(i).text(),
                    success: function (data) {
                        $(i).text(data.username);
                        table.columns.adjust();
                    }
                });
            }
        });
        // Add remove row event to delete link
        $("a.removeRow").click(function () {
            var $td = $(this);
            $("#confirmRemoveRow").click(function () {
                $('#removeRowModal').modal('hide');
                console.log("borrando... " + $td.attr("id"));
                $.ajax({
                    url: $td.attr("id"),
                    method: "DELETE",
                    success: function () {
                        console.log("Borrado");
                        table.row($td.parents('tr')).remove();
                        table.page(table.page.info().page).draw(false);
                    }
                });
            });
            $('#removeRowModal').modal('show');
        });
    });
    // Search event only when pressing enter
    $("input").unbind().keyup(function (e) {
        if (e.keyCode === 13) {
            table.search($(this).val()).draw();
        }
    });
}
