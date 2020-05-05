$('#appDrop').change(appChange);
$('#cpuDrop').change(cpuChange);

function cpuChange() {
    var value = $(this).val();
    var text = $(this).find('option:selected').text();
    var bm = $('#bmDrop')[0].value;
    if (value != '') {
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
    }

    clearForm();
    clearChart();

    if ($("#option1").is(":checked")) {
        getNodeChartData();
    }
    if ($("#option2").is(":checked") && bm) {
        getBmChartData();
    }
}

function clearForm() {
    $('#appDrop').val('');
    $('#bmDrop').val('');
    $("#bm").hide();
    $("#radio").hide();
    $("#option1").prop("checked", false);
    $("#option2").prop("checked", false);
}

function clearChart() {
    $('#bmBarChart').remove();
    $('#bmChart').append('<canvas id="bmBarChart" width="450" height="300" role="img"></canvas>');

    $('#nodeBarChart').remove();
    $('#nodeChart').append('<canvas id="nodeBarChart" width="450" height="300" role="img"></canvas>');

}

function appChange() {
    var app = $('#appDrop')[0].value;
    var cpu = $('#cpuDrop')[0].value;
    if (app != '') {
        $("#radio").show();

    } else if (app == '') {
        $("#radio").hide();
    }

    $.getJSON("/bms", {
        appName: app,
        cpu: cpu,
        ajax: 'true'
    }, function(data) {
        var html = '<option value="" selected="true" disabled="disabled">Benchmark</option>';
        var len = data.length;
        for (var i = 0; i < len; i++) {
            html += '<option value="' + data[i] + '">' +
                data[i] + '</option>';
        }
        html += '</option>';
        $('#bmDrop').html(html);
    });

    if ($("#option1").is(":checked")) {
        getNodeChartData();
    }

    if ($("#option2").is(":checked")) {
        $("#bmChart").hide();
    }
}

$("#option1")
    .change(function() {
        if ($(this).is(":checked")) {
            var val = $(this).val();
            if (val === "node") {
                $("#bm").hide();
                $("#bmChart").hide();
                $("#nodeChart").show();
                getNodeChartData();
            }
        }
    });


$("#option2")
    .change(function() {
        var app = $('#appDrop')[0].value;
        var cpu = $('#cpuDrop')[0].value;
        if ($(this).is(":checked")) {
            var val = $(this).val();
            if (val === "bench") {
                $("#nodeChart").hide();
                $("#bm").show();
                $.getJSON("/bms", {
                    appName: app,
                    cpu: cpu,
                    ajax: 'true'
                }, function(data) {
                    var html = '<option value="" selected="true" disabled="disabled">Benchmark</option>';
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                        html += '<option value="' + data[i] + '">' +
                            data[i] + '</option>';
                    }
                    html += '</option>';
                    $('#bmDrop').html(html);
                });
                $("#bmChart").show();
                $("#bm").on("change", getBmChartData);
            }
        }
    });


function getNodeChartData() {
    var cpu = $('#cpuDrop')[0].value;
    var app = $('#appDrop')[0].value;
    var node = 1;
    if (app && cpu && node) {
        $.getJSON("/chart/resultApp/" + cpu + "/" + app + "/" + node, function(data) {
            var label = data[0].labels;
            var result = data[0].dataset;
            var chartdata = {
                labels: label,
                datasets: [{
                    backgroundColor: ['#ff6666', '#ff9933', '#3399ff', '#9933ff', '#99004c', '#3333ff', '#808080', '#660000', '#006666', '#6666ff', '#003366', '#660066', '#ff9999', '#66b2ff', '#b266ff', '#660043', '#a0a0a0', '#666600', '#000066', '#ccoocc', '#ffcccc', '#ffcc99', '#99ccff', '#9999ff', '#cc99ff', '#ff99cc'],
                    borderWidth: 1,
                    data: result
                }]
            };
            var chartOptions = {
                legend: {
                    display: false,
                },
                title: {
                    display: true,
                    text: data[0].appCPUName
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
            var graphTarget = $("#nodeBarChart");
            var barGraph = new Chart(graphTarget, {
                type: 'bar',
                data: chartdata,
                options: chartOptions
            });
        });
    } else {
        clearChart();
    }
}


function getBmChartData() {
    $("#bmChart").show();
    var cpu = $('#cpuDrop')[0].value;
    var app = $('#appDrop')[0].value;
    var bm = $('#bmDrop')[0].value;

    if (app && cpu && bm) {
        $.getJSON("/chart/resultBm/" + cpu + "/" + app + "/" + bm, function(data) {
            var label = data[0].labels;
            var result = data[0].dataset;
            var chartdata = {
                labels: label,
                datasets: [{
                    backgroundColor: ['rgba(255, 99, 132, 0.2)', 'rgba(54, 162, 235, 0.2)', 'rgba(255, 206, 86, 0.2)', 'rgba(75, 192, 192, 0.2)', 'rgba(255, 159, 64, 0.2)', 'rgba(153, 102, 255, 0.2)', 'rgba(192, 0, 0, 0.2)'],
                    borderColor: ['rgba(255, 99, 132, 1)', 'rgba(54, 162, 235, 1)', 'rgba(255, 206, 86, 1)', 'rgba(75, 192, 192, 1)', 'rgba(255, 159, 64, 1)', 'rgba(153, 102, 255, 1)', 'rgba(192, 0, 0, 1)'],
                    borderWidth: 1,
                    data: result
                }]
            };

            var chartOptions = {
                legend: {
                    display: false,
                },
                title: {
                    display: true,
                    text: data[0].appCPUName
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

            $('#bmBarChart').remove();
            $('#bmChart').append('<canvas id="bmBarChart" width="450" height="300" role="img"></canvas>');

            var graphTarget = $("#bmBarChart");
            var barGraph = new Chart(graphTarget, {
                type: 'bar',
                data: chartdata,
                options: chartOptions
            });
        });
    }
}


function getRandomColorHex() {
    var hex = "0123456789ABCDEF",
        color = "#";
    for (var i = 1; i <= 6; i++) {
        color += hex[Math.floor(Math.random() * 16)];
    }
    return color;
}