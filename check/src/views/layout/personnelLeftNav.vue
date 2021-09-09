<template>
  <div>
    <h5 class="user-name">欢迎您，<span>{{$store.state.user.user.username}}</span></h5>
    <el-menu
      :default-active="activeIndex"
      :unique-opened='true'
      active-text-color='#409EFF'
      class="el-menu-vertical-demo"
      @open="handleOpen"
      @close="handleClose"
    >
      <router-link to="/personnel">
        <el-menu-item index="/personnel">
          <i class="iconfont icon-daoru"></i>
          <span slot="title">评分导入</span>
        </el-menu-item>
      </router-link>
      <!-- <router-link
        to="/personnel/principalSignatureList"
        v-if="role == '400'"
      >
        <el-menu-item index="/personnel/principalSignatureList">
          <i class="iconfont icon-jixiaopinggu"></i>
          <span slot="title">负责人签字pdf列表</span>
        </el-menu-item>
      </router-link> -->
      <router-link to="/personnel/deptCheck">
        <el-menu-item index="/personnel/deptCheck">
          <i class="iconfont icon-kaoheshezhi_"></i>
          <span slot="title">部门考核办法</span>
        </el-menu-item>
      </router-link>
      <router-link
        to="/personnel/deptCheckList"
        v-if="role == '400'"
      >
        <el-menu-item index="/personnel/deptCheckList">
          <i class="iconfont icon-pinggubaogao"></i>
          <span slot="title">部门考核列表</span>
        </el-menu-item>
      </router-link>
      <router-link
        to="/personnel/gradeLeadingOut"
        v-if="role != '400'"
      >
        <el-menu-item index="/personnel/gradeLeadingOut">
          <i class="iconfont icon-daochu"></i>
          <span slot="title">评分导出</span>
        </el-menu-item>
      </router-link>

      <el-submenu
        index="3"
        v-if="role == '400'"
      >
        <template slot="title">
          <i class="el-icon-s-custom"></i>
          <span>人员管理</span>
        </template>
        <!-- <el-menu-item>人员属性管理</el-menu-item> -->
        <router-link to="/personnel/personnelPeopleInfo">
          <el-menu-item index="/personnel/personnelPeopleInfo">人员信息</el-menu-item>
        </router-link>
        <router-link to="/personnel/personnelPeopleScore">
          <el-menu-item index="/personnel/personnelPeopleScore">人员分数</el-menu-item>
        </router-link>
      </el-submenu>
      <router-link
        to="/personnel/personnelLeader"
        v-if="role == '400'"
      >
        <el-menu-item index="/personnel/personnelLeader">
          <i class="iconfont icon-bumen"></i>
          <span slot="title">用户管理</span>
        </el-menu-item>
      </router-link>
      <router-link
        to="/personnel/complishSituation"
        v-if="role == '400'"
      >
        <el-menu-item index="/personnel/complishSituation">
          <i class="iconfont icon-completionstatus"></i>
          <span slot="title">部门考核完成情况</span>
        </el-menu-item>
      </router-link>
      <router-link
        to="/personnel/authorization"
        v-if="isagent == '0'"
      >
        <el-menu-item index="/personnel/authorization">
          <i class="iconfont icon-shouquan"></i>
          <span slot="title">部门长授权</span>
        </el-menu-item>
      </router-link>
      <router-link
        to="/personnel/personnelMessage"
        v-if="role == '400'"
      >
        <el-menu-item index="/personnel/personnelMessage">
          <i class="iconfont icon-xinxiduanxinxiaoxitixingyoujiansixinyouxiang"></i>
          <span slot="title">短信模板</span>
        </el-menu-item>
      </router-link>
      <router-link
        to="/personnel/notice"
        v-if="role == '400'"
      >
        <el-menu-item index="/personnel/notice">
          <i class="iconfont icon-gonggao"></i>
          <span slot="title">登录提示</span>
        </el-menu-item>
      </router-link>
      <router-link to="/personnel/updatePsd">
        <el-menu-item index="/personnel/updatePsd">
          <i class="iconfont icon-xiugaimima"></i>
          <span slot="title">修改密码</span>
        </el-menu-item>
      </router-link>
    </el-menu>
  </div>
</template>

<script>
export default {
  data() {
    return {
      role: "",
      isagent: ""
    };
  },
  created() {
    this.role = this.$store.state.user.user.rolecode;
    this.isagent = this.$store.state.user.user.isagent;
  },
  computed: {
    activeIndex() {
      return this.$route.path;
    }
  },
  methods: {
    handleOpen(key, keyPath) {},
    handleClose(key, keyPath) {}
  }
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
