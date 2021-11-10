<template>
  <div class="question">
    <el-row>
      <el-col class="title">精神卫生中心360°考核系统</el-col>
      <el-col class="content">
        <el-col class="toptic-title"> 基本信息 </el-col>
        <el-col>
          <div class="season-text">
            <div class="form-msg">
              <span>姓名：</span>
              <span>{{ detailData.username }}</span>
            </div>
            <div class="form-msg">
              <span>部门：</span>
              <span>{{ detailData.departmentname }}</span>
            </div>
            <div class="form-msg">
              <span>岗位：</span>
              <span>{{ detailData.stationname }}</span>
            </div>
          </div>
        </el-col>
        <el-col
          class="toptic-title"
          v-if="dutyJichu.length == 0 ? false : true"
        >
          基础指标
        </el-col>
        <el-col
          class="list"
          v-for="(item, index) in dutyJichu"
          :key="'a' + index"
        >
          <el-col class="question-title">
            <el-col :span="24" class="toptic"
              ><span v-html="item.dutyname"></span
            ></el-col>
          </el-col>
          <el-col class="question-title">
            <el-col :span="24" class="toptic">
              <font style="color: red"> * </font>自评：
              <el-input
                v-show="detailData.isedit == 0 ? true : false"
                style="width: 82%"
                maxlength="80"
                rows="3"
                show-word-limit
                type="textarea"
                v-model="item.zpsm"
              ></el-input>
              <span v-show="detailData.isedit == 1 ? true : false">{{
                item.zpsm
              }}</span>
            </el-col>
          </el-col>
        </el-col>
        <el-col
          class="toptic-title"
          style="margin-top: 0.5rem"
          v-if="dutyYiban.length == 0 ? false : true"
        >
          岗位职责
        </el-col>
        <el-col
          class="list"
          v-for="(item, index) in dutyYiban"
          :key="'b' + index"
        >
          <el-col class="question-title">
            <el-col :span="24" class="toptic"
              ><span v-html="item.dutyname"></span
            ></el-col>
          </el-col>
          <el-col class="question-title">
            <el-col :span="24" class="toptic">
              <font style="color: red"> * </font>自评：
              <el-input
                v-show="detailData.isedit == 0 ? true : false"
                style="width: 82%"
                maxlength="80"
                rows="3"
                show-word-limit
                type="textarea"
                v-model="item.zpsm"
              ></el-input>
              <span v-show="detailData.isedit == 1 ? true : false">{{
                item.zpsm
              }}</span>
            </el-col>
          </el-col>
        </el-col>
        <el-col
          class="toptic-title"
          style="margin-top: 0.5rem"
          v-if="dutyZhongdian.length == 0 ? false : true"
        >
          重点任务
        </el-col>
        <el-col
          class="list"
          v-for="(item, index) in dutyZhongdian"
          :key="'c' + index"
        >
          <el-col class="question-title">
            <el-col :span="24" class="toptic"
              ><span v-html="item.dutyname"></span
            ></el-col>
          </el-col>
          <el-col class="question-title">
            <el-col :span="24" class="toptic">
              <font style="color: red"> * </font>自评：
              <el-input
                v-show="detailData.isedit == 0 ? true : false"
                style="width: 82%"
                maxlength="80"
                rows="3"
                show-word-limit
                type="textarea"
                v-model="item.zpsm"
              ></el-input>
              <span v-show="detailData.isedit == 1 ? true : false">{{
                item.zpsm
              }}</span>
            </el-col>
          </el-col>
        </el-col>
        <el-col
          class="toptic-title"
          style="margin-top: 0.5rem"
          v-if="dutyMubiao.length == 0 ? false : true"
        >
          目标任务
        </el-col>
        <el-col
          class="list"
          v-for="(item, index) in dutyMubiao"
          :key="'d' + index"
        >
          <el-col class="question-title">
            <el-col :span="24" class="toptic"
              ><span v-html="item.dutyname"></span
            ></el-col>
          </el-col>
          <el-col class="question-title">
            <el-col :span="24" class="toptic">
              <font style="color: red"> * </font>自评：
              <el-input
                v-show="detailData.isedit == 0 ? true : false"
                style="width: 82%"
                maxlength="80"
                rows="3"
                show-word-limit
                type="textarea"
                v-model="item.zpsm"
              ></el-input>
              <span v-show="detailData.isedit == 1 ? true : false">{{
                item.zpsm
              }}</span>
            </el-col>
          </el-col>
        </el-col>
      </el-col>
      <el-col class="edit-btn" v-show="detailData.isedit == 0 ? true : false">
        <div v-if="laberror == '' ? false : true">
          <font style="color: red">{{ laberror }}</font>
        </div>
        <el-button type="primary" size="small" @click="submitAssess"
          >确认提交</el-button
        >
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getDetail2, scoring3 } from "@/api/home/home";
import qs from "qs";
export default {
  data() {
    return {
      activeName: "1",
      show: false,
      value: "",
      detailData: {},
      dutyJichu: [],
      dutyYiban: [],
      dutyZhongdian: [],
      dutyMubiao: [],
      laberror: "",
      submitLoading: false,
      totalScore: 0,
      year: "",
      month: "",
    };
  },
  components: {},
  mounted() {},
  created() {
    this.getDetail();
  },
  methods: {
    //select打开禁止页面滑动
    showHide(val) {
      if (val) {
        this.show = true;
      } else {
        this.show = false;
      }
    },
    redioChange() {
      this.laberror = "";
    },
    getDetail() {
      let data = {
        employeecode: this.$route.query.userCode,
        dbtype: this.$store.state.user.user.dbtype,
      };
      new Promise((response, reject) => {
        getDetail2(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.detailData = response.data.data.detail;
              this.dutyJichu = [];
              this.dutyYiban = [];
              this.dutyZhongdian = [];
              this.dutyMubiao = [];
              this.dutyJichu = response.data.data.dutyJichu;
              this.dutyYiban = response.data.data.dutyYiban;
              this.dutyZhongdian = response.data.data.dutyZhongdian;
              this.dutyMubiao = response.data.data.dutyMubiao;
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
    //提交考核
    submitAssess() {
      let data = {
        serialno: this.detailData.serialno,
        employeecode: this.detailData.employeecode,
      };
      this.laberror = "";
      //遍历基础和关键打分数据
      data.dutySm = [];
      this.dutyJichu.forEach((row) => {
        data.dutySm.push({
          topicId: row.dutycode,
          zpsm: row.zpsm,
        });
        if (row.zpsm == null || row.zpsm == "" || row.zpsm == undefined) {
          this.laberror = "基础指标，自评原因 不能为空.";
        }
      });
      if (this.laberror == "") {
        this.dutyYiban.forEach((row) => {
          data.dutySm.push({
            topicId: row.dutycode,
            zpsm: row.zpsm,
          });
          if (row.zpsm == null || row.zpsm == "" || row.zpsm == undefined) {
            this.laberror = "岗位职责，自评原因 不能为空.";
          }
        });
      }
      if (this.laberror == "") {
        this.dutyZhongdian.forEach((row) => {
          data.dutySm.push({
            topicId: row.dutycode,
            zpsm: row.zpsm,
          });
          if (row.zpsm == null || row.zpsm == "" || row.zpsm == undefined) {
            this.laberror = "重点任务，自评原因 不能为空.";
          }
        });
      }
      if (this.laberror == "") {
        this.dutyMubiao.forEach((row) => {
          data.dutySm.push({
            topicId: row.dutycode,
            zpsm: row.zpsm,
          });
          if (row.zpsm == null || row.zpsm == "" || row.zpsm == undefined) {
            this.laberror = "目标任务，自评原因 不能为空.";
          }
        });
      }

      this.submitLoading = true;
      if (this.laberror != "") {
        this.$message.warning(this.laberror);
        this.submitLoading = false;
        return;
      }
      data.dutySm = JSON.stringify(data.dutySm);
      data.month = this.detailData.month;
      data.year = this.detailData.year;
      data.dbtype = this.$store.state.user.user.dbtype;
      new Promise((response, reject) => {
        scoring3(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success",
              });
              this.$router.push({
                path: "/web/tabTwo",
                query: {},
              });
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
            this.submitLoading = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.form-msg {
  margin: 0.5rem 0;
}

.question {
  .mask-show {
    position: fixed;
    width: 100%;
    height: 100%;
    left: 0px;
    top: 0px;
    background: rgba(0, 0, 0, 0.3);
  }

  .title {
    background: #fff;
    text-align: center;
    font-weight: bold;
    font-size: 1rem;
    padding: 0.5rem 0px;
    color: #409eff;
  }

  .content {
    // padding: 0 0 52px 0;
    .list {
      background: #fff;
      margin-top: 0.5rem;
      font-size: 0.8rem;

      .question-title {
        text-align: left;
        font-weight: bold;
        padding: 0.5rem 0px;

        .index {
          text-align: center;
        }

        .name {
          .el-col {
            text-align: center;
            font-weight: normal;
            color: #a8a8a8;
          }
        }
      }

      .on-line-top::after {
        content: " ";
        position: absolute;
        pointer-events: none;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        left: 0;
        right: 0;
        top: 0;
        width: 100%;
        -webkit-transform: scaleY(0.5);
        transform: scaleY(0.5);
        border-bottom: 1px solid #ebedf0;
      }

      .answer {
        background: #fafafa;
        position: relative;
        padding: 1rem 0 1rem 0;

        .radio {
          text-align: center;
        }
      }
    }

    .message {
      margin: 1rem 0;
      color: #ccc;
      text-align: center;
    }
  }

  .edit-btn {
    width: 100%;
    position: fixed;
    bottom: 0px;
    left: 0px;
    text-align: center;
    background: #fff;
    padding: 10px 0px;

    .el-button {
      width: 50%;
    }
  }
}

.toptic-title {
  background: #409eff;
  color: #fff;
  padding: 0.1rem 0.6rem;
  border-top: 1px solid #f1f1f1;
  font-weight: 600;
  font-size: 0.9rem;
}

.toptic {
  padding: 0.1rem 0.6rem;
}

.season-text {
  padding: 0.1rem 0.6rem;
  background: #fff;
}

.season-content {
  word-wrap: break-word;
  text-align: justify;
}
</style><style>
.question .el-radio-button__orig-radio:checked + .el-radio-button__inner {
  background-color: #409eff;
  border-color: #409eff;
  -webkit-box-shadow: -1px 0 0 0 #409eff;
  box-shadow: -1px 0 0 0 #409eff;
}

.season-text .el-collapse-item__wrap {
  border: 0;
}

.question .el-select .el-input.is-focus .el-input__inner {
  border-color: #409eff;
}

.question .el-select .el-input__inner:focus {
  border-color: #409eff;
}
</style>
