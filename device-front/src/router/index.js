import VueRouter from 'vue-router'
import Vue from 'vue'
import signup from '@/components/login/signup.vue'
import login from '@/components/login/login.vue'
import Navigation from '@/components/layout/Navigation.vue'

import HomePage from '@/components/content/HomePage.vue'
import DevInformation from '@/components/content/DevInformation.vue'
import NewDevice from '@/components/content/NewDevice.vue'
import DataQuery from '@/components/content/DataQuery.vue'
import EditDevice from '@/components/content/EditDevice.vue'
import Map from '@/components/content/Map.vue'
//将VueRouter设置为Vue的插件
Vue.use(VueRouter)
const router = new VueRouter({
    // 指定hash属性与组件的对应关系
    routes: [
        { path: '/', redirect: '/login'}, //重定向/到/discover，避免在进入到首页(即根目录)时，页面空白，相当于设置了一个首页
        { path: '/signup', component: signup },
        { path: '/login', component: login },
        {   
            path: '/user/Navigation', 
            component: Navigation,
            // 通过children属性，嵌套声明子路由
            children: [
                { path: '/user/Navigation/homePage', component: HomePage,meta:{breadcrumbName:"首页"}},
                { path: '/user/Navigation/myDevice/devInformation', component: DevInformation ,meta:{breadcrumbName:"设备信息"}},
                { path: '/user/Navigation/myDevice/newDevice', component: NewDevice ,meta:{breadcrumbName:"创建设备"}},
                { path: '/user/Navigation/dataQuery', component: DataQuery ,meta:{breadcrumbName:"数据查询"}},
                { path: '/user/Navigation/myDevice/editDevice', component: EditDevice ,meta:{breadcrumbName:"修改设备"}},
                { path: '/user/Navigation/myDevice/devInformation/map', component: Map ,meta:{breadcrumbName:"历史轨迹"}}
                ]
        
        }, 
        // { path: '/friends', component: Friends },//链接和组件的对应关系。当跳转到这个路径时，渲染对应的组件渲染到<router-view>标签上，本质一个占位符
        // {   
        //     path: '/my', 
        //     component: My,
        //     children: [
        //         {path:"music:id",component:Music,props:true},   //props:true表示将id作为props传递给Music组件，路径使用动态参数
        //         ]                                               //动态路由，复用代码
        // },
        // { path:'/hello',component:()=>import('@/components/axios_demo/Hello.vue')}
    ]
    })
    export default router

//在APP.vue中创建超链接
//在index.js中作映射