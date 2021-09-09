<template>
  <div class="view-page">
    <el-button type="primary" class='view-btn' @click="open()">预览</el-button>
    <el-row class="view-content" v-if="show">
      <el-col :span='24' class="content">
        <div class="phone">
          <div class="phone-content">

          </div>

        </div>
        <div class="close-btn">
          <el-button type="primary"  @click="close()">关闭预览</el-button>
        </div>
      </el-col>
      <el-col :span='24' class="bg"></el-col>

    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    return {
      show:false,
    };
  },
  
  mounted() {},
  created() {
  },
  methods: {
    open(){
      this.show = true
    },
    close(){
      this.show = false
    },
  },
  watch: {

  }
};
</script>

<style lang="scss" scoped>
.view-page{
  .view-btn{
    position: fixed;
    right:10%;
    top:100px;
    z-index:2;
  }
  .view-content{
    .content{
      position: fixed;
      left:0;
      top:0;
      width: 100%;
      height:100%;
      z-index:99;
      .phone{
        width: 450px;
        height: 800px;
        background: url("../../assets/img/phone-bg.png") no-repeat 0% 0%;
        background-size: 100% 100%;
        margin: 20px auto;
        .phone-content{
          position: relative;
          width: 328px;
          height: 651px;
          background: red;
          top: 42px;
          left: 61px;
          overflow-y: scroll;
        }
      }
      .close-btn{
        .el-button{
          position: fixed;
          left: 50%;
          margin-left: 220px;
          top: 100px;
          z-index: 99;
        }
      }
    }
    .bg{
      position: fixed;
      left:0;
      top:0;
      width: 100%;
      height:100%;
      z-index:3;
      background:rgba(0,0,0,0.5);
    }

  }
}
</style>

