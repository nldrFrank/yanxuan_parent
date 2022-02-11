// 定义goodsController
angular.module("goods").controller("goodsController", function ( $scope, goodsService, $controller, categoryService, $routeParams) {

    // 监听视图内容是否加载完毕，加载完毕后触发回调函数
    $scope.$on("$viewContentLoaded", function (event) {
        // 获取URL中的id值
        var id = $routeParams.id;
        // 如果id不是undefined，就进行查询
        if (id !== undefined) {
            $scope.queryGoodsInfoById(id);
        }
        $scope.querycategory1();
    });

    // 继承其他的controller， baseController
    $controller("baseController", {$scope : $scope});

    // 初始化规格信息
    $scope.entity = {
        specCheckedList : []
    };

    // 根据主键id进行查询商品的信息
    $scope.queryGoodsInfoById = function(id){
        // 根据主键ID
        goodsService.get(id).then(
            function (res) {
                $scope.entity = res.data;
                // 设置在富文本编辑器中显示详细信息
                editor.txt.html($scope.entity.detail);
                // 把$scope.entity.picUrl 转换成JSON对象
                $scope.entity.picUrl = JSON.parse($scope.entity.picUrl);
                // 转换的是sku信息中的内容
                $scope.entity.skuList.forEach(function (sku) {
                    sku.picUrl = JSON.parse(sku.picUrl);
                    sku.specs = JSON.parse(sku.specs);
                });
                // 选中的规格项
                $scope.entity.specCheckedList = JSON.parse($scope.entity.specCheckedList);

                if ($scope.entity.specCheckedList===null) {
                    $scope.entity.specCheckedList = [];
                }

            }
        );
    };

    // 检查规格项是否设置为选中的状态
    // 查看specCheckedList中是否有传递进来的规格项
    $scope.checkSpecOption = function(specName, optionValue){
        // 根据规格名称进行查询
        var obj = queryObject($scope.entity.specCheckedList, specName);
        if(obj === null){
            return false;
        }else{
            if(obj.optionValue.indexOf(optionValue)<0){
                return false;
            }else {
                return true;
            }
        }
    };

    // 加载一级目录
    $scope.querycategory1 = function () {
        categoryService.get({parentId: 0}).then(
            function (res) {
                $scope.category1List = res.data.result;
            }
        );
    };
    // 监听一级目录
    $scope.$watch("entity.category1Id", function (newVal) {
        if (newVal === undefined) {
            $scope.category2List = [];
            return null;
        }
        // 查询二级类目
        categoryService.get({parentId: newVal}).then(
            function (res) {
                $scope.category2List = res.data.result;
            }
        );
    });
    // 监听二级目录
    $scope.$watch("entity.category2Id", function (newVal) {
        if (newVal === undefined) {
            $scope.category3List = [];
            return null;
        }
        // 查询三级类目
        categoryService.get({parentId: newVal}).then(
            function (res) {
                $scope.category3List = res.data.result;
            }
        );
    });
    // 监听三级类目，加载关联的品牌信息
    $scope.$watch("entity.category3Id", function (newVal) { 
        if (newVal === undefined) {
            return null;
        }
        // 查询三级类目的所有信息
        categoryService.get(newVal).then(
            function (res) {
                // 所有品牌信息
                $scope.brandList = JSON.parse(res.data.relation.brandIds);
                // 规格和规格项信息
                $scope.specList = res.data.specList;
            }
        );
    });
    // 保存商品信息
    $scope.save = function () {
        // 把富文本编辑器中的数据保存到$scope.entity中
        $scope.entity.detail = editor.txt.html();
        // 把 $scope.entity.picUrl 转换成字符串
        $scope.entity.picUrl = JSON.stringify($scope.entity.picUrl);
        // 转换sku中的picUrl、specs
        $scope.entity.skuList.forEach(function (sku) {
            sku.picUrl = JSON.stringify(sku.picUrl);
            sku.specs = JSON.stringify(sku.specs);
        });
        // 转换选中的规格项
        $scope.entity.specCheckedList = JSON.stringify($scope.entity.specCheckedList);

        if ($scope.entity.id === undefined) {
            // 发送post请求新增商品信息
            goodsService.post($scope.entity).then(
                function (res) {
                    alert("商品信息新增成功");
                    // 跳转到商品列表页面
                    window.location="#/goods/manage";
                }
            );
        } else {
            goodsService.put($scope.entity).then(
                function (res) {
                    alert("商品信息修改成功");
                    // 跳转到商品列表页面
                    window.location="#/goods/manage";
                }
            );
        }

    };
    // 上传商品主图
    $scope.uploadSpuPic = function () {
        $.Tupload.init({
            // 请求映射
            url: "/upload",
            title	  : "商品主图片",
            fileNum: 5, // 上传文件数量
            divId: "goodsPic", // div  id
            accept: "image/jpeg,image/x-png", // 上传文件的类型
            preViewData: $scope.entity.picUrl,
            onSuccess: function(data, i) {
                console.log(data);
                $scope.entity.picUrl = data.data;
            },
            onDelete: function(i) {

            }
        });
    };
    // 上传规格图片
    $scope.uploadSkuPic = function (index) {
        $.Tupload.init({
            // 请求映射
            url: "/upload",
            title	  : "商品规格图片",
            fileNum: 5, // 上传文件数量
            divId: "skuPic", // div  id
            accept: "image/jpeg,image/x-png", // 上传文件的类型
            preViewData: $scope.entity.skuList[index].picUrl,
            onSuccess: function(data, i) {
                console.log(data);
                $scope.entity.skuList[index].picUrl = data.data;
                console.log($scope.entity.skuList);
            },
            onDelete: function(i) {

            }
        });
    };
    // 销毁图片上传的插件
    $scope.destroyUpload = function (id) {
        $.Tupload.destroy({
            divId: id
        });
    };
    // 在集合中查询对应的对象
    var queryObject = function(list, specName) {
        var result = null;
        list.forEach(function (element) {
            if (element.specName === specName) {
                result = element;
            }
        });
        return result;
    };
    /**
     *
     * @param specName 规格名称
     * @param optionValue 选项值
     */
    $scope.getSpecCheckedList = function (event, specName, optionValue) {
        // 根据规格名称查询
        var obj = queryObject($scope.entity.specCheckedList, specName);
        // 规格名称在 $scope.entity.specCheckedList 中不存在
        if (obj === null) {
            $scope.entity.specCheckedList.push({
                specName : specName,
                optionValue : [
                    optionValue
                ]
            });
        } else {
            // 规格名称在 $scope.entity.specCheckedList 中存在
            // 规格名称存在时，只需要添加/删除选项信息
            // 选中时添加
            if (event.target.checked) {
                obj.optionValue.push(optionValue);
            } else {
                // 取消选中时移除
                obj.optionValue.splice(obj.optionValue.indexOf(optionValue), 1);
                if (obj.optionValue.length === 0) {
                    $scope.entity.specCheckedList.splice($scope.entity.specCheckedList.indexOf(obj), 1);
                }
            }
        }
        // 调用生成Sku的方法
        createSkuList();
        /*// 当规格信息中没有数据时（首次选或重选），添加规则如下
        if ($scope.entity.specCheckedList.length === 0){
            $scope.entity.specCheckedList.push({
                specName : specName,
                optionValue : [
                    optionValue
                ]
            });
        } else {
            var flag = true;
            // 不是首次选中数据时，遍历规格信息并判断其中是否存在相应的值
            $scope.entity.specCheckedList.forEach(function (value) {
                // 规格名称存在时，只需要添加/删除选项信息
                if (value["specName"] === specName) {
                    // 选中时添加
                    if (event.target.checked) {
                        value.optionValue.push(optionValue);
                    } else {
                        // 取消选中时移除
                        value.optionValue.splice(value.optionValue.indexOf(optionValue), 1);
                        if (value.optionValue.length === 0) {
                            $scope.entity.specCheckedList.splice(index, 1);
                        }
                    }
                    flag = false;
                }
            });

            if (flag) {
                $scope.entity.specCheckedList.push({
                    specName : specName,
                    optionValue : [
                        optionValue
                    ]
                });
            }
        }*/
    };
    // 根据选择的规格项生成SKU信息
    var createSkuList = function () {
        // 初始化SkuList
        $scope.entity.skuList = [{specs:{}, price:0, stockCount:999, picUrl:[]}];

        $scope.entity.specCheckedList.forEach(function (checkedSpec) {
            // 获取规格名称
            var specName = checkedSpec.specName;
            // 定义一个临时集合
            var tmpSkuList = [];
            // 遍历选中规格的规格项
            checkedSpec.optionValue.forEach(function (optionValue) {
                // 遍历skuList
                $scope.entity.skuList.forEach(function (sku) {
                    // 每次设置的值必须是一个新对象
                    var newSku = JSON.parse(JSON.stringify(sku));
                    newSku.specs[specName] = optionValue;
                    // 新对象放入临时集合中
                    tmpSkuList.push(newSku);
                });
            });
            // 临时变量存入skuList
            $scope.entity.skuList = tmpSkuList;
        });
    };
});