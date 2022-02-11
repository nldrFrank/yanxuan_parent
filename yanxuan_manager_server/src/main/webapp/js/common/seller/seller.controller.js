// 定义sellerController
angular.module("seller").controller("sellerController", function ( $scope, $controller, sellerService) {

    // 监听视图内容是否加载完毕，加载完毕后触发回调函数
    $scope.$on("$viewContentLoaded", function (event) {
        $scope.pageQuery();
    });

    // 继承其他的controller， baseController
    $controller("baseController", {$scope : $scope});

    $scope.pageQuery = function () {
        // 页面第一次加载时 $scope.name 为 undefined
        if ($scope.name === undefined) {
            $scope.name = "";
        }
        // 定义查询参数，查询未审核的商家信息
        var queryParams = {
            currentPage: $scope.pageOption.currentPage,
            pageSize: $scope.pageOption.pageSize,
            name : $scope.name,
            status : "0"
        };
        // 调用服务向后台发送 get 请求
        sellerService.get(queryParams).then(
            function (res) {
                // 总记录数
                $scope.pageOption.total = res.data.total;
                // 数据结果
                $scope.sellerList = res.data.result;
            }
        )
    };

    // 点击详情按钮触发
    $scope.initEntity = function (seller) {
        $scope.entity = seller;
    };

    // 更新状态
    $scope.updateStatus = function (id, status) {
        // 请求参数
        var entity = {
            id : id,
            status : status
        };
        // 调用服务向后台发送 put 请求
        sellerService.put(entity).then(
            function (res) {
                alert("状态修改成功")
                // 关闭模态窗口
                $("#newModal").modal("hide");
                // 刷新品牌列表
                $scope.pageQuery();
            }
        )
    }
});