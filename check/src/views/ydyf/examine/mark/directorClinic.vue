<template>
  <div class="question">
    <!-- 等级 -->
    <Level
      :level="detailData.level"
      v-if="detailData.step==5 || detailData.step==6"
    ></Level>
    <el-row>
      <Basic :detailData="detailData"></Basic>
      <el-col class="q-content">
        <template>
          <el-col class="toptic-title">
            {{detailData.username}}自我评分为：{{selfScore == 0 ? '未打分' : selfScore}} <span style="margin-left:15px;">评分等级为：{{selfScore>89 ?'优秀':selfScore>79 ?'良好':selfScore>59 ?'一般':'较差'}}</span>
            <div>提交时间：{{detailDataScore[0]['content'][0]['selfSubmitTime'] ? detailDataScore[0]['content'][0]['selfSubmitTime'] : '暂未提交'}}</div>
          </el-col>
          <el-col
            class="list"
            v-for="(item,index) in detailDataScore"
            :key="index+'only'"
          >
            <el-col class="question-title">
              <el-col
                :span="24"
                class="toptic"
              >{{item.title}}</el-col>
            </el-col>
            <el-col
              class="answer on-line-top"
              v-for="(k,kindex) in item.content"
              :key="kindex+'only'"
            >
              <el-col class="an-question">
                {{k.question}}
              </el-col>
              <el-col class="radio">
                <van-rate
                  v-model="k.selfScore"
                  disabled-color="#E6A23C"
                  :count="k.maxScore"
                  disabled
                />
              </el-col>
            </el-col>
          </el-col>

        </template>
        <template>
          <el-col class="toptic-title">
            部门负责人评分为：{{directorScore == 0 ? '未打分' : directorScore}} <span style="margin-left:15px;">评分等级为：{{directorScore>89 ?'优秀':directorScore>79 ?'良好':directorScore>59 ?'一般':'较差'}}</span>
            <div>提交时间：{{detailDataScore[0]['content'][0]['headSubmitTime'] ? detailDataScore[0]['content'][0]['headSubmitTime'] : '暂未提交'}}</div>
          </el-col>
          <el-col
            class="list"
            v-for="(item,index) in detailDataScore"
            :key="index"
          >
            <el-col class="question-title">
              <el-col
                :span="24"
                class="toptic"
              >{{item.title}}</el-col>
            </el-col>
            <el-col
              class="answer on-line-top"
              v-for="(k,kindex) in item.content"
              :key="kindex"
            >
              <el-col class="an-question">
                {{k.question}}
              </el-col>
              <el-col class="radio">
                <van-rate
                  v-model="k.headScore"
                  color="#f00"
                  disabled-color="#f00"
                  :count="k.maxScore"
                  :disabled="k.step == 1 || k.step == 2 ? false : true"
                  @change="onChange"
                />
              </el-col>
            </el-col>
          </el-col>
        </template>
        <template v-if="detailData.step == 5">
          <el-col class="toptic-title">
            党支部评分为：{{secretaryScore == 0 ? '未打分' : secretaryScore}}
            <span style="margin-left:15px;">评分等级为：{{secretaryScore>89 ?'优秀':secretaryScore>79 ?'良好':secretaryScore>59 ?'一般':'较差'}}
            </span>
            <div>提交时间：{{detailDataScore[0]['content'][0]['branchSubmitTime'] ? detailDataScore[0]['content'][0]['branchSubmitTime'] : '暂未提交'}}</div>
          </el-col>
          <el-col
            class="list"
            v-for="(item,index) in detailDataScore"
            :key="index"
          >
            <el-col class="question-title">
              <el-col
                :span="24"
                class="toptic"
              >{{item.title}}</el-col>
            </el-col>
            <el-col
              class="answer on-line-top"
              v-for="(k,kindex) in item.content"
              :key="kindex"
            >
              <el-col class="an-question">
                {{k.question}}
              </el-col>
              <el-col class="radio">
                <van-rate
                  v-model="k.branchScore"
                  color="#409EFF"
                  disabled-color="#409EFF"
                  :count="k.maxScore"
                  :disabled="true"
                />
              </el-col>
            </el-col>
          </el-col>

        </template>
        <el-col
          class="edit-btn"
          v-if="!directorFlag"
        >
          <el-button
            type="primary"
            size="small"
            @click="applyMakeSure"
            :disabled="directorFlag"
          >确认提交</el-button>
        </el-col>
      </el-col>
    </el-row>

    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="30%"
    >
      <div v-if="roleType == 4">
        您本次评分为：
        <span style="color:#409EFF;font-size:16px;">
          {{count}}分
        </span>
        <br /><br />
        <!-- <div>
          <span>目前考核等级为：</span>
          <span
            v-if="count>=90"
            style="color:#409EFF"
          >优秀</span>
          <span
            v-else-if="count>79 && count<90"
            style="color:#409EFF"
          >良好</span>
          <span
            v-else-if="count>59 && count<=79"
            style="color:#E6A23C"
          >一般</span>
          <span
            v-else-if="count<=59"
            style="color:#F56C6C"
          >较差</span>
        </div> -->
        <br />
        <div style="color:#909399">
          <div style="margin-bottom:10px;">D(最终得分)= A(自我评分) x 30%+ B(主任评分) x 35%+ C(书记评分) x 35%</div>
          <div><span style="color:#909399">优秀</span>：90 ≤ D ≤ 100 </div>
          <div><span style="color:#909399">良好</span>：80 ≤ D ≤ 89 </div>
          <div><span style="color:#909399">一般</span>：60 ≤ D ≤ 79 </div>
          <div><span style="color:#909399">较差</span>：0 ≤ D ≤ 59 </div>
        </div>
      </div>
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
import {
  baseInfo,
  getScore,
  submitScore,
  reScore,
} from "@/views/ydyf/api/Form/index";
import Level from "./scoreLevel";
import Basic from "./basic";
import qs from "qs";
export default {
  data() {
    return {
      activeName: "1",
      show: false,
      dialogVisible: false,
      options: [],
      detailData: {},
      submitLoading: false,
      roleType: 0,
      directorFlag: false,
      scoreData: [],
      detailDataScore: [],
      toast: "",
      tempArr: [],
      count: 0,
      selfScore: 0,
      directorScore: 0,
      secretaryScore: 0,
    };
  },
  components: {
    Level,
    Basic
  },
  mounted() {},
  created() {
    this.roleType = this.$route.query.roleType;
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
      this.calculateScore();
      this.dialogVisible = true;
    },
    //获取用户基本信息
    getDetail() {
      let data = {
        userId: this.$route.query.u_id,
      };
      this.toast = this.$toast.loading({
        message: "加载中...",
        forbidClick: true,
        duration: 0,
      });
      new Promise((response, reject) => {
        baseInfo(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.detailData = response.data.data;
              if (this.detailData.step == 3 || this.detailData.step == 4) {
                this.directorFlag = false;
              } else {
                this.directorFlag = true;
              }
              this.getScore();
            } else {
              this.$toast(response.data.msg);
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //获取打分分数
    getScore() {
      let data = {
        userId: this.$route.query.u_id,
      };
      new Promise((response, reject) => {
        getScore(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.detailDataScore = response.data.data;
              //计算打分总数
              this.computeTotal();
            } else {
              this.$toast(response.data.msg);
            }
            this.toast.clear();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //计算总分
    computeTotal() {
      for (let i = 0; i < this.detailDataScore.length; i++) {
        for (let j = 0; j < this.detailDataScore[i]["content"].length; j++) {
          //主任默认设置满分
          if (this.detailDataScore[i]["content"][j]["step"] == 1) {
            this.detailDataScore[i]["content"][j][
              "headScore"
            ] = this.detailDataScore[i]["content"][j]["maxScore"];
          }
          this.selfScore += Number(
            this.detailDataScore[i]["content"][j].selfScore
          );
          this.directorScore += Number(
            this.detailDataScore[i]["content"][j].headScore
          );
          this.secretaryScore += Number(
            this.detailDataScore[i]["content"][j].branchScore
          );
        }
      }
    },
    //计算主任打分
    calculateScore() {
      let count = 0;
      for (let i = 0; i < this.detailDataScore.length; i++) {
        for (let j = 0; j < this.detailDataScore[i]["content"].length; j++) {
          count += Number(this.detailDataScore[i]["content"][j].headScore);
          this.tempArr.push(this.detailDataScore[i]["content"][j]);
        }
      }
      this.count = count;
      // this.dialogVisible = true;
    },
    //提交考核
    submitAssess() {
      this.submitLoading = true;
      if (this.$route.query.type == 1) {
        //编辑
        let data = {
          scoreList: this.tempArr,
          roleType: this.roleType,
        };
        new Promise((response, reject) => {
          reScore(data)
            .then((response) => {
              if (response.data.code == 0) {
                this.$toast.success(response.data.msg);
                this.$router.push({
                  path: "/ydyf/yesAssessment",
                  query: { roleType: this.roleType },
                });
              } else {
                this.$toast(response.data.msg);
              }
              this.submitLoading = false;
            })
            .catch((error) => {
              reject(error);
            });
        });
      } else {
        //添加
        new Promise((response, reject) => {
          submitScore(this.tempArr)
            .then((response) => {
              if (response.data.code == 0) {
                this.$toast.success(response.data.msg);
                this.$router.push({
                  path: "/ydyf/yesAssessment",
                  query: { roleType: this.roleType },
                });
              } else {
                this.$toast(response.data.msg);
              }
              this.submitLoading = false;
            })
            .catch((error) => {
              reject(error);
            });
        });
      }
    },
    //科室主任实时动态打分
    onChange() {
      this.calculateScore();
      this.directorScore = this.count;
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
    color: #409EFF;
  }
  .q-content {
    padding: 0 0 15px 0;
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
    }
    .message {
      margin: 1rem 0;
      color: #ccc;
      text-align: center;
    }
  }
  .edit-btn {
    width: 100%;
    text-align: center;
    padding: 10px 0px;
    margin-top: 15px;
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
.an-question {
  padding: 0 20px;
  margin-bottom: 10px;
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
.question .el-select .el-input.is-focus .el-input__inner {
  border-color: #409EFF;
}
.question .el-select .el-input__inner:focus {
  border-color: #409EFF;
}
</style>