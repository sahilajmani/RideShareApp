rideEasyServerApp.controller('firstPageController', function($scope, $http) {

	$http.defaults.useXDomain = true;

  initialize = function(){
    $scope.url = window.location.href;
    //$scope.url = "abc";
  };

  $scope.callPayuRestApi = function(){
		var requestData =
		{
				"notifyUrl": "https://your.eshop.com/notify",
				"customerIp": "127.0.0.1",
				"merchantPosId": "145227",
				"description": "RTV market",
				"currencyCode": "PLN",
				"totalAmount": "21000",
				"products": [
				    {
				        "name": "Wireless Mouse for Laptop",
				        "unitPrice": "15000",
				        "quantity": "1"
				    },
				    {
				        "name": "HDMI cable",
				        "unitPrice": "6000",
				        "quantity": "1"
				    }
				]
		};
		requestData = {
			"email" :"rishabhgarg@nsitonline.in"
		};
		var requestConfig = {
			headers:{
				'Content-Type': 'application/json',
				'Authorization': 'Basic MTQ1MjI3OjEzYTk4MGQ0Zjg1MWYzZDlhMWNmYzc5MmZiMWY1ZTUw'
			}
		};
		requestConfig = {
			headers:{
				'Content-Type': 'application/json'
			}
		};
		$http.post('http://rideeasy.elasticbeanstalk.com/rest/profile/getUserDetailsViaEmail',requestData,{headers:{'Content-Type': 'application/json'}}).
			success(function(data, status, headers, config) {
				alert(data);
			}).
			error(function(data, status, headers, config) {
				alert(data + status + headers);
				var divElement = document.getElementById("responseDiv");
				divElement.innerHTML = data;
			});
/*
		$http.jsonp('https://secure.payu.com/api/v2_1/orders' + "&callback=JSON_CALLBACK",requestData,requestConfig).then(
			function(data, status, headers, config) {
				alert('success');
			},
			function(data, status, headers, config) {
				alert('error');
			});
*/
  };

  initialize();
});
