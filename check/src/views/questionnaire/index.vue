<template>
  <div class="index-container">
    <el-row class="search">
      <div class="content">
        <input
          type="text"
          class="serarchbox"
          @keyup.enter="searchEnterFun"
          v-model="search.title"
          placeholder="请输入问卷名搜索"
        >
        <el-button
          type="primary"
          @click="searchList"
          class='search-btn'
        >搜索</el-button>

        <el-button
          type="primary"
          @click="totalList"
        >全部问卷</el-button>

      </div>
    </el-row>

    <div class="content-right">
      <div class="title">
        <h2>
          <span>问卷列表</span>
          <router-link to="/questionnaire/create">
            <el-button type="primary">创建问卷</el-button>
          </router-link>
        </h2>

        <!-- <div class="float-right spinner-list">
          <el-dropdown placement="bottom">
            <span class="el-dropdown-link">
              状态<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>全部</el-dropdown-item>
              <el-dropdown-item>已发布</el-dropdown-item>
              <el-dropdown-item>未发布</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div> -->
        <!-- <div class="float-right spinner-list">
          <el-dropdown placement="bottom">
            <span class="el-dropdown-link">
              时间倒序<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>时间倒序</el-dropdown-item>
              <el-dropdown-item>时间正序</el-dropdown-item>
              <el-dropdown-item>答卷倒序</el-dropdown-item>
              <el-dropdown-item>答卷正序</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div> -->
      </div>
      <ul class="survey-list">
        <li
          class="survey-items"
          v-for="(item,index) in tableData"
          :key="index"
        >
          <template v-if="item.publishstatus == 1">
            <span
              class="tag"
              style="color: #fff;background: #b7b7b7;padding: 2px 4px;"
            >未发布</span>
          </template>
          <template v-else>
            <span
              class="tag"
              style="color: #fff;background: #409EFF;padding: 2px 4px;"
            >已发布</span>
          </template>
          <div class="index-title">
            <h3>{{item.title}}</h3>
          </div>
          <h5>创建时间：{{item.createtime}}<span>问卷ID：{{item.id}}</span></h5>
          <h4>{{item.answercount}}<span>答卷人数</span></h4>
          <div class="edit">
            <el-button
              type="primary"
              plain
              @click="edit(item)"
              size='small'
            >编辑问卷</el-button>
            <el-button
              type="primary"
              plain
              @click="openUrl(item.id)"
              v-if="item.publishstatus == 2"
              size='small'
            >查看链接</el-button>
            <el-button
              type="primary"
              plain
              @click="analyze(item)"
              v-if="item.publishstatus == 2"
              size='small'
            >分析问卷</el-button>
          </div>
          <div class="item-bot">
            <template v-if="item.publishstatus == 1">
              <el-button
                plain
                @click="issue(item.id)"
                class="start"
              ><i class="iconfont icon-kaishi"></i>发布</el-button>
            </template>
            <template v-else>
              <el-button
                plain
                @click="stop(item.id)"
                class="stop"
              ><i class="iconfont icon-stop"></i>停止</el-button>
            </template>
            <el-button
              plain
              @click="del(item.id)"
              class="del"
            ><i class="el-icon-delete"></i>删除</el-button>
          </div>
        </li>
        <li
          v-if="tableData.length <= 0"
          class="none-data"
        >暂无数据</li>
      </ul>
      <div class="paging">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="page.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        >
        </el-pagination>
      </div>
    </div>
    <el-dialog
      :visible.sync="dialogTableVisible"
      title="发布成功"
    >
      <el-row class="apply-box">
        <el-col
          class="code"
          :span="6"
          :offset="9"
        >
          <vue-qr
            :text="url"
            :size="300"
          ></vue-qr>
          <p>链接二维码</p>
        </el-col>
        <el-col class="link">
          <el-col class="message">考核链接</el-col>
          <el-input
            placeholder="请输入内容"
            readonly
            v-model="url"
          ></el-input>
          <el-button
            type="primary"
            @click="copy"
          >复制链接</el-button>
        </el-col>
        <!-- <el-col class="pwd">
          <el-col class="message">设置密码提示语</el-col>
          <el-input
            :maxlength="20"
            placeholder="请输入内容"
          ></el-input>
          <el-button type="primary">保存提示</el-button>
        </el-col> -->
      </el-row>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          type="primary"
          @click="dialogTableVisible = false"
        >确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import {
  getList,
  updatePublishStatus,
  updateFlag,
} from "@/api/questionnaire/index";
import qs from "qs";
import vueQr from "vue-qr";
export default {
  data() {
    return {
      dialogTableVisible: false,
      tableData: [],
      page: {
        pageNum: 1,
        pageSize: 10,
      },
      total: 0,
      search: {
        title: "",
      },
      url: "",
    };
  },
  created() {
    this.getList();
  },
  methods: {
    apply() {
      this.$router.push({
        path: "/questionnaire/apply",
        query: {},
      });
    },
    analyze(item) {
      if (item.answercount <= 0) {
        this.$message.warning("暂无人答卷，无法分析数据！");
        return;
      }
      this.$router.push({
        path: "/questionnaire/analyze",
        query: { id: item.id },
      });
    },
    //初始化
    into() {
      this.page.pageNum = 1;
      this.page.pageSize = 10;
    },
    //搜索
    searchList() {
      this.into();
      this.getList();
    },
    //回车搜索
    searchEnterFun(e) {
      var keyCode = window.event ? e.keyCode : e.which;
      var val = e.target.value;
      if (keyCode == 13 && val) {
        this.searchList();
      }
    },
    //设置每页多少条数据
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getList();
    },
    //翻页
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.getList();
    },
    //查询列表
    getList() {
      let params = this.page;
      params.title = this.search.title;
      new Promise((response, reject) => {
        getList(qs.stringify(params))
          .then((response) => {
            if (response.data.code == 0) {
              this.tableData = response.data.data;
              this.total = response.data.totalPages;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
            this.tableLoading = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //全部问卷
    totalList() {
      this.search.title = "";
      this.into();
      this.getList();
    },
    //发布问卷
    issue(id) {
      this.$confirm("如果您的问卷已准备就绪, 你可以", "提示", {
        confirmButtonText: "发布问卷",
        cancelButtonText: "我再想想",
        type: "warning",
      })
        .then(() => {
          let data = {
            id: id,
          };
          new Promise((response, reject) => {
            updatePublishStatus(qs.stringify(data))
              .then((response) => {
                if (response.data.code == 0) {
                  this.$message({
                    type: "success",
                    message: "发布成功!",
                  });
                  this.openUrl(id);
                  this.getList();
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error",
                  });
                }
                this.tableLoading = false;
              })
              .catch((error) => {
                reject(error);
              });
          });
        })
        .catch(() => {});
    },
    //停止问卷
    stop(id) {
      this.$confirm("状态设为“停止”后将不能填写，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let data = {
            id: id,
          };
          new Promise((response, reject) => {
            updatePublishStatus(qs.stringify(data))
              .then((response) => {
                if (response.data.code == 0) {
                  this.$message({
                    type: "success",
                    message: "停止成功!",
                  });
                  this.getList();
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error",
                  });
                }
                this.tableLoading = false;
              })
              .catch((error) => {
                reject(error);
              });
          });
        })
        .catch(() => {});
    },
    //复制链接
    copy() {
      var _input = document.createElement("input"); // 直接构建input
      _input.value = this.url; // 设置内容
      document.body.appendChild(_input); // 添加临时实例
      _input.select(); // 选择实例内容
      document.execCommand("Copy"); // 执行复制
      this.$message({
        type: "success",
        message: "复制成功!",
      });
      document.body.removeChild(_input); // 删除临时实例
    },
    //打开链接弹出框
    openUrl(id) {
      console.log();
      this.url =window.location.protocol + "//" + document.location.hostname+ ":"+ window.location.port +"/360check/#/questionList?id=" + id;
      this.dialogTableVisible = true;
    },
    //删除问卷
    del(id) {
      this.$confirm("此操作将删除该问卷，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let data = {
            id: id,
          };
          new Promise((response, reject) => {
            updateFlag(qs.stringify(data))
              .then((response) => {
                if (response.data.code == 0) {
                  this.$message({
                    type: "success",
                    message: "删除成功!",
                  });
                  this.getList();
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error",
                  });
                }
                this.tableLoading = false;
              })
              .catch((error) => {
                reject(error);
              });
          });
        })
        .catch(() => {});
    },
    //编辑问卷
    edit(item) {
      if (item.publishstatus == 2) {
        this.$message.warning("问卷已发布，如要编辑请先停止问卷！");
        return;
      }
      if (item.answercount > 0) {
        this.$message.warning("此问卷已有人作答，无法在编辑！");
        return;
      }
      this.$router.push({
        path: "/questionnaire/design",
        query: {
          id: item.id,
          title: item.title,
        },
      });
    },
  },
  components: {
    vueQr,
  },
};
</script>
<style lang="scss" scoped>
.index-container {
  position: relative;
  .search {
    background: #fff;
    .content {
      width: 1200px;
      margin: 0px auto;
      text-align: center;
      padding: 30px 0px;
      .serarchbox {
        width: 500px;
        border-radius: 20px 0px 0px 20px;
        height: 40px;
        border: 2px solid #409eff;
        padding: 0px 20px;
        vertical-align: top;
      }
      .search-btn {
        border-radius: 0px 20px 20px 0px;
      }
    }
  }

  .content-right {
    width: 1160px;
    margin: 20px auto;
    padding: 0px 20px;
    border-radius: 5px;
    .title {
      h2 {
        display: flex;
        line-height: 40px;
        padding: 20px;
        span {
          flex: 1;
        }
      }
    }
  }
  .paging {
    text-align: center;
    padding: 0px 20px 0px 10px;
    .el-pagination {
      padding: 10px 10px;
      background: #fff;
    }
  }
}
.survey-list {
  display: block;
  overflow: hidden;
  padding-left: 1%;
}
.survey-items {
  width: 32%;
  height: auto;
  background: #fff;
  border-radius: 2px;
  margin-bottom: 1%;
  box-shadow: 0px 0px 4px 1px #d0d0d0;
  border-radius: 5px;
  float: left;
  margin-right: 1%;
  position: relative;
  .index-title {
    padding: 20px 10px;
    border-bottom: 1px solid #eae9e9;
    margin-bottom: 20px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  h3 {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    white-space: normal;
    min-height: 48px;
  }
  .tag {
    position: absolute;
    right: 0px;
    top: 0px;
    padding: 0px 4px;
    font-size: 12px;
    border-radius: 0px 5px 0px 0px;
  }
  h5 {
    font-weight: normal;
    color: #969696;
    padding: 0px 10px;
    span {
      float: right;
    }
  }
  h4 {
    font-size: 30px;
    padding: 0px 10px;
    span {
      font-size: 12px;
      font-weight: normal;
      margin-left: 10px;
      color: #969696;
    }
  }
  .edit {
    padding: 30px 10px;
  }
  .item-bot {
    margin-top: 20px;
    .el-button {
      width: 50%;
      margin: 0px;
      border: 0px;
      border-top: 1px solid#eae9e9;
      background: #f9f9f9;
      i {
        font-size: 20px;
        position: relative;
        left: -5px;
        top: 2px;
      }
    }
    .start {
      color: #409EFF;
      border-radius: 0px 0px 0px 5px;
    }
    .stop {
      color: #f56c6c;
      border-radius: 0px 0px 0px 5px;
    }
    .del {
      color: #808080;
      border-radius: 0px 0px 5px 0px;
      border-left: 1px solid #eae9e9;
    }
  }
}
.apply-box {
  .code {
    background: #fafafa;
    border-radius: 5px;
    img {
      width: 60%;
      margin: 20px 20% 20px 20%;
    }
    p {
      text-align: center;
      height: 30px;
      line-height: 30px;
      background: #f4f4f4;
      border-radius: 0px 0px 5px 5px;
    }
  }
  .link {
    text-align: center;
    .message {
      margin-left: 8%;
      text-align: left;
      margin-bottom: 10px;
    }
    .el-input {
      width: 70%;
      margin-right: 10px;
    }
  }
  .pwd {
    text-align: center;
    margin-top: 20px;
    .message {
      margin-left: 8%;
      text-align: left;
      margin-bottom: 10px;
    }
    .el-input {
      width: 70%;
      margin-right: 10px;
    }
  }
}
.none-data {
  color: #999;
  text-align: center;
}
</style>
<style>
.index-container .el-dropdown-menu__item:focus,
.el-dropdown-menu__item:not(.is-disabled):hover {
  background-color: #e7f5e1;
  color: #606266;
}
</style>
