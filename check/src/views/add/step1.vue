<template>
  <div>
    <el-row>
      <el-col class="title">创建360°考核名称</el-col>
      <el-col class="input">
        <el-input
                placeholder="请编辑考核名称"
                v-model="input"
                clearable>
        </el-input>
      </el-col>
      <el-col class="btn">
        <el-button type="warning" @click="back">取消操作</el-button>
        <el-button type="primary" @click="next">立即创建</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
    export default {
        data() {
            return {
                input:'',
            };
        },
        components: {
        },
        mounted() {
        },
        created() {
        },

        methods: {
            back(){
                this.$router.push({
                    path: '/home',
                    query: {
                    }
                })
            },
            next(){
                this.$router.push({
                    path: '/detail/relationship',
                    query: {
                    }
                })
            }
        }
    };
</script>


<style lang="scss" scoped>
 .title{
   text-align: center;
   font-size:22px;
   color:#409EFF;
   margin:10% 0px 20px 0px;
 }
  .input{
    text-align: center;
    .el-input{
      width:350px;
    }
  }
  .btn{
    text-align: center;
    margin-top:25px;
    .el-button:first-child{
      margin-right:50px;
    }
  }
</style>
