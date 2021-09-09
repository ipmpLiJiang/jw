<template>
  <div class="container">
    <!-- <el-row>
      <img
        src="../../assets/img/demo.jpg"
        width="1200"
        style="margin-top:20px"
        alt=""
      >
    </el-row> -->
    <el-row>
      <el-col
        class="code"
        :span="6"
        :offset="9"
      >
        <img src="../../assets/img/code.png" />
        <p>链接二维码</p>
      </el-col>
      <el-col class="link">
        <el-col class="message">考核链接</el-col>
        <el-input
          v-model="input"
          placeholder="请输入内容"
        ></el-input>
        <el-button type="primary">复制链接</el-button>
      </el-col>
      <el-col class="pwd">
        <el-col class="message">设置密码提示语</el-col>
        <el-input
          v-model="input2"
          :maxlength="20"
          placeholder="请输入内容"
        ></el-input>
        <el-button type="primary">保存提示</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    return {
      active: 2,
      value8: 25,
      value9: 25,
      value10: 25,
      value11: 25,
      centerDialogVisible: false
    };
  },
  components: {},
  mounted() {},
  created() {},

  methods: {
    result() {
      this.$router.push({
        path: "/detail/questionnaire/send",
        query: {}
      });
    },
    formatTooltip(val) {
      return val + "%";
    },
    editQuan() {
      this.centerDialogVisible = true;
    },
    apply() {
      if (this.value8 + this.value9 + this.value10 + this.value11 == "100") {
        this.centerDialogVisible = false;
      } else {
        this.$message({
          message: "权重分配总分为100%，请检查您的分配比例",
          type: "error"
        });
      }
    }
  }
};
</script>


<style lang="scss" scoped>
.container {
  width: 1200px;
  margin: 0 auto;
}
.code {
  background: #fafafa;
  border-radius: 5px;
  img {
    width: 60%;
    margin: 20px 20% 20px 20%;
  }
  p {
    text-align: center;
    height: 30px;
    line-height: 30px;
    background: #f4f4f4;
    border-radius: 0px 0px 5px 5px;
  }
}
.link {
  text-align: center;
  margin-top: 20px;
  .message {
    margin-left: 8%;
    text-align: left;
    margin-bottom: 10px;
  }
  .el-input {
    width: 70%;
    margin-right: 10px;
  }
}
.pwd {
  text-align: center;
  margin-top: 20px;
  .message {
    margin-left: 8%;
    text-align: left;
    margin-bottom: 10px;
  }
  .el-input {
    width: 70%;
    margin-right: 10px;
  }
}
</style>
