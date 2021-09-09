<template>
  <div>
    <h4 class="title">
      <router-link to="/home"><span>首页</span></router-link>
      <i class="el-icon-arrow-right"></i>短信模板</h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="5">
            <el-form-item label="短信名称">
              <el-input
                placeholder="请输入短信名称"
                v-model="search.templatename"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-button
            type="primary"
            @click="searchList"
          >搜索</el-button>
          <el-button
            type="primary"
            @click="openAdd"
          >新增短信模板</el-button>
        </el-form>
      </el-col>
    </el-row>
    <el-row class="content">
      <el-table
        size="small"
        :data="tableData"
        border
        style="width: 100%"
        v-loading="tableLoading"
      >
        <el-table-column
          type="index"
          label="序号"
          width="100px"
        >
        </el-table-column>
        <el-table-column
          label="短信名称"
          prop="templatename"
        >
        </el-table-column>
        <el-table-column
          label="短信内容"
          prop="templatecontent"
        >
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="100"
        >
          <template slot-scope="scope">
            <el-button
              @click="editUser(scope.row)"
              type="text"
              size="small"
            >编辑</el-button>
            <el-button
              @click="deleteUser(scope.row)"
              type="text"
              size="small"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
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
    </el-row>
    <!-- 添加人员 -->
    <AddMessage
      :parentForms="forms"
      :dialogVisible="dialogVisible"
      @childClose="childClose"
      @childGetList="getList"
    ></AddMessage>
  </div>
</template>

<script>
import { getList, deleteMessage } from "@/api/message/index";
import AddMessage from "./addMessage";
import qs from "qs";
export default {
  data() {
    return {
      forms: {
        templatename: "",
        templatecontent: "",
      },
      search: {
        templatename: ""
      },
      tableData: [],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      dialogVisible: false,
      tableLoading: true
    };
  },
  components: {
    AddMessage
  },
  mounted() {},
  created() {
    this.getList();
  },
  methods: {
    //初始化
    into() {
      this.page.pageNum = 1;
      this.page.pageSize = 10;
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
    //关闭列表
    childClose(val) {
      this.dialogVisible = val;
    },
    //搜索
    searchList() {
      this.into();
      this.getList();
    },
    //查询列表
    getList() {
      let data = this.page;
      //flag 1-组织部  2-人事部
      data.flag = 1;
      data.templatename = this.search.templatename;
      new Promise((response, reject) => {
        getList(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.tableData = response.data.data;
              this.total = response.data.totalPages;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
            this.tableLoading = false;
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    //打开添加人员
    openAdd() {
      this.forms = {
        templatename: "",
        templatecontent: "",
      };
      this.dialogVisible = true;
    },
    //删除人员
    deleteUser(val) {
      this.$confirm("此操作将永久删除该短信模板, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let data = {
            templatecode: val.templatecode
          };
          new Promise((response, reject) => {
            deleteMessage(qs.stringify(data))
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                  this.getList();
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error"
                  });
                }
              })
              .catch(error => {
                reject(error);
              });
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    //编辑人员
    editUser(row) {
      this.forms = row;
      this.dialogVisible = true;
    },
  }
};
</script>


<style lang="scss" scoped>
.title {
  height: 50px;
  line-height: 50px;
  border-bottom: 1px solid #eaeaea;
  padding: 0px 10px;
  font-weight: 600;
  color: #424242;
  background: #fff;
  span {
    font-weight: normal;
    color: #9b9b9b;
  }
  i {
    margin: 0px 4px;
    color: #9b9b9b;
  }
}
.search {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  margin: 20px;
  border-radius: 4px;
  .el-form-item {
    margin: 0px;
  }
  .el-button {
    margin-left: 10px;
  }
}
.content {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  margin: 0px 20px 20px 20px;
  border-radius: 4px;
  .el-pagination {
    margin: 20px auto;
    text-align: center;
  }
  .table-expand {
    padding: 0px;
    .el-form-item {
      margin-right: 0;
      margin-bottom: 10px;
      width: 33.33%;
    }
    .el-select {
      width: 185px;
    }
  }
}
</style>
