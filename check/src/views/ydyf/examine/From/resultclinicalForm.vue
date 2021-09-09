<template>
  <div>
    <h4 class="title">
      
      临床人员考核
      <button
        class="print"
        @click="printimg()"
        v-if="
          this.$store.state.user.user.medicalEthicsRoleList.indexOf('100') != -1
        "
      >
        打印
      </button>
    </h4>
    <el-card class="form-container" shadow="never" ref="printarea">
      <el-col
        :span="24"
        class="title"
        style="text-align: center; font-size: 20px"
        >临床人员考核信息
      </el-col>

      <!-- 基本信息 -->
      <el-row :gutter="20" style="margin-top: 10px">
        <el-col :span="8">
          <div class="grid-content bg-purple">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>个人信息</span>
              </div>
              <div class="name-role">
                <span class="sender">姓名: </span>
                <span>{{ form.username }}</span>
              </div>
              <div class="name-role">
                <span class="sender">性别: </span>
                <span>{{ form.sex == 0 ? "女" : "男" }}</span>
              </div>
              <div class="name-role">
                <span class="registe-info">
                  发薪号：
                  {{ form.userId }}
                </span>
              </div>
              <el-divider></el-divider>
              <div class="personal-relation">
                <div class="relation-item">
                  考核年份:
                  <div style="float: right; padding-right: 20px">
                    {{ form.year }} 年
                  </div>
                </div>
              </div>
            </el-card>
          </div>
        </el-col>
        <el-col :span="16">
          <div class="grid-content bg-purple">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>基本资料</span>
              </div>
              <div>
                <el-col :span="12">
                  <el-form label-width="" size="small">
                    <el-form-item label="政治面貌：" class="item">
                      <span>{{ form.politicalStatusName }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="12">
                  <el-form label-width="" size="small">
                    <el-form-item label="文化程度：">
                      <span>{{ form.educationLevelName }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="12">
                  <el-form label-width="" size="small">
                    <el-form-item label="现聘岗位：">
                      <span>{{ form.currentPosition }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="12">
                  <el-form label-width="" size="small">
                    <el-form-item label="现有职称：">
                      <span>{{ form.titleName }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="12">
                  <el-form label-width="" size="small">
                    <el-form-item label="聘用时间：">
                      <span>{{ form.hireDate }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>

                <el-col :span="12">
                  <el-form label-width="" size="small">
                    <el-form-item label="出生年月：">
                      <span>{{ form.birth }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="12">
                  <el-form label-width="" size="small">
                    <el-form-item label="从事工作：">
                      <span>{{ form.jobContent }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>

      <!-- 个人总结 -->
      <el-row>
        <el-col
          :span="24"
          class="title"
          style="text-align: center; font-size: 18px"
          >本年度医德医风自评及工作总结</el-col
        >
        <el-col>
          <p
            style="
              border: 1px solid #dcdfe6;
              padding: 10px 20px;
            "
            v-html="form.selfSummary"
            disabled
          ></p>
        </el-col>
      </el-row>

      <!-- 考评项目评分 -->
      <el-row>
        <el-col
          :span="24"
          class="title"
          style="text-align: center; font-size: 18px"
          >考评项目评分</el-col
        >
        <el-col :span="24">
          <ul class="indicator">
            <li
              class="li-title"
              v-for="(item, index) in questionData"
              :key="index"
            >
              <div class="title" ref="abc">{{ item.title }}</div>
              <div
                class="label"
                v-for="(k, kIndex) in item.content"
                :key="kIndex"
              >
                {{ k.question }}

                <div class="value">
                  <div class="core">
                    <span class="score-title">自我评分:</span>
                    <span class="score-content" ref="abc">{{ k.selfScore }}</span>
                  </div>
                  <div
                    class="core"
                    v-if="
                      (form.step > 2 && PermissionList.indexOf('100') != -1) ||
                      PermissionList.indexOf('101') != -1 ||
                       PermissionList.indexOf('102') != -1 ||
                      PermissionList.indexOf('300') != -1
                    "
                  >
                    <span class="score-title">科室主任评分:</span>
                    <span class="score-content" v-if="form.step > 3" ref="abc" >{{
                      k.headScore
                    }}</span>
                    <span
                      class="score-content"
                      style="color: #e6a23c"
                      v-if="form.step < 4"
                      >科室主任尚未评分</span
                    >
                  </div>
                  <div
                    class="core"
                    v-if="
                      (form.step > 3 && PermissionList.indexOf('100') != -1) ||
                      PermissionList.indexOf('101') != -1 ||
                         PermissionList.indexOf('102') != -1 ||
                      PermissionList.indexOf('300') != -1
                    "
                  >
                    <span class="score-title">党支部书记评分:</span>
                    <span class="score-content" ref="abc">{{ k.branchScore }}</span>
                    <span
                      class="score-content"
                      style="color: #e6a23c"
                      v-if="form.step < 5"
                      >党支部书记尚未评分</span
                    >
                  </div>
                </div>
              </div>
            </li>
          </ul>
        </el-col>
      </el-row>

      <!-- 圆形进度条总分 -->
      <el-row :gutter="24">
        <el-col :span="8">
          <div class="grid-content bg-purple">
            <span class="clri-title">自我评分</span>
            <el-progress
              type="circle"
              :percentage="total.mytotal"
              :format="format"
            ></el-progress>
          </div>

          <!-- <span class="self-evaluation" > <span style="">自评时间:</span> <span>{{this.questionData[0].content[0].selfSubmitTime}}</span></span> -->

          <el-card style="margin-top: 20px">
            <h4>自我评分</h4>
            <p>
              {{ this.questionData[0].content[0].selfSubmitName }} 提交于
              <span style="color: #000; font-size: 16px" >{{
                this.questionData[0].content[0].selfSubmitTime
              }}</span>
            </p>
          </el-card>
        </el-col>

        <el-col :span="8" v-if="form.step > 3">
          <div class="grid-content bg-purple">
            <span class="clri-title">科室主任评分</span>
            <el-progress
              type="circle"
              :percentage="total.directortotal"
              :format="format"
            ></el-progress>
          </div>
          <el-card style="margin-top: 20px">
            <h4>科室主任评分</h4>
            <p>
              {{ this.questionData[0].content[0].headSubmitName }} 提交于
              <!-- <span style="color: #fe9a2e; font-size: 16px">{{
                this.questionData[0].content[0].headSubmitTime 
              }}</span> -->
               <span style="color: #000; font-size: 16px">{{
                this.questionData[0].content[0].headSubmitTime 
              }}</span>
            </p>
          </el-card>
        </el-col>

        <!-- <el-col :span="8" v-if="secretaryDate" -->
        <el-col :span="8" v-if="form.step > 4">
          <div class="grid-content bg-purple">
            <span class="clri-title">书记及合计评分</span>
            <el-progress
              type="circle"
              :percentage="total.excellent"
              :format="secretaryformat"
            
            ></el-progress>
          </div>

          <el-card style="margin-top: 20px">
            <h4>书记评分</h4>
            <p>
              {{ this.questionData[0].content[0].branchSubmitName }} 提交于
              <span style="color: #000; font-size: 16px" >{{
                this.questionData[0].content[0].branchSubmitTime
              }}</span>
            </p>
          </el-card>
        </el-col>
      </el-row>
      <el-col :span="24" v-if="form.step > 4">
        <h1 style="text-align: right; height: 40px; margin-right: 50px">
          本次考核成绩为：<span style="color: red" ref="dateColor">{{
            this.total.secretarytotal
          }}</span>
        </h1>
      </el-col>
    </el-card>
  </div>
</template>

<script>
import { initInfo, baseInfo, getScore } from "../../api/Form/index";
import score from "../../common/score";

import qs from "qs";
export default {
  data() {
    return {
      form: {},
      dialogVisible: true,
      active: 0,
      total: {
        mytotal: 28,
        directortotal: 86,
        secretarytotal: 50,
        ratio: 20,
      },
      PermissionList: [],
      questionData: [],
    };
  },
  components: {
    score,
  },
  mounted() {},
  created() {
    this.PermissionList = this.$store.state.user.user.medicalEthicsRoleList;
    this.getScore();

    this.getList();
  },
  methods: {
    getList() {
      let data = this.$route.query;
      new Promise((response, reject) => {
        baseInfo(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.form = response.data.data;
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
    //获取用户评分列表
    getScore() {
      let data = this.$route.query;
      new Promise((response, reject) => {
        getScore(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.questionData = response.data.data;

              for (let i = 0; i < this.questionData.length; i++) {
                for (
                  let j = 0;
                  j < this.questionData[i]["content"].length;
                  j++
                ) {
                  if (this.questionData[i]["content"][j].step == 1) {
                    this.questionData[i]["content"][
                      j
                    ].branchScore = this.questionData[i]["content"][j].maxScore;
                  }
                  if (this.questionData[i]["content"][j].step == 2) {
                    this.questionData[i]["content"][
                      j
                    ].headScore = this.questionData[i]["content"][j].headScore;
                  } else {
                    // this.questionData[i]["content"][
                    //   j
                    // ].headScore = this.questionData[i]["content"][j].maxScore;
                  }
                }
              }
              this.integral();
            } else {
              // this.$message({
              //   message: response.data.msg,
              //   type: "error",
              // });
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    format(percentage) {
      return percentage > 100 ? "100分" : `${percentage}分`;
    },
    secretaryformat(percentage) {
      return percentage > 100 ? "100分" : `${percentage}分`;

      // return percentage > 89
      //   ? "优秀"
      //   : percentage > 79
      //   ? "良好"
      //   : percentage > 59
      //   ? "一般"
      //   : percentage < 59
      //   ? "较差"
      //   : `${percentage}分`;
    },

    //计算自我评分
    integral() {
      let qData = this.questionData;
      let arr = [];
      for (let i = 0; i < qData.length; i++) {
        for (let j = 0; j < qData[i]["content"].length; j++) {
          qData[i]["content"][j].userId = this.$route.query.userId;
          arr.push(qData[i]["content"][j]);
        }
      }
      let Score = 0;
      let directorScore = 0;
      let secretary = 0;
      for (let k = 0; k < arr.length; k++) {
        Score += Number(arr[k].selfScore);
        directorScore += Number(arr[k].headScore);
        secretary += Number(arr[k].branchScore);
      }

      this.total.mytotal = Score;
      this.total.directortotal = directorScore;
      // this.total.excellent =
      //   Score * 0.3 + directorScore * 0.35 + secretary * 0.35;
      this.total.excellent = secretary;
      // this.total.secretarytotal=secretary
      this.total.secretarytotal = this.secretaryFormat(
        Score * 0.3 + directorScore * 0.35 + secretary * 0.35
      );
    },
    secretaryFormat(percentage) {
      return percentage >= 90
        ? "优秀"
        : percentage >= 80
        ? "良好"
        : percentage >= 60
        ? "一般"
        : percentage < 60
        ? "较差"
        : `${percentage}`;
    },
    //打印
    printimg() {
       
      
      for (let i = 0; i < this.$refs.abc.length; i++) {
       
        this.$refs.abc[i].style.color = "#000";
      }
       this.$refs.dateColor.style.color = "#000";
      this.$print(this.$refs.printarea);
          
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
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  .li-title {
    width: 100%;
    margin-bottom: 20px;
    .label {
      font-weight: 500;
      margin-bottom: 10px;
      font-size: 16px;
      padding: 0 20px;
      .value {
        margin-top: 10px;
      }
    }
    .core {
      width: 33%;
      display: inline-block;
      .score-content {
        color: #409EFF;
        margin-left: 10px;
        font-size: 16px;
      }
    }
    .core-input {
      width: 180px;
      margin-top: 10px;
    }
    .title {
      background-color: #409eff;
      color: #fff;
      font-size: 16px;
      font-weight: 500;
      width: 100%;
      padding: 0 20px;
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
// .personal{
//    height: 200px;
//   line-height: 50px;
//   border-bottom: 1px solid #eaeaea;
//   padding: 0px 10px;
//   font-weight: 600;
//   color: #424242;
//   background: #fff;
//   .el-card{
//   height: 100px;

//   }
// }

//卡片样式
.text {
  font-size: 16px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}

.box-card {
  width: 100%;
  min-height: 300px;
}
//文本样式区
.name-role {
  font-size: 16px;
  padding: 5px;
}

.registe-info {
  padding-top: 10px;
}
.personal-relation {
  font-size: 16px;
  padding: 0px 5px 15px;
  margin-right: 1px;
  width: 100%;
}

.relation-item {
  // padding: 12px;
}
.dialog-footer {
  padding-top: 10px;
  padding-left: 10%;
}
//布局样式区
.el-row {
  margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}
.el-col {
  border-radius: 4px;
}

.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
  text-align: center;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
.el-form-item__label {
  font-size: 16px;
}
.clri-title {
  display: block;
  text-align: center;
  font-size: 15px;
  font-weight: bold;
  margin-bottom: 20px;
}
//打印区域
.print {
  display: inline-block;
  width: 70px;
  height: 35px;
  line-height: 35px;
  float: right;
  margin-top: 5px;
}
</style>


<style lang="scss">
// 增加一个style是为了
.el-form-item__label {
  color: #000;
}
</style>
