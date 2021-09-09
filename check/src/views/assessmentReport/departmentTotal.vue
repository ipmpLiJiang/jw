<template>
  <div class="main-content" v-if="tableData.length > 0">
    <h2 class="title">{{tableData[0]['departmentname']}}人员对比报告</h2>
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
      <!-- <el-button
        type="primary"
        plain
        size="small"
      >下载到Word</el-button> -->
    </div>
    <div class="line"></div>
    <!-- <div class="box-data">
      <h4 class="bd-title">综合评分</h4>
      <Synthesis></Synthesis>
    </div> -->
    <div class="box-data">
      <h4 class="bd-title">维度得分</h4>
      <Dimensionality :data="tableData"></Dimensionality>
    </div>
  </div>
  <div class="none-data" v-else>该部门暂无评估报告！</div>
</template>

<script>
// import Synthesis from "./synthesisChart"; // 综合得分图表
import Dimensionality from "./dimensionalityChart"; // 维度得分图表
import { selectDeptReport } from "@/api/assessmentReport/index";
import qs from "qs";
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
      tableData:[]
    };
  },
  mounted() {},
  created() {
    this.getList();
  },
  components:{
    // Synthesis,
    Dimensionality
  },
  methods: {
    //查询列表
    getList() {
      new Promise((response, reject) => {
        selectDeptReport({
          dbtype: this.$store.state.user.user.dbtype
        })
          .then(response => {
            if (response.data.code == 0) {
              this.tableData = response.data.data;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
            this.tableLoading = false;
          })
          .catch(error => {
            reject(error);
          });
      });
    },
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
.none-data{
  text-align: center;
  color:#999;
  background: #fff;
  padding: 100px 0;

}
</style>
