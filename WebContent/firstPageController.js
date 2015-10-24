rideEasyServerApp.controller('firstPageController', function($scope, $http) {

	//$http.defaults.useXDomain = true;

  initialize = function(){
    $scope.url = window.location.href;
    //$scope.url = "abc";
  };

  $scope.callPayuRestApi = function() {
		var requestUrl = 'https://secure.payu.com/api/v2_1/orders';  	
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
		requestData =
		{
		   "notifyUrl": "https://your.eshop.com/notify",
		   "customerIp": "127.0.0.1",
		   "merchantPosId": "145227",
		   "description": "RTV market",
		   "currencyCode": "PLN",
		   "totalAmount": "15000",
//		   "extOrderId":"{YOUR_EXT_ORDER_ID}",
		   "buyer": {
		     "email": "john.doe@example.com",
		     "phone": "654111654",
		     "firstName": "John",
		     "lastName": "Doe"
		   },
		   "products": [
		       {
		           "name": "Wireless Mouse for Laptop",
		           "unitPrice": "15000",
		           "quantity": "1"
		       }
		   ]
		};	
		var requestConfig = {
			headers:{
				'Content-Type': 'application/json',
				'Authorization': 'Basic MTQ1MjI3OjEzYTk4MGQ0Zjg1MWYzZDlhMWNmYzc5MmZiMWY1ZTUw'
			}
		};
//		$http.post('https://secure.payu.com/api/v2_1/orders',requestData,requestConfig).
//			success(function(data, status, headers, config) {
//				//$scope.payuResponse = data;
//				var divElement = document.getElementById("payuSuccessResponseDiv");
//				divElement.innerHTML = data;				
//			}).
//			error(function(data, status, headers, config) {
//				alert(data + status + headers);
//				var divElement = document.getElementById("payuErrorResponseDiv");
//				divElement.innerHTML = data;
//			});
		$http.post(requestUrl, requestData, requestConfig)
			.then(function successCallback(response) {
					$scope.payuResponse = response;
				}, function errorCallback(response) {
					$scope.payuResponse = response;
				});			
	};

	$scope.callRideEasyRestApi = function(){
		var requestData = {
			"email" :"rishabhgarg@nsitonline.in"
		};
		var requestConfig = {
			headers:{
				'Content-Type': 'application/json'
			}
		};
		$http.post('http://rideeasy.elasticbeanstalk.com/rest/profile/getUserDetailsViaEmail',requestData,requestConfig).
			success(function(data, status, headers, config) {
				$scope.rideEasyResponse = data;
			}).
			error(function(data, status, headers, config) {
				alert(data + status + headers);
				var divElement = document.getElementById("rideEasyErrorResponseDiv");
				divElement.innerHTML = data;
			});
	};

	$scope.callRideEasyRestApiUpdated = function(){
		var requestUrl = 'http://rideeasy.elasticbeanstalk.com/rest/profile/getUserDetailsViaEmail';
		var requestData = {
			"email" :"rishabhgarg@nsitonline.in"
		};
		var requestConfig = {
			headers:{
				'Content-Type': 'application/json'
			}
		};
		$http.post(requestUrl, requestData, requestConfig)
			.then(function successCallback(response) {
					$scope.rideEasyResponse = response;
				}, function errorCallback(response) {
					$scope.rideEasyResponse = response;
				});
	};

  initialize();
});
