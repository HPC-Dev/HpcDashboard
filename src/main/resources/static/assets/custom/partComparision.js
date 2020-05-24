var BACKGROUND_COLORS = ['rgba(54, 162, 235, 0.2)',  'rgba(192, 0, 0, 0.2)','rgba(75, 192, 192, 0.2)',  'rgba(255, 206, 86, 0.2)' ,  'rgba(255, 159, 64, 0.2)', 'rgba(255, 206, 86, 0.2)',   'rgba(255, 206, 86, 0.2)' ];
var BORDER_COLORS = ['rgba(54, 162, 235, 1)', 'rgba(192, 0, 0, 1)',  'rgba(75, 192, 192, 1)', 'rgba(255, 206, 86, 1)',  'rgba(255, 159, 64, 1)', 'rgba(255, 206, 86, 1)' ,'rgba(255, 206, 86, 1)'  ];

Chart.defaults.global.defaultFontStyle = 'bold';
Chart.defaults.global.defaultFontFamily = 'Verdana';

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
        clearChart();
        getData();
    }
});

$("#cpu1").on("change", getData);
$("#cpu2").on("change", getData);

function clearChart() {
    $('#multiBarChart').remove();
    $('#multiChart').append('<canvas id="multiBarChart" width="450" height="300" role="img"></canvas>');
}

function getData() {
    var cpu1 = $('#cpuDrop1')[0].value;
    var cpu2 = $('#cpuDrop2')[0].value;
    var app = $('#appDrop')[0].value;

    if (app && cpu1 && cpu2) {

        $.getJSON("/avg/resultComparision/" + app + "/" + cpu1 + "/" + cpu2, function(data) {
            var columnNames = [];
            var transformedData = [];
            data.bmName.forEach(element => columnNames.push(element));
            data.resultData.forEach(element => transformedData.push(element));
            updateTable(columnNames, transformedData, data.comment);
        });
    } else {
        $('#tableNew').html('');
    }
    getMultiChartData(app, cpu1, cpu2);
}

function getMultiChartData(app, cpu1, cpu2) {
    var cpuList = [];
    cpuList.push(cpu1);
    cpuList.push(cpu2);
    var params = {};
    params.cpuList = cpuList;
    if (app && cpu1 && cpu2) {
        $.getJSON("/chart/result/" + app, $.param(params, true), function(data) {
            if(data.length >0){
            var label = data[0].labels;

            var chartdata = {
                labels: label,
                datasets: data[0].datasets.map(function(dataset, index) {
                    return {
                        label: dataset.cpuName,
                        backgroundColor: BACKGROUND_COLORS[index],
                        borderColor: BORDER_COLORS[index],
                        borderWidth: 1,
                        data: dataset.value
                    };
                })
            };
            var chartOptions = {
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
        table += " <p style='font-size:12px;text-align:left;font-family:verdana;'>" +"*" + comment + "</p>"
    } else {
        table = "<p>No data available</p>";
    }

    $('#tableNew').html(table);
}

function getHeaders(columns) {
    var headers = ['<thead><tr>'];


    //    headers.push('<th bgcolor="#D3D3D3">' + '</th>')
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