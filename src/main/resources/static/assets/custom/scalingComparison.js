var cpu1;
var cpu2;
var app;
var typeVal1;
var typeVal2;
var flag1;
var flag2;


$('#appDrop').on("change", function() {

    var preApp = $("#appDrop option:selected").val();
    var preCpu1 = $("#cpuDrop1 option:selected").val();
    var preCpu2 = $("#cpuDrop2 option:selected").val();
    var preType1 = $("#typeDrop1 option:selected").val();
    var preType2 = $("#typeDrop2 option:selected").val();

    var value = $(this).val();
    if (value != '') {

        $.getJSON("/cpus", {
            appName: $(this).val(),
            ajax: 'true'
        }, function(data) {
            var html = '<option value="" selected="true" disabled="disabled">-- CPU1 --</option>';
            //            var len = data.length;
            //            for (var i = 0; i < len; i++) {
            //                html += '<option value="' + data[i] + '">' +
            //                    data[i] + '</option>';
            //            }
            //            html += '</option>';



            for (var cpuGen in data) {

                html += '<optgroup label="' + cpuGen + '">'

                for (var cpu in data[cpuGen]) {
                    html += '<option value="' + data[cpuGen][cpu] + '">' +
                        data[cpuGen][cpu] + '</option>';

                }

                html += '</optgroup>'
            }

            $('#cpuDrop1').html(html);


            for (var cpuGen in data) {

                if (data[cpuGen].includes(preCpu1)) {
                    $('#cpuDrop1').val(preCpu1);
                    getRunType1(preType1);
                    break;
                } else {
                    $('#cpuDrop1').val('');
                }

            }

            getData();


        });


        $.getJSON("/cpus", {
            appName: $(this).val(),
            ajax: 'true'
        }, function(data) {
            var html = '<option value="" selected="true" disabled="disabled">-- CPU2 --</option>';
            //            var len = data.length;
            //            for (var i = 0; i < len; i++) {
            //                html += '<option value="' + data[i] + '">' +
            //                    data[i] + '</option>';
            //            }
            //            html += '</option>';

            for (var cpuGen in data) {

                html += '<optgroup label="' + cpuGen + '">'

                for (var cpu in data[cpuGen]) {
                    html += '<option value="' + data[cpuGen][cpu] + '">' +
                        data[cpuGen][cpu] + '</option>';

                }

                html += '</optgroup>'
            }

            $('#cpuDrop2').html(html);


            for (var cpuGen in data) {

                if (data[cpuGen].includes(preCpu2)) {
                    $('#cpuDrop2').val(preCpu2);
                    getRunType2(preType2);
                    break;
                } else {
                    $('#cpuDrop2').val('');
                }

            }

            getData();


        });

        $('#typeDrop1').val('');
        $('#typeDrop2').val('');
        clearHtml();
    }
});


$("#type1").on("change", getData);

$("#type2").on("change", getData);

$("#cpu1").on("change", getRunType1);

$("#cpu2").on("change", getRunType2);

function getRunType1(preType1) {

    cpu1 = $('#cpuDrop1')[0].value;
    app = $('#appDrop')[0].value;

    if ($("#typeDrop1 option:selected").val()) {
        var preType1 = $("#typeDrop1 option:selected").val();
    }

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

            if (data.includes(preType1)) {
                $('#typeDrop1').val(preType1);
            } else {
                $('#typeDrop1').val('');
            }
            getData();

        });
    }

    clearHtml();

}

function getRunType2(preType2) {


    cpu2 = $('#cpuDrop2')[0].value;
    app = $('#appDrop')[0].value;

    if ($("#typeDrop2 option:selected").val()) {
        var preType2 = $("#typeDrop2 option:selected").val();
    }


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

            if (data.includes(preType2)) {
                $('#typeDrop2').val(preType2);
            } else {
                $('#typeDrop2').val('');
            }
            getData();

        });
    }

    clearHtml();

}


function clearHtml() {
     $('#table').html('');
      $('#table1').html('');
      $('#table2').html('');
      $('#table3').html('');
      $("#p1").hide();
      $("#p2").hide();
      $("#p3").hide();
      $("#p4").hide();
    $('.collapse').collapse('hide');
}

function getData() {

    app = $('#appDrop')[0].value;
    cpu1 = $('#cpuDrop1')[0].value;
    cpu2 = $('#cpuDrop2')[0].value;
    type1 = $('#typeDrop1')[0].value;
    type2 = $('#typeDrop2')[0].value;

        clearHtml();

    var comment;
    if (app && cpu1 && cpu2 && type1 && type2) {

        $.getJSON("/avg/scalingComparisonNode/" + app + "/" + cpu1 + "/" + cpu2 + "/" + type1 + "/" + type2, function(data) {
            var transformedData = data.resultData;
            var columnNames = data.bmName;
            updateTable(columnNames, transformedData);

        });

        $.getJSON("/avg/scalingComparisonCore/" + app + "/" + cpu1 + "/" + cpu2 + "/" + type1 + "/" + type2, function(data) {
            var transformedData = data.resultData;
            var columnNames = data.bmName;
            updateTableCore(columnNames, transformedData);

        });

         $.getJSON("/avg/scalingComparisonCount/" + app + "/" + cpu1 + "/" + cpu2 + "/" + type1 + "/" + type2, function(data) {
                    var transformedData = data.resultData;
                    var columnNames = data.bmName;
                    updateTableCount(columnNames, transformedData);

                });

        $.getJSON("/avg/scalingComparisonVariance/" + app + "/" + cpu1 + "/" + cpu2 + "/" + type1 + "/" + type2, function(data) {
                            var transformedData = data.resultData;
                            var columnNames = data.bmName;
                            updateTableVariance(columnNames, transformedData);
        });

    }
    $('#table').html('');
    $('#table1').html('');
    $('#table2').html('');
    $('#table3').html('');
    $("#p1").hide();
    $("#p2").hide();
    $("#p3").hide();
    $("#p4").hide();

}

function updateTable(columns, data) {
    var table;

    if (data.length > 0) {
        table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
        $("#p1").show();
    } else {
        table = "<p>No data available</p>";
    }

    $('#table').html(table);
}

function updateTableCore(columns, data) {
    var table1;

    if (data.length > 0) {
        table1 = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
        $("#p2").show();

    } else {
        table1 = "<p>No data available</p>";
    }

    $('#table1').html(table1);
}

function updateTableCount(columns, data) {
    var table;

    if (data.length > 0) {
        table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
        $("#p3").show();
    }
    $('#table2').html(table);
}

function updateTableVariance(columns, data) {
    var table1;

    if (data.length > 0) {
        table1 = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
        $("#p4").show();
    }
    $('#table3').html(table1);
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

        if (column != 'Nodes' && column != 'Cores' && rowData[column] != undefined) {

            if (val < 3.0) {
                row.push('<td bgcolor="#C8E6C9">' + val.concat('%') + '</td>')
            } else if (val > 3.0 && val < 5.0) {
                row.push('<td bgcolor="#FFF9C4">' + val.concat('%') + '</td>')
            } else if (val > 5.0) {
                row.push('<td bgcolor="FFCDD2">' + val.concat('%') + '</td>')
            }
        } else {
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

        if (column != 'Nodes' && column != 'Cores' && rowData[column] != undefined) {

            if (val < 3) {
                row.push('<td bgcolor="#FFCDD2">' + val + '</td>')
            } else {
                row.push('<td bgcolor="#C8E6C9">' + val + '</td>')
            }
        } else {
            row.push('<td>' + val + '</td>')
        }
    });

    row.push('</tr>');

    return row.join('');
}