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
        v-if="flag == true"
      >我的考核</van-tabbar-item>
      <van-tabbar-item
        icon="orders-o"
        replace
        to="/web/webShowPersonal"
        v-if="flag == true"
      >个人报告</van-tabbar-item>
      <template v-if="$store.state.user.user.medicalEthicsRoleList.indexOf('400')!= -1 || $store.state.user.user.medicalEthicsRoleList.indexOf('300')!= -1">
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
      </template>

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
    };
  },
  created() {
    this.usercode = this.$route.query.usercode;
   
    if (!this.$store.state.user.user.medicalEthicsRoleList) {
      this.$router.push({ path: "/webShowLogin" });
      this.$store.commit("$_removeStorage");
      return;
    }
    //判断用户是否有组织全县
    this.$store.state.user.user.roleList.forEach((row) => {
      if (
        row.rolecode == 50 ||
        row.rolecode == 100 ||
        row.rolecode == 150 ||
        row.rolecode == 200 ||
        row.rolecode == 300
      ) {
        this.flag = true;
      }
    });
    if (!this.flag) {
      this.active = 2;
    }
  },
  watch: {
    $route(to, from) {
      console.log(to);
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
