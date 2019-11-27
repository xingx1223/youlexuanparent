// 定义服务层:
app.service("brandService",function($http){

    //保存  ：添加和修改
    this.save = function(methodName,entity){
        return $http.post("../brand/"+methodName+".do",entity);
    }
    //查询所有
    this.findAll = function(){
        return $http.get("../brand/findAll.do");
    }
    //查询一个
    //$http.get('../brand/findOne.do?id='+id)
    this.findOne=function (id) {
        return $http.get('../brand/findOne.do?id='+id);
    }
    //删除
    //$http.get('../brand/delete.do?ids='+$scope.selectIds)
    this.dele = function(ids){
        return $http.get("../brand/delete.do?ids="+ids);
    }
    //搜索
    this.search = function(page,rows,searchEntity){
        return $http.post("../brand/search.do?page="+page+"&rows="+rows,searchEntity);
    }

    /*this.findPage = function(page,rows){
        return $http.get("../brand/findPage.do?page="+page+"&rows="+rows);
    }

    this.update=function(entity){
        return $http.post("../brand/update.do",entity);
    }

    this.findById=function(id){
        return $http.get("../brand/findById.do?id="+id);
    }*/

    this.selectOptionList = function(){
        return $http.get("../brand/selectOptionList.do");
    }
});