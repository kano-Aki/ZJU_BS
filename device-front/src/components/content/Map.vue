<template>
  <div id="container"></div>
</template>

<script>
import AMapLoader from '@amap/amap-jsapi-loader'
import axios from 'axios'
	window._AMapSecurityConfig = {
  		securityJsCode: 'c8021939ceaa7dd45b93ae3b0460c3fc'
	}

export default {
  data() {
    return {
      map: null,
      logisticsInfoList : []
    }
  },
  methods: {
    initMap() {
      AMapLoader.load({
        key: 'ebb04fd06825682d9f08a9cd3ccca6f7', // 申请好的Web端开发者Key，首次调用 load 时必填
        version: '2.0', // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
        plugins: [''], // 需要使用的的插件列表，如比例尺'AMap.Scale'等
        AMapUI: {
            version: '1.1', // AMapUI 的版本号
            plugins: [], // 需要加载的 AMapUI 插件列表
        },
      })
        .then(AMap => {
          this.map = new AMap.Map('container', {
            //设置地图容器id
            viewMode: '3D', //是否为3D地图模式
            zoom: 10, //初始化地图级别
            center: [119.9, 30.1] //初始化地图中心点位置
          });
          const self=this;//保存当前this值


          var path = self.logisticsInfoList.map(point => [point.lng, point.lat]);

            var polyline = new AMap.Polyline({
                path: path,
                strokeColor: "#FF0000",  // 线颜色
                strokeOpacity: 1,        // 线透明度
                strokeWeight: 3,         // 线宽
                strokeStyle: "solid",    // 线样式
                strokeDasharray: [10, 5] // 补充线样式
            });

            self.map.add(polyline);
            if (self.logisticsInfoList && self.logisticsInfoList.length >= 2) {
                    console.log('开始规划路径');
                    const list = [...self.logisticsInfoList];
                    // 取出 起点、终点、途径点
                    const start = list.shift();
                    const end = list.pop();

                    // 创建途径点数组
                    const waypoints = list.map(point => [point.lng, point.lat]);
                        
                    AMapUI.loadUI(['overlay/SimpleMarker'], function(SimpleMarker) {
                        var startColor,endColor;
                        if(start.alert==1) startColor='red';
                        else startColor='green';
                        if(end.alert==1) endColor='red';
                        else endColor='green';
                        
                        const startMarker = new SimpleMarker({
                            iconLabel: '起始',
                            iconStyle: startColor, // 自定义样式
                            map: self.map,
                            position: [start.lng, start.lat] // 基于起点经纬度坐标
                        });
                        const infoWindowStart = new AMap.InfoWindow({
                                content: `<div>${start.info}</div>`,  // info 是你想在信息窗口中显示的信息
                                offset: new AMap.Pixel(0, -30)
                        });
                        infoWindowStart.open(self.map, startMarker.getPosition());
                        startMarker.on('mouseover', function (e) {
                            infoWindowStart.open(self.map, startMarker.getPosition());
                        });
                        startMarker.on('mouseout', function (e) {
                            infoWindowStart.close();
                        });

                        const endMarker = new SimpleMarker({
                            iconLabel: 'now',
                            iconStyle: endColor, // 自定义样式
                            map: self.map,
                            position: [end.lng, end.lat]
                        });
                        const infoWindowEnd = new AMap.InfoWindow({
                                content: `<div>${end.info}</div>`,  // info 是你想在信息窗口中显示的信息
                                offset: new AMap.Pixel(0, -30)
                        });
                        infoWindowEnd.open(self.map, endMarker.getPosition());
                        endMarker.on('mouseover', function (e) {
                            infoWindowEnd.open(self.map, endMarker.getPosition());
                        });
                        endMarker.on('mouseout', function (e) {
                            infoWindowEnd.close();
                        });

                        list.forEach(point => {
                            if(point.alert==1){
                                const marker = new SimpleMarker({
                                    // 配置项
                                    iconLabel: '告警',
                                    position: [point.lng, point.lat],
                                    map: self.map,
                                    iconStyle: 'red'
                                });
                                const infoWindow = new AMap.InfoWindow({
                                    content: `<div>${point.info}</div>`,  // info 是你想在信息窗口中显示的信息
                                    offset: new AMap.Pixel(0, -30)
                                });

                                infoWindow.open(self.map, marker.getPosition());
                                marker.on('mouseover', function (e) {
                                    infoWindow.open(self.map, marker.getPosition());
                                });

                                marker.on('mouseout', function (e) {
                                    infoWindow.close();
                                });
                            }
                            else{
                                const marker = new SimpleMarker({
                                    // 配置项
                                    iconLabel: '正常',
                                    position: [point.lng, point.lat],
                                    map: self.map,
                                    iconStyle: 'blue'
                                });
                                const infoWindow = new AMap.InfoWindow({
                                    content: `<div>${point.info}</div>`,  // info 是你想在信息窗口中显示的信息
                                    offset: new AMap.Pixel(0, -30)
                                });

                                infoWindow.open(self.map, marker.getPosition());
                                marker.on('mouseover', function (e) {
                                    infoWindow.open(self.map, marker.getPosition());
                                });

                                marker.on('mouseout', function (e) {
                                    infoWindow.close();
                                });
                            }
                            
                        });
                    });
            }
        //   AMapUI.load(['ui/misc/PathSimplifier'], function(PathSimplifier) {
        //     var pathSimplifierIns = new PathSimplifier({
        //     zIndex: 300,
        //     map: self.map, //所属的地图实例
        //     getPath: function(pathData, pathIndex) {
        //         //返回轨迹数据中的节点坐标信息，[AMap.LngLat, AMap.LngLat...] 或者 [[lng|number,lat|number],...]
        //         return pathData.path;
        //     },
        //     getHoverTitle: function(pathData, pathIndex, pointIndex) {
        //         //返回鼠标悬停时显示的信息
        //         if (pointIndex >= 0) {
        //             //鼠标悬停在某个轨迹节点上
        //             return pathData.name + '，点:' + pointIndex + '/' + pathData.path.length;
        //         }
        //         //鼠标悬停在节点之间的连线上
        //         return pathData.name + '，点数量' + pathData.path.length;
        //     },
        //     renderOptions: {
        //         //轨迹线的样式
        //         pathLineStyle: {
        //             strokeStyle: 'red',
        //             lineWidth: 6,
        //             dirArrowStyle: true
        //         }
        //     }
        //     });
        //     let path = self.logisticsInfoList.map(message => [message.lng, message.lat]);
        //     pathSimplifierIns.setData([{
        //         name: '轨迹0',
        //         path: path
        //     }]);
        //      var navg0 = pathSimplifierIns.createPathNavigator(0, //关联第1条轨迹
        //     {
        //         loop: true, //循环播放
        //         speed: 1000000
        //     });

        //     navg0.start();
        //   });

          // 加载插件 - AMap.Drivin
            // AMap.plugin('AMap.Driving', function () {
            //     // 插件加载完成后，初始化 Driving 实例
            //     const driving = new AMap.Driving({
            //         map: self.map,
            //         autoFitView: false,
            //         hideMarkers: true
            //     });// 使用上面得到的地图实例，表示，路径是画在我们当前初始化的这个地图上的
            //     // 得到 Driving 实例之后，就可以使用 driving.search() 方法，通过起点和终点绘制路径
            //     // 此处最好不要使用 logisticsInfoList 响应式数据进行地图绘制；
            //     // 重新声明一个变量，复制一份经纬度数据
            //     // 有可能我们的经纬度数据是没有的，所以在这块需要进行判断，并且必须 logisticsInfoList.value.length >= 2（必须要有起点 和 终点坐标）
            //     if (self.logisticsInfoList && self.logisticsInfoList.length >= 2) {
            //         console.log('开始规划路径');
            //         const list = [...self.logisticsInfoList];
            //         // 取出 起点、终点、途径点
            //         const start = list.shift();
            //         const end = list.pop();

            //         // 创建途径点数组
            //         const waypoints = list.map(point => [point.lng, point.lat]);
                        
            //         AMapUI.loadUI(['overlay/SimpleMarker'], function(SimpleMarker) {
            //             if(start.alert==1){
            //                 const startMarker = new SimpleMarker({
            //                     iconLabel: '起始',
            //                     iconStyle: 'red', // 自定义样式
            //                     map: self.map,
            //                     position: [start.lng, start.lat] // 基于起点经纬度坐标
            //                 });
            //             }
            //             else{
            //                 const startMarker = new SimpleMarker({
            //                     iconLabel: '起始',
            //                     iconStyle: 'green', // 自定义样式
            //                     map: self.map,
            //                     position: [start.lng, start.lat] // 基于起点经纬度坐标
            //                 });
            //             }
                        
            //             if(end.alert==1){
            //                 const endMarker = new SimpleMarker({
            //                     iconLabel: 'now',
            //                     iconStyle: 'red', // 自定义样式
            //                     map: self.map,
            //                     position: [end.lng, end.lat]
            //                 });
            //             }
            //             else{
            //                 const endMarker = new SimpleMarker({
            //                     iconLabel: 'now',
            //                     iconStyle: 'green', // 自定义样式
            //                     map: self.map,
            //                     position: [end.lng, end.lat]
            //                 });
            //             }
                        
            //             list.forEach(point => {
            //                 if(point.alert==1){
            //                     const marker = new SimpleMarker({
            //                         // 配置项
            //                         iconLabel: '告警',
            //                         position: [point.lng, point.lat],
            //                         map: self.map,
            //                         iconStyle: 'red'
            //                     });
            //                     const infoWindow = new AMap.InfoWindow({
            //                     content: `<div>${point.info}</div>`,  // info 是你想在信息窗口中显示的信息
            //                     offset: new AMap.Pixel(0, -30)
            //                     });

            //                     infoWindow.open(self.map, marker.getPosition());
            //                     marker.on('mouseover', function (e) {
            //                         infoWindow.open(self.map, marker.getPosition());
            //                     });

            //                     marker.on('mouseout', function (e) {
            //                         infoWindow.close();
            //                     });
            //                 }
            //                 else{
            //                     const marker = new SimpleMarker({
            //                         // 配置项
            //                         iconLabel: '正常',
            //                         position: [point.lng, point.lat],
            //                         map: self.map,
            //                         iconStyle: 'blue'
            //                     });
            //                     const infoWindow = new AMap.InfoWindow({
            //                     content: `<div>${point.info}</div>`,  // info 是你想在信息窗口中显示的信息
            //                     offset: new AMap.Pixel(0, -30)
            //                     });

            //                     infoWindow.open(self.map, marker.getPosition());
            //                     marker.on('mouseover', function (e) {
            //                         infoWindow.open(self.map, marker.getPosition());
            //                     });

            //                     marker.on('mouseout', function (e) {
            //                         infoWindow.close();
            //                     });
            //                 }
                            
            //             });
            //             // const marker = new AMap.Marker({
            //             //     position: [point.lng, point.lat],
            //             //     map: self.map
            //             // });
            //         });
            //         driving.search(
            //             [start.lng, start.lat],
            //             [end.lng, end.lat],
            //             {
            //                 waypoints: waypoints,
            //             },
            //             function(result) {
            //                 // 规划完毕
            //                 console.log(result);
            //             }
            //         );
            //     };
            // });
        });
    },
  },
  mounted() {
    //DOM初始化完成进行地图初始化
    axios.get('/user/Navigation/myDevice/devInformation/map',{
      params:{
        id:this.$store.state.dev_id,
        token:this.$store.state.token
      }
    }).then(res => {
        if(res.data.success){
            this.logisticsInfoList = res.data.data.messages;
            this.initMap()
        }
    })
  }
}
</script>

<style lang="less">
#container {
  padding: 0px;
  margin: 0px;
  width: 100%;
  height: 100%;
}
</style>

