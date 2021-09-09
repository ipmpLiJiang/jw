<template>
  <div class="tabOne">
    <el-row
      v-if="data.length <= 0"
      class="no-data"
    >
      <i class="el-icon-warning-outline"></i>
      <p>暂无考核任务</p>
    </el-row>
    <el-row v-else>
      <div
        class="list"
        v-for="(item,index) in data"
        :key="index"
        @click="toAnswer(item)"
      >
        <div class="line-bottom">
          <div class="left">
            <h3>{{item.title}}<span>去考核</span></h3>
            <h4>被考核人<span>{{item.scorredname}}</span></h4>
            <h5><span>{{item.year}}</span><span>{{item.monthname}}</span></h5>
          </div>
          <div class="right">
            <i class="el-icon-arrow-right"></i>
          </div>
        </div>
      </div>
      <div class="no-more">- 到底了 -</div>
    </el-row>
  </div>
</template>
<script>
import { mobilGetList } from "@/api/mobile/check";
import qs from "qs";
export default {
  data() {
    return {
      data: []
    };
  },
  created() {
    this.getList();
  },
  components: {},
  methods: {
    toAnswer(item) {
      this.$router.push({
        path: "/webShow",
        query: {
          serialNo: item.serialno,
          userCode: item.scorredcode,
          scorringCode: item.scorringcode
        }
      });
    },
    getList() {
      let data = {
        usercode: this.$store.state.user.user.usercode,
        dbtype: this.$store.state.user.user.dbtype
      };
      var toast = this.$toast.loading({
        mask: true,
        message: '加载中...',
        duration:0
      });
      new Promise((response, reject) => {
        mobilGetList(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.data = response.data.data;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
            toast.clear();
          })
          .catch(error => {
            reject(error);
          });
      });
    }
  }
};
</script>


<style lang="scss" scoped>
.list {
  position: relative;
  background: #fff;
  padding: 1rem;
  .line-bottom {
    display: flex;
    align-items: center;
    flex-direction: row;
  }
  .left {
    flex: 1;
  }
  h3 {
    font-size: 1rem;
    color: #333;
    span {
      background: #409EFF;
      color: #fff;
      font-size: 0.5rem;
      padding: 0.1rem 0.2rem;
      position: relative;
      top: -0.1rem;
      margin-left: 0.4rem;
      border-radius: 2px;
    }
  }
  h4 {
    font-size: 0.8rem;
    color: #333;
    margin-bottom: 0.2rem;
    span {
      margin-left: 5px;
      color: #409EFF;
    }
  }
  h5 {
    font-size: 0.8rem;
    color: #acacac;
    span {
      margin-right: 5px;
    }
  }
}
.no-more {
  text-align: center;
  padding: 10px 10px 0px 10px;
  font-size: 0.8rem;
  color: #acacac;
}
.no-data{
    position: absolute;
    width: 100%;
    height: 100%;
    top: -50px;
    left: 0px;
    background: #fff;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    i{
      font-size: 5rem;
      color: #dedede;
    }
    p{
      font-size: 0.8rem;
      margin-top: 1rem;
      color: #b4b4b4;
    }
  }
</style>
