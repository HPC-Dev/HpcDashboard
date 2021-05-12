var BACKGROUND_COLORS = ['rgb(19,91,105)', 'rgb(133,155,163)', 'rgb(20,116,132)', '#8DB9CA', 'rgb(173,183,191)', 'rgb(21,104,121)', 'rgba(255, 99, 132, 0.2)', 'rgba(75, 192, 192, 0.2)', 'rgba(255, 206, 86, 0.2)', 'rgba(153, 102, 255, 0.2)', '#EFEBE9', 'rgba(54, 162, 235, 0.2)', 'rgba(192, 0, 0, 0.2)', '#D1C4E9', '#BBDEFB', '#FFD180', , '#90A4AE', '#F9A825', '#C5E1A5', '#80CBC4', '#7986CB', '#7E57C2', '#3949AB', '#e57373', '#546E7A', '#A1887F'];

Chart.defaults.global.defaultFontStyle = 'bold';
Chart.defaults.global.defaultFontFamily = 'Verdana';

var app;
var cpuList = [];
var typeList = [];

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

    cpuList = [];
    typeList = [];

    cpu1 = $('#cpuDrop1')[0].value;
    cpu2 = $('#cpuDrop2')[0].value;
    cpu3 = $('#cpuDrop3')[0].value;
    cpu4 = $('#cpuDrop4')[0].value;

    type1 = $('#typeDrop1')[0].value;
    type2 = $('#typeDrop2')[0].value;
    type3 = $('#typeDrop3')[0].value;
    type4 = $('#typeDrop4')[0].value;

    cpuList.push(cpu1);
    cpuList.push(cpu2);
    cpuList.push(cpu3);
    cpuList.push(cpu4);

    typeList.push(type1);
    typeList.push(type2);
    typeList.push(type3);
    typeList.push(type4);

    var filteredTypeList = typeList.filter(function(type) {
        return type != "";
    });

    typeList = filteredTypeList;

    for (var i = 0; i < cpuList.length; i++) {
        if (cpuList[i].includes("CPU")) {
            cpuList.splice(i, 1);
            i--;
        }
    }

    if ((cpuList.length > 1 && typeList.length > 1 && (cpuList.length == typeList.length)) && !(cpu1 === cpu2 && type1 === type2)) {

        var params = {};
        params.cpuList = cpuList;
        params.typeList = typeList;

        $.getJSON("/avg/heatMap/", $.param(params, true), function(data) {
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

    var header = "";
    header += cpu1 + "_" + type1 + " vs ( ";

    for (i = 1; i < cpuList.length; i++) {
        header += cpuList[i] + "_" + typeList[i];
        if (i + 1 != cpuList.length)
            header += ", ";
    }
    header += " ) ";

    var table;
    if (Object.keys(data).length > 0) {
        $('#heading').empty();
        var heading = "<p style='font-weight: bold;font-size:15px;text-align:left;font-family:verdana;'>" + header + "</p>"
        $('#heading').append(heading);
        $('#heading').show();
        $('#footnote').show();

        //        table = "<table class='table table-responsive '>" + getHeaders(columns) + getBody(columns, data) + getFooters(columns) + "</table>";
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
        } else if (column === 'perNode1' || column === 'perCore1' ) {
            headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpu2 + '</br>' + "/ " + column.slice(3, -1) + '</font></th>')
        } else if (column === 'perNode2' || column === 'perCore2' ) {
            headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpu3 + '</br>' + "/ " + column.slice(3, -1) + '</font></th>')
        } else if (column === 'perNode3' || column === 'perCore3' ) {
            headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpu4 + '</br>' + "/ " + column.slice(3, -1) + '</font></th>')
        }
         else if ( column === 'perDollar1') {
         headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpu2 + '</br>' +  "Per$" + '</font></th>')
         }
         else if ( column === 'perDollar2') {
         headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpu3 + '</br>' +  "Per$" + '</font></th>')
         }
         else if ( column === 'perDollar3') {
         headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpu4 + '</br>' +  "Per$" + '</font></th>')
         }
         else if ( column === 'perWatt1') {
                  headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpu2 + '</br>' +  "PerW" + '</font></th>')
          }
          else if ( column === 'perWatt2') {
          headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpu3 + '</br>' +  "PerW" + '</font></th>')
          }
          else if ( column === 'perWatt3') {
          headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpu4 + '</br>' +  "PerW" + '</font></th>')
          }

        else {
            headers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white">' + column.charAt(0).toUpperCase() + column.slice(1) + '</font></th>')
        }
    });
    headers.push('</tr></thead>');

    return headers.join('');
}

function getFooters(columns) {
    var footers = ['<tfoot><tr>'];
    columns.forEach(function(column) {

        if (column === 'isv') {
            footers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white">' + column.toUpperCase() + '</font></th>')
        } else if (column === 'perNode1' || column === 'perCore1' || column === 'perDollar1' || column === 'perWatt1') {
            footers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpu2 + '</br>' + column.charAt(0).toUpperCase() + column.slice(3, -1) + '</font></th>')
        } else if (column === 'perNode2' || column === 'perCore2' || column === 'perDollar2' || column === 'perWatt2') {
            footers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpu3 + '</br>' + column.charAt(0).toUpperCase() + column.slice(3, -1) + '</font></th>')
        } else if (column === 'perNode3' || column === 'perCore3' || column === 'perDollar3' || column === 'perWatt3') {
            footers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white"> ' + cpu4 + '</br>' + column.charAt(0).toUpperCase() + column.slice(3, -1) + '</font></th>')
        } else {
            footers.push('<th style="border-bottom: 1px solid black;border-collapse: collapse"; bgcolor="#343A40"> <font color="white">' + column.charAt(0).toUpperCase() + column.slice(1) + '</font></th>')
        }


    });
    footers.push('</tr></tfoot>');

    return footers.join('');
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
            //            row.push('<td bgcolor="#D3D3D3" style="font-weight:bold">' + val + '</td>')
            row.push('<td bgcolor="#FFFFFF"  style="font-weight:bold ; white-space: nowrap">' + val + '</td>')

        }
    });

    row.push('</tr>');
    flag = 0;

    return row.join('');
}