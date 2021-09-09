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
      <router-link
        to="/home"
      >
        <el-menu-item index="/home">
          <i class="el-icon-s-home"></i>
          <span slot="title">首页</span>
        </el-menu-item>
      </router-link>
      <!-- <router-link to="/home/stepOne">
        <el-menu-item index="/home/stepOne">
          <i class="el-icon-menu"></i>
          <span slot="title">创建考核</span>
        </el-menu-item>
      </router-link> -->
      <el-submenu
        index="2"
        v-if="role == '100' || role == '50'"
      >
        <template slot="title">
          <i class="el-icon-s-finance"></i>
          <span>基础数据管理</span>
        </template>
        <router-link to="/home/department">
          <el-menu-item index="/home/department">部门管理</el-menu-item>
        </router-link>
        <router-link to="/home/post">
          <el-menu-item index="/home/post">岗位管理</el-menu-item>
        </router-link>
        <router-link to="/home/indicator">
          <el-menu-item index="/home/indicator">指标管理</el-menu-item>
        </router-link>
        <router-link to="/home/branch">
          <el-menu-item index="/home/branch">支部管理</el-menu-item>
        </router-link>
      </el-submenu>
      <el-submenu
        index="3"
        v-if="role == '100' || role == '50'"
      >
        <template slot="title">
          <i class="el-icon-s-custom"></i>
          <span>人员管理</span>
        </template>
        <!-- <el-menu-item>人员属性管理</el-menu-item> -->
        <router-link to="/home/people">
          <el-menu-item index="/home/people">人员信息录入</el-menu-item>
        </router-link>
      </el-submenu>
      <el-submenu
        index="4"
        v-if="role == '100' || role == '50'"
      >
        <template slot="title">
          <i class="el-icon-s-check"></i>
          <span>月结管理</span>
        </template>
        <router-link to="/home/scorePeople">
          <el-menu-item index="/home/scorePeople">评分关系管理</el-menu-item>
        </router-link>
        <router-link to="/home/gradeTotal">
          <el-menu-item index="/home/gradeTotal">评分汇总管理</el-menu-item>
        </router-link>
        <!-- <router-link to="/home/historyGrade">
          <el-menu-item index="/home/historyGrade">历史评分管理</el-menu-item>
        </router-link> -->
        <!-- <router-link to="/home/quarter">
          <el-menu-item index="/home/quarter">月度总结管理</el-menu-item>
        </router-link> -->
        <router-link to="/home/leaderUser">
          <el-menu-item index="/home/leaderUser">打分用户管理</el-menu-item>
        </router-link>
      </el-submenu>
      <!-- <router-link
        to="/home/notice"
        v-if="role == '100' || role == '50'"
      >
        <el-menu-item index="/home/notice">
          <i class="iconfont icon-gonggao"></i>
          <span slot="title">登录提示</span>
        </el-menu-item>
      </router-link> -->
      <router-link to="/home/updatePsd">
        <el-menu-item index="/home/updatePsd">
          <i class="iconfont icon-xiugaimima"></i>
          <span slot="title">修改密码</span>
        </el-menu-item>
      </router-link>
      <router-link to="/home/userGrade">
        <el-menu-item index="/home/userGrade">
          <i class="iconfont icon-pingfenshoucang-"></i>
          <span slot="title">人员评分管理</span>
        </el-menu-item>
      </router-link>
      <!-- <router-link to="/home/userQuarter" v-if="role != '150'">
        <el-menu-item index="/home/userQuarter">
          <i class="iconfont icon-weibiaoti12"></i>
          <span slot="title">个人月结管理</span>
        </el-menu-item>
      </router-link> -->
       <el-submenu
        index="5"
        v-if="role == '100' || role == '50' || role == '300' || role=='200' "
      >
        <template slot="title">
          <i class="el-icon-s-check"></i>
          <span>报表管理</span>
        </template>
        <router-link v-if="role == 300" to="/home/someone">
          <el-menu-item index="/home/someone">
            <i class="iconfont icon-pinggubaogao"></i>
            <span slot="title">评估报告</span>
          </el-menu-item>
        </router-link>
        <router-link v-else-if="role == 200" to="/home/departmentTotal">
          <el-menu-item index="/home/departmentTotal">
            <i class="iconfont icon-pinggubaogao"></i>
            <span slot="title">评估报告</span>
          </el-menu-item>
        </router-link>
        <router-link v-else-if="role == '100' || role == '50'" to="/home/totalReport">
          <el-menu-item index="/home/totalReport">
            <i class="iconfont icon-pinggubaogao"></i>
            <span slot="title">评估报告</span>
          </el-menu-item>
        </router-link>
        <router-link to="/home/historyGrade">
          <el-menu-item index="/home/historyGrade">历史评分管理</el-menu-item>
        </router-link>
      </el-submenu>
     <!-- <template v-if="role == 300">
        <router-link to="/home/someone">
          <el-menu-item index="/home/someone">
            <i class="iconfont icon-pinggubaogao"></i>
            <span slot="title">评估报告</span>
          </el-menu-item>
        </router-link>
        <router-link to="/home/historyGrade">
          <el-menu-item index="/home/historyGrade">历史评分管理</el-menu-item>
        </router-link>
      </template>
       <template v-else-if="role == 200">
        <router-link to="/home/departmentTotal">
          <el-menu-item index="/home/departmentTotal">
            <i class="iconfont icon-pinggubaogao"></i>
            <span slot="title">评估报告</span>
          </el-menu-item>
        </router-link>
        <router-link to="/home/historyGrade">
          <el-menu-item index="/home/historyGrade">历史评分管理</el-menu-item>
        </router-link>
      </template>
      <template v-else-if="role == '100' || role == '50'">
        <router-link to="/home/totalReport">
          <el-menu-item index="/home/totalReport">
            <i class="iconfont icon-pinggubaogao"></i>
            <span slot="title">评估报告</span>
          </el-menu-item>
          <router-link to="/home/historyGrade">
          <el-menu-item index="/home/historyGrade">历史评分管理</el-menu-item>
        </router-link>
        </router-link>
      </template> -->
      <!-- <template v-if="role == '100' || role == '50'">
        <router-link to="/home/message">
          <el-menu-item index="/home/message">
            <i class="iconfont icon-xinxiduanxinxiaoxitixingyoujiansixinyouxiang"></i>
            <span slot="title">短信模板</span>
          </el-menu-item>
        </router-link>
      </template> -->
    </el-menu>
  </div>
</template>

<script>
export default {
  data() {
    return {
      role: ""
    };
  },
  created() {
    this.role = this.$store.state.user.user.rolecode;
  },
  computed: {
    activeIndex() {
      return this.$route.path;
    }
  },
  methods: {
    handleOpen(key, keyPath) {
    },
    handleClose(key, keyPath) {
    }
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
