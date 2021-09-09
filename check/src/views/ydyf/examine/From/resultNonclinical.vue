<template>
  <div>
    <h4 class="title">非临床人员考核信息 <button class="print" @click="printimg()"  v-if="this.$store.state.user.user.medicalEthicsRoleList.indexOf('100') != -1">打印</button></h4>
 <div ref="printarea">
    <el-row class="content">
      <el-col :span="24" class="titleform" ref="titleColor">自我考评信息</el-col>
    
   
      <el-row :gutter="20" style="margin-top: 10px">
        <el-col :span="6">
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
        <el-col :span="18">
          <div class="grid-content bg-purple">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>基本资料</span>
              </div>
              <div>
                <el-col :span="12">
                  <el-form label-width="" size="small">
                    <el-form-item label="政治面貌：">
                      <span class="form-content">{{ form.politicalStatusName }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="12">
                  <el-form label-width="" size="small">
                    <el-form-item label="文化程度：">
                      <span class="form-content">{{ form.educationLevelName }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="12">
                  <el-form label-width="" size="small">
                    <el-form-item label="现聘岗位：">
                      <span class="form-content">{{ form.currentPosition }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="12">
                  <el-form label-width="" size="small">
                    <el-form-item label="现有职称：">
                      <span class="form-content">{{ form.titleName }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="12">
                  <el-form label-width="" size="small">
                    <el-form-item label="聘用时间：">
                      <span class="form-content">{{ form.hireDate }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>

                <el-col :span="12">
                  <el-form label-width="" size="small">
                    <el-form-item label="出生年月：">
                      <span class="form-content">{{ form.birth }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="12">
                  <el-form label-width="" size="small">
                    <el-form-item label="从事工作：">
                      <span class="form-content">{{ form.jobContent }}</span>
                    </el-form-item>
                  </el-form>
                </el-col>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>
      <el-col :span="24">
        <h1 id="summarize" ref="tltleColor">个人自我评价</h1>
        <p
          style="
            border: 1px solid #dcdfe6;
           
            padding: 10px 20px;
          "
          v-html="this.form.selfSummary"
        ></p>
        <el-card style="margin-top: 20px">
          <h4>个人总结</h4>
          <p>
            {{ this.form.selfSubmitName }} 提交于
            <span style="color: #fe9a2e; font-size: 16px" ref="mydirector">{{
              this.form.selfSubmitTime
            }}</span>
          </p>
        </el-card>
      </el-col>
    </el-row>

    <el-row class="content" v-if="step > 1">
      <el-col :span="24" class="titleform" >请部门负责人填写意见</el-col>
      <el-col :span="5">
        <h1>部门负责人意见：</h1>
      </el-col>

      <el-col :span="12" style="height: 200px">
        <span style="color:#409EFF;font-weight:bold;font-size:16px;" v-if="form.deptHeadOpinion == 1" ref="directorEvaluate">优秀</span>
        <span style="color:#409EFF;font-weight:bold;font-size:16px;" v-if="form.deptHeadOpinion == 2" ref="directorEvaluate">良好</span>
        <span style="color:#E6A23C;font-weight:bold;font-size:16px;" v-if="form.deptHeadOpinion == 3" ref="directorEvaluate">一般</span>
        <span style="color:#F56C6C;font-weight:bold;font-size:16px;" v-if="form.deptHeadOpinion == 4" ref="directorEvaluate">较差</span>
        <el-card style="margin-top: 20px; width: 400px"  v-if="this.form.headSubmitTime">
          <h4>主任评分</h4>
          <p>
            {{ this.form.headSubmitName }} 提交于
            <span style="color: #fe9a2e; font-size: 16px" ref="director">{{
              this.form.headSubmitTime
            }}</span>
          </p>
        </el-card>
      </el-col>
    </el-row>

    <el-row class="content" v-if="step > 2">
      <el-col :span="24" class="titleform" v-if="percent"
        >请党支部填写综合意见
        <span style="float: right; color: red; font-size: 18px"
          >*评优人选不能超过{{ this.maxExcellentPercent }}%</span
        >
      </el-col>
      <el-col :span="5">
        <h1>党支部意见：</h1>
      </el-col>
      <el-col :span="12" style="height: 200px">
        <span style="color:#409EFF;font-weight:bold;font-size:16px;" v-if="form.branchOpinion == 1" ref="secretaryEvaluate">优秀</span>
        <span style="color:#409EFF;font-weight:bold;font-size:16px;" v-if="form.branchOpinion == 2" ref="secretaryEvaluate">良好</span>
        <span style="color:#E6A23C;font-weight:bold;font-size:16px;" v-if="form.branchOpinion == 3" ref="secretaryEvaluate">一般</span>
        <span style="color:#F56C6C;font-weight:bold;font-size:16px;" v-if="form.branchOpinion == 4" ref="secretaryEvaluate">较差</span>
        <el-card style="margin-top: 20px; width: 400px" v-if="this.form.branchSubmitTime">
          <h4>书记评分</h4>
          <p>
            {{ this.form.branchSubmitName }} 提交于
            <span style="color: #fe9a2e; font-size: 16px" ref="dateColor">{{
              this.form.branchSubmitTime
            }}</span>
          </p>
        </el-card>
      </el-col>
    </el-row>
    </div>
  </div>
</template>

<script>
import { Formlist } from "../../api/Form/index";
import qs from "qs";
export default {
  data() {
    return {
      form: {},
      radio: "",
      secretaryDate: false,
      step: null,
    };
  },
  components: {

  },
  mounted() {},
  created() {
    this.formList();
  },
  methods: {
    formList() {
      let userId = this.$route.query.userId;
      new Promise((response, reject) => {
        Formlist(userId)
          .then((response) => {
            if (response.data.code == 0) {
              this.form = response.data.data; 
              this.step = response.data.data.step;
              this.form.deptHeadOpinion = response.data.data.deptHeadOpinion;
              // this.$refs.wrodTetx.content = response.data.data.selfSummary;
              // this.$refs.wrodTetx.setContent();
        
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

    printimg(){
     this.$refs.tltleColor.style.color="#000";
      this.$refs.tltleColor.style.background="#fff";
      this.$refs.dateColor.style.color="#000";
      this.$refs.directorEvaluate.style.color="#000";
      this.$refs.secretaryEvaluate.style.color="#000";
      this.$refs.director.style.color="#000";
      this.$refs.mydirector.style.color="#000";
  
     
      this.$print(this.$refs.printarea) 
    }
  },
};
</script>


<style lang="scss" scoped>
.title {

  height: 50px;
  line-height: 50px;
  border-bottom: 1px solid #eaeaea;
  padding: 0px 10px;
  font-weight: 600;
  color: #424242;
  background: #fff;
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
  .titleform {
    height: 50px;
    line-height: 50px;
    border-bottom: 1px solid #eaeaea;
    padding: 0px 10px;
    font-weight: 500;
    font-size: 16px;
    color: #000;
    margin-bottom: 20px;
    background-color: #409eff;
  }
  .el-checkbox-group {
    height: 200px;
  }
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
  h1 {
    font-size: 16px;
  }
  .submitOpinion {
    position: absolute;
    right: 15%;
    top: 67%;
    width: 107px;
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
#summarize {
  text-align: center;
  width: 250px;
  height: 45px;
  border: 1px solid #dcdfe6;
  margin: 0 auto;
  line-height: 45px;
  font-size: 15px;
  border-radius: 4px;
  background-color: #409EFF;
  color: #fff;
  margin-bottom: 20px;
}

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
  text-align: center;
  width: 100%;
  min-height: 300px;
  .span {
    float: left;
  }
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
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
.el-form-item__label {
  font-size: 16px;
}
.form-content{
  float: left;
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
