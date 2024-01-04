<template>
    <div>
        <el-table
            :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"  
            style="width: 100%">
            <el-table-column
              prop="id"
              label="ID"
              width="columnWidth">
            </el-table-column>
            <el-table-column
              prop="name"
              label="名称"
              width="columnWidth">
            </el-table-column>
            <el-table-column
              prop="type"
              label="类型"
              width="columnWidth">
            </el-table-column>
            <el-table-column label="操作">
            <template slot-scope="scope">
                <el-button
                size="mini"
                @click="handleEdit(scope.row)">编辑</el-button>
                <el-button
                size="mini"
                @click="handleMap(scope.row)">历史轨迹</el-button>
                <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.row)">删除</el-button>
            </template>
            </el-table-column>
        </el-table>

        <el-pagination
            @current-change="handlePageChange"
            background
            layout="prev, pager, next"
            :current-page="currentPage" 
            :page-size="pageSize"
            :total="itemNum">
        </el-pagination>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    data() {
      return {
        tableData: [],
        currentPage: 1,
        pageSize: 15, //设置每页展示的条目数
        itemNum: 0,
        columnWidth: '',
      }
    },
    methods: {
      handleEdit(row) {
        this.$store.commit('setDevInfo', row);//将要修改行的数据传入store，供editDevice使用
        this.$router.push('/user/Navigation/myDevice/editDevice');
        console.log(row);
      },
      handleDelete(row) {
        axios.delete('/user/Navigation/myDevice/delDevice',{
          params:{
            token:this.$store.state.token,
            id:row.id
          }
        }).then(res => {
          this.getDeviceList();//调用getDeviceList()方法，刷新页面
          this.$message({
                    showClose: true,
                    message: '删除成功',
                    type: 'success'
                });
        })
        console.log(row);
      },
      handleMap(row) {
        this.$store.commit('setDevInfo', row);//将要修改行的数据传入store，供map使用
        this.$router.push('/user/Navigation/myDevice/devInformation/map');
        console.log(row);
      },

      handlePageChange(val) {   //val为页码
        console.log(`当前页: ${val}`);
        this.currentPage = val;
      },

      getDeviceList() {
        axios.get('/user/Navigation/myDevice/devInformation',{
          params:{
            token:this.$store.state.token
          }
        }).then(res => {
          this.tableData = res.data.data.dev;
          this.itemNum = this.tableData.length;//计算总条目数
        })
      },
      updateColumnWidth() {
          this.columnWidth = window.innerWidth / 4;
      }
    },

    created() { //创建时直接调用得到数据
      this.getDeviceList();
    },
    mounted() {
      this.updateColumnWidth();
      window.addEventListener('resize', this.updateColumnWidth);
    },
    beforeDestroy() {
      window.removeEventListener('resize', this.updateColumnWidth);
    },

}
</script>

<style>

</style>