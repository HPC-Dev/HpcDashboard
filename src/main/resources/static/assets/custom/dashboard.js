$(document).ready(function () {
    var table = $('#ajax').removeAttr('width').DataTable({
        scrollY: "500px",
        scrollX: true,
        scrollCollapse: true,
        "processing": true,
        "serverSide": true,
        "ajax": {
            "url": "/resultAjax",
            "type": "POST",
            "dataType": "json",
            "contentType": "application/json",
            "data": function (d) {
                return JSON.stringify(d);
            }
        },
        "columns": [
            {"data": "job_id", width: 65, targets: 0},
            {"data": "app_name",width: 120, targets: 1},
            {"data": "bm_name", width: 140, targets: 2},
            {"data": "nodes", width: 60, targets: 3},
            {"data": "cores", width: 40, targets: 4},
            {"data": "node_name", width: 250, targets: 5},
            {"data": "result", width: 80, targets: 6},
            {"data": "cpu", width: 40, targets: 7}
           ],
           fixedColumns: true
    });
});

 $('#appDrop').change(
        function() {
        var value = $(this).val();
        if(value != ''){
            $("#bm").show();
        }
        else if(value == ''){
            $("#bm").show();
        }
        $.getJSON( "/bms", {
            appName : $(this).val(),
            ajax : 'true'
            }, function(data) {
                var html = '<option value="">--Benchmark--</option>';
                var len = data.length;
                for ( var i = 0; i < len; i++) {
                    html += '<option value="' + data[i] + '">'
                            + data[i] + '</option>';
                }
                html += '</option>';
                $('#bmDrop').html(html);
            });
        });


  $("#app").on("change", getApp);
  $("#bm").on("change", getBm);
  $("#node").on("change", getNode);
  $("#cpu").on("change", getCPU);


function getApp() {
    var app = $('#appDrop')[0].value;
}

function getBm() {
    var bm = $('#bmDrop')[0].value;
}

function getNode() {
    var node = $('#nodeDrop')[0].value;
}

function getCPU() {
    var cpu = $('#cpuDrop')[0].value;
}