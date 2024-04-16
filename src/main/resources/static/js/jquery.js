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

//first request to server to create order

const paymentStart = () => {

	console.log("payment Start");
	let amount = $("#payment_field").val();
	console.log(amount);

	if (amount == '' || amount == null) {
		/*alert("Amount is Required !!");*/
		alert("Failed!!", " Amount is Required !!", "error");
		return;
	}

	// we will use ajax to send request to server to create order -jquery

	$.ajax({
			url: '/user/create_order',
			data: JSON.stringify({ amount: amount, info: 'order_request' }),
			contentType: 'application/json',
			type: 'POST',
			dataType: 'json',
			success: function(response) {

				//invoked when success
				if (response.status == "created") {
					var options = {
						"key": "rzp_test_OJ96VqHCAEPfEt", // Enter the Key ID generated from the Dashboard
						"amount": response.amount, // Amount is in currency subunits. Default currency is INR. Hence, 50000 refers to 50000 paise
						"currency": "INR",
						"name": "Smart Contact Manager",
						"description": "Donation",
						"image": "https://res.cloudinary.com/demo/image/upload/v1312461204/sample.jpg",
						"order_id": response.id, //This is a sample Order ID. Pass the `id` obtained in the response of Step 1
						"handler": function (response){
							console.log(response.razorpay_payment_id);
							console.log(response.razorpay_order_id);
							console.log(response.razorpay_signature);

							updatePaymentOnServer(response.razorpay_payment_id,response.razorpay_order_id,'paid');

							
						},
						"prefill": {
							"name": "",
							"email": "",
							"contact": ""
						},
						"notes": {
							"address": "Razorpay Corporate Office"
						},
						"theme": {
							"color": "#3399cc"
						}
					};

					var rzp1 = new Razorpay(options);
					rzp1.on('payment.failed', function (response){
							alert(response.error.code);
							alert(response.error.description);
							alert(response.error.source);
							alert(response.error.step);
							alert(response.error.reason);
							alert(response.error.metadata.order_id);
							alert(response.error.metadata.payment_id);
					});

					rzp1.open();
				}
			},
			error: function(error) {
				//invoked when error
				console.log(error)
				alert("Something went wrong !!")
			}
		})
};

function updatePaymentOnServer(payment_id,order_id,my_status)
{
	$.ajax({
		url: '/user/update_order',
			data: JSON.stringify({ payment_id: payment_id, order_id: order_id , my_status : my_status }),
			contentType: 'application/json',
			type: 'POST',
			dataType: 'json',
			success:function(response){
				console.log("Payment success !!");
			},
			error:function(error){
				console.log("Payment Failed !!");
			}
	})
};