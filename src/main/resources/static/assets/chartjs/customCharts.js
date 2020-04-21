Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#000000';

$('#appDrop').change(appChange);

$('#cpuDrop').change(cpuChange);

function cpuChange(){
var value = $(this).val();
      var text = $(this).find('option:selected').text();
      if(value != ''){
        $("#app").show();
       }
      else if(value == ''){
          $("#app").hide();
       }
      getData();
}

function appChange(){
var app = $('#appDrop')[0].value;
    if(app != ''){
        $("#bm").show();
        $("#bm").on("change", getData);
    }
    else if(app == ''){
        $("#bm").show();
    }
    $.getJSON( "/bms", {
             appName : app,
             ajax : 'true'
             }, function(data) {
                 var html = '<option value="" selected="true" disabled="disabled">Benchmark</option>';
                 var len = data.length;
                 for ( var i = 0; i < len; i++) {
                     html += '<option value="' + data[i] + '">'
                             + data[i] + '</option>';
                 }
                 html += '</option>';
                 $('#bmDrop').html(html);
             });
  }

function getData() {
    var cpu = $('#cpuDrop')[0].value;
    var app = $('#appDrop')[0].value;
    var bm = $('#bmDrop')[0].value;

    if(app && cpu && bm) {
    var transformedData = [];
        $.getJSON( "/avg/result/" + cpu + "/" + app + "/" + bm, function( data ) {
           var columnNames = ['Nodes', 'Cores'];
           data.forEach((item, index) => {
           var row = transformedData.find(function(tr) { return tr.Nodes === item.nodes; });
           var rowIndex = transformedData.findIndex(function(tr) { return tr.Nodes === item.nodes; })
            if(!row) {
                row = {Nodes: item.nodes, Cores: item.cores * item.nodes};
                transformedData.push(row);
            }
            row[item.bmName] = item.avgResult;

            if(columnNames.indexOf(item.bmName) === -1 ) {
                columnNames.push(item.bmName);
            }
        });
//        updateTable(columnNames, transformedData);
    });
//    console.log(transformedData);
    } else {
        //$('#barChart').hide();
    }
}

var ctx = document.getElementById('barChart').getContext('2d');
var barGroupedChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ["acus_impinging"],
      datasets: [
        {
          label: "1 Node",
          backgroundColor: [getRandomColorHex()],
//          borderColor: ['rgba(255, 99, 132, 1)'],
          data: [17474.45],
          borderWidth: 1
        }, {
          label: "2 Node",
          backgroundColor: [getRandomColorHex()],
//          borderColor: ['rgba(54, 162, 235, 1)'],
          data: [8351.06],
          borderWidth: 1
        },
        {
          label: "4 Node",
          backgroundColor: [getRandomColorHex()],
//          borderColor: ['rgba(255, 206, 86, 1)'],
          data: [4098.96],
          borderWidth: 1
        }, {
          label: "8 Node",
          backgroundColor: [getRandomColorHex()],
//          borderColor: ['rgba(75, 192, 192, 1)'],
          data: [3440],
          borderWidth: 1
        },
        {
          label: "16 Node",
          backgroundColor: [getRandomColorHex()],
//          borderColor: ['rgba(255, 159, 64, 1)'],
          data: [936.33],
          borderWidth: 1
        },
        {
          label: "32 Node",
          backgroundColor: ['rgba(153, 102, 255, 0.2)', 'rgba(153, 102, 255, 0.2)'],
          borderColor: ['rgba(153, 102, 255, 1)', 'rgba(153, 102, 255, 1)'],
          data: [],
          borderWidth: 1
        },

      ]
    },
    options: {
      title: {
        display: true,
        text: 'AcuSolve'
      },
      scales: {
            yAxes: [{
                ticks: {
                    beginAtZero: true
                },
                gridLines: {
                          color: "rgb(234, 236, 244)",
                          zeroLineColor: "rgb(234, 236, 244)",
                          drawBorder: false,
                          borderDash: [2],
                          zeroLineBorderDash: [2]
                        }
            }],

        },
        layout: {
              padding: {
                left: 10,
                right: 25,
                top: 25,
                bottom: 0
              }
           },
           tooltips: {
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
               }

    }
});

function getRandomColorHex() {
    var hex = "0123456789ABCDEF",
        color = "#";
    for (var i = 1; i <= 6; i++) {
      color += hex[Math.floor(Math.random() * 16)];
    }
    return color;
  }
