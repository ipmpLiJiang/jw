<template>
  <div>
    <h4 class="title">修改密码</h4>
    <el-row class="content">
      <el-form
        :model="ruleForm"
        status-icon
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item
          label="旧密码"
          prop="pass"
        >
          <el-input
            type="password"
            v-model="ruleForm.password"
            class="w200"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="新密码"
          prop="pass"
        >
          <el-input
            type="password"
            v-model="ruleForm.newpassword"
            class="w200"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="确认密码"
          prop="checkPass"
        >
          <el-input
            type="password"
            v-model="ruleForm.respassword"
            class="w200"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="submitForm"
          >提交</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-row>
  </div>
</template>

<script>
import { updateUserPassword } from "@/api/user/user";
import qs from "qs";
export default {
  data() {
    return {
      ruleForm: {
        password: "",
        newpassword: "",
        respassword: ""
      }
    };
  },
  components: {},
  mounted() {},
  created() {
  },
  methods: {
    submitForm() {
      if (!this.ruleForm.password) {
        this.$message.warning("请输入老密码");
        return;
      }
      if (!this.ruleForm.newpassword) {
        this.$message.warning("请输入新密码");
        return;
      }
      if (!this.ruleForm.respassword) {
        this.$message.warning("请输入确认密码");
        return;
      }
      if (this.ruleForm.newpassword !== this.ruleForm.respassword) {
        this.$message.warning("两次新密码不一致");
        return;
      }
      let data = this.ruleForm;
      data.usercode = this.$store.state.user.user.usercode;
      new Promise((response, reject) => {
        updateUserPassword(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success"
              });
              this.$store.commit("$_removeStorage");
              this.$router.push({ path: "/" });
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
    resetForm() {
      this.ruleForm = {
        password: "",
        newpassword: "",
        respassword: ""
      };
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
  width: 260px;
}
</style>
