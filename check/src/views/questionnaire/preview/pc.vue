<template>
  <div class="question-container">
    <div class="question-content">
      <img
        src="../../../assets/img/dream-seeker@2x.jpg"
        class="question-header"
      >
      <div class="question-list">
        <el-row class="edit">
          <el-col
            :span="24"
            class="right"
          >
            <template>
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
                      <el-checkbox-group v-model="item.answer">
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
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>
<script>
import { previewDetail } from "@/api/questionnaire/index";
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
      id: "",
      explain: "",
    };
  },
  beforeCreate() {
    document
      .querySelector("body")
      .setAttribute(
        "style",
        "background-color: #f3f6fa;background-image: url(../../../assets/img/dream-seeker-bg@2x.jpg);background-repeat: repeat-x; background-size: 1px 300px;"
      );
  },
  methods: {
    getDetail() {
      let data = {
        id: this.id,
      };
      new Promise((response, reject) => {
        previewDetail(qs.stringify(data))
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
              this.topicArr = this.data;
              this.explain = response.data.data.content;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
            this.tableLoading = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    submit() {
      this.$alert("此为预览页面，不能参与作答", "友情提示", {
        confirmButtonText: "确定",
        callback: (action) => {},
      });
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
  background-image: url(../../../assets/img/dream-seeker-bg@2x.jpg);
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
</style>