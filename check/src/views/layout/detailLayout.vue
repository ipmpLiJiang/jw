<template>
  <div>
    <!-- <TopNav></TopNav>
    <LeftNav></LeftNav> -->
    <div class="inside-main">
      <el-main class="content">
        <router-view />
      </el-main>
    </div>
  </div>
</template>

<script>
import LeftNav from "@/components/common/leftNav";
import TopNav from "@/components/common/topNav";
export default {
  data() {
    return {};
  },
  components: {
    LeftNav,
    TopNav
  },
  mounted() {},
  created() {},

  methods: {}
};
</script>


<style lang="scss" scoped>
.inside-main {
  width: 100%;
  padding-top: 70px;
  *z-index: 10088;
  padding-bottom: 70px;
  padding-left: 100px;
  text-align: center;
  .content {
    width: 1000px;
    margin: 20px auto;
    padding: 20px;
    overflow: initial;
  }
  .el-main {
    background: none;
  }
}
</style>
