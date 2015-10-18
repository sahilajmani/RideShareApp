rideEasyServerApp.controller('firstPageController', function($scope, $location) {
  initialize = function(){
    $scope.url = window.location.href;
    //$scope.url = "abc";
  };
  initialize();
});
