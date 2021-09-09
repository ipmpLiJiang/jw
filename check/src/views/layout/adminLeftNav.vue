<template>
  <div>
    <h5 class="user-name">
      欢迎您，
      <span>{{$store.state.user.user.username}}</span>
    </h5>
    <el-menu
      :default-active="activeIndex"
      :unique-opened="true"
      active-text-color="#409EFF"
      class="el-menu-vertical-demo"
      @open="handleOpen"
      @close="handleClose"
    >
      <router-link to="/admin/jurisdiction">
        <el-menu-item index="/admin/jurisdiction">
          <i class="iconfont icon-pingfenshoucang-"></i>
          <span slot="title">问卷权限管理</span>
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
    };
  },
  created() {
    this.role = this.$store.state.user.user.rolecode;
  },
  computed: {
    activeIndex() {
      return this.$route.path;
    },
  },
  methods: {
    handleOpen(key, keyPath) {},
    handleClose(key, keyPath) {},
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
