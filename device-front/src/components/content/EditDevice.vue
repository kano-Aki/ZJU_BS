<template>
    <div>
        <div style="margin: 2vh;"></div>
        <el-form  label-width="4.5vw" :model="dev">
          <el-form-item label="设备ID">
            <el-input v-model="dev.id" :disabled="true" ></el-input>
            <!-- ID为添加设备时，自动生成的，不允许修改 -->
          </el-form-item>
          <el-form-item label="设备名称">
            <el-input v-model="dev.name"></el-input>
          </el-form-item>
          <el-form-item label="设备类型">
            <el-input v-model="dev.type"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">提交</el-button>
            <el-button @click="back">返回</el-button>
          </el-form-item>
        </el-form>
    </div>
</template>

<script>
import axios from 'axios';
  export default {
    name:'EditDevice',
    data() {
      return{
        dev:{
            id:'',
            name:'',
            type:''
        }
      };
    },
    methods: {
      onSubmit(){
        if(this.name===''){
            this.$message({
                showClose: true,
                message: '设备名不能为空',
                type: 'error'
                });
        }
        else if(this.type===''){
            this.$message({
                showClose: true,
                message: '设备类型不能为空',
                type: 'error'
                });
        }
        else{
            axios.put('/user/Navigation/myDevice/editDevice',{
                id:this.dev.id,
                name:this.dev.name,
                type:this.dev.type,
                token:this.$store.state.token
            }).then(res=>{
            if(res.data.success){
                this.$message({
                    showClose: true,
                    message: '修改成功',
                    type: 'success'
                });
                this.$router.push('/user/Navigation/myDevice/devInformation');
            }
            else{
                this.$message({
                    showClose: true,
                    message: '设备名称重复',
                    type: 'error'
                });
            }
            })
        }
        },

        back(){
            this.$router.push('/user/Navigation/myDevice/devInformation');
        }
    },

    created(){  //页面加载时，获取当前要修改设备的信息
        this.dev.id = this.$store.state.dev_id;
        this.dev.name = this.$store.state.dev_name;
        this.dev.type = this.$store.state.dev_type;
        console.log(this.dev);
    }
  }
</script>

<style>

</style>