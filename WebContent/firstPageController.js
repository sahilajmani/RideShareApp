rideEasyServerApp.controller('firstPageController', function($scope) {
  initialize = function(){
    var url = window.location.href;
		var parametersString = url.split("?")[1];
		var parametersArray = [];
		if(parametersString)
		{
			parametersArray = (url.split("?")[1]).split("&");
		}
		else
		{
			alert('Invalid Operation');
			return;
		}
		var personCost;
		var personFirstName;
		var personEmail;
		var personPhone;
		if(parametersArray.length===4)
		{
			personCost = parametersArray[0].split("=")[1];
			personFirstName = parametersArray[1].split("=")[1];
			personEmail = parametersArray[2].split("=")[1];
			personPhone = parametersArray[3].split("=")[1];
		}
		else
		{
			alert('Invalid Operation');
			return;
		}
		if(personCost && personFirstName && personEmail && personPhone)
		{
			$scope.person = {
				productInfo: "product1",
				surl: "http://rideeasy.elasticbeanstalk.com/TransactionSuccess.html",
				furl: "http://rideeasy.elasticbeanstalk.com/TransactionFailure.html",
				cost: personCost,
				firstName: personFirstName,
				email: personEmail,
				phone: personPhone
			};
		}
		else
		{
			alert('Invalid Operation');
			return;
		}
  };
  initialize();
});
