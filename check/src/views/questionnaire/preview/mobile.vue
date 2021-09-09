<template>
  <div class="question-container-phone">
    <div class="question-content">
      <p class="top">武汉市精神卫生中心360°考核系统</p>
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
                    <el-col
                      :span="24"
                      class="title_question_all"
                    >
                      <el-tag
                        v-if="item.iswrite == 1"
                        type="danger"
                        effect="dark"
                        size='mini'
                      >必填</el-tag>
                      <el-col
                        :span="2"
                        class="topic_number"
                      >
                        {{item.questionorder}}、
                      </el-col>
                      <el-col
                        class="jz-title"
                        v-html="item.title"
                      ></el-col>
                    </el-col>
                    <el-col class="people">
                      <el-col
                        v-for="(j,jindex) in item.optionInfoList"
                        :key="jindex"
                        class="ulradiocheck"
                        :span="11"
                      >
                        <el-radio
                          :label="j.id"
                          v-model="item.answer"
                          style="height:auto;padding: 10px;"
                          border
                        >{{j.name}}<el-input
                            v-if="j.gap"
                            class="choice-input"
                            v-model="j.gaptext"
                            style="display:block;"
                          ></el-input>
                        </el-radio>
                      </el-col>
                    </el-col>
                  </el-col>
                </template>
                <template v-else-if="item.type == 2">
                  <el-col
                    :span="24"
                    class="list"
                  >
                    <el-col
                      :span="24"
                      class="title_question_all"
                    >
                      <el-tag
                        v-if="item.iswrite == 1"
                        type="danger"
                        effect="dark"
                        size='mini'
                      >必填</el-tag>
                      <el-col
                        :span="2"
                        class="topic_number"
                      >
                        {{item.questionorder}}、
                      </el-col>
                      <el-col
                        :span="22"
                        class="jz-title"
                        v-html="item.title"
                      ></el-col>
                      <div
                        class="remark"
                        v-if="item.multiple != -1 && item.multiple"
                      >[{{item.multipletext}}]</div>
                    </el-col>
                    <el-col class="people">
                      <el-checkbox-group v-model="item.answer">
                        <template>
                          <el-checkbox
                            v-for="(j,jindex) in item.optionInfoList"
                            :key="jindex"
                            :label="j.id"
                            border
                            style="height:auto;"
                          >{{j.name}}<el-input
                              v-if="j.gap"
                              class="choice-input"
                              v-model="j.gaptext"
                              style="display:block;"
                            ></el-input>
                          </el-checkbox>
                        </template>
                      </el-checkbox-group>
                    </el-col>
                  </el-col>
                </template>
                <template v-else-if="item.type == 3">
                  <el-col
                    :span="24"
                    class="list"
                  >
                    <el-col
                      :span="24"
                      class="title_question_all"
                    >
                      <el-tag
                        v-if="item.iswrite == 1"
                        type="danger"
                        effect="dark"
                        size='mini'
                      >必填</el-tag>
                      <el-col
                        :span="2"
                        class="topic_number"
                      >
                        {{item.questionorder}}、
                      </el-col>
                      <el-col
                        :span="22"
                        class="jz-title"
                        v-html="item.title"
                      ></el-col>
                    </el-col>
                    <el-col class="people">
                      <el-col class="ulradiocheck-input">
                        <el-input
                          v-model="item.answer"
                          clearable
                        ></el-input>
                      </el-col>
                    </el-col>
                  </el-col>
                </template>
                <template v-else-if="item.type == 4">
                  <el-col
                    :span="24"
                    class="one-title-list"
                  >
                    <el-col
                      :span="6"
                      class="one-title-line"
                    ></el-col>
                    <el-col
                      :span="12"
                      class="one-title"
                      v-html="item.title"
                    ></el-col>
                    <el-col
                      :span="6"
                      class="one-title-line"
                    ></el-col>
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
        "background-color: #f3f6fa;background-image: url(../../assets/img/dream-seeker-bg@2x.jpg);background-repeat: repeat-x; background-size: 1px 300px;"
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
<style lang="scss" scoped>
/* 有一些公共样式在 mobileQusetionList 页面上*/
.question-container-phone {
  padding-bottom: 2rem;
  background: #efefef;
  .question-list {
    padding: 1rem;
  }
  .top {
    background: #fff;
    padding: 1rem 0;
    text-align: center;
    font-size: 0.7rem;
    color: #828282;
  }
  .title {
    font-size: 1.5rem;
    text-align: center;
    padding-bottom: 1rem;
  }
  .list {
    background-color: #fff;
    -webkit-box-shadow: 0 1px 4px 0 rgba(0, 0, 0, 0.3);
    box-shadow: 0 1px 4px 0 rgba(0, 0, 0, 0.3);
    padding: 1rem 1rem 0 1rem;
    margin-bottom: 1rem;
    border-radius: 10px;
  }
  .title_question_all {
    position: relative;
    margin-bottom: 0.5rem;
    .topic_number {
      font-weight: bold;
    }
    .el-tag {
      font-weight: normal;
      font-size: 0.6rem;
      position: absolute;
      top: -1rem;
      right: -1rem;
      border-radius: 0px 10px 0px 10px;
    }
    .jz-title {
      font-size: 1.1rem;
    }
  }
  .ulradiocheck {
    margin-bottom: 4%;
    width: 100%;
    .el-radio {
      width: 100%;
      display: flex;
      flex-direction: row;
      align-items: center;
    }
  }
  .ulradiocheck:nth-child(even) {
    margin-right: 0px;
  }
  .el-checkbox {
    width: 100%;
    margin-bottom: 4%;
    display: flex;
    flex-direction: row;
    align-items: center;
  }
  .el-checkbox:nth-child(even) {
    margin-right: 0px;
  }
  .ulradiocheck-input {
    width: 100%;
    margin-bottom: 4%;
  }
  .submit {
    padding-top: 1rem;
    .el-button {
      width: 100%;
    }
  }
}
.remark {
  display: inline-block;
  color: #999;
  margin-left: 6px;
}
.jz-title {
  display: inline-block;
  vertical-align: top;
  font-weight: bold;
  width: auto;
}
</style>
