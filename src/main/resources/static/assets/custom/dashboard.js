var table = $('table#ajax').DataTable({
		'ajax' : '/datatable/dashboard',
		'scrollY': "450px",
        'scrollX': true,
        'scrollCollapse': true,
        'processing': true,
        'serverSide': true,
		columns : [
		    {
                data: 'jobId'
            },
            {
                data: 'appName'
            },
            {
                data: 'bmName'
            },
            {
                data: 'nodes'
            },
            {
                data: 'cores'
            },
            {
                data: 'nodeName'
            },
            {
                data: 'result'
            },
            {
                data: 'cpu'
            }
		]
	});


$('#appDrop').change(
        function() {
        var value =
        $.getJSON( "/bms", {
            appName : $(this).val(),
            ajax : 'true'
            }, function(data) {
                var html = '<option value="" selected="true" disabled="disabled">--Benchmark--</option>';
                var len = data.length;
                for ( var i = 0; i < len; i++) {
                    html += '<option value="' + data[i] + '">'
                            + data[i] + '</option>';
                }
                html += '</option>';
                $('#bmDrop').html(html);
            });
        });

     $('#appDrop').change(function () {
        var filter = '';
        $('#appDrop option:selected').each(function () {
            filter += $(this).text() + "+";
        });
        filter = filter.substring(0, filter.length - 1);
        table.column(1).search(filter).draw();
    });

     $('select#bmDrop').change(function () {
        var filter = '';
        $('select#bmDrop option:selected').each(function () {
            filter += $(this).text() + "+";
        });
        filter = filter.substring(0, filter.length - 1);
        table.column(2).search(filter).draw();
    });

    $('select#nodeDrop').change(function () {
        var filter = '';
        $('select#nodeDrop option:selected').each(function () {
            filter += $(this).text() + "+";
        });
        filter = filter.substring(0, filter.length - 1);
        table.column(3).search(filter).draw();
    });

    $('select#cpuDrop').change(function () {
        var filter = '';
        $('select#cpuDrop option:selected').each(function () {
            filter += $(this).text() + "+";
        });
        filter = filter.substring(0, filter.length - 1);
        table.column(7).search(filter).draw();
    });
