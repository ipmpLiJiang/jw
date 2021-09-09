<template>
  <div>
    <van-pull-refresh
      v-model="refreshing"
      @refresh="onRefresh"
    >
      <div
        class="list"
        v-for="(item,index) in data"
        :key="index"
        @click="toAnswer(item)"
      >
        <div class="line-bottom">
          <div class="left">
            <h3><span class="list-title">考核年份：</span><span>{{item.year}}</span></h3>
            <h4><span class="list-title">人员名称：</span><span>{{item.username}}</span></h4>
            <h4><span class="list-title">人员类型：</span><span>{{item.personType == 0 ? '临床人员' : "非临床人员"}}</span></h4>
            <h4><span class="list-title">支部名称：</span><span>{{item.branchName}}</span></h4>
            <h4><span class="list-title">科室名称：</span><span>{{item.departmentName}}</span></h4>
          </div>
          <div class="right">
            <i class="el-icon-arrow-right"></i>
          </div>
        </div>
      </div>
    </van-pull-refresh>
  </div>
</template>
<script>
import { getMyList } from "../../api/addData/addData";
import qs from "qs";
export default {
  data() {
    return {
      data: [],
      loading: false,
      refreshing: false,
    };
  },
  created() {
    this.onLoad();
  },
  methods: {
    onLoad() {
      if (this.refreshing) {
        this.data = [];
        this.refreshing = false;
      }
      let data = {};
      new Promise((response, reject) => {
        getMyList(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.data = response.data.data;
            } else {
              this.$toast.fail(response.data.msg);
            }
            this.loading = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    onRefresh() {
      // 清空列表数据
      this.data = [];
      // 重新加载数据
      this.onLoad();
    },
    toAnswer(item) {
      if (item.personType == 1) {
        //非临床
        this.$router.push({
          path: "/web/myNoclinic",
          query: {
            u_id: item.userId,
            roleType: this.roleType,
            type: this.type,
          },
        });
      } else {
        //临床
        this.$router.push({
          path: "/web/myClinic",
          query: {
            u_id: item.userId,
            roleType: this.roleType,
            type: this.type,
          },
        });
      }
    },
  },
  watch: {
    $route(to, from) {},
  },
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
    .list-title {
      display: inline-block;
      width: 5rem;
      color: #333;
    }
  }
  h3 {
    color: #333;
    span {
      font-size: 0.9rem;
      position: relative;
      top: -0.1rem;
      border-radius: 2px;
      color: #409EFF;
    }
  }
  h4 {
    font-size: 0.9rem;
    color: #333;
    margin-bottom: 0.2rem;
    span {
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
.no-data {
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
  i {
    font-size: 5rem;
    color: #dedede;
  }
  p {
    font-size: 0.8rem;
    margin-top: 1rem;
    color: #b4b4b4;
  }
}
</style>