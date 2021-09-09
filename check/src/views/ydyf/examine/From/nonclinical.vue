<template>
  <div>
    <h4 class="title">非临床人员考核</h4>

    <el-row class="content">
      <el-col :span="24" class="titleform">填写自我考评信息</el-col>
      <el-form ref="form" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="姓名" prop="username">
            <el-input
              v-model="form.username"
              style="width: 350px; margin-left: 28px"
               disabled
                clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="选择性别">
            <!-- <el-input
              v-model="form.sex"
              style="width: 350px; margin-left: 28px"
              :disabled="userForm"
            ></el-input> -->
            <el-select
              v-model="form.sex"
              placeholder="请选择性别"
              style="width: 350px"
              disabled
            >
              <el-option
                v-for="item in sex"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="出生年月" prop="birth">
            <el-input
              v-model="form.birth"
              style="width: 350px"
               disabled
                clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发薪号码" prop="date">
            <el-input
              v-model="form.userId"
              style="width: 350px"
              disabled
               clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="政治面貌" prop="politicalStatus">
            <el-select
              v-model="form.politicalStatus"
              placeholder="请选择政治面貌"
              style="width: 350px"
              :disabled="userForm"
               clearable
            >
              <el-option
                v-for="item in appellation"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文化程度" prop="educationLevel">
            <el-select
              v-model="form.educationLevel"
              placeholder="请选择文化程度"
              style="width: 350px"
              :disabled="userForm"
                clearable
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="选择职称" prop="title">
            <el-select
              v-model="form.title"
              placeholder="请选择职称"
              style="width: 350px"
              :disabled="userForm"
              clearable
            >
              <el-option
                v-for="item in technical"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="现聘岗位" prop="currentPosition">
            <el-input
              v-model="form.currentPosition"
              style="width: 350px"
              :disabled="userForm"
               clearable
            ></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="聘用时间" prop="hireDate">
            <!-- <el-input v-model="form.hireDate" style="width: 350px"></el-input> -->
            <el-date-picker
              style="width: 350px"
              v-model="form.hireDate"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd"
              format="yyyy-MM-dd"
              :disabled="userForm"
            >
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="从事工作" prop="jobContent">
            <el-input
              v-model="form.jobContent"
              style="width: 350px"
              :disabled="userForm"
               clearable
            ></el-input>
          </el-form-item>
        </el-col>

        <el-col
          :span="30"
          :offset="6"
          v-show="this.form.step == 0"
          v-if="dialogVisible"
        >
          <el-form-item label="">
            <el-button
              @click="openAdd(scope.row)"
              type="info"
              plain
              style="margin-left: 55px"
              >请填写以下本年度医德医风自评及工作总结</el-button
            >
          </el-form-item>
        </el-col>
      </el-form>
      <el-col v-if="dialogVisible">
        <AddQuarter
          :parentForms="forms"
          :dialogVisible="dialogVisible"
          :isAdd="1"
          :type="1"
          @childClose="childClose"
          @childGetList="getList"
          v-model="form.content"
          ref="wrodTetx"
        ></AddQuarter>
      </el-col>
      <!-- <el-col :span="20" v-show="this.form.step==0">
        <el-button type="primary" style="margin-left: 45%" @click="onSubmit()"
          >确认提交</el-button
        >
      </el-col> -->
      <el-col :span="24" v-if="dialogVisible" style="text-align: center">
        <el-button type="primary" style="margin-top: 12px" @click="saveSummary"
          >保存总结</el-button
        >
        <el-button type="primary" style="margin-left: 40px" @click="onSubmit()"
          >确认提交</el-button
        >
      </el-col>
      <el-col
        :span="24"
        v-if="dialogVisible"
        style="text-align: center; margin-top: 15px"
      >
        <span style="color: #999"
          >友情提示：为避免意外情况总结丢失，请不定时保存一下哦！</span
        >
      </el-col>


      <!-- <el-col :span="20">
          <div id="summarize"></div>
      </el-col> -->
    </el-row>
 

   >
  </div>
</template>

<script>
import { updateUserPassword } from "@/api/user/user";
import { Formlist, mySbmit } from "../../api/Form/index";
import AddQuarter from "../../user/addQuarter";
import personal from "../../common/personal";
import qs from "qs";
export default {
  data() {
    return {
      form: {
        name: "",
        sex: null,
        date: "",
        status: "",
        workingTtime: "",
        politicalStatus: "", //政治面貌
        jobContent: "", //从事工作
        content: "",
        currentPosition: "", //职称
        educationLevel: "", //文化程度
        selfSummary: "", //自我评价
        deptHeadOpinion: "", //科室主任评价
        branchOpinion: "", //党支部书记评价,
      },
      dialogVisible: true, //个人信息富文本
      userForm: false, //用户信息表单是否输入
      self: false, //富文本自我评价显示
      lead: false, //科室主任评价输入框是否显示
      leadForm: false, //科室主任表单输入框是否输入
      secretary: false, //党支部书记评价
      forms: {},
      getList: {},
      rules: {
        username: [{ required: true, message: "请输入姓名", trigger: "blur" }],
        sex: [{ required: true, message: "请选择性别", trigger: "blur" }],
        birth: [{ required: true, message: "请输入出生年月", trigger: "blur" }],
        politicalStatus: [
          { required: true, message: "请选择政治面貌", trigger: "blur" },
        ],
        title : [{ required: true, message: "请选择职称", trigger: "blur" }],
        educationLevel: [
          { required: true, message: "请选择文化程度", trigger: "blur" },
        ],
        currentPosition: [
          { required: true, message: "请选择现聘岗位", trigger: "blur" },
        ],
        hireDate: [
          { required: true, message: "请选择聘用时间", trigger: "blur" },
        ],
        jobContent: [
          { required: true, message: "请选择从事工作", trigger: "blur" },
        ],
      },
      textarea: "",
      radio: "",
      RadioShow: false,
      deptHeadRadioShow: false,
      secretaryDate: false,
      percenTage:false,
      options: [
        {
          value: "1",
          label: "大专",
        },
        {
          value: "2",
          label: "本科",
        },
        {
          value: "3",
          label: "研究生",
        },
        {
          value: "4",
          label: "博士生",
        },
        {
          value: "5",
          label: "院士",
        },
      ],
      appellation: [
        {
          value: "1",
          label: "中国共产党员",
        },
        {
          value: "2",
          label: "团员",
        },
        {
          value: "3",
          label: "民盟",
        },
        {
          value: "4",
          label: "民建",
        },
        {
          value: "5",
          label: "民进",
        },
       
        {
          value: "6",
          label: "农工党",
        },

      
        {
          value: "8",
          label: "致公党",
        },

        {
          value: "9",
          label: "九三学社",
        },
        {
          value: "10",
          label: "无党派人士",
        },
        {
          value: "11",
          label: "群众",
        },
        {
          value: "12",
          label: "其他",
        },
      ],
      technical: [
        {
          value: "1",
          label: "教授",
        },
        {
          value: "2",
          label: "副教授",
        },
        {
          value: "3",
          label: "讲师",
        },
        {
          value: "4",
          label: "助教",
        },
        {
          value: "5",
          label: "正高级工程师",
        },
        {
          value: "6",
          label: "高级工程师",
        },
        {
          value: "7",
          label: "工程师",
        },
        {
          value: "8",
          label: "助理工程师",
        },
        {
          value: "9",
          label: "工人",
        },
        {
          value: "10",
          label: "研究员",
        },
        {
          value: "11",
          label: "副研究员",
        },
        {
          value: "12",
          label: "助理研究员",
        },
        {
          value: "13",
          label: "主任医师",
        },
        {
          value: "14",
          label: "副主任医师",
        },
        {
          value: "15",
          label: "主治医师",
        },
        {
          value: "16",
          label: "医师",
        },
        {
          value: "17",
          label: "主任药师",
        },
        {
          value: "18",
          label: "副主任药师",
        },
        {
          value: "19",
          label: "主管药师",
        },
        {
          value: "20",
          label: "药师",
        },
        {
          value: "21",
          label: "药士",
        },
        {
          value: "22",
          label: "主任护师",
        },

        {
          value: "23",
          label: "副主任护师",
        },
        {
          value: "24",
          label: "主管护师",
        },
        {
          value: "25",
          label: "护师",
        },
        {
          value: "26",
          label: "护士",
        },
        {
          value: "27",
          label: "主任技师",
        },

        {
          value: "28",
          label: "副主任技师",
        },
        {
          value: "29",
          label: "主管技师",
        },
        {
          value: "30",
          label: "技师",
        },
        {
          value: "31",
          label: "技士",
        },
        {
          value: "32",
          label: "三级职员",
        },
        {
          value: "33",
          label: "四级职员",
        },
        {
          value: "34",
          label: "五级职员",
        },
        {
          value: "35",
          label: "六级职员",
        },
        {
          value: "36",
          label: "七级职员",
        },
        {
          value: "37",
          label: "八级职员",
        },
        {
          value: "38",
          label: "九级职员",
        },
        {
          value: "39",
          label: "十级职员",
        },
        {
          value: "40",
          label: "其他",
        },
      ],
      sex: [
        {
          value: 0,
          label: "女",
        },
        {
          value: 1,
          label: "男",
        },
      ],
      oneselForm: false,
      Submit: false, //提交按钮显示
      percent: true, //评优百分比,
      percentscore: 0,
      maxExcellentPercent: 1,
      branchId: 0,
     
    };
  },
  components: {
    AddQuarter,
    personal,
  },
  mounted() {
    
  },
  created() {
    this.formList();
   
    
    
  },
  methods: {
    // 自评提交
    onSubmit() {
      let flag = null;
      this.$refs.wrodTetx.addSubmit();
      let content = this.$refs.wrodTetx.form.content;
      this.$refs["form"].validate((valid) => {

        if (valid) {
          flag = true;
          if (!content) {
            true;
          } else {
            this.$confirm(
              '请确认已填写的个人信息,提交后无法修改"?',
              "提交个人信息",
              {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
              }
            ).then(() => {
              let form = this.form;
              this.form.selfSummary = this.$refs.wrodTetx.form.content;
              this.form.isTemp = 0;
              new Promise((response, reject) => {
                mySbmit(form).then((response) => {
                  if (response.data.code == 0) {
                    this.$message.success("提交成功，我们将尽快为您进行评分");
                    this.dialogVisible = false;
                    this.self = true;
                    this.$router.push({
                      path: "/ydyf/evaluationList",
                    });
                  } else {
                    this.$message({
                      message: response.data.msg,
                      type: "error",
                    });
                  }
                });
              }).catch((error) => {
                reject(error);
                // this.$message({
                //   type: 'info',
                //   message: '已取消'
                // });
              });

              
            });
          }
        } else {
          flag = false;
        }
      });
      return flag;
      this.$refs.wrodTetx.addSubmit();
    },
    //保存总结
    saveSummary() {
      
      this.$refs.wrodTetx.addSubmit();
      this.text = this.$refs.wrodTetx.form.content;
      if (!this.text) {
        return;
      }
      let data = this.form;
      data.selfSummary = this.text;
      data.userId = this.$store.state.user.user.moneycard;
      data.isTemp = 1;
      new Promise((response, reject) => {
        mySbmit(data)
          .then((response) => {
            if (response.data.code == 0) {
              this.$message({
                message: "保存成功",
                type: "success",
              });
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
  

 
    childClose() {
      // this.dialogVisible = val;
      // this.messageDialogVisible = val;
    },
   
    formList() {
      let userId = this.$route.query.userId;
  
 
      // let userId = this.$store.state.user.user.moneycard;
      new Promise((response, reject) => {
        Formlist(userId)
          .then((response) => {
            if (response.data.code == 0) {
              this.form = response.data.data;
              this.form.deptHeadOpinion = response.data.data.deptHeadOpinion;
              this.$refs.wrodTetx.content = response.data.data.selfSummary;
              this.$refs.wrodTetx.setContent();
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
    color: #fff;
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
  font-size: 14px;
  border-radius: 4px;
  background-color: #f4f4f5;
  color: #909399;
  margin-bottom: 20px;
}
</style>
