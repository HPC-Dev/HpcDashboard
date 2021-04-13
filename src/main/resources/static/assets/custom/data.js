var cpus = []
var typeVal;
var flag;
$('#typeDrop').change(typeChange);
$("#app").on("change", getData);

$('#cpuDrop').on("change", function() {
    cpu = $('#cpuDrop')[0].value;
    var preType = $("#typeDrop option:selected").val();
    var preApp = $("#appDrop option:selected").val();

    if (cpu) {
     $("#type").show();
        $.getJSON("/runTypesByCPU", {
            cpu: cpu,
            ajax: 'true'
        }, function(data) {
            var html = '<option value="" selected="true" disabled="disabled">-- Run Type --</option>';
            var len = data.length;
            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i] + '">' +
                    data[i] + '</option>';
            }
            html += '</option>';
            $('#typeDrop').html(html);

             if (data.includes(preType)) {
                            $('#typeDrop').val(preType);
                            typeChange(preApp);
                        } else {
                            $('#typeDrop').val('');
                        }
            getData();
        });

    } else if (value == '') {
        $("#type").hide();
        $("#app").hide();
    }

     $("#p1").hide();
     $("#p2").hide();
     $("#p3").hide();
     $("#p4").hide();
    $('#typeDrop').val('');
    $('#appDrop').val('');
    $('#table').html('');
    $('#tableScaling').html('');
    $('#tableCV').html('');
    $('#tableCount').html('');

});

function typeChange(preApp) {

    cpu = $('#cpuDrop')[0].value;
    type = $('#typeDrop')[0].value;

     if($("#appDrop option:selected").val())
        {
            var preApp = $("#appDrop option:selected").val();
        }

    if (cpu && type) {
        $("#app").show();
        $.getJSON("/appsByType", {
            cpu: cpu,
            type: type,
            ajax: 'true'
        }, function(data) {
            var html = '<option value="" selected="true" disabled="disabled">-- App --</option>';
            var len = data.length;
            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i] + '">' +
                    data[i] + '</option>';
            }
            html += '</option>';
            $('#appDrop').html(html);

             if (data.includes(preApp)) {
                  $('#appDrop').val(preApp);
                      getData();
                        } else {
                            $('#appDrop').val('');
                        }
        });
    } else if (value == '') {
        $("#app").hide();
    }

    $('#appDrop').val('');
    $('#table').html('');
    $('#tableScaling').html('');
    $('#tableCV').html('');
    $('#tableCount').html('');

    getData();
}


function findCpuCore(cpu) {
    return cpus.find(function(eachCpu) {
        return eachCpu.cpuSku === cpu
    })['cores'];
}

function getData() {
    var cpu = $('#cpuDrop')[0].value;
    var app = $('#appDrop')[0].value;
    var runType = $('#typeDrop')[0].value;

    var comment;
    if (app && cpu && runType) {
        $.getJSON("/avg/result/" + cpu + "/" + app + "/" + runType, function(data) {
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
                        Cores: item.cores
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

        $.getJSON("/chart/scalingTable/" + cpu + "/" + app + "/" + runType, function(data) {
                    comment = data.comment;
                    if(data.scalingResultData.length >1) {
                    updateTableScaling(data.nodeLabel, data.scalingResultData);
                    }
                    updateTableCV(data.nodeLabel, data.cvdata);
                    updateTableCount(data.nodeLabel, data.countData);
                });

    }
     $('#table').html('');
     $('#tableScaling').html('');
     $('#tableCV').html('');
     $('#tableCount').html('');
     $("#p1").hide();
     $("#p2").hide();
     $("#p3").hide();
     $("#p4").hide();

}

function updateTable(columns, data) {
    var table;

    if (data.length > 0) {
        table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
        //  table += " <p style='font-size:12px;text-align:left;font-family:verdana;'>" +"*" + comment + "</p>"
        $("#p1").show();

    } else {
        table = "<p>No data available</p>";
    }

    $('#table').html(table);
}

function updateTableScaling(columns, data) {
    var table;

    if (data.length > 0) {
        table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
        $("#p2").show();
    }
    $('#tableScaling').html(table);
}

function updateTableCV(columns, data) {
    var table;

    if (data.length > 0) {
        table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBodyVariance(columns, data) + "</table>";
        $("#p3").show();
    }
    $('#tableCV').html(table);
}

function updateTableCount(columns, data) {
    var table;

    if (data.length > 0) {
        table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBodyCount(columns, data) + "</table>";
        $("#p4").show();
    }
    $('#tableCount').html(table);
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

        row.push('<td>' + val + '</td>')

    });

    row.push('</tr>');

    return row.join('');
}

function getBodyVariance(columns, data) {
    var body = ['<tbody>'];
    data.forEach(function(row) {
        body.push(generateRowVariance(columns, row))
    });

    body.push('</tbody>');

    return body.join('');
}


function generateRowVariance(columns, rowData) {
    var row = ['<tr>'];
    var val;

    columns.forEach(function(column) {

        if (rowData[column] != undefined) {
            val = rowData[column];

        } else {
            val = '';
        }

        if(column != 'Nodes' &&  column != 'Cores' && rowData[column] != undefined){

            if(val < 3.0){
                row.push('<td bgcolor="#C8E6C9">' + val.concat('%') +  '</td>')
            }
            else if(val > 3.0 && val < 5.0 ){
                row.push('<td bgcolor="#FFF9C4">' + val.concat('%') + '</td>')
            }
            else if(val > 5.0 ){
                row.push('<td bgcolor="FFCDD2">' + val.concat('%') + '</td>')
            }
        }
       else{
            row.push('<td>' + val + '</td>')
       }
    });

    row.push('</tr>');

    return row.join('');
}


function getBodyCount(columns, data) {
    var body = ['<tbody>'];
    data.forEach(function(row) {
        body.push(generateRowCount(columns, row))
    });

    body.push('</tbody>');

    return body.join('');
}


function generateRowCount(columns, rowData) {
    var row = ['<tr>'];
    var val;

    columns.forEach(function(column) {

        if (rowData[column] != undefined) {
            val = rowData[column];

        } else {
            val = '';
        }

        if(column != 'Nodes' &&  column != 'Cores' && rowData[column] != undefined){

            if(val < 3){
                row.push('<td bgcolor="#FFCDD2">' + val +  '</td>')
            }
            else {
                row.push('<td bgcolor="#C8E6C9">' + val + '</td>')
            }
        }
       else{
            row.push('<td>' + val + '</td>')
       }
    });

    row.push('</tr>');

    return row.join('');
}