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
console.log($("#appDrop").val());

getMultiChartData();
var BACKGROUND_COLORS = ['rgba(255, 99, 132, 0.2)','rgba(54, 162, 235, 0.2)',   'rgba(255, 159, 64, 0.2)','rgba(75, 192, 192, 0.2)', 'rgba(153, 102, 255, 0.2)','rgba(192, 0, 0, 0.2)', 'rgba(255, 206, 86, 0.2)'];
var BORDER_COLORS = ['rgba(255, 99, 132, 1)', 'rgba(54, 162, 235, 1)',  'rgba(255, 159, 64, 1)', 'rgba(75, 192, 192, 1)', 'rgba(153, 102, 255, 1)', 'rgba(192, 0, 0, 1)', 'rgba(255, 206, 86, 1)'];

function getMultiChartData() {
        var app = $('#appDrop')[0].value;
        	if(app){
                $.getJSON( "/chart/result/" + app , $.param(params, true), function( data ) {
                        var label = data[0].labels;

		                var chartdata = {
                            labels: label,
                            datasets:
                            data[0].datasets.map(function(dataset, index){
                                return {
                                          label: dataset.cpuName,
                                          backgroundColor: BACKGROUND_COLORS[index],
                                          borderColor: BORDER_COLORS[index],
                                          borderWidth: 1,
                                          data: dataset.value
                                       };
                            })
                        };
                    console.log(data[0].datasets, chartdata.datasets);
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

                     $('#multiBarChart').remove();
                     $('#multiChart').append('<canvas id="multiBarChart" width="450" height="300" role="img"></canvas>');
                    var graphTarget = $("#multiBarChart");
                    var barGraph = new Chart(graphTarget, {
                        type: 'bar',
                        data: chartdata,
                        options: chartOptions
                    });
                });
            }
        }

   });