<template>
  <div>
    <el-container style="height: 100vh; border: 1px solid #eee">
            <el-aside width="9vw" style="background-color: #545c64">
                <el-menu :default-openeds="['1', '3']" background-color="#545c64"
                  text-color="#fff" active-text-color="#ffd04b" :default-active="$route.path">
                  <el-menu-item index="/user/Navigation/homePage" @click="homePage" class="custom-menu">
                      <template slot="title" ><i class="el-icon-s-home"></i>首页</template>
                  </el-menu-item>
                  <el-submenu index="2" >
                      <template slot="title" ><i class="el-icon-menu"></i>我的设备</template>
                      <el-menu-item index="/user/Navigation/myDevice/devInformation" @click="devInformation" class="custom-menu">设备信息</el-menu-item>
                      <el-menu-item index="/user/Navigation/myDevice/newDevice" @click="newDevice" class="custom-menu">创建设备</el-menu-item>
                  </el-submenu>
                  <el-menu-item index="/user/Navigation/dataQuery" @click="dataQuery" class="custom-menu">
                      <template slot="title"><i class="el-icon-search"></i>数据查询</template>
                  </el-menu-item>
                </el-menu>
            </el-aside>
            
            <el-container>
                <el-header style="text-align: right; font-size: 1.8vh" class="header">
                  <el-breadcrumb separator="/" class="breadcrumb">
                    <el-breadcrumb-item v-for="(route,index) in breadcrumbName" :key="index">
                        {{ route }}
                    </el-breadcrumb-item>
                  </el-breadcrumb>
                  <el-dropdown @command="handleCommand">
                      <span class="el-dropdown-link" style="margin-right: 1.8vh">
                        设置<i class="el-icon-arrow-down el-icon--right"></i>
                      </span>
                      <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item command="login">登出</el-dropdown-item>
                      <el-dropdown-item command="signup">注册新账号</el-dropdown-item>
                      </el-dropdown-menu>
                  </el-dropdown>
                  <span>{{this.$store.state.username}}</span>
                </el-header>
                
                <el-main class="main"> <!-- 显示内容  -->
                  <router-view></router-view> <!-- 路由占位符 -->
                </el-main>
            </el-container>
    </el-container>
  </div>
</template>

<script>
  export default {
    name: 'Navigation',

    methods: {
      homePage() {
        if(this.$route.path !== '/user/Navigation/homePage') //避免重复push
          this.$router.push('/user/Navigation/homePage');
      },
      devInformation() {
        if(this.$route.path !== '/user/Navigation/myDevice/devInformation')
          this.$router.push('/user/Navigation/myDevice/devInformation');
      },
      newDevice() {
        if(this.$route.path !== '/user/Navigation/myDevice/newDevice')
          this.$router.push('/user/Navigation/myDevice/newDevice');
      },
      dataQuery() {
        if(this.$route.path !== '/user/Navigation/dataQuery')
          this.$router.push('/user/Navigation/dataQuery');
      },

       handleCommand(command) {
        this.$store.commit('reset');
        console.log(this.$store.state);
        this.$router.push('/'+command);
      },
    },


    computed: {
      breadcrumbName() {  //通过当前路由路径得到各面包屑值
        let str = this.$route.path;
        let target = "/";
        let count = str.split(target).length - 1;
        if(count === 3) { // /homePage
          return [this.$route.matched[1].meta.breadcrumbName];
        }
        else if(count === 4){
          return ["我的设备",this.$route.matched[1].meta.breadcrumbName];
        }
        else if(count==5){
          return ["我的设备","设备信息",this.$route.matched[1].meta.breadcrumbName];
        }
      }
    },
  };
</script>

<style scoped>
  .el-header {
    background-color: #B3C0D1;
    color: #333;
    line-height: 7vh;
  }
  
  .el-aside {
    color: #545c64; 
  }

  .header {
  position: relative;
}

.breadcrumb {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
}

.breadcrumb /deep/ .el-breadcrumb__inner {
  color: #333; /* 设置字的颜色为黑色 */
}

.breadcrumb /deep/ .el-breadcrumb__separator {
  color: #333; /* 设置/的颜色为黑色 */
}

/* .custom-menu {
  font-size: 0.828vw; //设置菜单字体大小
} */

</style>