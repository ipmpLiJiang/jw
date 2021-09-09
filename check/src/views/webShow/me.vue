<template>
  <div class="me">
    <el-row class="top">
      <el-col
        :span="5"
        class="head"
      >
        <!-- <i class="el-icon-s-custom"></i> -->
        <van-uploader :after-read="afterRead">
          <img
            class="el-icon-s-custom"
            :src="data.picturepath"
            alt=""
            v-if="data.picturepath"
          >
          <i
            class="el-icon-s-custom"
            v-else
          ></i>
        </van-uploader>
      </el-col>
      <el-col
        :span="19"
        class="name"
      >
        <h3>{{data.username}}<span v-if="data.stationname">{{data.departmentname}}</span></h3>
        <h4>{{data.stationname}}</h4>
      </el-col>
    </el-row>
    <el-row>
      <div
        class="list"
        @click="myYdyf"
        v-if="$store.state.user.user.medicalEthicsRoleList.indexOf('200') != -1"
      >
        <el-col class="line-bottom">
          <el-col
            :span="20"
            class="left"
          ><i class="el-icon-notebook-2"></i>我的医德考评</el-col>
          <el-col
            :span="4"
            class="right"
          ><i class="el-icon-arrow-right"></i></el-col>
        </el-col>
      </div>
      <div
        class="list"
        @click="updatePsd"
      >
        <el-col class="line-bottom">
          <el-col
            :span="20"
            class="left"
          ><i class="el-icon-unlock"></i>修改密码</el-col>
          <el-col
            :span="4"
            class="right"
          ><i class="el-icon-arrow-right"></i></el-col>
        </el-col>
      </div>
      <div
        class="list"
        @click="loginOut"
      >
        <el-col class="line-bottom">
          <el-col
            :span="20"
            class="left"
          ><i class="iconfont icon-dingbudaohang-zhangh"></i>退出登录</el-col>
          <el-col
            :span="4"
            class="right"
          ><i class="el-icon-arrow-right"></i></el-col>
        </el-col>
      </div>
      <!-- <div
        class="list"
        @click="updatePsd"
      >
        <el-col class="line-bottom">
          <el-col
            :span="20"
            class="left"
          ><i class="el-icon-s-order"></i>历史查看</el-col>
          <el-col
            :span="4"
            class="right"
          ><i class="el-icon-arrow-right"></i></el-col>
        </el-col>
      </div>
      <div
        class="list"
        @click="updatePsd"
      >
        <el-col class="line-bottom">
          <el-col
            :span="20"
            class="left"
          ><i class="el-icon-s-tools"></i>系统更新</el-col>
          <el-col
            :span="4"
            class="right"
          ><i class="el-icon-arrow-right"></i></el-col>
        </el-col>
      </div>
      <div
        class="list"
        @click="updatePsd"
      >
        <el-col class="line-bottom">
          <el-col
            :span="20"
            class="left"
          ><i class="el-icon-s-help"></i>关于我们</el-col>
          <el-col
            :span="4"
            class="right"
          ><i class="el-icon-arrow-right"></i></el-col>
        </el-col>
      </div> -->
    </el-row>
  </div>
</template>

<script>
import { upload } from "@/api/common/common";
import { findUserByUserCode } from "@/api/mobile/check";
import qs from "qs";
export default {
  data() {
    return {
      data: {},
    };
  },
  components: {},
  created() {
    this.getDetail();
  },
  methods: {
    // 返回 Promise
    afterRead(file) {
      // 此时可以自行将文件上传至服务器
      let data = new FormData();
      data.append("usercode", this.$store.state.user.user.usercode);
      data.append("file", file.file);
      new Promise((response, reject) => {
        upload(data)
          .then((response) => {
            if (response.data.code == 0) {
              this.data.picturepath = response.data.picturepath;
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
    updatePsd() {
      this.$router.push({
        path: "/web/mobileUpdatePsd",
        query: { usercode: this.$store.state.user.user.usercode },
      });
    },
    myYdyf() {
      this.$router.push({
        path: "/web/ydyfUser",
        query: {},
      });
    },
    getDetail() {
      let data = {};
      data.usercode = this.$store.state.user.user.usercode;
      new Promise((response, reject) => {
        findUserByUserCode(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.data = response.data.data;
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
    loginOut() {
      this.$dialog
        .confirm({
          title: "友情提示",
          message: "是否确定退出该系统？",
          confirmButtonColor: "#409EFF",
        })
        .then(() => {
          // on confirm
          this.$router.push({ path: "/webShowLogin" });
          this.$store.commit("$_removeStorage");
        })
        .catch(() => {
          // on cancel
        });
    },
  },
};
</script>


<style lang="scss" scoped>
.top {
  background: #fff;
  padding: 2em 10px;
  margin-bottom: 10px;
  .head {
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: row;
    .el-icon-s-custom {
      color: #ddd;
      width: 4rem;
      height: 4rem;
      display: block;
      background: whitesmoke;
      text-align: center;
      line-height: 4rem;
      border-radius: 10px;
      font-size: 3rem;
      object-fit: cover;
    }
  }
  .name {
    padding-left: 10px;
    h3 {
      font-size: 1.8rem;
      color: #333;
      span {
        font-size: 0.8rem;
        background: #409EFF;
        color: #fff;
        padding: 0.2rem 0.3rem;
        margin-left: 10px;
        position: relative;
        top: -0.2rem;
        border-radius: 2px;
      }
    }
    h4 {
      font-size: 0.8rem;
      color: #acacac;
    }
  }
}
.list {
  position: relative;
  height: 45px;
  line-height: 45px;
  background: #fff;
  .left {
    padding-left: 1rem;
    i {
      margin-right: 0.4rem;
    }
  }
  .right {
    padding-right: 1rem;
    text-align: right;
  }
}
</style>
