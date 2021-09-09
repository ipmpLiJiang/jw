<template>
  <div class="container">
    <!-- <el-row class="step">
            <el-steps
                    :active="active"
                    finish-status="success"
                    align-center
            >
                <el-step title="创建评价关系"></el-step>
                <el-step title="编辑考核"></el-step>
                <el-step title="发布考核"></el-step>
            </el-steps>
        </el-row> -->
    <el-row class="design-nav">
      <el-col
        :span="24"
        class="design-btn"
      >
        <router-link to="/questionnaire">
          <el-button icon="el-icon-back">返回列表</el-button>
        </router-link>
        <el-button
          type="primary"
          @click="preview"
          icon="el-icon-mobile-phone"
        >预览</el-button>
        <el-button
          type="primary"
          @click="openApply"
          icon="el-icon-finished"
        >完成</el-button>
      </el-col>
      <el-col>
        <ul class="nav-ul">
          <li class="clearfix">
            <div class="title">标题</div>
            <div
              class="dn-content"
              @click="topic(4)"
            >
              <el-button
                type="primary"
                plain
                size="mini"
              >添加一级标题</el-button>
            </div>
          </li>
          <li class="clearfix">
            <div class="title">选择题</div>
            <div
              class="dn-content"
              @click="topic(1)"
            >
              <el-button
                type="primary"
                plain
                size="mini"
              >添加单选</el-button>
            </div>
            <div
              class="dn-content"
              @click="topic(2)"
            >
              <el-button
                type="primary"
                plain
                size="mini"
              >添加多选</el-button>
            </div>
          </li>
          <li
            class="clearfix"
            @click="topic(3)"
          >
            <div class="title">填空题</div>
            <div class="dn-content">
              <el-button
                type="primary"
                plain
                size="mini"
              >添加填空题</el-button>
            </div>
          </li>
          <!-- <li class="clearfix">
          <div class="title">评分题</div>
          <div class="dn-content">
            <i class="icon iconfont icon-icon-test"></i>
            <span>量表题</span>
          </div>
        </li>
        <li class="clearfix">
          <div class="title">高级题型</div>
          <div class="dn-content">
            <i class="icon iconfont icon-icon-test"></i>
            <span>滑动条</span>
          </div>
        </li> -->
        </ul>
      </el-col>
    </el-row>
    <el-row class="edit">
      <el-col
        :span="24"
        class="right"
      >
        <el-col class="title">
          {{form.title}}
          <span
            class="edit-btn"
            @click="openEditTitle"
          >编辑</span>
        </el-col>
        <div class="explain" v-html="explain">
        </div>
        <el-col
          class="list"
          v-for="(item,index) in topicArr"
          :key="index"
        >
          <template v-if="item.type == 1">
            <div>
              <ChoiceRadio
                @getTitle="getTitle"
                @delTopic="delTopic"
                @upTopic="upTopic"
                @downTopic="downTopic"
                @topmostTopic="topmostTopic"
                @bottomTopic="bottomTopic"
                @funcEditShow="funcEditShow"
                @childFinishEdit="finishEdit"
                :order="index"
                :object="topicArr[index]"
              ></ChoiceRadio>
            </div>
          </template>
          <template v-else-if="item.type == 2">
            <div>
              <ChoiceCheck
                @getTitle="getTitle"
                @delTopic="delTopic"
                @upTopic="upTopic"
                @downTopic="downTopic"
                @topmostTopic="topmostTopic"
                @bottomTopic="bottomTopic"
                @funcEditShow="funcEditShow"
                @childFinishEdit="finishEdit"
                @multipleControl="multipleControl"
                :order="index"
                :object="topicArr[index]"
              ></ChoiceCheck>
            </div>
          </template>
          <template v-else-if="item.type == 3">
            <div>
              <ChoiceGap
                @getTitle="getTitle"
                @delTopic="delTopic"
                @upTopic="upTopic"
                @downTopic="downTopic"
                @topmostTopic="topmostTopic"
                @bottomTopic="bottomTopic"
                @funcEditShow="funcEditShow"
                @childFinishEdit="fillFinishEdit"
                :order="index"
                :object="topicArr[index]"
              ></ChoiceGap>
            </div>
          </template>
          <template v-else-if="item.type == 4">
            <div>
              <ChoiceTitle
                @getTitle="getTitle"
                @delTopic="delTopic"
                @upTopic="upTopic"
                @downTopic="downTopic"
                @topmostTopic="topmostTopic"
                @bottomTopic="bottomTopic"
                @funcEditShow="funcEditShow"
                @childFinishEdit="fillFinishEdit"
                :order="index"
                :object="topicArr[index]"
              ></ChoiceTitle>
            </div>
          </template>
        </el-col>
        <el-col
          v-if="topicArr.length <= 0"
          class="none-data"
        >请在左侧选择题目...</el-col>

      </el-col>
    </el-row>
    <!-- 编辑title -->
    <el-dialog
      title="修改标题"
      :visible.sync="titleDialogFormVisible"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      width="36%"
      center
    >
      <el-form :model="form">
        <el-form-item label="标题">
          <el-input
            v-model="form.title"
            autocomplete="off"
            style="width:429px"
          ></el-input>
        </el-form-item>
        <el-form-item label="说明">
          <Ckeditor
            ref="ckditor"
            :type="1"
            :fatherContent="explain"
            @getContent="getContent"
            style="display: inline-block;"
          ></Ckeditor>
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          type="primary"
          @click="titleDialogFormVisible = false"
        >确 定</el-button>
      </div>
    </el-dialog>
    <!-- 选择问卷类型 -->
    <el-dialog
      title="问卷类型"
      :visible.sync="typeDialogFormVisible"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      width="30%"
      center
    >
      <el-form :model="form">
        <el-form-item label="问卷类型">
          <el-select
            v-model="form.type"
            placeholder="请选择项目类型"
            style="width:70%"
          >
            <el-option
              label="院内职工问卷"
              value="1"
            ></el-option>
            <el-option
              label="非院内职工问卷"
              value="2"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          type="primary"
          @click="apply"
        >确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
/**
 * 题目编号
 * 选择题单选 = 1 文件名 = choiceRadio
 * 选择题多选 = 2 文件名 = choiceCheck
 * 单选填空题 = 3 文件名 = choiceGap
 * 一级标题 = 4 文件名 = choiceTitle
 * 滑动条题目 = 5 文件名 = choiceSlider 暂未用到
 */
import ChoiceRadio from "./question/choiceRadio";
import ChoiceCheck from "./question/choiceCheck";
import ChoiceGap from "./question/choiceGap";
import ChoiceTitle from "./question/choiceTitle";
import {
  addQuestionnaire,
  updateQuestionnaire,
  getDetail,
  previewAdd,
} from "@/api/questionnaire/index";
import qs from "qs";
import Ckeditor from "../common/ckeditor";
export default {
  data() {
    return {
      active: 1,
      radio: "",
      checked: true,
      content: "请输入标题",
      topicArr: [],
      form: {
        title: "",
        type: "1",
      },
      id: "",
      titleDialogFormVisible: false,
      typeDialogFormVisible: false,
      previewId: "",
      explain:""
    };
  },
  components: {
    ChoiceRadio,
    ChoiceCheck,
    ChoiceGap,
    ChoiceTitle,
    Ckeditor
  },
  mounted() {},
  created() {
    this.form.title = this.$route.query.title;
    if (this.$route.query.id) {
      this.id = this.$route.query.id;
      this.getDetail();
    }
  },
  methods: {
    getDetail() {
      let data = {
        id: this.id,
      };
      new Promise((response, reject) => {
        getDetail(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success",
              });
              //把gap转换为true和false
              response.data.batchJsonList.forEach((row)=>{
                row.optionInfoList.forEach((r)=>{
                  if(r.gap == 1){
                    r.gap = true;
                  }else{
                    r.gap = false;
                  }
                })
              })
              this.topicArr = response.data.batchJsonList;
              this.explain = response.data.data.content;
              this.previewId = response.data.data.previewid;
              this.form.type = "" + response.data.data.surveytype;
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
    apply() {
      let data = {
        title: this.form.title,
        content: this.explain,
        surveytype: this.form.type,
        batchjson: JSON.stringify(this.topicArr),
        previewid: this.previewId,
      };
      if (this.id) {
        //修改
        data.id = this.id;
        new Promise((response, reject) => {
          updateQuestionnaire(qs.stringify(data))
            .then((response) => {
              if (response.data.code == 0) {
                this.$message({
                  message: response.data.msg,
                  type: "success",
                });
                this.$router.push({
                  path: "/questionnaire",
                  query: {},
                });
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
      } else {
        //添加
        new Promise((response, reject) => {
          addQuestionnaire(qs.stringify(data))
            .then((response) => {
              if (response.data.code == 0) {
                this.$message({
                  message: response.data.msg,
                  type: "success",
                });
                this.$router.push({
                  path: "/questionnaire",
                  query: {},
                });
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
      }
    },
    //选择题目
    topic(type) {
      if (type == 1) {
        this.topicArr.push({
          title: "",
          nav:
            '[{"name":"很不满意","gap":false,"gaptext":""},{"name":"不满意","gap":false,"gaptext":""},{"name":"一般","gap":false,"gaptext":""},{"name":"满意","gap":false,"gaptext":""},{"name":"很满意","gap":false,"gaptext":""}]',
          type: type,
          editShow: true,
          iswrite: true,
        });
      } else if (type == 2) {
        this.topicArr.push({
          title: "",
          nav:
            '[{"name":"很不满意","gap":false,"gaptext":""},{"name":"不满意","gap":false,"gaptext":""},{"name":"一般","gap":false,"gaptext":""},{"name":"满意","gap":false,"gaptext":""},{"name":"很满意","gap":false,"gaptext":""}]',
          type: type,
          editShow: true,
          iswrite: true,
          multiple:-1,
          multipleText:"",
        });
      } else if (type == 3) {
        this.topicArr.push({
          title: "",
          nav: '[{"name":""}]',
          type: type,
          editShow: true,
          iswrite: true,
        });
      } else if (type == 4) {
        this.topicArr.push({
          title: "",
          nav: "[]",
          type: type,
          editShow: true,
          iswrite: true,
        });
      }
    },
    //删除题目
    delTopic(index) {
      this.topicArr.splice(index, 1);
    },
    //上移题目
    upTopic(index) {
      if (index == 0) {
        return;
      }
      this.common.swapItems(this.topicArr, index, index - 1);
    },
    //下移题目
    downTopic(index) {
      if (index == this.topicArr.length - 1) {
        return;
      }
      this.common.swapItems(this.topicArr, index, index + 1);
    },
    //最上题目
    topmostTopic(index) {
      this.topicArr.unshift(this.topicArr.splice(index, 1)[0]);
    },
    //最下题目
    bottomTopic(index) {
      let arr = this.topicArr;
      arr.push(arr[index]);
      arr.splice(index, 1);
      this.topicArr = arr;
    },
    //完成编辑
    finishEdit(index, arr, req) {
      this.topicArr[index]["nav"] = arr;
      this.topicArr[index]["iswrite"] = req;
    },
    //填空题完成编辑
    fillFinishEdit(index, req) {
      this.topicArr[index]["iswrite"] = req;
    },
    //获取title
    getTitle(val, index) {
      this.topicArr[index]["title"] = val;
    },
    //保存编辑状态
    funcEditShow(val, index) {
      this.topicArr[index]["editShow"] = val;
    },
    //打开编辑标题
    openEditTitle() {
      this.titleDialogFormVisible = true;
    },
    openApply() {
      this.typeDialogFormVisible = true;
    },
    //预览
    preview() {
      let data = {
        title: this.form.title,
        content: this.explain,
        surveytype: this.form.type,
        batchjson: JSON.stringify(this.topicArr),
        id: this.previewId,
      };
      new Promise((response, reject) => {
        previewAdd(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.previewId = response.data.id;
              let routeUrl = this.$router.resolve({
                path: "/questionPreview",
                query: { id: this.previewId },
              });
              window.open(routeUrl.href, "_blank");
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
    //获取说明
    getContent(val) {
      this.explain = val;
    },
    //多选题多项控制
    multipleControl(index,params){
      this.topicArr[index]["multiple"] = params.value;
      this.topicArr[index]["multipleText"] = params.label;
    }
  },
  watch: {
    content(val) {
      if (val == "") {
        this.content = "标题";
      } else {
        this.content = val;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.container {
  width: 1230px;
  margin: 20px auto;
  .design-nav {
    width: 250px;
    overflow: auto;
    position: fixed;
    .design-btn {
      .el-button {
        width: 100%;
        margin-bottom: 10px;
      }
      .el-button + .el-button {
        margin-left: 0px;
      }
    }
  }
  .nav-ul {
    padding: 20px 20px 0px 20px;
    background: #fff;
    border: 1px solid #d9d9d9;
    li {
      margin-bottom: 5px;
    }
    .title {
      font-weight: bold;
      margin-bottom: 15px;
    }
    .dn-content {
      width: 50%;
      float: left;
      margin-bottom: 15px;
      cursor: pointer;
      &:hover {
        color: #409EFF;
        span {
          color: #409EFF;
        }
      }
      span {
        color: #666;
        vertical-align: middle;
      }
      .icon {
        margin-right: 5px;
        vertical-align: middle;
      }
    }
  }
}
.step {
  background: #fff;
  padding: 20px;
  border-radius: 5px;
}
.edit {
  margin-top: 10px;
  border-radius: 2px;
  border: 1px solid rgba(217, 217, 217, 1);
  width: 962px;
  padding-bottom: 5px;
  margin: 0;
  margin-top: 8px;
  overflow: auto;
  background: #ffffff;
  position: relative;
  margin-left: 268px;
  .title {
    text-align: center;
    font-size: 22px;
    font-weight: bold;
    padding: 20px;
  }
  .left {
    text-align: center;
    background: #fff;
    padding: 20px 20px 0px 20px;
    border-radius: 5px;
    margin-top: 10px;
    position: fixed;
    width: 160px;
    right: 10px;
    top: 100px;
    .el-button {
      margin-bottom: 20px;
    }
    .el-button + .el-button {
      margin-left: 0px;
    }
  }
  .right {
    background: #fff;
    padding: 20px;
    border-radius: 5px;
    .list {
      position: relative;
      padding-bottom: 45px;
      padding-top: 20px;
      border-bottom: 4px dashed #e7e7e7;
      .content {
        .text-message {
          margin-top: 10px;
          margin-bottom: 10px;
          .el-col {
            text-align: center;
            color: #e6a23c;
          }
          .name {
            text-align: center;
          }
        }
        .people {
          padding: 10px 0px;
          .el-col {
            margin: 8px 0;
          }
          .name {
            text-align: center;
          }
          .el-radio {
            margin-left: 15px;
          }
        }
      }
      .edit-content {
        background: #f8f8f8;
        border: 1px solid #e7e7e7;
        padding: 20px;
        margin-top: 10px;
        .editer {
          background: #fff;
        }
        .must {
          margin: 10px 0px;
        }
        .input-title {
          background: #f3f3f3;
          padding: 10px 10px;
          margin-bottom: 10px;
        }
        .input {
          .el-button {
            position: relative;
            top: -5px;
          }
        }
      }
      .hover {
        position: absolute;
        z-index: 4;
        left: 0px;
        bottom: 6px;
        text-align: right;
        .el-button {
        }
      }
    }
  }
}
.jz-title {
  display: inline-block;
  vertical-align: top;
  font-weight: bold;
}
.none-data {
  color: #d9d9d9;
}
.edit-btn {
  color: #e6a23c;
  font-size: 12px;
  padding: 2px 5px;
  border: 1px solid #e6a23c;
  cursor: pointer;
  border-radius: 2px;
  margin-left: 10px;
}
</style>

