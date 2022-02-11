// 定义searchController
angular.module("yanxuan").controller("searchController", function ( $scope, searchService) {

    // 发送分页的请求
    $scope.search = function(){
        searchService.get($scope.queryParams)
            .then(
                function (value) {
                    // 当前页显示的商品数据
                    $scope.dataList = value.data.result;
                    // 遍历设置picUrl
                    $scope.dataList.forEach(
                        function (element) {
                            element.picUrl = JSON.parse(element.picUrl);
                        }
                    );
                    // 类目信息
                    $scope.categoryList = value.data.category;
                    // 品牌信息
                    $scope.brandList = value.data.brandList;
                    // 规格信息
                    $scope.specList = value.data.specList;
                }
            );
    };

    /**
     * {
     *      keywords: '',
     *      category:'',
     *      brand:'',
     *      price: '',
     *      spec: {
     *          '机身内存':'256GB',
     *          '运行内存':'8GB'
     *      }
     * }
     * @param key
     * @param value
     */
    // 添加查询条件
    $scope.addSearchOption = function (key, value) {
        if($scope.queryParams.spec === undefined){
            $scope.queryParams.spec = {};
        }

        console.log(key);
        // 向查询条件中添加类目、品牌的信息
        if(key ==='category' || key ==="brand" || key==="price"){
            $scope.queryParams[key] = value;
        }else{
            $scope.queryParams.spec[key] = value;
        }

        // 发送请求
        $scope.search();
    };

    // 移除查询条件
    $scope.removeSearchOption = function (key) {
        if(key ==='category' || key ==='brand' || key==="price"){
            delete $scope.queryParams[key];
        }else{
            delete $scope.queryParams.spec[key];
        }

        // 发送请求
        $scope.search();
    }

});