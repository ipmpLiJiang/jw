<template>
  <!-- 入口容器-->
  <div id="app">
    <!-- 全局框架-->
    <el-container class="all-container">
      <!-- 顶部导航-->
      <el-header  style="height:60px;">
        <TopNav></TopNav>
      </el-header>
      <el-container>
        <!-- 左侧导航-->
        <el-aside style="width:200px;">
          <LeftNav></LeftNav>
        </el-aside>
        <!-- 右侧容器-->
        <el-container>

          <el-main>
            <!-- 路由容器-->
            <router-view />
          </el-main>
          <!-- 底部信息-->
          <el-footer style="height:50px;">武汉市精神卫生中心360°考核系统</el-footer>
        </el-container>

      </el-container>
    </el-container>
  </div>
</template>

<script>
import TopNav from "./TopNav";
import LeftNav from "./adminLeftNav";
export default {
  data() {
    return {
    };
  },
  components: {
    TopNav,
    LeftNav
  },
  methods: {

  },
  mounted() {
  },
};
</script>

