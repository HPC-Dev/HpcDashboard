Chart.defaults.global.defaultFontStyle = 'bold';
Chart.defaults.global.defaultFontFamily = 'Verdana';
$('#appDrop').on("change", function() {
    var value = $(this).val();
    $('#checkbox').empty();
    $.getJSON("/cpus", {
        appName: value,
        ajax: 'true'
    }, function(data) {
        var len = data.length;
        var html = '';
        for (var i = 0; i < len; i++) {
            html += ' <div id="cpuCheckBox" class="custom-control custom-checkbox custom-control-inline">';
            html += '<input class="custom-control-input" type="checkbox" name="type" id="' + data[i] + '" value="' + data[i] + '"/>' +
                '<label class="custom-control-label" text="' + data[i] + '" for="' + data[i] + '" >' + data[i] + '</label>';
            html += '</div>';
        }

        $('#checkbox').append(html);
        $("#checkbox").show();
        $("#button").show();
    });
    clearChart();
    $('#tableNew').html('');

});

function clearChart() {
    $('#multiBarChart').remove();
    $('#multiChart').append('<canvas id="multiBarChart" width="450" height="300" role="img"></canvas>');
}

$('button').on('click', function() {
    var cpuList = [];
    $("input:checked").each(function() {
        cpuList.push($(this).val());
    });

    var params = {};
    params.cpuList = cpuList;

    if ($("#appDrop").val() === null) {
        console.log("empty");
    }

    getMultiChartData();
//    var BACKGROUND_COLORS = ['rgba(255, 99, 132, 0.2)', 'rgba(75, 192, 192, 0.2)', 'rgba(255, 206, 86, 0.2)', 'rgba(153, 102, 255, 0.2)', 'rgba(255, 159, 64, 0.2)', 'rgba(54, 162, 235, 0.2)', 'rgba(192, 0, 0, 0.2)','#FF9E80', '#03A9F4', '#FFD180', , '#90A4AE', '#F9A825',  '#C5E1A5', '#80CBC4', '#7986CB', '#7E57C2', '#3949AB', '#e57373', '#546E7A', '#A1887F'];
//    var BORDER_COLORS = ['rgba(255, 99, 132, 1)', 'rgba(75, 192, 192, 1)', 'rgba(255, 206, 86, 1)', 'rgba(153, 102, 255, 1)', 'rgba(255, 159, 64, 1)', 'rgba(54, 162, 235, 1)', 'rgba(192, 0, 0, 1)'];

      var BACKGROUND_COLORS = ['rgb(19,91,105)','rgb(21,104,121)','rgb(20,116,132)','rgb(133,155,163)','rgb(173,183,191)'   ,'rgba(255, 99, 132, 0.2)', 'rgba(75, 192, 192, 0.2)', 'rgba(255, 206, 86, 0.2)', 'rgba(153, 102, 255, 0.2)', 'rgba(255, 159, 64, 0.2)', 'rgba(54, 162, 235, 0.2)', 'rgba(192, 0, 0, 0.2)','#FF9E80', '#03A9F4', '#FFD180', , '#90A4AE', '#F9A825',  '#C5E1A5', '#80CBC4', '#7986CB', '#7E57C2', '#3949AB', '#e57373', '#546E7A', '#A1887F'];
      //var BORDER_COLORS = ['rgba(255, 99, 132, 1)', 'rgba(75, 192, 192, 1)', 'rgba(255, 206, 86, 1)', 'rgba(153, 102, 255, 1)', 'rgba(255, 159, 64, 1)', 'rgba(54, 162, 235, 1)', 'rgba(192, 0, 0, 1)'];


    function getMultiChartData() {
        getData();
        var app = $('#appDrop')[0].value;
        if (app && cpuList.length > 1) {
            $.getJSON("/chart/multiCPUResult/" + app, $.param(params, true), function(data) {
                var label = data.cpus;
                var chartdata = {
                    labels: label,
                    datasets: data.dataset.map(function(dataset, index) {
                        return {
                            label: dataset.bmName,
                            backgroundColor: BACKGROUND_COLORS[index],
                            //borderColor: BORDER_COLORS[index],
                            borderWidth: 1,
                            data: dataset.value,
                            fill: false,
                        };
                    })
                };
                var chartOptions = {
                    title: {
                        display: true,
                        text: data.appName
                    },
                    scales: {
                        yAxes: [{
                            ticks: {
                                beginAtZero: true,
                            },
                            scaleLabel: {
                                display: true,
                                labelString: 'Relative Performance'
                            },
                            gridLines: {
                                color: "rgb(234, 236, 244)",
                                zeroLineColor: "rgb(234, 236, 244)",
                                drawBorder: false,
                                borderDash: [2],
                                zeroLineBorderDash: [2]
                            }
                        }],
                        xAxes: [{
                            offset: true
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

            });
        }
        else{
           clearChart();
           $('#tableNew').html('');
        }
    }

    function getData() {
        var app = $('#appDrop')[0].value;

        var cpuList = [];
            $("input:checked").each(function() {
                cpuList.push($(this).val());
            });

            var params = {};
            params.cpuList = cpuList;

        if (app && cpuList.length > 1) {

            $.getJSON("/chart/multiCPUTable/" + app, $.param(params, true), function(data) {
                updateTable(data.label, data.resultData);

            });
        } else {
            $('#tableNew').html('');
        }
    }

    function updateTable(columns, data) {

        var table;
        if (Object.keys(data).length > 0) {
            table = "<table class='table table-responsive table-bordered '>" + getHeaders(columns) + getBody(columns, data) + "</table>";
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
            row.push("<td>" + val + "</td>")
        });

        row.push('</tr>');

        return row.join('');
    }



});