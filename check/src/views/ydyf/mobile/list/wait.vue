<template>
  <div>
    <van-pull-refresh
      v-model="refreshing"
      @refresh="onRefresh"
    >
      <van-list
        v-model="loading"
        :finished="finished"
        finished-text="没有更多了"
        @load="onLoad"
      >
        <div
          class="list"
          v-for="(item,index) in data"
          :key="index"
          @click="toAnswer(item)"
        >
          <div class="line-bottom">
            <div class="left">
              <h3><span class="list-title">姓名：</span><span>{{item.userName}}</span></h3>
              <h4><span class="list-title">发薪号：</span><span>{{item.userId}}</span></h4>
              <h4><span class="list-title">人员类型：</span><span>{{item.personType == 0 ? '临床人员' : "非临床人员"}}</span></h4>
              <h4><span class="list-title">支部：</span><span>{{item.branchName}}</span></h4>
            </div>
            <div class="right">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </div>
      </van-list>
    </van-pull-refresh>
  </div>
</template>
<script>
import { notFilled } from "../../api/Form/index";
import qs from "qs";
export default {
  data() {
    return {
      data: [],
      loading: false,
      finished: false,
      refreshing: false,
      page: {
        pageNum: 1,
        pageSize: 10,
      },
      search: {
        searchKey: "",
        scoreLevel: 0,
        personType: 2,
      },
    };
  },
  props: ["type", "roleType"],

  created() {},
  methods: {
    onLoad() {
      setTimeout(() => {
        if (this.refreshing) {
          this.data = [];
          this.refreshing = false;
        }
        let data = Object.assign({}, this.page, this.search);
        data.type = this.type;
        data.roleType = this.roleType;
        new Promise((response, reject) => {
          notFilled(qs.stringify(data))
            .then((response) => {
              if (response.data.code == 0) {
                // this.data = response.data.data;
                // response.data.data.forEach((row) => {
                //   this.data.push(row);
                // });
                if (this.type == 1) {
                  //调用过滤已考核数据
                  this.filtration(response.data.data);
                } else {
                  //调用过滤待考核数据
                  this.unfiltration(response.data.data);
                }
              } else {
                this.$toast.fail(response.data.msg);
                this.finished = true;
              }
              this.loading = false;
            })
            .catch((error) => {
              reject(error);
            });
        });
      }, 1000);
    },
    onRefresh() {
      // 清空列表数据
      this.finished = false;
      this.data = [];
      // 重新加载数据
      // 将 loading 设置为 true，表示处于加载状态
      this.loading = true;
      this.page.pageNum = 1;
      this.page.pageSize = 10;
      this.onLoad();
    },
    toAnswer(item) {
      if (item.personType == 1) {
        //非临床
        if (this.roleType == 4) {
          this.$router.push({
            path: "/web/ydyfDirectorNoclinic",
            query: {
              u_id: item.userId,
              roleType: this.roleType,
              type: this.type,
            },
          });
        } else if (this.roleType == 7) {
          this.$router.push({
            path: "/web/ydyfSecretaryNoclinic",
            query: {
              u_id: item.userId,
              roleType: this.roleType,
              type: this.type,
            },
          });
        }
      } else {
        //临床
        if (this.roleType == 4) {
          this.$router.push({
            path: "/web/ydyfDirectorClinic",
            query: {
              u_id: item.userId,
              roleType: this.roleType,
              type: this.type,
            },
          });
        } else if (this.roleType == 7) {
          this.$router.push({
            path: "/web/ydyfSecretaryClinic",
            query: {
              u_id: item.userId,
              roleType: this.roleType,
              type: this.type,
            },
          });
        }
      }
    },
    //过滤已考核数据
    filtration(tData) {
      for (let i = 0; i < tData.length; i++) {
        if (this.roleType == 4) {
          //判断主任可显示数据
          if (tData[i]["personType"] == 0) {
            if (tData[i]["step"] >= 4) {
              this.data.push(tData[i]);
            }
          } else {
            if (tData[i]["step"] >= 2) {
              this.data.push(tData[i]);
            }
          }
        } else if (this.roleType == 7) {
          //判断书记可显示数据
          if (tData[i]["personType"] == 0) {
            if (tData[i]["step"] >= 5) {
              this.data.push(tData[i]);
            }
          } else {
            if (tData[i]["step"] >= 3) {
              this.data.push(tData[i]);
            }
          }
        }
      }
      if (tData < 10) {
        this.finished = true;
      }
      this.page.pageNum++;
    },
    //过滤未考核数据
    unfiltration(tData) {
      for (let i = 0; i < tData.length; i++) {
        if (this.roleType == 4) {
          //判断主任可显示数据
          if (tData[i]["personType"] == 0) {
            if (tData[i]["step"] == 3) {
              this.data.push(tData[i]);
            }
          } else {
            if (tData[i]["step"] == 1) {
              this.data.push(tData[i]);
            }
          }
        } else if (this.roleType == 7) {
          //判断书记可显示数据
          if (tData[i]["personType"] == 0) {
            if (tData[i]["step"] == 4) {
              this.data.push(tData[i]);
            }
          } else {
            if (tData[i]["step"] == 2) {
              this.data.push(tData[i]);
            }
          }
        }
      }
      if (tData < 10) {
        this.finished = true;
      }
      this.page.pageNum++;
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
      width: 4rem;
      color: #333;
    }
  }
  h3 {
    color: #333;
    span {
      font-size: 1rem;
      position: relative;
      top: -0.1rem;
      border-radius: 2px;
      color: #409EFF;
    }
  }
  h4 {
    font-size: 0.8rem;
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