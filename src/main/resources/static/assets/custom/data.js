var cpus = []
getCpuCores();

$('#cpuDrop').on("change", function() {
    var value = $(this).val();
    var text = $(this).find('option:selected').text();
    if (value != '') {
        $("#core").show();
        $("#coreCount").show();
        $("#coreValue")[0].value = 'Cores: ' + findCpuCore(text);
        $("#app").show();
                $.getJSON("/apps", {
                    cpu: value,
                    ajax: 'true'
                }, function(data) {
                    var html = '<option value="" selected="true" disabled="disabled">App</option>';
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                        html += '<option value="' + data[i] + '">' +
                            data[i] + '</option>';
                    }
                    html += '</option>';
                    $('#appDrop').html(html);
                });
        $("#app").on("change", getData);
    } else if (value == '') {
        $("#core").hide();
        $("#app").hide();
        $("#coreCount").hide();
    }

    $('#appDrop').val('');
    getData();
});

function getCpuCores() {
    return $.getJSON( "/avg/cpus", function( data ) {
        cpus = data;
    });
}

function findCpuCore(cpu) {
    return cpus.find(function(eachCpu) {
        return eachCpu.cpuSku === cpu
    })['cores'];
}

function getData() {
    var cpu = $('#cpuDrop')[0].value;
    var app = $('#appDrop')[0].value;

    if (app && cpu) {
        $.getJSON("/avg/result/" + cpu + "/" + app, function(data) {
            var transformedData = [];
            var columnNames = ['Nodes', 'Cores'];
            data.forEach((item, index) => {
                var row = transformedData.find(function(tr) {
                    return tr.Nodes === item.nodes;
                });
                var rowIndex = transformedData.findIndex(function(tr) {
                    return tr.Nodes === item.nodes;
                })

                if (!row) {
                    row = {
                        Nodes: item.nodes,
                        Cores: item.cores * item.nodes
                    };
                    transformedData.push(row);
                }

                row[item.bmName] = item.avgResult;

                if (columnNames.indexOf(item.bmName) === -1) {
                    columnNames.push(item.bmName);
                }

            });

            updateTable(columnNames, transformedData);
        });
    } else {
        $('#table').html('');
    }
}

function updateTable(columns, data) {
    var table;

    if (data.length > 0) {
        table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
    } else {
        table = "<p>No data available</p>";
    }

    $('#table').html(table);
}

function getHeaders(columns) {
    var headers = ['<thead><tr>'];

    columns.forEach(function(column) {
        headers.push('<th bgcolor="#D3D3D3">' + column + '</th>')
    });
    headers.push('</tr></thead>');

    return headers.join('');
}

function getBody(columns, data) {
    var body = ['<tbody>'];
    data.forEach(function(row) {
        body.push(generateRow(columns, row))
    });

    body.push('</tbody>');

    return body.join('');
}

function generateRow(columns, rowData) {
    var row = ['<tr>'];
    var val;

    columns.forEach(function(column) {
        if (rowData[column] != undefined) {
            val = rowData[column];
        } else {
            val = '';
        }
        row.push("<td>" + val + "</td>")
    });

    row.push('</tr>');

    return row.join('');
}