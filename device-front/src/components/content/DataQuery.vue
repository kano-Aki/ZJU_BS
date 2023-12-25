<template>
    <div>
        <div style="margin: 2vh;"></div>
        <el-form  label-width="4.5vw" :model="dev" >
          <el-row>
            <el-col :span="12">
          <el-form-item label="设备ID">
            <el-input v-model="dev.id" ></el-input>
          </el-form-item>
            </el-col>
            <el-col :span="12">
          <el-form-item label="设备名称">
            <el-input v-model="dev.name"></el-input>
          </el-form-item>
            </el-col>
          </el-row>
          
          <el-row>
            <el-col :span="12">
          <el-form-item label="设备类型">
            <el-input v-model="dev.type"></el-input>
          </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
          <el-form-item label="日期">
                <el-date-picker
                  v-model="time"
                  type="datetimerange"
                  :picker-options="pickerOptions"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  align="right">
                </el-date-picker>
          </el-form-item>
            </el-col>
          </el-row>
          <el-form-item>
            <el-button type="primary" @click="query">查询</el-button>
            <el-button @click="reset">重置</el-button>
          </el-form-item>
        </el-form>

        <!-- <div style="border-bottom: 20px solid white;"></div>
        <div style="border-bottom: 1px solid #eee;"></div> -->
        <el-divider></el-divider>
        <el-table
            :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"  
            style="width: 100%">
            <el-table-column
              prop="deviceId"
              label="设备ID"
              width="columnWidth">
            </el-table-column>
            <el-table-column
              prop="info"
              label="上报信息"
              width="columnWidth">
            </el-table-column>
            <el-table-column
              prop="alert"
              label="是否告警"
              width="columnWidth">
            </el-table-column>
            <el-table-column
              prop="time"
              label="发送时间"
              width="columnWidth">
            </el-table-column>
            <!-- <el-table-column
              prop="data"
              label="数据"
              width="columnWidth">
            </el-table-column> -->
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
export default {
    data() {
      return {
        dev: {
            id: '',
            name: '',
            type: ''
        },
        time: '',
        formed_start_date: '',
        formed_end_date: '',//YYYY-MM-DD形式的日期

        tableData:[],
        columnWidth:'',

        currentPage: 1,
        pageSize: 9, //设置每页展示的条目数
        itemNum: 0,

        pickerOptions: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        },

      }
    },
    methods: {
        query() {
            this.$http.get("/user/Navigation/dataQuery",{
              params:{
                id: this.dev.id,
                name: this.dev.name,
                type: this.dev.type,
                start_date:this.formed_start_date,
                end_date:this.formed_end_date ,//若全为空则显示所有数据,
                token: this.$store.state.token
              }
                
            }).then(res => {
                this.tableData = res.data.data.data;
                this.itemNum = this.tableData.length;
                this.currentPage=1;//查询完成之后，页码重置为1
            }).catch(err => {
                console.log(err);
            })
        },
        reset() {
            this.dev.id = '';
            this.dev.name = '';
            this.dev.type = '';
            this.time = '';
            this.query();//重置后自动查询，显示所有数据   
        },
        formatDate(date) {
          const year = date.getFullYear();
          const month = (date.getMonth() + 1).toString().padStart(2, '0');
          const day = date.getDate().toString().padStart(2, '0');
          const hours = date.getHours().toString().padStart(2, '0');
          const minutes = date.getMinutes().toString().padStart(2, '0');
          const seconds = date.getSeconds().toString().padStart(2, '0');
          
          return `${year}/${month}/${day} ${hours}:${minutes}:${seconds}`;
        },

        handlePageChange(val) {   //val为页码
          console.log(`当前页: ${val}`);
          this.currentPage = val;
      },

        updateColumnWidth() {
          this.columnWidth = window.innerWidth / 6;
      }
    },

    watch: {  //监视time变量的变化，若改变则进行形式化
      time(newDate) {
        if(newDate===''){
          this.formed_start_date='';
          this.formed_end_date='';
        }
        else{
          this.formed_start_date=this.formatDate(newDate[0]);
          this.formed_end_date=this.formatDate(newDate[1]);
        }
        console.log(this.formed_start_date);
        console.log(this.formed_end_date);
      },
    },

    created() { //创建时直接调用得到数据
      this.query();
    },
    mounted() { //监听窗口大小变化，改变列宽
      this.updateColumnWidth();
      window.addEventListener('resize', this.updateColumnWidth);
    },
    beforeDestroy() {
      window.removeEventListener('resize', this.updateColumnWidth);
    },
}
</script>
  
<style scoped>

</style>