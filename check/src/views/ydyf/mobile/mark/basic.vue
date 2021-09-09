<template>
  <div class="question">
    <el-row>
      <el-col class="title">武汉市精神卫生中心医院医德考评系统</el-col>
      <el-col class="q-content">
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
              <span>{{detailData.userId}}</span>
            </div>
            <div class="form-msg">
              <span>性别：</span>
              <span>{{detailData.sex == 0 ? '女' : '男'}}</span>
            </div>
            <div class="form-msg">
              <span>出生日期：</span>
              <span>{{detailData.birth}}</span>
            </div>
            <div class="form-msg">
              <span>政治面貌：</span>
              <span>{{detailData.politicalStatusName}}</span>
            </div>
            <div class="form-msg">
              <span>文化程度：</span>
              <span>{{detailData.educationLevelName}}</span>
            </div>
            <div class="form-msg">
              <span>职称：</span>
              <span>{{detailData.titleName}}</span>
            </div>
            <div class="form-msg">
              <span>现聘岗位：</span>
              <span>{{detailData.currentPosition}}</span>
            </div>
            <div class="form-msg">
              <span>聘用时间：</span>
              <span>{{detailData.hireDate}}</span>
            </div>
            <div class="form-msg">
              <span>从事工作：</span>
              <span>{{detailData.jobContent}}</span>
            </div>
          </div>
        </el-col>
        <el-col class="toptic-title">
          自我评价
        </el-col>
        <el-col>
          <div class="season-text">
            <el-collapse
              v-model="activeName"
              style="border:0"
            >
              <el-collapse-item
                :title="detailData.username"
                name="1"
              >
                <div
                  class="season-content"
                  v-html="detailData.selfSummary"
                ></div>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-col>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeName: "1",
    };
  },
  props: ["detailData"],
  mounted() {},
  created() {},
  methods: {},
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
</style>
