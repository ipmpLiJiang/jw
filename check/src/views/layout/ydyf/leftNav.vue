<template>
  <div>
    <h5 class="user-name">
      欢迎您，
      <span>{{ $store.state.user.user.username }}</span>
    </h5>
    <el-menu
      :default-active="activeIndex"
      :unique-opened="true"
      active-text-color="#409EFF"
      class="el-menu-vertical-demo"
      @open="handleOpen"
      @close="handleClose"
    >
      <el-submenu
        index="1"
        v-if="roleList.indexOf('200') != -1"
      >
        <!-- <el-submenu index="1"> -->
        <template slot="title">
          <i class="iconfont icon-pingfenshoucang-"></i>
          <span>我的考评</span>
        </template>
        <router-link to="/ydyf/evaluationList">
          <el-menu-item index="/ydyf/evaluationList">
            <i class="iconfont icon-pingfenshoucang-"></i>
            我的考评
          </el-menu-item>
        </router-link>
        <!-- <router-link
          to="/ydyf/nonclinical"
          v-if="personType == '1' || role == '100'"
        > -->
        <!-- <router-link to="/ydyf/nonclinical">
          <el-menu-item index="/ydyf/nonclinical">
            <i class="iconfont icon-pingfenshoucang-"></i>
            我的考评(非临床)
          </el-menu-item>
        </router-link> -->
        <!-- <router-link
          to="/ydyf/clinical"
          v-if="personType == '0' || role == '100'"
        > -->
        <!-- <router-link to="/ydyf/clinical">
          <el-menu-item index="/ydyf/clinical">
            <i class="iconfont icon-pingfenshoucang-"></i>
            我的考评(临床)
          </el-menu-item>
        </router-link> -->
      </el-submenu>

      <!-- <el-submenu index="2"> -->
      <el-submenu
        index="2"
        v-if="roleList.indexOf('100') != -1 || roleList.indexOf('101') != -1 || roleList.indexOf('102') != -1 "
      >
        <template slot="title">
          <i class="iconfont icon-renyuanguanli"></i>
          <span>人员管理</span>
        </template>
        <router-link to="/ydyf/personal">
          <el-menu-item index="/ydyf/personal">
            <i class="iconfont icon-renyuanguanli"></i>
            <span slot="title">全部人员</span>
          </el-menu-item>
        </router-link>
        <router-link to="/ydyf/submitted">
          <el-menu-item index="/ydyf/submitted">
            <i class="iconfont icon-tuifeiyitijiao"></i>
            <span slot="title">已提交人员</span>
          </el-menu-item>
        </router-link>
        <router-link to="/ydyf/notSubmitted">
          <el-menu-item index="/ydyf/notSubmitted">
            <i class="iconfont icon-weitijiao"></i>
            <span slot="title">未提交人员</span>
          </el-menu-item>
        </router-link>
         <router-link to="/ydyf/jurisdictionList" v-if="roleList.indexOf('100') != -1">
          <el-menu-item index="/ydyf/jurisdictionList">
            <i class="iconfont icon-quanxianguanli4"></i>
            <span slot="title">人员权限管理</span>
          </el-menu-item>
        </router-link>
      </el-submenu>

      <el-submenu
        index="3"
        v-if="roleList.indexOf('100') != -1 || roleList.indexOf('102') != -1"
      >
        <!-- <el-submenu index="3"> -->
        <template slot="title">
          <i class="iconfont icon-manager"></i>
          <span>书记信息管理</span>
        </template>
        <router-link to="/ydyf/secretary">
          <el-menu-item index="/ydyf/secretary">
            <i class="iconfont icon-manager"></i>
            <span slot="title">书记信息管理</span>
          </el-menu-item>
        </router-link>
        <router-link to="/ydyf/director">
          <el-menu-item index="/ydyf/director">
            <i class="iconfont icon-banzhuren"></i>
            <span slot="title">主任信息管理</span>
          </el-menu-item>
        </router-link>
      </el-submenu>
      <!-- <el-submenu index="4" v-if="roleList.indexOf('100') != -1 || roleList.indexOf('101') != -1 || roleList.indexOf('300') != -1 || roleList.indexOf('102') != -1">
         -->
          <el-submenu index="4" v-if="roleList.indexOf('100') != -1">
     
    
        <template slot="title" >
          <i class="iconfont icon-yuangongkaopingjiandingbiao"></i>
          <span>考核统计分析</span>
        </template>
        <router-link to="/ydyf/analyseList" >
          <el-menu-item index="/ydyf/analyseList">
            <i class="iconfont icon-chengji"></i>
            <span slot="title">考核统计分析列表</span>
          </el-menu-item>
        </router-link>
        <!-- <router-link to="/ydyf/analyse"  v-if="roleList.indexOf('100') != -1 || roleList.indexOf('102') != -1"> -->
        <router-link to="/ydyf/analyse"  v-if="roleList.indexOf('100') != -1 ">
          <el-menu-item index="/ydyf/analyse">
            <i class="iconfont icon-yuangongkaopingjiandingbiao"></i>
            <span slot="title">考核统计分析</span>
          </el-menu-item>
        </router-link>
         <!-- <router-link to="/ydyf/tabulateList"  v-if="roleList.indexOf('100') != -1 "> -->
          <router-link to="/ydyf/tabulateList"  v-if="roleList.indexOf('100') != -1 || roleList.indexOf('102') != -1">
          <el-menu-item index="/ydyf/tabulateList">
            <i class="iconfont icon-2shujuliebiao"></i>
            <span slot="title">医德考评列表</span>
          </el-menu-item>
        </router-link>
        
      </el-submenu> 

      <el-submenu
        index="5"
        v-if="roleList.indexOf('100') != -1|| roleList.indexOf('102') != -1" 
      >
        <!-- <el-submenu index="5" > -->

        <template slot="title">
          <i class="iconfont icon-dangjian"></i>
          <span>党支部信息</span>
        </template>
        <router-link to="/ydyf/party">
          <el-menu-item index="/ydyf/party">
            <i class="iconfont icon-dangjian"></i>
            <span slot="title">党支部信息维护</span>
          </el-menu-item>
        </router-link>

      </el-submenu>
      <el-submenu
        index="6"
        v-if="roleList.indexOf('100') != -1 || roleList.indexOf('300') != -1 || roleList.indexOf('101') != -1 || roleList.indexOf('102') != -1"
      >
        <!-- <el-submenu index="6" v-if="roleList.indexOf('100') != -1"> -->

        <template slot="title">
          <i class="iconfont icon-lingdaoguanhuai"></i>
          <span>科室信息</span>
        </template>
        <router-link
          to="/ydyf/deparTment"
          v-if="roleList.indexOf('100') != -1 || roleList.indexOf('102') != -1"
        >
          <el-menu-item index="/ydyf/deparTment">
            <i class="iconfont icon-lingdaoguanhuai"></i>
            <span slot="title">科室信息维护</span>
          </el-menu-item>
        </router-link>

        <router-link to="/ydyf/departmentPersonnel">
          <el-menu-item index="/ydyf/departmentPersonnel">
            <i class="iconfont icon-bumenrenyuanguanli"></i>
            <span slot="title">科室人员管理</span>
          </el-menu-item>
        </router-link>

      </el-submenu>
      <el-submenu
        index="7"
        v-if="roleList.indexOf('400') != -1|| roleList.indexOf('300') != -1" 
      >

        <!-- <el-submenu index="7" v-if="roleList.indexOf('100') != -1 "> -->

        <!-- <el-submenu index="6" > -->

        <template slot="title">
          <i class="iconfont icon-kaoheguanlix"></i>
          <span>考核管理</span>
        </template>
        <router-link to="/ydyf/yesAssessment">
          <el-menu-item index="/ydyf/yesAssessment">
            <i class="iconfont icon-kaohe"></i>
            <span slot="title">已考核人员</span>
          </el-menu-item>
        </router-link>
        <router-link to="/ydyf/notAssessment">
          <el-menu-item index="/ydyf/notAssessment">
            <i class="iconfont icon-kaoheshezhi"></i>
            <span slot="title">待考核人员</span>
          </el-menu-item>
        </router-link>
      </el-submenu>

    </el-menu>
  </div>
</template>

<script>
import { authority } from "@/views/ydyf/api/addData/addData.js";
import qs from "qs";
export default {
  data() {
    return {
      role: "",
      personType: "0",
      roleList: [],
    };
  },
  created() {
    this.roleList = this.$store.state.user.user.medicalEthicsRoleList;
    console.log(this.$store.state.user.user.medicalEthicsRoleList)
  },
  computed: {
    activeIndex() {
      return this.$route.path;
    },
  },
  methods: {
    handleOpen(key, keyPath) {},
    handleClose(key, keyPath) {},
    // jurisdiction() {
    //   new Promise((response, reject) => {
    //     authority()
    //       .then((response) => {

    //         this.$store.commit("$_setStorageYDYF", {
    //           YdyfRole: response.data.data.roleCode,
    //         });
    //            this.$store.commit("$_setStorageYdyfRoleList", {
    //           YdyfRoleList: response.data.data.roleList,
    //         });
    //         localStorage.setItem("YdyfRoleList", response.data.data.roleList);
    //         localStorage.setItem("YdyfRole", response.data.data.roleCode);
    //         this.roleList=response.data.data.roleList
    //         if (response.data.code == 0) {
    //           (this.role = response.data.data.roleCode),
    //             (this.personType = response.data.data.personType);
    //           // if(response.data.data.roleList.indexOf('200')!=-1|| roleList.indexOf('100') != -1){
    //           //     this.$router.push({
    //           //     path: "evaluationList",
    //           //     query: {},
    //           //   });
    //           // }else{
    //           //     this.$router.push({
    //           //     path: "ydyf",
    //           //     query: {},
    //           //   });
    //           // }
    //           // if (response.data.data.personType == 0) {

    //           // } else {
    //           //   this.$router.push({
    //           //     path: "/ydyf/nonclinical",
    //           //     query: {},
    //           //   });
    //           // }
    //         } else {
    //           this.$message({
    //             message: response.data.msg,
    //             type: "error",
    //           });
    //         }
    //       })
    //       .catch((error) => {
    //         reject(error);
    //       });
    //   });
    // },
  },
};
</script>
<style lang="scss">
.el-menu-item,
.el-submenu__title {
  height: 45px;
  line-height: 45px;
  border-bottom: 1px solid #f5f5f5;
}
.el-submenu.is-active .el-submenu__title {
  border-bottom: none;
  background: #fafafa;
}
.el-aside {
  background-color: #fff;
  text-align: left;
  border-right: 1px solid #eaeaea;
  .el-menu {
    border-right: none;
  }
  .user-name {
    height: 50px;
    line-height: 50px;
    border-bottom: 1px solid #eaeaea;
    background: #fff;
    padding: 0px 10px;
  }
  .iconfont {
    margin-right: 5px;
    width: 24px;
    text-align: center;
    font-size: 18px;
    vertical-align: middle;
    display: inline-block;
  }
}
</style>
