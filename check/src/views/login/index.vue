<template>
  <div class="login">
    <el-row class="logo">
      <!-- <div class="logo-text">武汉市精神卫生中心360度考核系统</div> -->
      <img src="../../assets/img/login-logo.png" />
    </el-row>
    <el-row class="bg">
      <el-col class="content">
        <el-col
          :span="18"
          class="left"
        >
          <el-input
            placeholder="请输入账号"
            v-model="form.moneycard"
            clearable
          >
            <template slot="prepend">账号</template>
          </el-input>
          <el-input
            placeholder="请输入密码"
            v-model="form.password"
            show-password
            clearable
          >
            <template slot="prepend">密码</template>
          </el-input>
        </el-col>
        <el-col
          :span="6"
          class="right"
        >
          <el-button
            @click="login"
            type="primary"
            :loading="isLogin"
          >登录</el-button>
        </el-col>
      </el-col>
    </el-row>
    <el-dialog
      title="请选择登录系统"
      :visible.sync="dialogVisible"
      width="20%"
    >
      <div class="choice-button">
        <el-button
          type="primary"
          class="btn"
          @click="choiceSystem(1)"
        >人事处考核</el-button>
        <el-button
          type="primary"
          class="btn"
          @click="choiceSystem(2)"
        >组织部考核</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { isLogin, isLoginId } from "@/api/login/login";
import { getDetail } from "@/api/notice/notice";
import qs from "qs";
export default {
  data() {
    return {
      form: {
        moneycard: "",
        password: "",
      },
      isLogin: false,
      dialogVisible: false,
      roleList: [],
      data: {},
    };
  },
  components: {},
  mounted() {
    if (
      navigator.userAgent.match(
        /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
      )
    ) {
      this.$router.push({
        path: "/webShowLogin",
        query: {},
      });
    } else {
      this.$router.push({
        path: "/",
        query: {},
      });
    }
  },
  created() {
    let _this = this;
    document.onkeypress = function (e) {
      var keycode = document.all ? event.keyCode : e.which;
      if (keycode == 13) {
        _this.login();
      }
    };
    if (this.$route.query.isoa == 1) {
      this.form.moneycard = this.$route.query.userid;
      this.submitFormId();
    }
  },
  methods: {
    //登录
    login() {
      if (!this.form.moneycard) {
        this.$message({
          message: "请输入账号",
          type: "warning",
        });
        return;
      }
      if (!this.form.password) {
        this.$message({
          message: "请输入密码",
          type: "warning",
        });
        return;
      }
      if (this.isLogin) {
        return;
      }

      this.isLogin = true;
      new Promise((response, reject) => {
        isLogin(qs.stringify(this.form))
          .then((response) => {
            if (response.data.code == 0) {
              this.$store.commit("$_setStorage", {
                user: response.data.data,
              });
              //人事部最高权限状态
              localStorage.setItem("flagStatus", response.data.flagStatus);

              //查询用户是否有两种权限
              if (response.data.data) { // 这里原来是1  现在改为任何人进来 都要选择系统
                // this.dialogVisible = true;
                //查询是否手动考核，并给与提示
                this.manualNotice(response.data.state, response.data.time);
                localStorage.setItem(
                  "roleList",
                  JSON.stringify(response.data.data.roleList)
                );
                localStorage.setItem("data", JSON.stringify(response.data));
                localStorage.setItem("checkState", response.data.state);

                this.$router.push({
                  path: "/index",
                  query: {},
                });
                this.isLogin = false;
                return;
              }
              this.$message({
                message: response.data.msg,
                type: "success",
              });
              document.onkeypress = undefined;                                                                                
              if (response.data.data.rolecode != 600) {
                this.isNotice();
              }
              if (response.data.data.rolecode == 400) {
                this.$router.push({
                  path: "/personnel",
                  query: {},
                });
              } else if (response.data.data.rolecode == 500) {
                localStorage.setItem("data", JSON.stringify(response.data));
                localStorage.setItem("checkState", response.data.state);
                this.$router.push({
                  path: "/index",
                  query: {},
                });
                this.isLogin = false;
                return;
              } else if (response.data.data.rolecode == 600) {
                this.$router.push({
                  path: "/questionnaire",
                  query: {},
                });
              } else if (response.data.data.rolecode == 700) {
                if (
                  response.data.data.medicalEthicsRoleList.indexOf("100") !=
                    -1 ||
                  response.data.data.medicalEthicsRoleList.indexOf("102") != -1
                ) {
                  this.$router.push({
                    path: "/ydyf/personal",
                    query: {},
                  });
                } else if (
                  response.data.data.medicalEthicsRoleList.indexOf("200") != -1
                ) {
                  this.$router.push({
                    path: "/ydyf",
                    query: {},
                  });
                } else if (
                  response.data.data.medicalEthicsRoleList.indexOf("300") != -1
                ) {
                  this.$router.push({
                    path: "/ydyf/departmentPersonnel",
                    query: {},
                  });
                } else if (
                  response.data.data.medicalEthicsRoleList.indexOf("101") != -1
                ) {
                  this.$router.push({
                    path: "/ydyf/personal",
                    query: {},
                  });
                }
              } else {
                //查询是否手动考核，并给与提示
                this.manualNotice(response.data.state, response.data.time);
                this.$router.push({
                  path: "/index",
                  query: {},
                });
              }
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
            this.isLogin = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //查询是否有消息提醒
    isNotice() {
      /*
      let rolecode = this.$store.state.user.user.rolecode;
      let data = {
        rolecode: rolecode,
      };
      new Promise((response, reject) => {
        getDetail(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.notice = response.data.data;
              if (response.data.data.logintype == 1) {
                this.$notify({
                  title: "友情提示",
                  message: response.data.data.logininfo,
                  type: "warning",
                  duration: 0,
                });
              }
              //判断人事部是否有评分导入提示信息
              if (rolecode == 400 || rolecode == 500) {
                if (response.data.data.inportlogin == 1) {
                  this.$notify({
                    title: "友情提示",
                    message: response.data.data.inportinfo,
                    type: "warning",
                    duration: 0,
                  });
                }
              }
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
      */
    },
    //通过oa登录
    submitFormId() {
      let data = qs.stringify({
        usercode: this.form.moneycard,
      });
      return new Promise((response, reject) => {
        isLoginId(data)
          .then((response) => {
            //判断是否是手动考核给出提示
            if (response.data.state == 1) {
              this.manualNotice(response.data.time);
            }
            //成功
            if (response.data.code == 0) {
              this.$store.commit("$_setStorage", {
                user: response.data.data,
              });
              localStorage.setItem("flagStatus", response.data.flagStatus);
              //查询用户是否有两种权限
              if (response.data.data.roleList.length > 1) {
                //查询是否手动考核，并给与提示
                this.manualNotice(response.data.state, response.data.time);
                localStorage.setItem(
                  "roleList",
                  JSON.stringify(response.data.data.roleList)
                );
                localStorage.setItem("data", JSON.stringify(response.data));
                localStorage.setItem("checkState", response.data.state);
                this.$router.push({
                  path: "index",
                  query: {},
                });
                this.isLogin = false;
                return;
              }
              this.$message.success(response.data.msg);
              //判断路由跳转位置
              if (response.data.data.rolecode != 600) {
                this.isNotice();
              }
              if (response.data.data.rolecode == 400) {
                this.$router.push({
                  path: "/personnel",
                  query: {},
                });
                return;
              } else if (response.data.data.rolecode == 500) {
                localStorage.setItem("data", JSON.stringify(response.data));
                localStorage.setItem("checkState", response.data.state);

                this.$router.push({
                  path: "/personnel",
                  query: {},
                });
                this.isLogin = false;
                return;
              } else if (response.data.data.rolecode == 600) {
                this.$router.push({
                  path: "/questionnaire",
                  query: {},
                });
              } else if (response.data.data.rolecode == 700) {
                if (
                  response.data.data.medicalEthicsRoleList.indexOf("100") !=
                    -1 ||
                  response.data.data.medicalEthicsRoleList.indexOf("102") != -1
                ) {
                  this.$router.push({
                    path: "/ydyf/personal",
                    query: {},
                  });
                } else if (
                  response.data.data.medicalEthicsRoleList.indexOf("200") != -1
                ) {
                  this.$router.push({
                    path: "/ydyf",
                    query: {},
                  });
                } else if (
                  response.data.data.medicalEthicsRoleList.indexOf("300") != -1
                ) {
                  this.$router.push({
                    path: "/ydyf/departmentPersonnel",
                    query: {},
                  });
                } else if (
                  response.data.data.medicalEthicsRoleList.indexOf("101") != -1
                ) {
                  this.$router.push({
                    path: "/ydyf/personal",
                    query: {},
                  });
                }
              } else {
                //查询是否手动考核，并给与提示
                this.manualNotice(response.data.state, response.data.time);
                this.$router.push({
                  path: "/home",
                  query: {},
                });
              }
              // if (this.$route.query.redirect) {
              //   this.$router.push({ path: this.$route.query.redirect });
              // } else {
              //   this.$router.push({ path: "home" });
              // }
            } else {
              this.$message.error(response.data.msg);
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //手动考核提示
    manualNotice(state, time) {
      //判断是否是手动考核给出提示
      // if (state == 1) {
      //   this.$notify({
      //     title: "友情提示",
      //     message: "当前组织部考核开启时间改为:" + time,
      //     type: "warning",
      //   });
      // }
    },
    //找回密码
    getBack() {
      this.$router.push({
        path: "/forgetPsd",
        query: {},
      });
    },
  },
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
      margin-bottom:30px;
    }
  }
  .bg {
    width: 580px;
    height: 200px;
    background: url(../../assets/img/login-input-bg.jpg);
    background-size: 100% 100%;
    border-radius: 5px;
    border: 1px solid rgba(251, 255, 250, 0.16);
    -webkit-box-shadow: 1px 2px 5px 0 rgba(0, 0, 0, 0.4);
    box-shadow: 1px 2px 5px 0 rgba(0, 0, 0, 0.4);
    margin-bottom: 10px;
    .content {
      width: 500px;
      height: 120px;
      background: rgba(54, 122, 202, 0.45);
      margin-top: 40px;
      margin-left: 40px;
      border-radius: 5px;
      .left {
        padding: 15px 10px 0px 20px;
        .el-input:first-child {
          margin-bottom: 10px;
        }
      }
      .right {
        padding: 15px 20px 0px 0px;
        .el-button {
          width: 100%;
        }
      }
    }
  }
}
.choice-button {
  display: flex;
  justify-content: center;
  .btn {
    margin: 0 15px;
  }
}
.mt10 {
  margin-top: 10px;
  margin-left: 0;
}
.logo-text {
  position: absolute;
  color: #fff;
  font-weight: bold;
  font-size: 18px;
  width: 250px;
  text-align: center;
  left: 164px;
  top: 56px;
  line-height: 1.2;
  padding: 0 30px;
}
</style>
