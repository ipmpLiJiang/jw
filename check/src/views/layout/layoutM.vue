<template>
  <!-- 入口容器-->
  <div id="app">
    <div class="content"><router-view /></div>
     <BottomNavM></BottomNavM>
  </div>
</template>

<script>
import BottomNavM from "./BottomNavM";
export default {
  data() {
    return {
    };
  },
  components: {
      BottomNavM
  },
  methods: {

  },
  mounted() {
  },
};
</script>

<style lang="scss">
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
  .content{
    margin-bottom: 60px;
  }
</style>
