<template>
  <div class="BottomNavM">
    <van-tabbar
      v-model="active"
      active-color="#409EFF"
      inactive-color="#000"
    >
      <van-tabbar-item
        icon="home-o"
        replace
        to="/web"
      >我的考核</van-tabbar-item>
      <!-- <van-tabbar-item
        icon="orders-o"
        replace
        to="/web/webShowPersonal"
        v-if="flag == true"
      >个人报告</van-tabbar-item> -->
      <!-- <template v-if="$store.state.user.user.medicalEthicsRoleList.indexOf('400')!= -1 || $store.state.user.user.medicalEthicsRoleList.indexOf('300')!= -1">
        <van-tabbar-item
          icon="notes-o"
          replace
          to="/web/ydyfMultiple"
          v-if="$store.state.user.user.medicalEthicsRoleList.indexOf('400')!= -1 && $store.state.user.user.medicalEthicsRoleList.indexOf('300')!= -1"
        >医德医风</van-tabbar-item>
        <van-tabbar-item
          icon="notes-o"
          replace
          v-else
          @click="ydyfClick"
        >医德医风</van-tabbar-item>
      </template> -->
      <van-tabbar-item
        icon="thumb-circle-o"
        replace
        to="/web/tabTwo"
      >自评</van-tabbar-item>
      <van-tabbar-item
        icon="manager-o"
        replace
        to="/web/me"
      >我</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script>
export default {
  data() {
    return {
      active: 0,
      flag: false,
      nameTo: '我的考核',
      dbtype: this.$store.state.user.user.dbtype,
    };
  },
  created() {
    this.usercode = this.$route.query.usercode;
   
    if (!this.$store.state.user.user.medicalEthicsRoleList) {
      this.$router.push({ path: "/webShowLogin" });
      this.$store.commit("$_removeStorage");
      return;
    }
    
    this.active = this.$store.state.user.user.active
  },
  watch: {
    $route(to, from) {
      console.log(to);
      if(to.name=='我的考核'){
        this.$store.state.user.user.active = 0;
      }else if(to.name=='自评'){
        this.$store.state.user.user.active = 1;
      }else if(to.name=='我'){
        this.$store.state.user.user.active = 2;
      }
    },
  },
  methods: {
    ydyfClick() {
      let type = 0;
      let list = this.$store.state.user.user.medicalEthicsRoleList;
      if (list.indexOf("300") != -1) {
        type = 4;
      } else if (list.indexOf("400") != -1) {
        type = 7;
      }
      this.$router.push({
        path: "/web/ydyfMobileList",
        query: { roleType: type },
      });
    },
  },
};
</script>
<style lang="scss" scoped>
</style>
