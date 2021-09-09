<template>
  <div class="ydyf-list">
    <van-search
      v-model="searchKey"
      @search="onSearch"
      placeholder="请输入发薪号或姓名搜索"
    />
    <van-tabs
      v-model="active"
      title-active-color="#409EFF"
      color="#409EFF"
      swipeable
      @change="handleChange"
       style="font-size:16px;"
    >
      <van-tab title="待考核" style="font-size:16px;">
        <wait
          ref="refWait"
          :type="0"
          :roleType="roleType"
        ></wait>
      </van-tab>
      <van-tab title="已考核">
        <van-dropdown-menu>
          <van-dropdown-item
            v-model="gradeValue"
            :options="gradeOption"
             @change="gradeChange"
          />
          <van-dropdown-item
            v-model="personValue"
            :options="personOption"
             @change="personChange"
          />
        </van-dropdown-menu>
        <wait
          ref="refPass"
          :type="1"
          :roleType="roleType"
        ></wait>
      </van-tab>
    </van-tabs>
  </div>
</template>
<script>
import wait from "./wait";
import already from "./already";
export default {
  data() {
    return {
      active: 0,
      list: [],
      loading: false,
      finished: false,
      refreshing: false,
      roleType: 0,
      searchKey: "",
      gradAactive: 0,
      gradeValue:0,
      gradeOption: [
        { text: "全部成绩", value: 0 },
        { text: "优秀", value: 1 },
        { text: "良好", value: 2 },
        { text: "一般", value: 3 },
        { text: "较差", value: 4 },
      ],
      personValue:2,
      personOption: [
        { text: "全部人员", value: 2 },
        { text: "临床人员", value: 0 },
        { text: "非临床人员", value: 1 },
      ],
    };
  },
  components: {
    wait,
    already,
  },
  mounted() {
    this.roleType = this.$route.query.roleType;
  },
  methods: {
    //考核状态切换
    handleChange(name, title) {
      this.searchKey = "";
      if (this.active == 0) {
        this.$refs.refWait.onRefresh();
      } else if (this.active == 1) {
        this.$refs.refPass.onRefresh();
      }
    },
    //搜索框模糊搜索
    onSearch() {
      if (this.active == 0) {
        this.$refs.refWait.search.searchKey = this.searchKey;
        this.$refs.refWait.onRefresh();
      } else if (this.active == 1) {
        this.$refs.refPass.search.searchKey = this.searchKey;
        this.$refs.refPass.onRefresh();
      }
    },
    //已考核成绩搜索
    gradeChange(val){
      this.$refs.refPass.search.scoreLevel = this.gradeValue;
      this.$refs.refPass.onRefresh();
    },
    //人员类型搜索
    personChange(val){
      this.$refs.refPass.search.personType = this.personValue;
      this.$refs.refPass.onRefresh();
    }
  },
  watch: {
    $route(to, from) {},
  },
};
</script>
<style>
.ydyf-list .van-tab{
  font-size:16px;
}
</style>