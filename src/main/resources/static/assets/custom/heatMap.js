var BACKGROUND_COLORS = ['rgb(19,91,105)', 'rgb(133,155,163)', 'rgb(20,116,132)', '#8DB9CA', 'rgb(173,183,191)', 'rgb(21,104,121)', 'rgba(255, 99, 132, 0.2)', 'rgba(75, 192, 192, 0.2)', 'rgba(255, 206, 86, 0.2)', 'rgba(153, 102, 255, 0.2)', '#EFEBE9', 'rgba(54, 162, 235, 0.2)', 'rgba(192, 0, 0, 0.2)', '#D1C4E9', '#BBDEFB', '#FFD180', , '#90A4AE', '#F9A825', '#C5E1A5', '#80CBC4', '#7986CB', '#7E57C2', '#3949AB', '#e57373', '#546E7A', '#A1887F'];

Chart.defaults.global.defaultFontStyle = 'bold';
Chart.defaults.global.defaultFontFamily = 'Verdana';

var app;

function clearHtml() {
    $('#heading').empty();
    $('#footnote').hide();
    $('.collapse').collapse('hide')
}


$('#cpuDrop1').on("change", function() {
    var value = $(this).val();

    var preValue1 = $("#typeDrop1 option:selected").val();
    // $("#type1").show();
    clearHtml();
    $('#tableNew').html('');
    if (value != '') {
        $.getJSON("/runTypesByCPU", {

            cpu: $(this).val(),
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

            if (data.includes(preValue1)) {
                $('#typeDrop1').val(preValue1);
            } else {

                $('#typeDrop1').val('');
            }
            getData();
        });
    }

});


$('#cpuDrop2').on("change", function() {
    var value = $(this).val();
    var preValue2 = $("#typeDrop2 option:selected").val();

    clearHtml();
    // $("#type2").show();
    $('#tableNew').html('');
    if (value != '') {
        $.getJSON("/runTypesByCPU", {
            cpu: $(this).val(),
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

            if (data.includes(preValue2)) {
                $('#typeDrop2').val(preValue2);
            } else {

                $('#typeDrop2').val('');
            }
            getData();

        });

    }


});


$('#cpuDrop3').on("change", function() {
    var value = $(this).val();
    var preValue3 = $("#typeDrop3 option:selected").val();
    clearHtml();
    // $("#type2").show();
    $('#tableNew').html('');
    if (value != '') {
        $.getJSON("/runTypesByCPU", {
            cpu: $(this).val(),
            ajax: 'true'
        }, function(data) {
            var html = '<option value="" selected="true" disabled="disabled">-- RunType3 --</option>';
            var len = data.length;

            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i] + '">' +
                    data[i] + '</option>';
            }
            html += '</option>';
            $('#typeDrop3').html(html);

            if (data.includes(preValue3)) {
                $('#typeDrop3').val(preValue3);
            } else {

                $('#typeDrop3').val('');
            }
            getData();
        });

    }


});


$('#cpuDrop4').on("change", function() {
    var value = $(this).val();
    var preValue4 = $("#typeDrop4 option:selected").val();
    clearHtml();
    // $("#type2").show();
    $('#tableNew').html('');
    if (value != '') {
        $.getJSON("/runTypesByCPU", {
            cpu: $(this).val(),
            ajax: 'true'
        }, function(data) {
            var html = '<option value="" selected="true" disabled="disabled">-- RunType4 --</option>';
            var len = data.length;

            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i] + '">' +
                    data[i] + '</option>';
            }
            html += '</option>';
            $('#typeDrop4').html(html);

            if (data.includes(preValue4)) {
                $('#typeDrop4').val(preValue4);
            } else {

                $('#typeDrop4').val('');
            }
            getData();
        });

    }


});



$("#type1").on("change", getData);

$("#type2").on("change", getData);

$("#type3").on("change", getData);

$("#type4").on("change", getData);


function getData() {
    clearHtml();
    $('#tableNew').html('');

    cpu1 = $('#cpuDrop1')[0].value;
    cpu2 = $('#cpuDrop2')[0].value;
    cpu3 = $('#cpuDrop3')[0].value;
    cpu4 = $('#cpuDrop4')[0].value;

    type1 = $('#typeDrop1')[0].value;
    type2 = $('#typeDrop2')[0].value;
    type3 = $('#typeDrop3')[0].value;
    type4 = $('#typeDrop4')[0].value;


    if (cpu1 && cpu2 && cpu3 && cpu4 && type1 && type2 && type3 && type4 && !(cpu1 === cpu2 && type1 === type2)) {
        $.getJSON("/avg/heatMap/" + cpu1 + "/" + cpu2 + "/" + cpu3 + "/" + cpu4 + "/" +  type1 + "/" + type2 + "/" +  type3 + "/" + type4, function(data) {
            if (data.heatMapResults != null && data.heatMapResults.length > 1) {
                updateTable(data.columns, data.heatMapResults);
            } else {
                table = "<p>No data available</p>";
                $('#tableNew').html(table);
            }
        });
    } else {
        clearHtml();
        $('#tableNew').html('');
    }

}


function updateTable(columns, data, comment) {

    var table;
    if (Object.keys(data).length > 0) {
        $('#heading').empty();
        var heading = "<p style='font-weight: bold;font-size:15px;text-align:left;font-family:verdana;'>" + cpu1 + '_' + type1 + ' vs ' + ' ( ' + cpu2 + '_' + type2 + ' , ' + cpu3 + '_' + type3 + ' , ' + cpu4 + '_' + type4     + ' ) ' + "</p>"
        $('#heading').append(heading);
        $('#heading').show();
        $('#footnote').show();

        table = "<table class='table table-responsive '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
    } else {
        clearHtml();
        table = "<p>No data available</p>";
    }

    $('#tableNew').html(table);
}

function getHeaders(columns) {
    var headers = ['<thead><tr>'];
    columns.forEach(function(column) {

        if (column === 'isv') {
            headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white">' + column.toUpperCase() + '</font></th>')
        } else {
            headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white">' + column.charAt(0).toUpperCase() + column.slice(1) + '</font></th>')
        }
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
    var row = ['<tr style="border: 1px solid black;border-collapse: collapse";>'];
    var val;
    var flag = 0;

    columns.forEach(function(column) {
        if (rowData[column] != undefined) {
            val = rowData[column];
        } else {
            val = '';
        }

        if (column === 'category' && rowData[column] != null) {
            row = [];
            row = ['<tr style="border: 1px solid black;border-collapse: collapse"; bgcolor="#78909C">']
            flag = 1;
        } else if ((column === 'isv' && rowData[column] != null)) {
            row = [];
            row = ['<tr style="border: 1px solid black;border-collapse: collapse"; bgcolor="#ECEFF1"> <td bgcolor="white"></td>']
            flag = 2;
        }

        if (column != "") {

            if (flag == 1) {
                if (val.startsWith("+")) {
                    row.push('<td style="border: 1px solid black;border-collapse: collapse"; bgcolor="#C8E6C9">' + val.substring(1) + '</td>')
                } else if (val.startsWith("-")) {
                    row.push('<td style="border: 1px solid black;border-collapse: collapse"; bgcolor="FFCDD2">' + val + '</td>')
                } else {
                    row.push('<td style="border: 1px solid black;border-collapse: collapse";> <font color="white" font-weight: bold >' + val + '</font></td>')
                }

            } else if (flag == 2) {
                if (val.startsWith("+")) {
                    row.push('<td style="border: 1px solid black;border-collapse: collapse"; bgcolor="#ECEFF1"> <b>' + val.substring(1) + '</b></td>')
                } else if (val.startsWith("-")) {
                    row.push('<td style="border: 1px solid black;border-collapse: collapse"; bgcolor="ECEFF1"> <b>' + val + ' </b></td>')
                } else {
                    row.push('<td style="border: 1px solid black;border-collapse: collapse;"  > <b>' + val + '</b></td>')
                }

            } else {

                if (val != "") {
                    if (val.startsWith("+")) {
                        row.push('<td style="border: 1px solid black;border-collapse: collapse">' + val.substring(1) + '</td>')
                    } else {
                        row.push('<td style="border: 1px solid black;border-collapse: collapse" >' + val + '</td>')
                    }
                } else {
                    row.push('<td>' + val + '</td>')
                }
            }

        } else {
            row.push('<td bgcolor="#D3D3D3" style="font-weight:bold">' + val + '</td>')
        }
    });

    row.push('</tr>');
    flag = 0;

    return row.join('');
}