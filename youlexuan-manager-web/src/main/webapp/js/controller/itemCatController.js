 //控制层 
app.controller('itemCatController' ,function($scope,$controller,itemCatService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}


    /*
当前显示的是哪一分类的列表，我们就将这个商品分类新增到这个分类下。
实现思路：我们需要一个变量去记住上级ID，在保存的时候再根据这个ID来新增分类
修改itemCatController.js,  定义变量
*/
    //保存的时候，用到此变量
    $scope.parentId=0;//上级ID
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
            $scope.entity.parentId=$scope.parentId;//赋予上级ID
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询
                    $scope.findByParentId($scope.parentId);//重新加载
		        	//$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds = [];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}


	
	// 根据父ID查询分类
	$scope.findByParentId =function(parentId){
        //查询时记录上级ID
        $scope.parentId=parentId;//记住上级ID

		itemCatService.findByParentId(parentId).success(function(response){
			$scope.list=response;
		});
	}

/*
当为1级，珠宝 为null，银饰 为null。
$scope.entity_1=null;
$scope.entity_2=null;
当为2级，珠宝 有值，银饰 为null。
$scope.entity_1=entity;
$scope.entity_2=null;
当为3级，珠宝 有值，银饰 有值。
$scope.entity_2=entity;
注意：$scope.entity_1 默认保留上级的值
*/
	// 定义一个变量记录当前是第几级分类
	$scope.grade = 1;//默认为1级


    //设置级别
	$scope.setGrade = function(value){
		$scope.grade = value;
	}

    //读取列表
	$scope.selectList = function(p_entity){
		
		if($scope.grade == 1){//如果为1级
			$scope.entity_1 = null;
			$scope.entity_2 = null;
		}
		if($scope.grade == 2){//如果为2级
			$scope.entity_1 = p_entity;
			$scope.entity_2 = null;
		}
		if($scope.grade == 3){//如果为3级
			$scope.entity_2 = p_entity;
		}
		
		$scope.findByParentId(p_entity.id);//查询此级下级列表
	}


	
	

    
});	
