<template>
  <div>
    <el-col :span="24" class="title">请对考评项目自我评分</el-col>
    <el-form ref="forms" :model="forms" :rules="rules">
      <el-col :span="24">
        <ul class="indicator">
          <li
            class="li-title"
            v-for="(item, index) in questionData"
            :key="index"
          >
            <div class="title">{{ item.title }}</div>
            <div
              class="label"
              v-for="(k, kIndex) in item.content"
              :key="kIndex"
            >
              {{ k.question }}
              <el-form ref="forms" :model="k" :rules="rules">
                <div class="value">
                  <div class="core">
                    <el-form-item label="自我评分" prop="selfScore">
                      <el-input
                        placeholder="请输入分数"
                        clearable
                        class="core-input"
                        v-model="k.selfScore"
                        @change="handlerPageNo"
                        oninput="value=value.replace(/[^0-9]/g,'')">
                      >
                      </el-input>
                    </el-form-item>
                    
                  </div>

                  <!-- <div class="core" >
                  <el-form-item label="科室主任评分" prop="directorHonest">
                    <el-input
                      placeholder="请输入分数"
                      clearable
                      class="core-input"
                      v-model="k.headScore"
                    >
                    </el-input>
                  </el-form-item>
                </div> -->

                  <!-- <div class="core">
                  <el-form-item label="党支部书记评分" prop="committeeHonest">
                    <el-input
                      placeholder="请输入分数"
                      clearable
                      class="core-input"
                      v-model="k.branchScore"
                    >
                    </el-input>
                  </el-form-item>
                </div> -->
                </div>
              </el-form>
            </div>
          </li>
        </ul>
      </el-col>
      <el-col :span="2" class="calculate">
        <div>评分:</div>
      </el-col>
      <el-col :span="10">
        <!-- <el-progress type="circle"  span="6" :text-inside="true">88</el-progress> -->
        <div class="content-view">
          <!-- <el-progress
            :text-inside="true"
            :stroke-width="26"
            :percentage="progressNum"
          ></el-progress> -->
          <el-progress :percentage="progressNum" :format="format" :text-inside="true"
            :stroke-width="26"></el-progress>
        </div>
      </el-col>
    </el-form>

  

    <!-- <button @click="submit('forms')">提交</button> -->
  </div>
</template>

<script>
import { updateUserPassword } from "@/api/user/user";
import { initInfo, initFormData, submitScore } from "../api/Form/index";

import qs from "qs";
export default {
  data() {
    var checkAge = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("分数不能为空"));
      }
    };

    return {
      questionData: [
      
      ],
      form: {
        productGroup: [{ num: "", price: "" }],
      },
      dialogVisible: true,
      k: {
        selfScore: "",
      },
      forms: {},
      getList: {},
      active: 0,
      PermissionID: "",
      rules: {
        selfScore: [
          { validator: checkAge, trigger: "blur" },
          {}
          ],
      },
      progressNum: '100',
      startTimer: "",
      endTimer: "",
    };
  },
  components: {},
  mounted() {
  },
  created() {
    // this.getlist()
    this.PermissionID = this.$store.state.YdyfRole;
    this.integral()
    
  },
  methods: {
    childClose(val) {
      this.dialogVisible = val;
      this.messageDialogVisible = val;
    },
    change() {
      this.$forceUpdate();
    },

    checkScore(dataList) {
      let res = false;
      for (let i = 0; i < dataList.length; i++) {
        if (
          dataList[i].selfScore > dataList[i].maxScore ||
          dataList[i].selfScore < 0
        ) {
          return res;
        }
        if (
          dataList[i].headScore > dataList[i].maxScore ||
          dataList[i].headScore < 0
        ) {
          return res;
        }
        if (
          dataList[i].headScore > dataList[i].maxScore ||
          dataList[i].headScore < 0
        ) {
          return res;
        }
      }
      return true;
    },

    //子组件检验,传递到父组件
    validateForm(forms) {
      let flag = null;
      this.$refs["forms"].validate((valid) => {
        if (valid) {
          flag = true;
        } else {
          flag = false;
        }
      });
      return flag;
    },

    getlist() {
      const userId = this.$store.state.user.user.moneycard;
      new Promise((response, reject) => {
        initFormData(userId)
          .then((response) => {
            if (response.data.code == 0) {
              this.questionData = response.data.data;
              for (let i = 0; i < this.questionData.length; i++) {
                for (
                  let j = 0;
                  j < this.questionData[i]["content"].length;
                  j++
                ) {
                  this.questionData[i]["content"][
                    j
                  ].selfScore = this.questionData[i]["content"][j].maxScore;
                }
              }
              this.integral();
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
    //提交
    SubmitFrom() {
     
      let qData = this.questionData;
      let arr = [];
      for (let i = 0; i < qData.length; i++) {
        for (let j = 0; j < qData[i]["content"].length; j++) {
          qData[i]["content"][j].userId = this.$route.query.userId;

          if(qData[i]["content"][j].selfScore==''){
            this.$message.warning("请将所有评分填写完~")
            return false
          }
          // if(qData[i]["content"][j].selfScore==''){
          //   this.$message.warning("评分请填写整数,不能填写小数哦~")
          //   return false
          // }
            
          arr.push(qData[i]["content"][j]);
          
        }
      }
     
  
      const RESULT = this.checkScore(arr);
      if (!RESULT) {
        this.$confirm("所填分数不能大于要求最高值", "提交失败", {
          type: "warning",
        }).then(() => {
          return false;
        });
      } else {
         this.$confirm("您所填的评分总计为:"+this.progressNum+"分", "提交成绩", {
          type: "success",
        }).then((
            
        ) => {
         
          new Promise((response, reject) => {
          submitScore(arr).then((response) => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success",
              });
              this.$router.go(-1);
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
          });
        });
        });
      
      }
    },
    //获取输入框累积
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
      for (let k = 0; k < arr.length; k++) {
      
        Score += Number(arr[k].selfScore);

        // Score+=a
      }
      this.progressNum = Score;

    },
     //评分展示
       format(percentage) {
        return percentage === 100 ? '100分' : `${percentage}分`;
      },
      handlerPageNo(){
     
        this.integral()
      }
  },
};
</script>


<style lang="scss" scoped>
.form-container {
  width: 1000px;
  margin: 0 auto;
  .step {
    float: right;
    margin-top: 20px;
    margin-bottom: 50px;
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
      // margin-top: 10px;
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
  .el-form-item__content {
    margin-top: 6px;
  }
  .el-form-item {
    margin-top: 7px;
  }
}
.content-view {
  height: calc(100vh - 84px);
  background-color: #ffffff;
}
</style>
