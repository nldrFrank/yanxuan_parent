// 定义goodsController
angular.module("goods").controller("goodsManageController", function ( $scope, goodsService, $controller) {

    // 监听视图内容是否加载完毕，加载完毕后触发回调函数
    $scope.$on("$viewContentLoaded", function (event) {
        $scope.pageQuery();
    });

    // 继承其他的controller， baseController
    $controller("baseController", {$scope : $scope});

    // 商品的状态数组
    $scope.statusArray = ["待审核","审核通过","审核退回","删除"];

    /*$scope.statusArray = new Array();
    $scope.statusArray[0] = "待审核";
    $scope.statusArray[1] = "审核通过";
    $scope.statusArray[2] = "审核退回";
    $scope.statusArray[3] = "删除";*/
    // $scope.statusArray = new Array("待审核","审核通过","审核退回","删除");

    // 发送分页的请求
    $scope.pageQuery = function(){
        // 页面初始化时$scope.name == undefined
        if($scope.name === undefined){
            $scope.name ="";
        }
        // 定义查询参数
        var queryParams = {
            currentPage : $scope.pageOption.currentPage,
            pageSize : $scope.pageOption.pageSize,
            name : $scope.name,
            status : $scope.status
        };
        // 发送get请求
        goodsService.get(queryParams).then(
            function (value) {
                // 总记录数
                $scope.pageOption.total = value.data.total;
                // 当前页显示的数据
                $scope.goodsList = value.data.result;
            });
    };
    // 发送delete请求
    $scope.deleteGoodsInfo = function (id) {
        // 调用goodsService的delete方法去删除对应的记录
        goodsService.delete(id).then(
            function (value) {
                // 提示信息
                console.log(value);
                // 刷新页面数据
                $scope.pageQuery();
            }
        )
    }
});