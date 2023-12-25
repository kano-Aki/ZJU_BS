<template>
    <div>
        <div id='graph' style="width: 60vw;height:60vh;"></div>
    </div>
</template>

<script>
import * as echarts from 'echarts'
import axios from 'axios';


export default {
    name: 'HomePage',
    data() {
        return {
            num:[]
        }
    },
    methods:{
        drawChart(data) {
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('graph'));

            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: '我的设备总览'
                },
                tooltip: {},
                legend: {
                    data:['数量']
                },
                xAxis: {
                    data: ["设备总量","接收数据总量","告警信息总量"]
                },
                yAxis: {},
                series: [{
                    name: '数量',
                    type: 'bar',
                    data: data
                }]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        },
        resizeChart() {     //浏览器视图改变时，图表也改变大小
            var myChart=echarts.getInstanceByDom(document.getElementById('graph'));
            if(myChart){
                myChart.resize({
                    width: 0.6*window.innerWidth,
                    height: 0.6*window.innerHeight
                });
            }
        }
    },

    mounted() {     //mount时向后端请求数据，然后画图
        this.$http.get('/user/Navigation/homePage',{
            params:{
                token:this.$store.state.token
            }
            }).then( (response)=> {
                    console.log(response.data);
                    this.drawChart(response.data.data.graphData);
                })
                .catch(function (error) {
                    console.log(error);
                });
        window.addEventListener('resize', this.resizeChart);
    },

    beforeDestroy() {
        window.removeEventListener('resize', this.resizeChart);
    },
}
</script>

<style>

</style>