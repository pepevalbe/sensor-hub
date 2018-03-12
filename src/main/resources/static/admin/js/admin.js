// Constants
var GET_USERS_URL = "/persons";
var GET_TEMPHUMIDITIES_URL = "/tempHumidities";
var GET_DOOREVENTS_URL = "/doorEvents";
var GET_SENSORREADINGS_URL = "/sensorReadings";

var usersTable;
var tempHumiditiesTable;
var doorEventsTable;
var sensorReadingsTable;

$(document).ready(function () {
    // Create tablae for users
    $("#usersNav").click(function () {
        if (!usersTable) {
            usersTable = $("#usersTableId").DataTable({
                lengthMenu: [20, 50, 100, 200],
                serverSide: true,
                searching: false,
                ajax: function (data, callback) {
                    var columns = ["id", "username", "email", "firstName", "lastName", "creationTimestamp"];
                    datatablesToSpringRest(data, GET_USERS_URL, "persons", columns, callback);
                },
                columns: [
                    {data: function (row) {
                            return row._links.self.href.split('/').pop();
                        }},
                    {data: 'username'},
                    {data: 'email'},
                    {data: 'firstName'},
                    {data: 'lastName'},
                    {data: 'creationTimestamp'},
                    {data: function (row) {
                            var aux = "<a class=\"removeRow\" id=\"{id}\" href=\"#\">Borrar</a>";
                            return aux.replace("{id}", row._links.self.href);
                        }}
                ]
            });
            // Remove row from datatables and database
            usersTable.on('draw', function () {
                $("a.removeRow").click(function () {
                    var $td = $(this);
                    $("#confirmRemoveRow").click(function () {
                        $('#removeRowModal').modal('hide');
                        removeRow($td.attr("id"), $td.parents('tr'), usersTable);
                    });
                    $('#removeRowModal').modal('show');
                });
            });
        }
    });
    // Create temperature table
    $("#tempHumiditiesNav").click(function () {
        if (!tempHumiditiesTable) {
            tempHumiditiesTable = $("#tempHumiditiesTableId").DataTable({
                lengthMenu: [20, 50, 100, 200],
                serverSide: true,
                ajax: function (data, callback) {
                    var columns = ["id", "user", "timestamp", "temperature", "humidity"];
                    if (data.search.value !== "") {
                        datatablesToSpringRest(data, GET_TEMPHUMIDITIES_URL + "/search/findByUsername?username=", "tempHumidities", columns, callback, data.search.value);
                    } else {
                        datatablesToSpringRest(data, GET_TEMPHUMIDITIES_URL, "tempHumidities", columns, callback);
                    }
                },
                columns: [
                    {data: function (row) {
                            return row._links.self.href.split('/').pop();
                        }},
                    {data: function (row) {
                            return row._links.owner.href;
                        }},
                    {data: 'timestamp'},
                    {data: 'temperature'},
                    {data: 'humidity'},
                    {data: function (row) {
                            var aux = "<a class=\"removeRow\" id=\"{id}\" href=\"#\">Borrar</a>";
                            return aux.replace("{id}", row._links.self.href);
                        }}
                ]
            });
            // Get and set usernames in second column after init
            tempHumiditiesTable.on('init', function () {
                tempHumiditiesTable.column(1).nodes().each(function (i) {
                    getUsername(i);
                });
            });
            // Remove row from datatables and database
            tempHumiditiesTable.on('draw', function () {
                $("a.removeRow").click(function () {
                    var $td = $(this);
                    $("#confirmRemoveRow").click(function () {
                        $('#removeRowModal').modal('hide');
                        removeRow($td.attr("id"), $td.parents('tr'), tempHumiditiesTable);
                    });
                    $('#removeRowModal').modal('show');
                });
            });
            // Trigger search only when pressing enter
            $("#tempHumiditiesTableId_filter input").unbind().keyup(function (e) {
                if (e.keyCode === 13) {
                    tempHumiditiesTable.search($(this).val()).draw();
                }
            });
        }
    });
    // Create door events table
    $("#doorEventsNav").click(function () {
        if (!doorEventsTable) {
            doorEventsTable = $("#doorEventsTableId").DataTable({
                lengthMenu: [20, 50, 100, 200],
                serverSide: true,
                ajax: function (data, callback) {
                    var columns = ["id", "user", "timestamp"];
                    if (data.search.value !== "") {
                        datatablesToSpringRest(data, GET_DOOREVENTS_URL + "/search/findByUsername?username=", "doorEvents", columns, callback, data.search.value);
                    } else {
                        datatablesToSpringRest(data, GET_DOOREVENTS_URL, "doorEvents", columns, callback);
                    }
                },
                columns: [
                    {data: function (row) {
                            return row._links.self.href.split('/').pop();
                        }},
                    {data: function (row) {
                            return row._links.owner.href;
                        }},
                    {data: 'timestamp'},
                    {data: function (row) {
                            var aux = "<a class=\"removeRow\" id=\"{id}\" href=\"#\">Borrar</a>";
                            return aux.replace("{id}", row._links.self.href);
                        }}
                ]
            });
            // Get and set usernames in second column after init
            doorEventsTable.on('init', function () {
                doorEventsTable.column(1).nodes().each(function (i) {
                    getUsername(i);
                });
            });
            // Remove row from datatables and database
            doorEventsTable.on('draw', function () {
                $("a.removeRow").click(function () {
                    var $td = $(this);
                    $("#confirmRemoveRow").click(function () {
                        $('#removeRowModal').modal('hide');
                        removeRow($td.attr("id"), $td.parents('tr'), doorEventsTable);
                    });
                    $('#removeRowModal').modal('show');
                });
            });
            // Trigger search only when pressing enter
            $("#doorEventsTableId_filter input").unbind().keyup(function (e) {
                if (e.keyCode === 13) {
                    doorEventsTable.search($(this).val()).draw();
                }
            });
        }
    });
    // Create generic sensor table
    $("#sensorReadingsNav").click(function () {
        if (!sensorReadingsTable) {
            sensorReadingsTable = $("#sensorReadingsTableId").DataTable({
                lengthMenu: [20, 50, 100, 200],
                search: false,
                serverSide: true,
                ajax: function (data, callback) {
                    var columns = ["id", "user", "timestamp", "value1", "value2", "value3"];
                    if (data.search.value !== "") {
                        datatablesToSpringRest(data, GET_SENSORREADINGS_URL + "/search/findByUsername?username=", "sensorReadings", columns, callback, data.search.value);
                    } else {
                        datatablesToSpringRest(data, GET_SENSORREADINGS_URL, "sensorReadings", columns, callback);
                    }
                },
                columns: [
                    {data: function (row) {
                            return row._links.self.href.split('/').pop();
                        }},
                    {data: function (row) {
                            return row._links.owner.href;
                        }},
                    {data: 'timestamp'},
                    {data: 'value1'},
                    {data: 'value2'},
                    {data: 'value3'},
                    {data: function (row) {
                            var aux = "<a class=\"removeRow\" id=\"{id}\" href=\"#\">Borrar</a>";
                            return aux.replace("{id}", row._links.self.href);
                        }}
                ]
            });
            // Get and set usernames in second column after init
            sensorReadingsTable.on('init', function () {
                sensorReadingsTable.column(1).nodes().each(function (i) {
                    getUsername(i);
                });
            });
            // Remove row from datatables and database
            sensorReadingsTable.on('draw', function () {
                $("a.removeRow").click(function () {
                    var $td = $(this);
                    $("#confirmRemoveRow").click(function () {
                        $('#removeRowModal').modal('hide');
                        removeRow($td.attr("id"), $td.parents('tr'), sensorReadingsTable);
                    });
                    $('#removeRowModal').modal('show');
                });
            });
            // Trigger search only when pressing enter
            $("#sensorReadingsTableId_filter input").unbind().keyup(function (e) {
                if (e.keyCode === 13) {
                    sensorReadingsTable.search($(this).val()).draw();
                }
            });
        }
    });
}
);
// Calculate paging for spring rest, make ajax call to server, prepare response for datatables and call callback function
function datatablesToSpringRest(data, baseUrl, entityName, columns, fnCallback, searchParam) {
    if (typeof (searchParam) === 'undefined')
        searchParam = "?";
    var page = (data.start / data.length).toString();
    var size = data.length.toString();
    var sort = columns[data.order[0].column] + "," + data.order[0].dir;
    var url = baseUrl + searchParam + "&page=" + page + "&size=" + size + "&sort=" + sort;
    $.ajax({
        "url": url,
        "success": function (responseData) {
            var dataToReturn = new Object();
            dataToReturn.draw = data.draw;
            dataToReturn.length = responseData.page.size;
            dataToReturn.recordsTotal = responseData.page.totalElements;
            dataToReturn.recordsFiltered = responseData.page.totalElements;
            dataToReturn.data = responseData._embedded[entityName];
            fnCallback(dataToReturn);
        }
    });
}

function getUsername(td) {
    $.ajax({
        url: $(td).text(),
        success: function (data) {
            $(td).text(data.username);
        }
    });
}

function removeRow(url, row, table) {
    $.ajax({
        url: url,
        method: "DELETE",
        success: function () {
            table.row(row).remove().draw();
        }
    });
}