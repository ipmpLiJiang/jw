<template>
  <div class="main-content">
    <h2 class="title">所有人员对比报告</h2>
    <div class="box-wrap">
      <!-- <el-select
        v-model="value"
        placeholder="请选择"
        class="box-select"
        size="small"
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select> -->
      <!-- <el-button
        plain
        size="small"
      >分享全部报告</el-button> -->
      <el-button
        type="primary"
        plain
        size="small"
      >下载到Word</el-button>
    </div>
    <div class="line"></div>
    <div class="box-data">
      <h4 class="bd-title">综合评分</h4>
      <Synthesis></Synthesis>
    </div>
    <div class="box-data">
      <h4 class="bd-title">维度得分</h4>
      <Dimensionality></Dimensionality>
    </div>
  </div>
</template>

<script>
import Synthesis from "./synthesisChart"; // 综合得分图表
import Dimensionality from "./dimensionalityChart"; // 维度得分图表
export default {
  data() {
    return {
      options: [
        {
          value: "选项1",
          label: "平均分"
        },
        {
          value: "选项2",
          label: "总分"
        }
      ],
      value: "平均分",
    };
  },
  mounted() {},
  created() {
    
  },
  components:{
    Synthesis,
    Dimensionality
  },
  methods: {
    login() {
      this.$router.push({
        path: "/home",
        query: {}
      });
    }
  }
};
</script>


<style lang="scss" scoped>
.main-content{
  background: #fff;
  padding: 20px;
}
.title {
  text-align: center;
  font-size: 18px;
  margin: 20px 0 10px;
}
.box-wrap {
  text-align: right;
  margin: 30px auto;
}
.box-select {
  margin-right: 10px;
}
.line {
  border-bottom: 1px dashed #d7d8d9;
}
.box-data {
  padding-top: 20px;
  .bd-title {
    color: #409eff;
    position: relative;
    font-weight: bold;
    padding-left: 10px;
    line-height: 1;
    text-align: left;
    margin-bottom: 20px;
    &::after {
      content: "";
      position: absolute;
      top: 0;
      left: 0;
      bottom: 0;
      width: 4px;
      background-color: #30a6f5;
      border-radius: 2px;
    }
  }
}
</style>
