<template>
  <div class="login">
    <el-row class="logo">
      <img src="../../assets/img/login-logo.png" />
    </el-row>
    <el-row
      class="bg"
      :class="{'step1':step == 1}"
    >
      <el-col
        class="content"
        v-if="step == 1"
      >
        <el-col
          :span="18"
          class="left"
        >
          <el-input
            placeholder="请输入发薪号"
            v-model="moneycard"
            clearable
          >
            <template slot="prepend">发薪号</template>
          </el-input>
        </el-col>
        <el-col
          :span="6"
          class="right"
        >
          <el-button
            @click="nextStep"
            type="primary"
            :loading="nextLoading"
          >下一步</el-button>
        </el-col>
      </el-col>
      <el-col
        class="content"
        v-if="step == 2"
      >
        <div class="form-box">
          <el-col
            :span="24"
            class="left"
          >
            <el-input
              v-model="mobile"
              clearable
              :disabled="true"
            >
              <template slot="prepend">手机号</template>
            </el-input>
          </el-col>
        </div>
        <div class="form-box">
          <el-col
            :span="18"
            class="left"
          >
            <el-input
              placeholder="请输入验证码"
              v-model="code"
              clearable
            >
              <template slot="prepend">验证码</template>
            </el-input>
          </el-col>
          <el-col
            :span="6"
            class="right"
          >
            <el-button
              @click="sendCode"
              type="primary"
              :disabled="codeDisabled"
            >{{btnTitle}}</el-button>
          </el-col>
        </div>
        <div class="form-box">
          <el-col
            :span="24"
            class="left"
          >
            <el-input
              placeholder="请输入新密码"
              v-model="newPsd"
              clearable
              show-password
            >
              <template slot="prepend">新密码</template>
            </el-input>
          </el-col>
        </div>
        <div class="form-box">
          <el-col
            :span="24"
            class="left"
          >
            <el-input
              placeholder="请再次输入新密码"
              v-model="resPsd"
              clearable
              show-password
            >
              <template slot="prepend">确认密码</template>
            </el-input>
          </el-col>
        </div>
        <div class="form-box">
          <el-col
            :span="12"
            class="right"
          >
            <el-button
              @click="submit"
              type="primary"
            >确认修改</el-button>
          </el-col>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {
  getUserByMoneyCard,
  sendCheckCode,
  updateNewPassword
} from "@/api/user/user";
import qs from "qs";
export default {
  data() {
    return {
      moneycard: "",
      mobile: "",
      code: "",
      newPsd: "",
      resPsd: "",
      data: {},
      step: 1,
      btnTitle: "发送验证码",
      codeDisabled: false,
      repetitionSend: false,
      nextLoading: false,
      repetitionSubmit: false
    };
  },
  components: {},
  mounted() {},
  created() {
    let _this = this;
    document.onkeypress = function(e) {
      var keycode = document.all ? event.keyCode : e.which;
      if (keycode == 13) {
        _this.login();
      }
    };
  },
  methods: {
    //查询是否有发薪号
    nextStep() {
      if (!this.moneycard) {
        this.$message.warning("请先输入发薪号");
        return;
      }
      let data = {
        moneycard: this.moneycard
      };
      new Promise((response, reject) => {
        getUserByMoneyCard(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.mobile = response.data.data.mobile;
              if (!this.mobile || this.mobile.length != 11) {
                this.$message({
                  message:
                    "该用户未检查到手机号或手机号有误,请联系管理员添加手机号码后在尝试",
                  type: "warning",
                  duration: 15000
                });
                return;
              }
              this.step = 2;
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
    //发送验证码
    sendCode() {
      if (this.repetitionSend) {
        return;
      }
      this.repetitionSend = true;
      this.validateBtn();
      let data = {
        moneycard: this.moneycard
      };
      new Promise((response, reject) => {
        sendCheckCode(qs.stringify(data))
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
            this.repetitionSend = false;
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    //倒计时
    validateBtn() {
      let time = 60;
      let timer = setInterval(() => {
        if (time == 0) {
          clearInterval(timer);
          this.codeDisabled = false;
          this.repetitionSend = false;
          this.btnTitle = "发送验证码";
        } else {
          this.btnTitle = time + "秒后重试";
          this.codeDisabled = true;
          time--;
        }
      }, 1000);
    },
    //提交
    submit() {
      if (this.repetitionSubmit) {
        return;
      }
      if (!this.code || this.code.length != 6) {
        this.$message.warning("请输入正确的验证码");
        return;
      }
      if (!this.newPsd) {
        this.$message.warning("请输入新密码");
        return;
      }
      if (this.newPsd !== this.resPsd) {
        this.$message.warning("两次新密码不一致");
        return;
      }
      let data = {
        moneycard: this.moneycard,
        checkcode: this.code,
        password: this.newPsd
      };
      this.repetitionSubmit = true;
      new Promise((response, reject) => {
        updateNewPassword(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success"
              });
              this.$router.push({
                path: "/login",
                query: {}
              });
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
                duration:8000
              });
            }
            this.repetitionSubmit = false;
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
.login {
  background: url(../../assets/img/login-bg.jpg);
  background-size: 100% 100%;
  width: 100%;
  height: 100%;
  position: absolute;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  .logo {
    width: 580px;
    margin-top: -140px;
    img {
      width: 500px;
      margin-left: 40px;
    }
  }
  .bg {
    width: 580px;
    height: 340px;
    background: url(../../assets/img/login-input-bg.jpg);
    background-size: 100% 100%;
    border-radius: 5px;
    border: 1px solid rgba(251, 255, 250, 0.16);
    -webkit-box-shadow: 1px 2px 5px 0 rgba(0, 0, 0, 0.4);
    box-shadow: 1px 2px 5px 0 rgba(0, 0, 0, 0.4);
    margin-bottom: 10px;
    .content {
      width: 500px;
      height: 260px;
      background: rgba(54, 122, 202, 0.45);
      margin-top: 40px;
      margin-left: 40px;
      border-radius: 5px;
      display: flex;
      justify-content: center;
      align-items: center;
      flex-wrap: wrap;
      padding: 10px 0;
      box-sizing: border-box;
      .form-box {
        width: 450px;
        display: flex;
        justify-content: center;
      }
      .left {
        padding: 0px 10px;
      }
      .right {
        padding-right: 10px;
        .el-button {
          width: 100%;
        }
      }
    }
  }
  .step1 {
    height: 150px;
    .content {
      height: 70px;
    }
  }
}
.left-title {
  width: 120px;
  text-align: center;
}
.choice-button {
  display: flex;
  justify-content: center;
  .btn {
    margin: 0 15px;
  }
}
</style>
