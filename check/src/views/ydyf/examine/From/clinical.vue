<template>
  <div>
    <h4 class="title">临床人员考核</h4>
    <el-card
      class="form-container"
      shadow="never"
    >
      <el-steps
        :active="active"
        finish-status="success"
        align-center
      >
        <el-step title="填写个人信息"> </el-step>
        <el-step title="填写本年度医德医风自评及工作总结"> </el-step>
        <el-step title="请对考评项目自我评分"> </el-step>
      </el-steps>

      <el-row
        class="active0"
        v-show="active === 0"
      >

        <infoFrom ref="child"></infoFrom>
        <el-row class="step">
          <el-button
            type="primary"
            style="margin-top: 12px"
            @click="next"
          >下一步</el-button>
        </el-row>
      </el-row>

      <el-row
        class="active1"
        v-show="active === 1"
      >
        <work ref="text"></work>
        <el-row class="step">
          <!-- <el-button type="warning" style="margin-top: 12px" @click="stepChange"
            >上一步</el-button
          > -->
          <span style="color:#999;">友情提示：为避免意外情况总结丢失，请不定时保存一下哦！</span>
          <el-button
            type="primary"
            style="margin-top: 12px"
            @click="saveSummary"
          >保存总结</el-button>
          <el-button
            type="primary"
            style="margin-top: 12px"
            @click="textNext"
          >下一步</el-button>
        </el-row>
      </el-row>

      <el-row
        class="active2"
        v-show="active === 2"
      >
        <score ref="From"></score>
        <el-row class="step">
          <!-- <el-button type="warning" style="margin-top: 12px" @click="stepChange"
            >上一步</el-button
          > -->
          <el-button
            type="primary"
            style="margin-top: 12px;width: 120px;"
            @click="SubmitFrom"
          >提交</el-button>
        </el-row>
        <!-- <el-row class="count">
          <h3>实时评优百分比：</h3>
         
         <el-progress type="circle" :percentage="25" ></el-progress>
        </el-row> -->
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { updateUserPassword } from "@/api/user/user";
import AddQuarter from "../../user/addQuarter";
import infoFrom from "../../common/infoFrom";
import { MessageBox } from "element-ui";
import {
  initInfo,
  updateSelfSummary,
  updateBaseInfo,
  submitScore,
  updateSelfSummaryTemp,
} from "../../api/Form/index";
import work from "../../common/workSummary";
import score from "../../common/score";
import qs from "qs";
export default {
  data() {
    return {
      form: {
        // name: "",
        // sex: "",
        // date: "",
        // station: "",
        // status: "",
        // workingTtime: "",
        // job: "",
        // text: "",
      },
      dialogVisible: true,
      forms: {},
      // getList: {},
      active: 0,
      PermissionID: "",
    };
  },
  components: {
    AddQuarter,
    work,
    infoFrom,
    score,
  },
  mounted() {},
  created() {
    // this.getList()
  },
  methods: {
    childClose(val) {
      this.dialogVisible = val;
      this.messageDialogVisible = val;
    },
    next() {
      if (!this.$refs.child.validateForm()) {
        this.$message.warning("请填写完整表单");
        return;
      }
      this.$confirm(
        "请确认已填写的个人信息,提交后无法修改, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          let data = this.$refs.child.form;
          new Promise((response, reject) => {
            updateBaseInfo(qs.stringify(data))
              .then((response) => {
                if (response.data.code == 0) {
                  if (this.active++ > 2) this.active = 0;
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error",
                  });
                }
              })
              .catch((error) => {
                reject(error);
              });
          });
        })
        .catch(() => {});
      //  if(!this.$refs['inform'].validateForm()){
      //    this.$message.warning("请填写完整表单")
      //  }else{
      //   if (this.active++ > 2) this.active = 0;
      //     }
    },
    stepChange() {
      if (this.active-- < 0) this.active = 0;
    },
    //保存总结
    saveSummary() {
      this.text = this.$refs.text.nextSubmit();
      let data = {
        selfSummary: this.text,
        userId: this.$store.state.user.user.moneycard,
      };
      new Promise((response, reject) => {
        updateSelfSummaryTemp(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.$message({
                message: "保存成功",
                type: "success",
              });
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //富文本下一步
    textNext() {
      this.text = this.$refs.text.nextSubmit();
      if (!this.text) {
        return;
      }
      this.$confirm(
        "请确认已填写的总结内容,提交后无法修改, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          let data = {};
          (data.selfSummary = this.text),
            (data.userId = this.$store.state.user.user.moneycard);
          new Promise((response, reject) => {
            updateSelfSummary(qs.stringify(data))
              .then((response) => {
                if (response.data.code == 0) {
                  if (this.active++ > 2) this.active = 0;
                  this.$refs.From.getlist();
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error",
                  });
                }
              })
              .catch((error) => {
                reject(error);
              });
          });
        })
        .catch(() => {});
    },
    //提交
    SubmitFrom() {
      let qData = this.$refs.From.questionData;
      let arr = [];
      for (let i = 0; i < qData.length; i++) {
        for (let j = 0; j < qData[i]["content"].length; j++) {
          qData[i]["content"][j].userId = this.$store.state.user.user.moneycard;
          arr.push(qData[i]["content"][j]);
        }
      }
          
          
      this.$refs.From.SubmitFrom();
   
    },

    getinfo() {
      this.PermissionID = this.$store.state.YdyfRole;
    },
  },
};
</script>


<style lang="scss" scoped>
.form-container {
  width: 1000px;
  margin: 0 auto;
  .step {
    float: right;
    // margin-top: 20px;
    // margin-bottom: 50px;
    margin: 20px 30px 50px 0;
  }
}
.title {
  height: 50px;
  line-height: 50px;
  border-bottom: 1px solid #eaeaea;
  padding: 0px 10px;
  font-weight: 600;
  color: #424242;
  background: #fff;
  margin-bottom: 20px;
  span {
    font-weight: normal;
    color: #9b9b9b;
  }
  i {
    margin: 0px 4px;
    color: #9b9b9b;
  }
}
.search {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  margin: 20px;
  border-radius: 4px;
  .el-form-item {
    margin: 0px;
  }
  .el-button {
    margin-left: 10px;
  }
}
.content {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  margin: 20px;
  border-radius: 4px;
  .el-pagination {
    margin: 20px auto;
    text-align: center;
  }
  .table-expand {
    padding: 0px;
    .el-form-item {
      margin-right: 0;
      margin-bottom: 10px;
      width: 33.33%;
    }
    .el-select {
      width: 185px;
    }
  }
}
.w200 {
  width: 260px;
}
.accessory {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  margin: 20px;
  border-radius: 4px;
  .el-pagination {
    margin: 20px auto;
    text-align: center;
  }
  .table-expand {
    padding: 0px;
    .el-form-item {
      margin-right: 0;
      margin-bottom: 10px;
      width: 33.33%;
    }
    .el-select {
      width: 185px;
    }
  }
}
.active0 {
  width: 100%;
  height: 100%;
  margin-top: 50px;

  .title {
    text-align: center;
    font-size: 20px;
  }
}
.active1 {
  width: 100%;
  height: 100%;
  margin-top: 50px;
  .title {
    text-align: center;
    font-size: 20px;
  }
}
.active2 {
  width: 100%;
  height: 100%;
  margin-top: 50px;
  .title {
    text-align: center;
    font-size: 20px;
  }
  .count {
    text-align: center;
    .el-progress-circle__path {
      fill: red;
    }
  }
}

.indicator {
  display: flex;
  flex-wrap: wrap;
  background: #fff;
  margin: 10px;
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  .li-title {
    width: 100%;
    margin-bottom: 20px;
    padding: 0 20px;
    .label {
      font-weight: 500;
      margin-bottom: 10px;
    }
    .core {
      width: 33%;
      display: inline-block;
    }
    .core-input {
      width: 180px;
      margin-top: 10px;
    }
    .title {
      line-height: 10px;
      background-color: #409eff;
      color: #fff;
      font-size: 16px;
      font-weight: 500;
      width: 100%;
    }
  }
  .title {
    font-size: 16px;
    font-weight: 500;
    padding-top: 20px;
  }
  li {
    width: 100%;
    margin-bottom: 20px;
    padding: 0 20px;
    .label {
      font-weight: 500;
      margin-bottom: 10px;
      margin-top: 20px;
    }
    .core {
      width: 20%;
      display: inline-block;
    }
    .core-input {
      width: 150px;
    }
  }
}
</style>
