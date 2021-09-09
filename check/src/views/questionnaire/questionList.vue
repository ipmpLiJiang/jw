<template>
  <div class="question-container">
    <div class="question-content">
      <img
        src="../../assets/img/dream-seeker@2x.jpg"
        class="question-header"
      >
      <div class="question-list">
        <el-row class="edit">
          <el-col
            :span="24"
            class="right"
          >
            <template v-if="publishstatus == 2">
              <el-col class="title">
                {{title}}
              </el-col>
              <div v-html="explain"></div>
              <el-col
                v-for="(item,index) in topicArr"
                :key="index"
              >
                <template v-if="item.type == 1">
                  <el-col
                    :span="24"
                    class="list"
                  >
                    <div class="title_question_all">
                      <strong class="topic_number">{{item.questionorder}}.</strong>
                      <div
                        class="jz-title"
                        v-html="item.title"
                      ></div>
                      <span
                        v-if="item.iswrite == 1"
                        class="required"
                      >必填项</span>
                    </div>
                    <el-col class="people">
                      <el-col
                        v-for="(j,jindex) in item.optionInfoList"
                        :key="jindex"
                        class="ulradiocheck"
                      >
                        <el-radio
                          :label="j.id"
                          v-model="item.answer"
                          class="urc"
                        >{{j.name}}</el-radio>
                        <el-input
                          v-if="j.gap"
                          class="choice-input"
                          v-model="j.gaptext"
                        ></el-input>
                      </el-col>
                    </el-col>
                  </el-col>
                </template>
                <template v-else-if="item.type == 2">
                  <el-col
                    :span="24"
                    class="list"
                  >
                    <div class="title_question_all">
                      <strong class="topic_number">{{item.questionorder}}.</strong>
                      <div
                        class="jz-title"
                        v-html="item.title"
                      ></div>
                      <div
                        class="remark"
                        v-if="item.multiple != -1  && item.multiple"
                      >[{{item.multipletext}}]</div>
                      <span
                        v-if="item.iswrite == 1"
                        class="required"
                      >必填项</span>
                    </div>
                    <el-col class="people">
                      <el-checkbox-group
                        v-model="item.answer"
                        :max="item.multiple != -1 ? item.multiple : item.optionInfoList.length"
                      >
                        <el-checkbox
                          v-for="(j,jindex) in item.optionInfoList"
                          :key="jindex"
                          :label="j.id"
                          class="ulradiocheck urc"
                        >{{j.name}}<el-input
                            v-if="j.gap"
                            class="choice-input"
                            v-model="j.gaptext"
                          ></el-input>
                        </el-checkbox>
                      </el-checkbox-group>
                    </el-col>
                  </el-col>
                </template>
                <template v-else-if="item.type == 3">
                  <el-col
                    :span="24"
                    class="list"
                  >
                    <div class="title_question_all">
                      <strong class="topic_number">{{item.questionorder}}.</strong>
                      <div
                        class="jz-title"
                        v-html="item.title"
                      ></div>
                      <span
                        v-if="item.iswrite == 1"
                        class="required"
                      >必填项</span>
                    </div>
                    <el-col class="people">
                      <el-col class="ulradiocheck">
                        <el-input v-model="item.answer"></el-input>
                      </el-col>
                    </el-col>
                  </el-col>
                </template>
                <template v-else-if="item.type == 4">
                  <el-col :span="24">
                    <div
                      v-html="item.title"
                      style='text-align:center;padding: 20px;'
                    ></div>
                  </el-col>
                </template>
              </el-col>
              <el-col class="submit">
                <el-button
                  type="primary"
                  icon="el-icon-tickets"
                  @click="submit"
                >提交</el-button>
              </el-col>
            </template>
            <template v-else>
              <el-col class="not-issue">
                该问卷暂未发布或已过期，不可作答！
              </el-col>
            </template>
          </el-col>
        </el-row>
      </div>
    </div>
    <el-dialog
      title="请填写用户信息"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
      width="30%"
      class="dialog-uid"
    >

      <div class="form-box">
        <el-col
          :span="24"
          class="left"
        >
          <el-input
            v-model="uid"
            clearable
          >
            <template slot="prepend">发薪号</template>
          </el-input>
        </el-col>
      </div>
      <div class="form-box">
        <el-col
          :span="24"
          class="left"
        >
          <el-input
            v-model="phoneNum"
            clearable
          >
            <template slot="prepend">手机号</template>
          </el-input>
        </el-col>
      </div>
      <div class="form-box">
        <el-col
          :span="18"
          class="left"
        >
          <el-input
            placeholder="请输入验证码"
            v-model="checkCode"
            clearable
          >
            <template slot="prepend">验证码</template>
          </el-input>
        </el-col>
        <el-col
          :span="6"
          class="right"
        >
          <el-button
            @click="sendCode"
            type="primary"
            :disabled="codeDisabled"
          >{{btnTitle}}</el-button>
        </el-col>
      </div>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          type="primary"
          @click="uidVerify"
        >确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  getDetail,
  batchInsert,
  verifyUser,
  sendVoteCheckCode,
} from "@/api/questionnaire/index";
import qs from "qs";
export default {
  data() {
    return {
      title: "",
      topicArr: [],
      uid: "",
      publishstatus: 0,
      data: [],
      dialogFormVisible: false,
      type: "",
      explain: "",
      btnTitle: "发送验证码",
      codeDisabled: false,
      repetitionSend: false,
      repetitionSubmit: false,
      checkCode: "",
      phoneNum: "",
    };
  },
  beforeCreate() {
    document
      .querySelector("body")
      .setAttribute(
        "style",
        "background-color: #f3f6fa;background-image: url(../../assets/img/dream-seeker-bg@2x.jpg);background-repeat: repeat-x; background-size: 1px 300px;"
      );
  },
  created() {
    this.getDetail();
  },
  mounted() {
    if (this._isMobile()) {
      this.$router.replace("/mobileQuestionList?id=" + this.$route.query.id);
    }
  },
  methods: {
    getDetail() {
      let data = {
        id: this.$route.query.id,
      };
      const loading = this.$loading({
        lock: true,
        text: "拼命加载中...",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      new Promise((response, reject) => {
        getDetail(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              //复选框转化为数组
              response.data.batchJsonList.forEach((row) => {
                if (row.type == 2) {
                  row.answer = [];
                }
              });
              this.data = response.data.batchJsonList;
              this.title = response.data.data.title;
              this.type = response.data.data.surveytype;
              this.publishstatus = response.data.data.publishstatus;
              this.explain = response.data.data.content;
              if (this.publishstatus == 2) {
                if (this.type != 2) {
                  this.open();
                } else {
                  this.topicArr = this.data;
                }
              }
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
            this.tableLoading = false;
            loading.close();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //发送验证码
    sendCode() {
      if (this.repetitionSend) {
        return;
      }
      if (this.phoneNum.length != 11) {
        this.$message({
          message: "请输入正确的手机号",
          type: "warning",
        });
        return;
      }
      this.repetitionSend = true;
      let data = {
        uId: this.uid,
        phoneNum: this.phoneNum,
      };
      data.surveyInfoId = this.$route.query.id;
      new Promise((response, reject) => {
        sendVoteCheckCode(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.validateBtn();
              this.$message({
                message: response.data.msg,
                type: "success",
              });
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
            this.repetitionSend = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //倒计时
    validateBtn() {
      let time = 60;
      let timer = setInterval(() => {
        if (time == 0) {
          clearInterval(timer);
          this.codeDisabled = false;
          this.repetitionSend = false;
          this.btnTitle = "发送验证码";
        } else {
          this.btnTitle = time + "秒后重试";
          this.codeDisabled = true;
          time--;
        }
      }, 1000);
    },
   submit() {
      //先删除type=4的标题元素
      let ttArr = [];
      this.topicArr.forEach((row) => {
        if (row.type != 4) {
          ttArr.push(row);
        }
      });
      //判断必填项是否选择
      for (let i = 0; i < ttArr.length; i++) {
        if (ttArr[i]["iswrite"] == 1 && ttArr[i]["type"] != 4) {
          if (!ttArr[i]["answer"] || ttArr[i]["answer"].length <= 0) {
            this.$message.warning(
              "检测到必填项第" + (i + 1) + "题未作答，请先作答在提交！"
            );
            return;
          }
        }
      }
      const loading = this.$loading({
        lock: true,
        text: "正在提交中...",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      //复选框答案变成字符串
      let arr = [...this.topicArr];
      arr.forEach((row) => {
        if (row.type == 2) {
          row.answer = row.answer.join(",");
        }
      });
      //把optionInfoList答案赋值给nav
      arr.forEach((row) => {
        row.nav =
          row.optionInfoList.length > 0
            ? JSON.stringify(row.optionInfoList)
            : "";
      });
      let data = {
        surveryinfoid: this.$route.query.id,
        moneycard: this.uid,
        batchjson: JSON.stringify(arr),
      };
      new Promise((response, reject) => {
        batchInsert(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success",
              });
              this.$router.push({
                path: "/submitSuccess",
              });
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
            this.tableLoading = false;
            loading.close();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //发薪号输入
    open() {
      this.dialogFormVisible = true;
    },
    //发薪号核验
    uidVerify() {
      let data = {};
      if (!this.uid) {
        this.$message.warning("请先填写发薪号");
        return;
      }
      data.uId = this.uid;
      data.checkCode = this.checkCode;
      new Promise((response, reject) => {
        verifyUser(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.topicArr = this.data;
              this.dialogFormVisible = false;
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
    //判断设备是pc还是mobile
    _isMobile() {
      let flag = navigator.userAgent.match(
        /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
      );
      return flag;
    },
  },
};
</script>
<style lang="scss">
@media screen and (max-width: 640px) {
  .dialog-uid {
    .el-dialog {
      width: 50% !important;
    }
  }
}
</style>
<style lang="scss" scoped>
@media screen and (max-width: 640px) {
  .question-container {
    .question-content {
      width: 100% !important;
      font-size: 0;
    }
    .question-header {
      width: 100% !important;
      font-size: 0;
    }
  }
  .question-list {
    .edit {
      .title {
        padding: 0;
      }
      .right {
        padding: 30px 20px;
      }
    }
  }
}
.question-container {
  background-color: #f3f6fa;
  background-image: url(../../assets/img/dream-seeker-bg@2x.jpg);
  background-repeat: repeat-x;
  background-size: 1px 300px;
  .question-content {
    width: 920px;
    margin: 0 auto;
    font-size: 0;
  }
  .question-header {
    width: 920px;
    font-size: 0;
  }
  .question-list {
    box-shadow: 0px 1px 6px 0px rgba(205, 220, 245, 1);
    font-size: 14px;
    background: #fff;
  }
}

.edit {
  margin-top: 10px;
  border-radius: 2px;
  border: 1px solid rgba(217, 217, 217, 1);
  padding-bottom: 5px;
  margin: 0;
  overflow: auto;
  background: #ffffff;
  box-shadow: 0px 2px 5px 0px #d9d9d9;
  position: relative;
  .title {
    text-align: center;
    font-size: 22px;
    font-weight: bold;
    padding: 20px;
    color: #5a83e5;
  }
  .right {
    background: #fff;
    padding: 30px 70px;
    border-radius: 5px;
    .list {
      position: relative;
      padding-bottom: 10px;
      padding-top: 20px;
      border-bottom: 4px dashed #e7e7e7;
      .people {
        margin: 10px 0;
      }
    }
  }
}
.submit {
  text-align: center;
  padding-top: 20px;
}
.jz-title {
  display: inline-block;
  vertical-align: top;
  font-weight: bold;
}
.topic_number {
  width: 28px;
  display: inline-block;
}
.title_question_all {
  padding-top: 2px;
  font-size: 15px;
  color: #444444;
  font-weight: bold;
  height: auto;
  line-height: 20px;
  position: relative;
}
.ulradiocheck {
  clear: both;
  padding-top: 5px;
  padding-left: 24px;
  padding-bottom: 2px;
  font-size: 15px;
  color: #333333;
  display: block;
}
.urc {
  line-height: 22px;
  position: relative;
  margin-right: 0;
}
.not-issue {
  text-align: center;
  font-size: 16px;
  font-weight: bold;
  padding: 50px 0 100px 0;
  color: #595959;
}
.required {
  margin-left: 10px;
  color: #f56c6c;
}
.choice-input {
  width: 150px;
  margin-left: 10px;
}
.remark {
  display: inline-block;
  color: #999;
  margin-left: 6px;
}
.form-box {
  display: flex;
  justify-content: center;
  margin: 10px 0;
  .left {
    padding: 0px 10px;
  }
  .right {
    padding-right: 10px;
    .el-button {
      width: 100%;
      padding: 12px 0;
    }
  }
}
.dialog-footer {
  text-align: center;
  button {
    width: 30%;
  }
}
</style>