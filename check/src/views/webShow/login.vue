<template>
  <div class="login">
    <el-row class="content">
      <el-col class="title">精神卫生中心360°考核系统</el-col>
      <el-col class="pwd">
        <el-input
          placeholder="请输入发薪号"
          v-model="usercode"
          type="text"
        >
          <template slot="prepend">发薪号</template>
          <i
            slot="suffix"
            :class="eyes?'icon iconfont icon-open-eye':'icon iconfont icon-close-eye'"
          >
          </i>

        </el-input>
      </el-col>
      <el-col
        class="pwd"
        style="margin-top:1rem"
      >
        <el-input
          placeholder="请输入密码"
          v-model="psd"
          type="password"
        >
          <template slot="prepend">密&nbsp;&nbsp;&nbsp;码</template>
          <i
            slot="suffix"
            :class="eyes?'icon iconfont icon-open-eye':'icon iconfont icon-close-eye'"
          >
          </i>

        </el-input>
      </el-col>
      <el-col class="edit-btn">
        <el-button
          type="primary"
          @click="login"
        >确 定</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mobileLogin } from "@/api/login/login";
import VConsole from "vconsole/dist/vconsole.min.js"; //import vconsole
import qs from "qs";
export default {
  data() {
    return {
      usercode: "",
      psd: "",
      eyes: "1",
    };
  },
  components: {},
  mounted() {},
  created() {
    if (this.$store.state.user) {
      //如果没有该字段直接退出登录
      if (!this.$store.state.user.user.medicalEthicsRoleList) {
        this.$router.push({ path: "/webShowLogin" });
        this.$store.commit("$_removeStorage");
        return;
      }
      this.$router.push({
        path: "/web?usercode=" + this.$store.state.user.user.usercode,
        query: {},
      });
    }
  },

  methods: {
    login() {
      if (!this.usercode) {
        this.$toast("请填写发薪号");
        return;
      }
      if (!this.psd) {
        this.$toast("请填写发薪号");
        return;
      }
      let data = {
        moneycard: this.usercode,
        password: this.psd,
      };
      new Promise((response, reject) => {
        mobileLogin(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.$store.commit("$_setStorage", {
                user: response.data.data,
              });
              this.$toast(response.data.msg);
              //多角色
              if (response.data.data.roleList.length > 0) {
                this.$router.push({
                  path: "/web?usercode=" + response.data.data.usercode,
                  query: {},
                });
              } else {
                //医德医风用户
                if (response.data.data.rolecode == "700") {
                  //判断是否同时拥有主任书记角色
                  if (
                    response.data.data.medicalEthicsRoleList.indexOf("300") !=
                      -1 &&
                    response.data.data.medicalEthicsRoleList.indexOf("400") !=
                      -1
                  ) {
                    this.$router.push({
                      path: "/web/ydyfMultiple",
                      query: {},
                    });
                  } else {
                    let list = response.data.data.medicalEthicsRoleList;
                    let type;
                    //当用户只是主任或书记角色的逻辑
                    if (
                      list.indexOf("300") != -1 ||
                      list.indexOf("400") != -1
                    ) {
                      if (list.indexOf("300") != -1) {
                        type = 4;
                      } else if (list.indexOf("400") != -1) {
                        type = 7;
                      }
                      this.$router.push({
                        path: "/web/ydyfMobileList",
                        query: { roleType: type },
                      });
                    }else{
                      this.$router.push({
                        path: "/web/me",
                        query: {},
                      });
                    }
                  }
                }
              }
            } else {
              this.$toast(response.data.msg);
            }
            this.tableLoading = false;
            this.submitLoading = false;
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
.login {
  background: url(../../assets/img/login-bg.jpg);
  background-size: cover;
  width: 100%;
  height: 100%;
  position: absolute;
  .content {
    position: relative;
    width: 90%;
    height: 16rem;
    background: #fff;
    border-radius: 5px;
    top: 5%;
    left: 5%;
    box-shadow: 0px 0px 20px rgba(32, 106, 55, 0.32);
    .title {
      margin: 1rem 0;
      text-align: center;
      font-weight: bold;
      font-size: 0.9rem;
      padding: 0.5rem 0px;
      color: #409EFF;
    }
    .pwd {
      padding: 0px 10px;
      i {
        display: inline-block;
        width: 40px;
        height: 40px;
        position: relative;
        right: -4px;
        line-height: 40px;
      }
      .message {
        margin-bottom: 0.5rem;
        color: #ccc;
        text-align: center;
      }
    }
    .edit-btn {
      position: absolute;
      width: 100%;
      bottom: 0px;
      left: 0px;
      .el-button {
        width: 100%;
        border-radius: 0px 0px 5px 5px;
      }
    }
  }
}
</style>
