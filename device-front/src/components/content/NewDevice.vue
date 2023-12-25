<template>
    <div>
        <div style="margin: 2vh;"></div>
        <el-form  label-width="4.5vw" :model="dev">
          <el-form-item label="设备名称">
            <el-input v-model="dev.name"></el-input>
          </el-form-item>
          <el-form-item label="设备类型">
            <el-input v-model="dev.type"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">提交</el-button>
            <el-button @click="reset">重置</el-button>
          </el-form-item>
        </el-form>
    </div>
</template>

<script>
  export default {
    data() {
      return{
        dev:{
            name:'',
            type:''
        }
      }
    },
    methods: {
      onSubmit(){
        if(this.dev.name===''){
            this.$message({
                showClose: true,
                message: '设备名不能为空',
                type: 'error'
                });
        }
        else if(this.dev.type===''){
            this.$message({
                showClose: true,
                message: '设备类型不能为空',
                type: 'error'
                });
        }
        else{
            this.$http.post('/user/Navigation/myDevice/newDevice',{
                name:this.dev.name,
                type:this.dev.type,
                token:this.$store.state.token
            }).then(res=>{
              if(res.data.success){
                  this.$message({
                      showClose: true,
                      message: '创建成功',
                      type: 'success'
                  });
                  this.reset(); //创建完成之后，重置表单，然后跳转回设备信息页面
                  this.$router.push('/user/Navigation/myDevice/devInformation');
              }
              else if(res.data.message === 'name repeated') {
                this.$message({
                  showClose: true,
                  message: '设备名重复',
                  type: 'error'
                  });
              }
            })
        }
        },

        reset(){
            this.dev.name='';
            this.dev.type='';
        }
    }
  }
</script>

<style>

</style>