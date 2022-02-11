angular.module("seller").controller("sellerController", function ($scope, $http) {
    //  将前端获取的数据发送到后台服务器，前端数据封装在$scope.entity中
    $scope.save = function () {
        $http.post("/seller", $scope.entity).then(
            function (res) {
                console.log(res);
            }
        )
    }

});