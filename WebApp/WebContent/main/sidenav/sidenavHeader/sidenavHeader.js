app.controller("SidenavHeaderCtrl",SHCtrl);

function SHCtrl($scope){
	var sidenavWidth = $("#leftSidenav").width();
	var sidenavHeight = $("#leftSidenav").height();
	$scope.username = $scope.getUser().name;
	if($scope.customs.userWallpaper=='default'){
			var pattern = Trianglify({
					width: sidenavWidth,
					height: sidenavHeight/4,
					variance: 1,
					x_colors: 'YlGnBu'
			});
			$scope.userWallpaper = pattern.png();
	} else {
		$scope.userWallpaper = $scope.customs.userWallpaper;
	}
	$scope.customs.userImage;
	$("#backgroundContainer").css("background-image", "url("+$scope.userWallpaper+")")
	
	
}
