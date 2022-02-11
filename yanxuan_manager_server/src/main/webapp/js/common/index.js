var app = angular.module("yanxuan", ["ngRoute", "pagination"]);
// angular路由设置
app.config(["$routeProvider", function ($routeProvider) {
    $routeProvider.when("/", {
        templateUrl: "home.html"
    }).when("/goods/brand", {
        templateUrl: "pages/goods/brand.html",
        controller: "brandController"
    }).when("/goods/spec", {
        templateUrl: "pages/goods/spec.html"
    }).when("/goods/category/:pId", {
        templateUrl: "pages/goods/category.html"
    }).when("/goods/audit", {
        templateUrl: "pages/goods/audit.html"
    }).when("/seller/audit", {
        templateUrl: "pages/seller/audit.html"
    }).when("/seller/manage", {
        templateUrl: "pages/seller/manage.html"
    }).when("/ad/type", {
        templateUrl: "pages/ad/type.html"
    }).when("/ad/content", {
        templateUrl: "pages/ad/content.html"
    }).when("/ad/edit/", {
        templateUrl: "pages/ad/edit.html"
    }).when("/ad/edit/:id", {
        templateUrl: "pages/ad/edit.html"
    }).otherwise({redirectTo: '/'});
}]);

// brandController发送请求
app.controller("brandController", function ($http, $scope) {
    // 发送请求获取所有品牌信息
    $scope.queryAll = function () {
        $http.get("../../brand/queryAll")
            .then(
                function (value) {
                    console.log(value);
                    $scope.brandList = value.data;
                },
                function (reason) {
                    console.log(reason);
                }
            );
    };

    // 分页参数
    $scope.pageOption  = {
        total : 0 , // 总记录数
        currentPage : 1 , // 当前页码值，初始值为1
        pageSize : 10, // 每页显示的记录数，初始值为10
        pageSizeArr : [10, 20 ,30, 40, 50] , // 每页显示记录的选择数组
        onChange : function () {
        // 页码和每页显示记录数发生变化执行触发的业务逻辑，，可以用来请求数据查询的操作
            $scope.pageQuery();
        }
    };

    // 商品状态数组
    $scope.statusArray =["正常", "停用"];

    // 发送分页请求
    $scope.pageQuery = function() {
        // 页面加载时，初始化$scope.englishName
        if ($scope.englishName === undefined) {
            $scope.englishName = "";
        }
        $http.get("../../brand/pageQuery?currentPage="+$scope.pageOption.currentPage+"&pageSize="+$scope.pageOption.pageSize+"&englishName="+$scope.englishName)
            .then(
                function (value) {
                    // 总记录数
                    $scope.pageOption.total = value.data.total;
                    // 当前页数据
                    $scope.brandList = value.data.result;
                }
            );
    };

    // 页面加载时调用分页请求
    $scope.pageQuery();

    // 发送保存商品品牌信息请求
    $scope.save = function () {
        // 判断是新增保存还是修改保存
        if($scope.brand.id === undefined) {
            // 新增保存
            // 发送请求
            $http.post("../../brand/save", $scope.brand)
                .then(
                    function (value) {
                        // 保存成功
                        if (value.data.flag) {
                            alert(value.data.message);
                            // 关闭模态框
                            $("#newModal").modal("hide");
                            // 调用分页查询
                            $scope.pageQuery();
                        }
                    },
                    function (reason) {
                        // 保存失败
                        console.log(reason);
                    }
                );
        } else {
            // 修改保存
            $http.post("../../brand/update", $scope.brand)
                .then(
                    function (value) {
                        // 修改成功
                        if (value.data.flag) {
                            alert(value.data.message);
                            // 关闭模态框
                            $("#newModal").modal("hide");
                            // 调用分页查询
                            $scope.pageQuery();
                        }
                    },
                    function (reason) {
                        // 修改失败
                        console.log(reason);
                    }
                );
        }
    };

    // 初始化待修改的数据
    $scope.initData = function (brand) {
        $scope.brand = brand;
    }

    // 删除（逻辑删除）商品品牌信息
    $scope.delete = function (id) {
        // 发送请求
        $http.get("../../brand/delete?id="+id)
            .then(
                function (value) {
                    // 删除成功
                    if (value.data.flag) {
                        alert(value.data.message);
                        // 关闭模态框
                        $("#newModal").modal("hide");
                        // 调用分页查询
                        $scope.pageQuery();
                    }
                },
                function (reason) {
                    // 删除失败
                    console.log(reason);
                }
            );
    }

});