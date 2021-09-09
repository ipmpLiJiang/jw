<template>
  <div class="question">
    
    <el-row>
      <el-col class="title">精神卫生中心360°考核系统</el-col>
      <el-col class="content">
        <el-col class="toptic-title">
          基本信息
        </el-col>
        <el-col>
          <div class="season-text">
            <div class="form-msg">
              <span>姓名：</span>
              <span>{{detailData.username}}</span>
            </div>
            <div class="form-msg">
              <span>发薪号：</span>
              <span>{{detailData.usercode}}</span>
            </div>
            <div class="form-msg">
              <span>岗位：</span>
              <span>{{detailData.stationname}}</span>
            </div>
          </div>
        </el-col>
        <el-col class="toptic-title">
          月结内容
        </el-col>
        <el-col>
          <div class="season-text">
            <el-collapse
              v-model="activeName"
              style="border:0"
            >
              <el-collapse-item
                :title="detailData.title"
                name="1"
              >
                <div class="season-content" v-html="detailData.content"></div>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-col>
        <el-col class="toptic-title">
          基础量化指标(总分20)
        </el-col>
        <el-col
          class="list"
          v-for="(item,index) in dutyJichu"
          :key="index"
        >
          <el-col class="question-title">
            <el-col
              :span="24"
              class="toptic"
            >{{item.dutyname}}</el-col>
          </el-col>
          <el-col class="answer on-line-top">
            <el-col class="radio">
              <el-radio-group
                v-model="item.score"
                size="small"
                :disabled="detailData.isedit == 1 ? true : false"
              >
                <el-radio-button :label="4">优秀({{item.ascore}})</el-radio-button>
                <el-radio-button :label="3">良好({{item.bscore}})</el-radio-button>
                <el-radio-button :label="2">一般({{item.cscore}})</el-radio-button>
                <el-radio-button :label="1">较差({{item.dscore}})</el-radio-button>
              </el-radio-group>
            </el-col>
          </el-col>
        </el-col>
        <el-col
          class="toptic-title"
          style="margin-top:.5rem;"
        >
          关键量化指标(总分80)
        </el-col>

        <el-col
          class="list"
          v-for="(item,index) in dutyYiban"
          :key="index"
        >
          <el-col class="question-title">
            <el-col
              :span="24"
              class="toptic"
            >{{item.dutyname}}</el-col>
            <el-col class="name">
              <el-col :span="6">优秀({{item.ascore}})</el-col>
              <el-col :span="6">良好({{item.bscore}})</el-col>
              <el-col :span="5">一般({{item.cscore}})</el-col>
              <el-col :span="5">较差({{item.dscore}})</el-col>
            </el-col>
          </el-col>
          <el-col class="answer on-line-top">
            <el-col class="radio">
              <el-select
                v-model="item.score"
                @visible-change="showHide"
                placeholder="请选择分数"
                :disabled="detailData.isedit == 1 ? true : false"
              >
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-col>
          </el-col>
        </el-col>
        <!-- <el-col class="message">- 共2道题，您做完了-</el-col> -->
      </el-col>
      <el-col class="edit-btn">
        <el-button
          type="primary"
          size="small"
          @click="applyMakeSure"
        >确认提交</el-button>
      </el-col>
    </el-row>

    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="90%"
    >
      <span>确认提交此考核,总分为:<span style="color:#409EFF;">{{totalScore}}</span>分</span>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="submitAssess"
          :loading="submitLoading"
        >确 定</el-button>
      </span>
    </el-dialog>
    <div
      class="mask-show"
      v-if="show"
      @touchmove.prevent
    ></div>
  </div>
</template>

<script>
import { getTotalScore, mobilGetDetail } from "@/api/mobile/check";
import qs from "qs";
export default {
  data() {
    return {
      activeName: "1",
      show: false,
      dialogVisible: false,
      options: [
        {
          value: "1",
          label: "1分"
        },
        {
          value: "2",
          label: "2分"
        },
        {
          value: "3",
          label: "3分"
        },
        {
          value: "4",
          label: "4分"
        },
        {
          value: "5",
          label: "5分"
        },
        {
          value: "6",
          label: "6分"
        },
        {
          value: "7",
          label: "7分"
        },
        {
          value: "8",
          label: "8分"
        },
        {
          value: "9",
          label: "9分"
        },
        {
          value: "10",
          label: "10分"
        },
        {
          value: "11",
          label: "11分"
        },
        {
          value: "12",
          label: "12分"
        },
        {
          value: "13",
          label: "13分"
        },
        {
          value: "14",
          label: "14分"
        },
        {
          value: "15",
          label: "15分"
        },
        {
          value: "16",
          label: "16分"
        }
      ],
      value: "",
      detailData: {},
      dutyJichu: [],
      dutyYiban: [],
      submitLoading: false,
      year:"",
      month:""
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
    applyMakeSure() {
      if (this.compute()) {
        this.dialogVisible = true;
      }
    },
    getDetail() {
      let data = {
        employeecode: this.$route.query.userCode,
        scorringcode: this.$route.query.scorringCode,
        isvalidation: 1,
        dbtype: this.$store.state.user.user.dbtype
      };
      new Promise((response, reject) => {
        mobilGetDetail(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.detailData = response.data.data.detail;
              this.dutyJichu = [];
              this.dutyYiban = [];
              this.dutyJichu = response.data.data.dutyJichu;
              this.dutyYiban = response.data.data.dutyYiban;
              this.year = response.data.data.detail.year;
              this.month = response.data.data.detail.month;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    //计算总分
    compute() {
      let totalScore = 0;
      let scoreArr = [];
      for (let i = 0; i < this.dutyJichu.length; i++) {
        let val = this.dutyJichu[i];
        if (!val.score) {
          this.$message.warning("请先打完分数");
          return false;
        } else {
          if (!isNaN(val.score)) {
            if (val.score > 4 || val.score < 1) {
              this.$message.warning("基础量化指标请填写1-4之间数字");
              return false;
            }
            totalScore += parseInt(val.score);
            scoreArr.push(val.score);
          } else {
            this.$message.warning("基础量化指标请填写正确数字");
          }
        }
      }
      for (let i = 0; i < this.dutyYiban.length; i++) {
        let val = this.dutyYiban[i];
        if (!val.score) {
          this.$message.warning("请先打完分数");
          return false;
        } else {
          if (!isNaN(val.score)) {
            if (val.score > 16 || val.score < 1) {
              this.$message.warning("关键量化指标请填写1-16之间数字");
              return false;
            }
            totalScore += parseInt(val.score);
            scoreArr.push(val.score);
          } else {
            this.$message.warning("关键量化指标请填写正确数字");
          }
        }
      }
      this.totalScore = totalScore;
      return true;
    },
    //提交考核
    submitAssess() {
      if (this.compute()) {
        let data = {
          serialno: this.detailData.serialno,
          employeecode: this.detailData.employeecode,
          scorringcode: this.$route.query.scorringCode,
          total: this.totalScore,
          year:this.year,
          month:this.month
        };
        //遍历基础和关键打分数据
        data.dutyJiChu = [];
        data.dutyYiban = [];
        this.dutyJichu.forEach(row => {
          data.dutyJiChu.push({ topicId: row.dutycode, score: row.score });
        });
        this.dutyYiban.forEach(row => {
          data.dutyYiban.push({ topicId: row.dutycode, score: row.score });
        });
        data.dutyJiChu = JSON.stringify(data.dutyJiChu);
        data.dutyYiban = JSON.stringify(data.dutyYiban);
        data.dbtype= this.$store.state.user.user.dbtype;
        this.submitLoading = true;
        new Promise((response, reject) => {
          getTotalScore(qs.stringify(data))
            .then(response => {
              if (response.data.code == 0) {
                this.$message({
                  message: response.data.msg,
                  type: "success"
                });
                this.$router.push({
                  path: "/webShowSuccess",
                  query: {}
                });
              } else {
                this.$message({
                  message: response.data.msg,
                  type: "error"
                });
              }
              this.tableLoading = false;
              this.submitLoading = false;
            })
            .catch(error => {
              reject(error);
            });
        });
      }
    }
  }
};
</script>


<style lang="scss" scoped>
.form-msg{
  margin: .5rem 0;
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
    color: #409EFF;
  }
  .content {
    padding: 0 0 52px 0;
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
  background: #409EFF;
  color: #fff;
  padding: 0.8rem;
  border-top: 1px solid #f1f1f1;
  font-weight: 600;
  font-size: 0.9rem;
}
.toptic {
  padding: 0.4rem 0.8rem;
}
.season-text {
  padding: 0.4rem 0.8rem;
  background: #fff;
}
.season-content {
  word-wrap: break-word;
  text-align: justify;
}
</style>
<style>
.question .el-radio-button__orig-radio:checked + .el-radio-button__inner {
  background-color: #409EFF;
  border-color: #409EFF;
  -webkit-box-shadow: -1px 0 0 0 #409EFF;
  box-shadow: -1px 0 0 0 #409EFF;
}
.season-text .el-collapse-item__wrap {
  border: 0;
}
.question .el-select .el-input.is-focus .el-input__inner{
  border-color:#409EFF;
}
.question .el-select .el-input__inner:focus{
  border-color:#409EFF;
}
</style>