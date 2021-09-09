<template>
  <div>
    <h4 class="title">登录提示</h4>
    <el-row class="content">
      <el-form
        :model="ruleForm"
        status-icon
        label-width="130px"
        class="demo-ruleForm"
      >
        <el-form-item
          label="提示信息"
          prop="pass"
        >
          <el-input
            type="text"
            v-model="ruleForm.logininfo"
            class="w200"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="开启"
          prop="pass"
        >
          <el-checkbox v-model="ruleForm.checked"></el-checkbox>
        </el-form-item>
        <el-form-item
          label="评分导入提示信息"
          prop="pass"
          v-if="$store.state.user.user.rolecode == 400"
        >
          <el-input
            type="text"
            v-model="ruleForm.inportinfo"
            class="w200"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="开启"
          prop="pass"
          v-if="$store.state.user.user.rolecode == 400"
        >
          <el-checkbox v-model="ruleForm.inportlogin"></el-checkbox>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            @click="submitForm"
          >提交</el-button>
        </el-form-item>
      </el-form>
    </el-row>
  </div>
</template>

<script>
import { getDetail, updateNotice } from "@/api/notice/notice";
import qs from "qs";
export default {
  data() {
    return {
      ruleForm: {
        logininfo: "",
        checked: false,
        inportinfo: "",
        inportlogin: false
      }
    };
  },
  components: {},
  mounted() {},
  created() {
      this.getDetail();
  },
  methods: {
    getDetail() {
      let data = {
        rolecode : this.$store.state.user.user.rolecode
      }
      new Promise((response, reject) => {
        getDetail(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
                this.ruleForm.logininfo = response.data.data.logininfo;
                if(response.data.data.logintype == 0){
                    this.ruleForm.checked = false;
                }else{
                    this.ruleForm.checked = true;
                }
                this.ruleForm.inportinfo = response.data.data.inportinfo;
                if(response.data.data.inportlogin == 0){
                    this.ruleForm.inportlogin = false;
                }else{
                    this.ruleForm.inportlogin = true;
                }
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    submitForm() {
      let data = {
          logininfo:this.ruleForm.logininfo,
          logintype:this.ruleForm.checked ? "1" : "0",
          inportinfo:this.ruleForm.inportinfo,
          inportlogin:this.ruleForm.inportlogin ? "1" : "0",
          rolecode : this.$store.state.user.user.rolecode
      };
      new Promise((response, reject) => {
        updateNotice(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success"
              });
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
          })
          .catch(error => {
            reject(error);
          });
      });
    }
  }
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
  width: 75%;
}
</style>
