<template>
  <div class="box">
    <div class="box-content">
      <button
        class="m-btn btn-blue"
        @click="checkList(4)"
      >主任考核</button>
    </div>
    <div class="box-content">
      <button
        class="m-btn btn-green"
        @click="checkList(7)"
      >书记考核</button>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {};
  },
  components: {},
  created() {},
  methods: {
    checkList(type) {
      this.$router.push({
        path: "/web/ydyfMobileList",
        query: {roleType:type},
      });
    },
  },
  watch: {
    $route(to, from) {},
  },
};
</script>
<style lang="scss" scoped>
.box {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  height: 92vh;
  padding: 10vh 0;
}
.box-content {
  width: 100%;
}
.m-btn {
  width: 40vw;
  display: block;
  border: 0;
  border-radius: 50%;
  height: 40vw;
  font-weight: bold;
  color: #fff;
  font-size: 18px;
  margin: 0 auto;
}
.btn-blue {
  background: linear-gradient(to right, #409eff, #35d0d8);
}
.btn-green {
  background: linear-gradient(to right, #34c187, #35d895);
}
</style>