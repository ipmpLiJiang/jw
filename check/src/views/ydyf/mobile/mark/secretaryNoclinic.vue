<template>
  <div class="question">
    <Level :level="detailData.level"  v-if="detailData.step==3  || detailData.step==6"></Level>
    <el-row>
      <Basic :detailData="detailData"></Basic>
      <el-col class="q-content">
        <template>
          <el-col class="toptic-title">
            部门负责人意见
            <div>提交时间：{{detailData.headSubmitTime ? detailData.headSubmitTime : '暂未提交'}}</div>
          </el-col>
          <el-col class="answer on-line-top">
            <el-col class="radio">
              <el-radio-group
                v-model="detailData.deptHeadOpinion"
                size="small"
                :disabled="true"
              >
                <el-radio-button :label="1">优秀</el-radio-button>
                <el-radio-button :label="2">良好</el-radio-button>
                <el-radio-button :label="3">一般</el-radio-button>
                <el-radio-button :label="4">较差</el-radio-button>
              </el-radio-group>
            </el-col>
          </el-col>
          <el-col class="toptic-title">
            党支部意见
            <div>提交时间：{{detailData.branchSubmitTime ? detailData.branchSubmitTime : '暂未提交'}}</div>
          </el-col>
          <el-col class="answer on-line-top">
            <el-col class="radio">
              <el-radio-group
                v-model="detailData.branchOpinion"
                size="small"
                :disabled="directorFlag"
              >
                <el-radio-button :label="1">优秀</el-radio-button>
                <el-radio-button :label="2">良好</el-radio-button>
                <el-radio-button :label="3">一般</el-radio-button>
                <el-radio-button :label="4">较差</el-radio-button>
              </el-radio-group>
            </el-col>
          </el-col>
        </template>
        <el-col class="toptic-title">
          实时评优百分比优秀比上限为<span style="color:#F56C6C">{{maxPercent}}%</span>
        </el-col>
        <el-col>
          <div
            class="season-text"
            style="padding: 1.5rem 1rem;"
          >
            <van-progress
              :percentage="currentPercent"
              stroke-width="8"
              color="#409EFF"
            />
          </div>
        </el-col>
        <el-col
          class="edit-btn"
          v-if="!directorFlag"
        >
          <el-button
            type="primary"
            size="small"
            @click="applyMakeSure"
          >确认提交</el-button>
        </el-col>
      </el-col>
    </el-row>

    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="90%"
    >
      <div v-if="roleType == 7">党支部打分为:
        <span style="color:#409EFF;font-size:16px;">
          <span v-if="detailData.branchOpinion == 1">优秀</span>
          <span v-if="detailData.branchOpinion == 2">良好</span>
          <span v-if="detailData.branchOpinion == 3">一般</span>
          <span v-if="detailData.branchOpinion == 4">较差</span>
        </span>
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
  Formlist,
  mySbmit,
  ProfiCiency,
  editById,
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
      maxPercent: 0,
      currentPercent: 0,
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
      if (this.roleType == 4) {
        if (!this.detailData.deptHeadOpinion) {
          this.$toast("请评完分在提交哦");
          return;
        }
      } else if (this.roleType == 7) {
        if (!this.detailData.branchOpinion) {
          this.$toast("请评完分在提交哦");
          return;
        }
      }
      this.dialogVisible = true;
    },
    getDetail() {
      new Promise((response, reject) => {
        Formlist(this.$route.query.u_id)
          .then((response) => {
            if (response.data.code == 0) {
              this.detailData = response.data.data;
              if (this.detailData.step == 2 || this.detailData.step == 3) {
                this.directorFlag = false;
              } else {
                this.directorFlag = true;
              }
              //获取占优百分比
              this.getPercentage();
            } else {
              this.$toast(response.data.msg);
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //提交考核
    submitAssess() {
      //判断占优评分比人数是否已达上限
      if (this.detailData.branchOpinion == 1) {
        if (!this.getPercent()) {
          this.$toast("优秀率已满,无法再次评优哦~");
          return false;
        }
      }
      let data = this.detailData;
      this.submitLoading = true;
      if (this.$route.query.type == 1) {
        //编辑
        data.roleType = this.roleType;
        new Promise((response, reject) => {
          editById(data)
            .then((response) => {
              if (response.data.code == 0) {
                this.$toast.success(response.data.msg);
                this.$router.push({
                  path: "/web/ydyfMobileList",
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
          mySbmit(data)
            .then((response) => {
              if (response.data.code == 0) {
                this.$toast.success(response.data.msg);
                this.$router.push({
                  path: "/web/ydyfMobileList",
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
    //获取占优百分比
    getPercentage() {
      new Promise((response, reject) => {
        ProfiCiency(this.detailData.branchId)
          .then((response) => {
            if (response.data.code == 0) {
              this.excellentFrom = response.data.data;
              //计算当前打分百分比
              this.currentPercent = parseInt(
                (this.excellentFrom.currentExcellentCount /
                  this.excellentFrom.totalCount) *
                  100
              );
              this.maxPercent = Number(response.data.data.maxExcellentPercent);
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
    //判断优秀是否大于上限
    getPercent() {
        console.log(this.excellentFrom.totalCount*this.excellentFrom.maxExcellentPercent*100/10000)
      console.log(dCount)
      console.log(this.excellentFrom.currentExcellentCount+1)
      // let dCount =
      //   ((this.excellentFrom.currentExcellentCount + 1) /
      //     this.excellentFrom.totalCount) *
      //   100; 
       let dCount=Math.round(this.excellentFrom.totalCount*this.excellentFrom.maxExcellentPercent*100/10000)
        
        //当前优秀人数
      if (dCount >= this.excellentFrom.currentExcellentCount+1) {
        return true;
      } else {
        return false;
      }
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