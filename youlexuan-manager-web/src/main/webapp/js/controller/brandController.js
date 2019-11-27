//品牌控制层
app.controller('brandController' ,function($scope,$http,$controller,brandService){

    //$controller也是angular提供的一个服务，可以实现伪继承，实际上就是与BaseController共享$scope
    $controller('baseController',{$scope:$scope});//继承父控制器

    //保存:修改JS的save方法
    $scope.save=function(){
        var methodName='add';//方法名称
        if($scope.entity.id!=null){//如果有ID
            methodName='update';//则执行修改方法
        }
        brandService.save(methodName,$scope.entity).success(
            function(response){
                if(response.success){
                    //重新查询
                    $scope.reloadList();//重新加载
                }else{
                    alert(response.message);
                }
            }
        );
    }

    //查询实体
    $scope.findOne=function(id){
        brandService.findOne(id).success(
            function(response){
                $scope.entity= response;
            }
        );
    }

    //批量删除
    $scope.dele=function(){
        //获取选中的复选框
        brandService.dele($scope.selectIds).success(
            function(response){
                if(response.success){
                    $scope.reloadList();//刷新列表
                }else{
                    alert($scope.message);
                }
            }
        );
    }

    //模糊查询
    $scope.searchEntity = {};//定义搜索对象
    //条件查询
    $scope.search = function(page, rows) {
        brandService.search(page,rows,$scope.searchEntity).success(function(response) {
            $scope.paginationConf.totalItems = response.total;//总记录数
            $scope.list = response.rows;//给列表变量赋值
        });
    }

/*
    //分页
    $scope.findPage=function(page,rows){
        $http.get('../brand/findPage.do?page='+page+'&rows='+rows).success(function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }*/
    /* //读取列表数据绑定到表单中：查询所有的品牌列表的方法:
        $scope.findAll=function(){
            //向后台发送请求：
            brandService.findAll().success(function(response){
                    $scope.list=response;
                }
            );
        }*/

    //下拉列表数据
    this.selectOptionList=function(){
        return $http.get('../brand/selectOptionList.do');
    }

});
