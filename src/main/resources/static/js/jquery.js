const search = () => {

    let query = $("#search-input").val();

    if (query == "") {
        $(".search-result").hide();
    }
    else {
        // sending request to server
        let url = 'http://localhost:8081/search/' + query;

        fetch(url)
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                //data....

                let text = `<div class='list-group'>`;

                data.forEach((element) => {
                    text += `<a href= '/user/contact/${element.cId}' class='list-group-item list-group-action'> ${element.name} </a>`;
                });

                text += `</div>`
                $(".search-result").html(text);
                $(".search-result").show();
            });
    }
};