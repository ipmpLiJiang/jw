<template>
  <div class="indexNew">
    <el-header>
      <TopNav></TopNav>
    </el-header>
    <el-row class="container">
      <el-col class="left">
       <el-col class="card">
          <div
            class="top-btn more"
            @click="choiceSystem(1)"
          >
            <i class="icon iconfont icon-keyan"></i>党部考核
          </div>
        </el-col> 
        <el-col class="card">
          <div
            class="top-btn more"
            @click="choiceSystem(2)"
          >
            <i class="icon iconfont icon-keyanxitong"></i>干部考核
          </div>
        </el-col>
        <!-- <el-col
          class="card"
          v-if="isQuestionnaire"
        >
          <router-link to="/questionnaire">
            <div class="top-btn more">
              <i class="icon iconfont icon-keyanxitong"></i>调查问卷
            </div>
          </router-link>
        </el-col>
        <el-col class="card" v-if="jurisdiction">
          <router-link to="/admin/jurisdiction">
            <div class="top-btn more">
              <i class="icon iconfont icon-keyanxitong"></i>管理员配置
            </div>
          </router-link>
        </el-col> -->
<!-- 
         <el-col class="card">
          <template v-if="$store.state.user.user.medicalEthicsRoleList.indexOf('100')!=-1">
            <router-link to="/ydyf/personal">
              <div class="top-btn more" >
                <i class="icon iconfont icon-keyanxitong"></i>医德考评
              </div>
            </router-link>
          </template>
          <template v-else-if="$store.state.user.user.medicalEthicsRoleList.indexOf('101')!=-1 || $store.state.user.user.medicalEthicsRoleList.indexOf('300')!=-1">
            <router-link to="/ydyf/yesAssessment">
              <div class="top-btn more" >
                <i class="icon iconfont icon-keyanxitong"></i>医德考评
              </div>
            </router-link>
          </template>
          <template v-else-if="$store.state.user.user.medicalEthicsRoleList.indexOf('200')!=-1">
            <router-link to="/ydyf">
              <div class="top-btn more" >
                <i class="icon iconfont icon-keyanxitong"></i>医德考评
              </div>
            </router-link>
          </template>
        </el-col> -->
      </el-col>
    </el-row>
  </div>
</template>

<script>
import TopNav from "../layout/TopNav";
import { getDetail } from "@/api/notice/notice";
import qs from "qs";
export default {
  data() {
    return {
      roleList: [],
      data: {},
      isQuestionnaire: false,
      rolecode: "",
      jurisdiction: false,
      MedicalEthics:false
    };
  },
  created() {
    this.roleList = this.$store.state.user.user.roleList;
   
    this.rolecode = this.$store.state.user.user.rolecode;
    this.data = JSON.parse(localStorage.getItem("data"));
    if (this.roleList.length > 0) {
      this.roleList.forEach((row) => {
        
        //问卷调查权限
        if (row.rolecode == "50" || row.rolecode == "600") {
          this.isQuestionnaire = true;
        }
        //角色分配权限
        if (row.rolecode == "50") {
          this.jurisdiction = true;
        }
         //医德医风权限
        if (row.rolecode == "700") {
          this.MedicalEthics = true;
        }
      });
    } else {
      //问卷调查权限
      if (this.rolecode == "50" || this.rolecode == "600") {
        this.isQuestionnaire = true;
      }
      //角色分配权限
      if (this.rolecode == "50") {
        this.jurisdiction = true;
      }
      //  //医德医风权限
      //   if (row.rolecode == "700") {
      //     this.MedicalEthics = true;
      //   }
    }
  },
  components: {
    TopNav,
  },
  methods: {
    //选择系统
    choiceSystem(type) {
      // if (type == 1) {
      //   this.data.data.rolecode = this.roleList[1]["rolecode"];
      //   this.data.data.rolename = this.roleList[1]["rolename"];
      // } else if (type == 2) {
        if(this.roleList.length>0){
        this.data.data.rolecode = this.roleList[0]["rolecode"];
        this.data.data.rolename = this.roleList[0]["rolename"];
        }
      //}
      // 党部考核 还是普通考核
      this.data.data.dbtype = type
      this.$message({
        message: this.data.msg,
        type: "success",
      });
      this.$store.commit("$_setStorage", {
        user: this.data.data,
      });
      document.onkeypress = undefined;
      this.isNotice();
      // if (type == 1) {
      //   this.$router.push({
      //     path: "/personnel",
      //     query: {},
      //   });
      // } else {
        this.$router.push({
          path: "/home",
          query: {},
        });
      //}
    },
    //查询是否有消息提醒
    isNotice() {
      let rolecode = this.$store.state.user.user.rolecode;
      let data = {
        rolecode: rolecode,
      };
      new Promise((response, reject) => {
        getDetail(qs.stringify(data))
          .then((response) => {
            console.log(response)
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
    },
  },
};
</script>
<style lang="scss" scoped>
.indexNew {
  position: absolute;
  width: 100%;
  height: 100%;
  background: #fff;
  background: -webkit-linear-gradient(
    bottom right,
    #e7f2fc,
    #fff
  ); /* Safari 5.1 - 6.0 */
  background: -o-linear-gradient(
    bottom left,
    #e7f2fc,
    #fff
  ); /* Opera 11.1 - 12.0 */
  background: -moz-linear-gradient(
    bottom left,
    #e7f2fc,
    #fff
  ); /* Firefox 3.6 - 15 */
  background: linear-gradient(to bottom left, #e7f2fc, #fff); /* 标准的语法 */
  .el-header {
    position: fixed;
    left: 0px;
    top: 0px;
    width: 100%;
    z-index: 10;
  }
  .container {
    padding: 70px 10px 10px 10px;
    position: relative;
    height: 100%;
    background: url("../../assets/img/xiehe-bg.jpg") no-repeat 0% 0%;
    background-size: cover;
    display: flex;
    display: -webkit-flex;
    align-items: center;
    justify-content: center;
  }
  .left {
    width: 900px;
    display: flex;
    display: -webkit-flex;
    justify-content: space-around;
    .card {
      width: 160px;
      height: 160px;
      margin: 10px;
    }
    .top-btn {
      border-radius: 6px;
      height: 160px;
      display: flex;
      display: -webkit-flex;
      align-items: center;
      justify-content: center;
      flex-direction: column;
      font-size: 18px;
      color: #fff;
      i {
        font-size: 60px;
        color: rgba(255, 255, 255, 0.7);
      }
    }
    //    .one{
    //        background: -webkit-linear-gradient(bottom right, #FDEB71 , #F8D800); /* Safari 5.1 - 6.0 */
    //        background: -o-linear-gradient(bottom left, #FDEB71, #F8D800); /* Opera 11.1 - 12.0 */
    //        background: -moz-linear-gradient(bottom left, #FDEB71, #F8D800); /* Firefox 3.6 - 15 */
    //        background: linear-gradient(to bottom left, #FDEB71 , #F8D800); /* 标准的语法 */
    //    }
    //    .one:hover{
    //        background: -webkit-linear-gradient(bottom right, #F8D800 , #FDEB71); /* Safari 5.1 - 6.0 */
    //        background: -o-linear-gradient(bottom left, #F8D800, #FDEB71); /* Opera 11.1 - 12.0 */
    //        background: -moz-linear-gradient(bottom left, #F8D800, #FDEB71); /* Firefox 3.6 - 15 */
    //        background: linear-gradient(to bottom left, #F8D800 , #FDEB71); /* 标准的语法 */
    //    }
    //    .two{
    //        background: -webkit-linear-gradient(bottom right, #ABDCFF , #0396FF); /* Safari 5.1 - 6.0 */
    //        background: -o-linear-gradient(bottom left, #ABDCFF, #0396FF); /* Opera 11.1 - 12.0 */
    //        background: -moz-linear-gradient(bottom left, #ABDCFF, #0396FF); /* Firefox 3.6 - 15 */
    //        background: linear-gradient(to bottom left, #ABDCFF , #0396FF); /* 标准的语法 */
    //    }
    //    .two:hover{
    //        background: -webkit-linear-gradient(bottom right, #0396FF , #ABDCFF); /* Safari 5.1 - 6.0 */
    //        background: -o-linear-gradient(bottom left, #0396FF, #ABDCFF); /* Opera 11.1 - 12.0 */
    //        background: -moz-linear-gradient(bottom left, #0396FF, #ABDCFF); /* Firefox 3.6 - 15 */
    //        background: linear-gradient(to bottom left, #0396FF , #ABDCFF); /* 标准的语法 */
    //    }
    //    .three{
    //        background: -webkit-linear-gradient(bottom right, #FEB692 , #EA5455); /* Safari 5.1 - 6.0 */
    //        background: -o-linear-gradient(bottom left, #FEB692, #EA5455); /* Opera 11.1 - 12.0 */
    //        background: -moz-linear-gradient(bottom left, #FEB692, #EA5455); /* Firefox 3.6 - 15 */
    //        background: linear-gradient(to bottom left, #FEB692 , #EA5455); /* 标准的语法 */
    //    }
    //    .three:hover{
    //        background: -webkit-linear-gradient(bottom right, #EA5455 , #FEB692); /* Safari 5.1 - 6.0 */
    //        background: -o-linear-gradient(bottom left, #EA5455, #FEB692); /* Opera 11.1 - 12.0 */
    //        background: -moz-linear-gradient(bottom left, #EA5455, #FEB692); /* Firefox 3.6 - 15 */
    //        background: linear-gradient(to bottom left, #EA5455 , #FEB692); /* 标准的语法 */
    //    }
    //    .four{
    //        background: -webkit-linear-gradient(bottom right, #ce9ffc , #7367f0); /* Safari 5.1 - 6.0 */
    //        background: -o-linear-gradient(bottom left, #ce9ffc, #7367f0); /* Opera 11.1 - 12.0 */
    //        background: -moz-linear-gradient(bottom left, #ce9ffc, #7367f0); /* Firefox 3.6 - 15 */
    //        background: linear-gradient(to bottom left, #ce9ffc , #7367f0); /* 标准的语法 */
    //    }
    //    .four:hover{
    //        background: -webkit-linear-gradient(bottom right, #7367f0 , #ce9ffc); /* Safari 5.1 - 6.0 */
    //        background: -o-linear-gradient(bottom left, #7367f0, #ce9ffc); /* Opera 11.1 - 12.0 */
    //        background: -moz-linear-gradient(bottom left, #7367f0, #ce9ffc); /* Firefox 3.6 - 15 */
    //        background: linear-gradient(to bottom left, #7367f0 , #ce9ffc); /* 标准的语法 */
    //    }
    .more {
      background: -webkit-linear-gradient(
        bottom right,
        #32ccbc,
        #4ab1a6
      ); /* Safari 5.1 - 6.0 */
      background: -o-linear-gradient(
        bottom left,
        #32ccbc,
        #4ab1a6
      ); /* Opera 11.1 - 12.0 */
      background: -moz-linear-gradient(
        bottom left,
        #32ccbc,
        #4ab1a6
      ); /* Firefox 3.6 - 15 */
      background: linear-gradient(
        to bottom left,
        #32ccbc,
        #4ab1a6
      ); /* 标准的语法 */
    }
    .more:hover {
      background: -webkit-linear-gradient(
        bottom right,
        #1c998c,
        #24ada0
      ); /* Safari 5.1 - 6.0 */
      background: -o-linear-gradient(
        bottom left,
        #1c998c,
        #24ada0
      ); /* Opera 11.1 - 12.0 */
      background: -moz-linear-gradient(
        bottom left,
        #1c998c,
        #24ada0
      ); /* Firefox 3.6 - 15 */
      background: linear-gradient(
        to bottom left,
        #1c998c,
        #24ada0
      ); /* 标准的语法 */
    }
    .news {
      .title {
        height: 40px;
        line-height: 40px;
        border-bottom: 1px solid #f3f4f6;
        padding: 0px 10px;
        .el-button {
          float: right;
          position: relative;
          top: 7px;
        }
      }
      .content {
        padding: 20px;
        min-height: 250px;
        .list {
          margin: 10px 0px;
          .name {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
          .time {
            text-align: right;
          }
        }
        .user {
          p {
            margin-bottom: 10px;
            span {
              font-weight: bold;
              margin-left: 10px;
            }
          }
        }
      }
    }
  }
}
</style>

