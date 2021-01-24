var BACKGROUND_COLORS = ['rgb(19,91,105)', 'rgb(133,155,163)', 'rgb(20,116,132)', '#8DB9CA', 'rgb(173,183,191)', 'rgb(21,104,121)', 'rgba(255, 99, 132, 0.2)', 'rgba(75, 192, 192, 0.2)', 'rgba(255, 206, 86, 0.2)', 'rgba(153, 102, 255, 0.2)', '#EFEBE9', 'rgba(54, 162, 235, 0.2)', 'rgba(192, 0, 0, 0.2)', '#D1C4E9', '#BBDEFB', '#FFD180', , '#90A4AE', '#F9A825', '#C5E1A5', '#80CBC4', '#7986CB', '#7E57C2', '#3949AB', '#e57373', '#546E7A', '#A1887F'];

Chart.defaults.global.defaultFontStyle = 'bold';
Chart.defaults.global.defaultFontFamily = 'Verdana';

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
//        $("#type1").hide();
//        $("#type2").hide();
        clearChart();
        clearHtml();
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
//            if (data.length > 1) {
//                flag1 = 1;
//                $("#dummytype1").hide();
//                $("#type1").show();
//                if (!data.includes($('#typeDrop1')[0].value)) {
//                    $('#typeDrop1').val('');
//                } else {
//                    $('#typeDrop1').val($('#typeDrop1')[0].value);
//                    getData();
//                }
//            } else {
//                $("#type1").hide();
//                $("#dummytype1").show();
//                flag1 = 0;
//                typeVal1 = data[0];
//                getData();
//            }
        });
    }
//    $('#typeDrop1').val('');
    clearChart();
    clearHtml();

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

//            if (data.length > 1) {
//                flag2 = 1;
//                $("#type2").show();
//                if (!data.includes($('#typeDrop2')[0].value)) {
//                    $('#typeDrop2').val('');
//                } else {
//                    $('#typeDrop2').val($('#typeDrop2')[0].value);
//                    getData();
//                }
//            } else {
//                $("#type2").hide();
//                flag2 = 0;
//                typeVal2 = data[0];
//                getData();
//            }

        });
    }
//    $('#typeDrop2').val('');

    clearHtml();
    clearChart();

}

function clearChart() {
    $('#multiBarChart').remove();
    $('#multiChart').append('<canvas id="multiBarChart" width="450" height="300" role="img"></canvas>');
}

function clearHtml() {
    $('#tableNew').html('');
       $('#footnote').hide();
       $('.collapse').collapse('hide');
}

function getData() {
    app = $('#appDrop')[0].value;
    cpu1 = $('#cpuDrop1')[0].value;
    cpu2 = $('#cpuDrop2')[0].value;

//    if(flag1 == 1)
//        {
//        type1 = $('#typeDrop1')[0].value;
//        }
//        else{
//           type1 = typeVal1;
//           }
//
//        if(flag2 == 1)
//            {
//            type2 = $('#typeDrop2')[0].value;
//            }
//            else{
//            type2 = typeVal2;
//            }

    type1 = $('#typeDrop1')[0].value;
    type2 = $('#typeDrop2')[0].value;
    clearChart();
    clearHtml();
    if (app && cpu1 && cpu2 && type1 && type2 && !(cpu1===cpu2 && type1===type2)) {

        $.getJSON("/avg/resultComparison/" + app + "/" + cpu1 + "/" + cpu2 + "/" + type1 + "/" + type2, function(data) {

            var columnNames = [];
            var transformedData = [];
            data.bmName.forEach(element => columnNames.push(element));
            data.resultData.forEach(element => transformedData.push(element));
            updateTable(columnNames, transformedData, data.comment);
        });
    }
    else {
        clearHtml();
    }
    getMultiChartData(app, cpu1, cpu2, type1, type2);
}

function getMultiChartData(app, cpu1, cpu2, type1, type2) {
    var cpuList = [];
    var typeList = [];
    cpuList.push(cpu1);
    cpuList.push(cpu2);
    typeList.push(type1);
    typeList.push(type2);
    var params = {};
    params.cpuList = cpuList;
    params.typeList = typeList;
    if (app && cpu1 && cpu2 && type1 && type2 && !(cpu1===cpu2 && type1===type2)) {
        $.getJSON("/chart/result/" + app, $.param(params, true), function(data) {


            if (data.length >0 && data[0].datasets[0].value.length > 0 && data[0].datasets[1].value.length > 0) {
                var label = data[0].labels;

                var chartdata = {
                    labels: label,
                    datasets: data[0].datasets.map(function(dataset, index) {
                        return {
                            label: dataset.cpuName,
                            backgroundColor: BACKGROUND_COLORS[index],
                            //borderColor: BORDER_COLORS[index],
                            borderWidth: 1,
                            data: dataset.value
                        };
                    })
                };
                var chartOptions = {
                legend: {
                          display: true,
                          position: 'right'
                        },
                    title: {
                        display: true,
                        text: data[0].appName
                    },
                    scales: {
                        yAxes: [{
                            ticks: {
                                beginAtZero: true
                            },
                            scaleLabel: {
                                display: true,
                                labelString: data[0].metric
                            },
                            gridLines: {
                                color: "rgb(234, 236, 244)",
                                zeroLineColor: "rgb(234, 236, 244)",
                                drawBorder: false,
                                borderDash: [2],
                                zeroLineBorderDash: [2]
                            }
                        }]
                    },
                    tooltips: {
                        callbacks: {
                            label: function(tooltipItem) {
                                return tooltipItem.yLabel;
                            }
                        },
                        titleMarginBottom: 10,
                        titleFontColor: '#6e707e',
                        titleFontSize: 14,
                        backgroundColor: "rgb(255,255,255)",
                        bodyFontColor: "#858796",
                        borderColor: '#dddfeb',
                        borderWidth: 1,
                        xPadding: 15,
                        yPadding: 15,
                        displayColors: false,
                        caretPadding: 10,
                    },
                    layout: {
                        padding: {
                            top: 25,
                            bottom: 20
                        }
                    }
                };

                clearChart();
                var graphTarget = $("#multiBarChart");
                var barGraph = new Chart(graphTarget, {
                    type: 'bar',
                    data: chartdata,
                    options: chartOptions
                });
            } else {
                clearChart();
                $('#footnote').hide();
                $('.collapse').collapse('hide');
                $('#tableNew').html("<p>No comparison data available</p>");
            }
        });
    } else {
        clearChart();
    }

}


function updateTable(columns, data, comment) {
    var table;
    if (Object.keys(data).length > 0) {
        table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
        table += " <p style='font-size:12px;text-align:left;font-family:verdana;'>" + "*" + comment + "</p>"

        $('#footnote').show();
    } else {
        $('#footnote').hide();
        $('.collapse').collapse('hide');
        table = "<p>No data available</p>";
    }

    $('#tableNew').html(table);
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

        if (column != "") {

            if (val.startsWith("+")) {
                row.push('<td bgcolor="#C8E6C9">' + val + '</td>')
            } else if (val.startsWith("-")) {
                row.push('<td bgcolor="FFCDD2">' + val + '</td>')
            } else {
                row.push("<td>" + val + "</td>")
            }
        } else {
            row.push('<td bgcolor="#D3D3D3" style="font-weight:bold">' + val + '</td>')
        }
    });

    row.push('</tr>');

    return row.join('');
}