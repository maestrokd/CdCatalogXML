
(function() {

    var self = window.catalogController || {};
    window.catalogController = self;

    var uploadedPage;

    var uploadedTotalPage;


    function getTableRowFromArray(data) {

        if (!Array.isArray(data)) { return ''}

        var result = '';

        for (var i = 0; i < data.length; i++) {
            result += '<td>' + data[i] + '</td>\n';
        }

        return ['<tr>', result, '</tr>'].join('\n');
    }


    function createTableContent(data) {
        return [
            "<thead>",
            "<tr>",
            "<th>TITLE</th>",
            "<th>ARTIST</th>",
            "<th>COUNTRY</th>",
            "<th>COMPANY</th>",
            "<th>PRICE</th>",
            "<th>YEAR</th>",
            "</tr>",
            "</thead>",
            "<tbody>",
            data,
            "</tbody>"
        ].join('\n');
    }


    function getTableRowsFromXML(xml) {
        var data = [];

        $(xml).find('CD').each(function() {
            var innerString = [
                this.getElementsByTagName("TITLE")[0].childNodes[0].nodeValue,
                this.getElementsByTagName("ARTIST")[0].childNodes[0].nodeValue,
                this.getElementsByTagName("COUNTRY")[0].childNodes[0].nodeValue,
                this.getElementsByTagName("COMPANY")[0].childNodes[0].nodeValue,
                this.getElementsByTagName("PRICE")[0].childNodes[0].nodeValue,
                this.getElementsByTagName("YEAR")[0].childNodes[0].nodeValue
            ];

            data.push(getTableRowFromArray(innerString));
        });

        return data.join('\n');
    }


    self.refreshTableWithDecrement = function () {
        window.pageGlobal = Number(uploadedPage) - 1;
        loadTable();
    };


    self.refreshTableWithIncrement = function() {
        window.pageGlobal = Number(uploadedPage) + 1;
        loadTable();
    };


    function getButton(className, text, onClickFunction) {
        return [
            '<button onclick=catalogController.' + onClickFunction + '() class="' + className + '">',
            text,
            '</button>'
        ].join('\n');
    }


    function refreshButtons() {
        var buttonsPlaceholder = $("#buttons");

        buttonsPlaceholder.empty();

        if (uploadedPage > 1) {
            var buttonPrevious = getButton('btn btn-success', 'Previous', 'refreshTableWithDecrement');
            buttonsPlaceholder.html(buttonsPlaceholder.html() + buttonPrevious);
        }

        if (uploadedPage < uploadedTotalPage) {
            var buttonNext = getButton('btn btn-success', 'Next', 'refreshTableWithIncrement');
            buttonsPlaceholder.html(buttonsPlaceholder.html() + buttonNext);
        }
    }


    function onDataLoaded(xml) {
        var rows = getTableRowsFromXML(xml);
        var table = createTableContent(rows);
        $('#table_ForAjax_Id').html(table);

        uploadedPage = $(xml).find('PAGE').text();
        uploadedTotalPage = $(xml).find('TOTALPAGE').text();

        refreshButtons();
    }


    function loadTable() {
        $.ajax({
            type: "GET",
            url: "/api/catalog/pageResource?page=" + pageGlobal + "&size=" + sizeGlobal,
            cache: false,
            dataType: "xml",
            success: onDataLoaded
        });
    }


    $(document).ready(function () {
        loadTable();
        $('#table_DataTable_Id').DataTable();
    });

})();