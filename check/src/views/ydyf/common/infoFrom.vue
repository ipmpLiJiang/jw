<template>
  <div>
    <el-col :span="24" class="title">填写个人信息</el-col>
    <el-form ref="form" :model="form" :rules="rules">
      <el-col :span="12">
        <el-form-item label="姓名" prop="username">
          <el-input
            v-model="form.username"
            style="width: 350px; margin-left: 28px"
            disabled
          ></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="选择性别" prop="sex">
          <!-- <el-input
              v-model="form.sex"
              style="width: 350px; margin-left: 28px"
            ></el-input> -->
          <el-select v-model="form.sex" placeholder="请选择"  style="width: 350px"  disabled >
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
        <el-form-item label="发薪号码" prop="userId">
          <el-input v-model="form.userId" style="width: 350px"  disabled></el-input>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item label="选择职称" prop="title">
          <el-select
            v-model="form.title"
            placeholder="请选择职称"
            style="width: 350px"
            
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
        <el-form-item label="出生年月" prop="birth">
          <el-input v-model="form.birth" style="width: 350px"  disabled></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="政治面貌" prop="politicalStatus">
          <el-select
            v-model="form.politicalStatus"
            placeholder="请选择政治面貌"
            style="width: 350px"
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
          >
            <el-option
              v-for="item in culture"
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
          ></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="聘用时间" prop="hireDate">
          <!-- <el-input v-model="form.hireDate" style="width: 350px"></el-input> -->
          <el-date-picker
            v-model="form.hireDate"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd"
            format="yyyy-MM-dd"
            style="width: 350px"
          >
          </el-date-picker>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="从事工作" prop="jobContent">
          <el-input v-model="form.jobContent" style="width: 350px"></el-input>
        </el-form-item>
      </el-col>
    </el-form>

    <el-row class="step">
      <!-- <el-button type="primary" style="margin-top: 12px" @click="next"
        >下一步</el-button
      > -->
    </el-row>
  </div>
</template>

<script>
import { updateUserPassword } from "@/api/user/user";
import qs from "qs";
import { initInfo, updateSelfSummary, updateBaseInfo } from "../api/Form/index";
export default {
  props: [],
  data() {
    return {
      form: {
        username: "",
        sex: null,
        userId: "",
        status: "",
        workingTtime: "",
        educationLevel: "",
        title: "",
        hireDate: "",
        jobContent: "",
        currentPosition: "",
        birth: "",
      },
      sexValue:"",

      dialogVisible: true,
      forms: {},
      // getList: {},
      rules: {
        username: [{ required: true, message: "请输入姓名", trigger: "blur" }],
        sex: [{ required: true, message: "请选择性别", trigger: "blur" }],
        birth: [{ required: true, message: "请输入出生年月", trigger: "blur" }],
        politicalStatus: [
          { required: true, message: "请选择政治面貌", trigger: "blur" },
        ],
        technical: [{ required: true, message: "请选择职称", trigger: "blur" }],
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
        userId: [{ required: true, message: "请填写发薪号", trigger: "blur" }],
        title: [{ required: true, message: "请填写发薪号", trigger: "blur" }],
      },
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
      sex:[
         {
          value: 0,
          label: "女",
        },
        {
          value: 1,
          label: "男",
        },
      ],

      culture: [
        {
          value: "1",
          label: "大专及以下",
        },
        {
          value: "2",
          label: "本科",
        },
        {
          value: "3",
          label: "硕士研究生",
        },
        {
          value: "4",
          label: "博士研究生",
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
          label: "三级职称",
        },
        {
          value: "33",
          label: "四级职称",
        },
        {
          value: "34",
          label: "五级职称",
        },
        {
          value: "35",
          label: "六级职称",
        },
        {
          value: "36",
          label: "七级职称",
        },
        {
          value: "37",
          label: "八级职称",
        },
        {
          value: "38",
          label: "九级职称",
        },
        {
          value: "39",
          label: "十级职称",
        },
        {
          value: "40",
          label: "其他",
        },
      ],
    };
  },
  computed: {},
  created() {
    this.getList();
  },
  mounted() {},
  watch: {},
  methods: {
    //子组件校验，传递到父组件
    validateForm() {
      let flag = null;
      this.$refs["form"].validate((valid) => {
        if (valid) {
          flag = true;
        } else {
          flag = false;
        }
      });
      return flag;
    },

    getList() {
      const userId = this.$store.state.user.user.moneycard;
      new Promise((response, reject) => {
        initInfo(userId)
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
  },
  components: {},
};
</script>

<style scoped lang="scss">
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
}

.indicator {
  display: flex;
  flex-wrap: wrap;
  background: #fff;
  margin: 10px;
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  .li-title {
    width: 100%;
    margin-bottom: 20px;
    padding: 0 20px;
    .label {
      font-weight: 500;
      margin-bottom: 10px;
    }
    .core {
      width: 33%;
      display: inline-block;
    }
    .core-input {
      width: 180px;
      margin-top: 10px;
    }
    .title {
      line-height: 10px;
      background-color: #409eff;
      color: #fff;
      font-size: 16px;
      font-weight: 500;
      width: 100%;
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
    padding: 0 20px;
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
</style>
