<template>
  <div>
    <el-row>
      <el-col class="title">创建调查问卷名称</el-col>
      <el-col class="input">
        <el-input
          placeholder="请编辑问卷名称"
          v-model="questionnaireTitle"
          clearable
        >
        </el-input>
      </el-col>
      <el-col class="btn">
        <el-button
          type="warning"
          @click="back"
        >取消操作</el-button>
        <el-button
          type="primary"
          @click="next"
        >立即创建</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    return {
      questionnaireTitle: ""
    };
  },
  components: {},
  mounted() {},
  created() {},

  methods: {
    back() {
      this.$router.push({
        path: "/questionnaire",
        query: {}
      });
    },
    next() {
      if(!this.questionnaireTitle){
        this.$message.wanring("请先输入问卷名称");
        return;
      }
      this.$router.push({
        path: "/questionnaire/design",
        query: {
          title: this.questionnaireTitle
        }
      });
    }
  }
};
</script>


<style lang="scss" scoped>
.title {
  text-align: center;
  font-size: 22px;
  color: #409eff;
  margin: 10% 0px 20px 0px;
}
.input {
  text-align: center;
  .el-input {
    width: 350px;
  }
}
.btn {
  text-align: center;
  margin-top: 25px;
  .el-button:first-child {
    margin-right: 50px;
  }
}
</style>
