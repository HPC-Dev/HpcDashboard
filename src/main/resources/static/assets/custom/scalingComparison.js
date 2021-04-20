var typeVal;
var flag;

var cpu1;
var cpu2;
var app;
var typeVal1;
var typeVal2;
var flag1;
var flag2;


$('#appDrop').on("change", function() {
    var value = $(this).val();
    if (value != '') {

        $.getJSON("/cpus", {
            appName: $(this).val(),
            ajax: 'true'
        }, function(data) {
            var html = '<option value="" selected="true" disabled="disabled">-- CPU1 --</option>';
            var len = data.length;
            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i] + '">' +
                    data[i] + '</option>';
            }
            html += '</option>';
            $('#cpuDrop1').html(html);
        });


        $.getJSON("/cpus", {
            appName: $(this).val(),
            ajax: 'true'
        }, function(data) {
            var html = '<option value="" selected="true" disabled="disabled">-- CPU2 --</option>';
            var len = data.length;
            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i] + '">' +
                    data[i] + '</option>';
            }
            html += '</option>';
            $('#cpuDrop2').html(html);
        });

        $('#cpuDrop1').val('');
        $('#cpuDrop2').val('');
        $('#typeDrop1').val('');
        $('#typeDrop2').val('');
//        clearChart();
//        clearHtml();
    }
});

$("#type1").on("change", getData);

$("#type2").on("change", getData);

$("#cpu1").on("change", getRunType1);

$("#cpu2").on("change", getRunType2);

function getRunType1() {
    cpu1 = $('#cpuDrop1')[0].value;
    app = $('#appDrop')[0].value;
    if (app && cpu1) {
        $.getJSON("/runTypesByAPPCPU", {
            appName: app,
            cpu: cpu1,
            ajax: 'true'
        }, function(data) {
            var html = '<option value="" selected="true" disabled="disabled">-- RunType1 --</option>';
            var len = data.length;

            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i] + '">' +
                    data[i] + '</option>';
            }
            html += '</option>';
            $('#typeDrop1').html(html);

            type1 = $('#typeDrop1')[0].value;

        });
    }

//    clearChart();
//    clearHtml();

}

function getRunType2() {
    cpu2 = $('#cpuDrop2')[0].value;
    app = $('#appDrop')[0].value;
    if (app && cpu2) {

        $.getJSON("/runTypesByAPPCPU", {
            appName: app,
            cpu: cpu2,
            ajax: 'true'
        }, function(data) {
            var html = '<option value="" selected="true" disabled="disabled">-- RunType2 --</option>';
            var len = data.length;
            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i] + '">' +
                    data[i] + '</option>';
            }
            html += '</option>';
            $('#typeDrop2').html(html);
            type2 = $('#typeDrop2')[0].value;

        });
    }

//    clearHtml();
//    clearChart();

}

//function clearChart() {
//    $('#multiBarChart').remove();
//    $('#multiChart').append('<canvas id="multiBarChart" width="450" height="300" role="img"></canvas>');
//}
//
//function clearHtml() {
//    $('#tableNew').html('');
//       $('#footnote').hide();
//       $('.collapse').collapse('hide');
//}


function getData() {

    app = $('#appDrop')[0].value;
    cpu1 = $('#cpuDrop1')[0].value;
    cpu2 = $('#cpuDrop2')[0].value;
    type1 = $('#typeDrop1')[0].value;
    type2 = $('#typeDrop2')[0].value;

//    clearChart();
//    clearHtml();

    var comment;
    if (app && cpu1 && cpu2 && type1 && type2) {


        $.getJSON("/avg/result/" + cpu1 + "/" + app + "/" + type1, function(data) {

            console.log(data);
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

        $.getJSON("/avg/result/" + cpu2 + "/" + app + "/" + type2, function(data) {
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
                    updateTable1(columnNames, transformedData);

                });

        $.getJSON("/chart/scalingTable/" + cpu1 + "/" + app + "/" + type1, function(data) {
                    comment = data.comment;
                    if(data.scalingResultData.length >1) {
                    updateTableScaling(data.nodeLabel, data.scalingResultData);
                    }

                });

        $.getJSON("/chart/scalingTable/" + cpu2 + "/" + app + "/" + type2, function(data) {
                       comment = data.comment;
                       if(data.scalingResultData.length >1) {
                       updateTableScaling1(data.nodeLabel, data.scalingResultData);
                       }
                     });

    }
     $('#table').html('');
     $('#tableScaling').html('');
     $('#table1').html('');
     $('#tableScaling1').html('');
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

function updateTable1(columns, data) {
    var table1;

    if (data.length > 0) {
        table1 = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
        //  table += " <p style='font-size:12px;text-align:left;font-family:verdana;'>" +"*" + comment + "</p>"
        $("#p2").show();

    } else {
        table1 = "<p>No data available</p>";
    }

    $('#table1').html(table1);
}

function updateTableScaling(columns, data) {
    var table;

    if (data.length > 0) {
        table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
        $("#p3").show();
    }
    $('#tableScaling').html(table);
}

function updateTableScaling1(columns, data) {
    var table1;

    if (data.length > 0) {
        table1 = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
        $("#p4").show();
    }
    $('#tableScaling1').html(table1);
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