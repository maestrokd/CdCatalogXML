$(document).ready(function () {
    loadTable();
    $('#table_DataTable_Id').DataTable();
});

function loadTable() {
    $.ajax({
        type: "GET",
        url: "/api/catalog/pageResource?page=" + pageGlobal + "&size=" + sizeGlobal,
        cache: false,
        dataType: "xml",
        success: function (xml) {
            var table = "<thead>\n" +
                "                    <tr>\n" +
                "                        <th>TITLE</th>\n" +
                "                        <th>ARTIST</th>\n" +
                "                        <th>COUNTRY</th>\n" +
                "                        <th>COMPANY</th>\n" +
                "                        <th>PRICE</th>\n" +
                "                        <th>YEAR</th>\n" +
                "                    </tr>\n" +
                "                    </thead>\n" +
                "                    <tbody>";

            $(xml).find('CD').each(function () {
                table += "<tr><td>" +
                    this.getElementsByTagName("TITLE")[0].childNodes[0].nodeValue +
                    "</td><td>" +
                    this.getElementsByTagName("ARTIST")[0].childNodes[0].nodeValue +
                    "</td><td>" +
                    this.getElementsByTagName("COUNTRY")[0].childNodes[0].nodeValue +
                    "</td><td>" +
                    this.getElementsByTagName("COMPANY")[0].childNodes[0].nodeValue +
                    "</td><td>" +
                    this.getElementsByTagName("PRICE")[0].childNodes[0].nodeValue +
                    "</td><td>" +
                    this.getElementsByTagName("YEAR")[0].childNodes[0].nodeValue +
                    "</td></tr>";
            });

            table += "</tbody>";
            document.getElementById("table_ForAjax_Id").innerHTML = table;

            var page = $(xml).find('PAGE').text();
            var totalPage = $(xml).find('TOTALPAGE').text();
            var sizeLocal = $(xml).find('SIZE').text();

            $("#buttons").empty()

            if (page > 1) {
                var buttonPrevious = document.createElement("button");
                buttonPrevious.className = "btn btn-success";
                buttonPrevious.appendChild(document.createTextNode("Previous"));
                buttonPrevious.setAttribute("id", "buttonPreviousId");
                $("#buttons").append(buttonPrevious);

                $("#buttonPreviousId").click(function () {
                    window.pageGlobal = Number(page) - 1;
                    loadTable();
                });
            }

            if (page < totalPage) {
                var buttonNext = document.createElement("button");
                buttonNext.className = "btn btn-success";
                buttonNext.appendChild(document.createTextNode("Next"));
                buttonNext.setAttribute("id", "buttonNextId");
                $("#buttons").append(buttonNext);

                $("#buttonNextId").click(function () {
                    window.pageGlobal = Number(page) + 1;
                    loadTable();
                });
            }

        }
    });
}
