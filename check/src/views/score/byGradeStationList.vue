<template>
  <div>
    <h4 class="title">
      <router-link to="/home"><span>首页</span></router-link><i class="el-icon-arrow-right"></i>岗位关系管理
    </h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="5">
            <el-form-item label="所在部门">
              <DepartmentList
                @childSelectDepartment="getSelectDepartment"
                :selectedOptions="fulldepartmentcode"
              ></DepartmentList>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="岗位姓名">
              <el-input
                placeholder="请输入岗位姓名"
                v-model="search.stationName"
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
            @click="addGrade"
          >新增</el-button>
          <el-button
            type="danger"
            @click="batchDeleteScore"
          >批量删除</el-button>
        </el-form>
      </el-col>
    </el-row>
    <el-row class="content">
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          width="55"
        >
        </el-table-column>
        <el-table-column
          prop="id"
          label="关系ID"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="评分岗位"
          show-overflow-tooltip
        >
          <template
            slot-scope="scope"
            v-if="scope.row.scorringstationname"
          >
            {{scope.row.scorringstationname}}({{scope.row.departmentname1}})
          </template>
        </el-table-column>
        <el-table-column
          label="被评分岗位"
          show-overflow-tooltip
        >
          <template
            slot-scope="scope"
            v-if="scope.row.scorredstationname"
          >
            {{scope.row.scorredstationname}}({{scope.row.departmentname2}})
          </template>
        </el-table-column>
        <el-table-column
          prop="scoretype"
          label="评分类型"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="100"
        >
          <template slot-scope="scope">
            <el-button
              @click="editScore(scope.row)"
              type="text"
              size="small"
            >修改</el-button>
            <el-button
              type="text"
              size="small"
              @click="deleteScore(scope.row)"
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
  </div>
</template>

<script>
import DepartmentList from "../common/departmentList";
import {
  getList,
  deleteScore,
  getScorringList,
  batchDelete
} from "@/api/score/scoreStation";
import qs from "qs";
export default {
  data() {
    return {
      search: {
        departmentcode: "",
        stationName: ""
      },
      tableData: [],
      departmentcode: [""],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      fulldepartmentcode: [""],
      dialogVisible: false,
      uslDialogVisible: false,
      data: {},
      userArr: []
    };
  },
  components: {
    DepartmentList
  },
  mounted() {},
  created() {
    this.page.scorredstationcode = this.$route.query.code;
    this.page.scoretype = this.$route.query.type;
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
    //搜索
    searchList() {
      this.into();
      this.getList();
    },
    //查询列表
    getList() {
      let data = this.page;
      if (this.search.departmentcode.length > 0) {
        data.departmentcode = this.search.departmentcode[0];
      } else {
        data.departmentcode = "";
      }
      data.stationname = this.search.stationName;
      if (!data.scorredstationcode) {
        return;
      }
      data.dbtype = this.$store.state.user.user.dbtype
      new Promise((response, reject) => {
        getScorringList(qs.stringify(data))
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
    //获取岗位选择
    getSelectStation(data, row) {
      this.search.stationcode = [];
      if (data) {
        this.search.stationcode.push(data);
      }
      if (row.length > 0) {
        this.fullstationcode = row;
      }else{
        this.fullstationcode = [];
      }
    },
    //关闭
    childClose() {
      this.dialogVisible = false;
      this.uslDialogVisible = false;
    },
    //设置评分人
    addGrade(row, type) {
      this.dialogVisible = true;
    },
    //编辑评分人
    editScore(val) {
      this.uslDialogVisible = true;
      this.data = val;
    },
    //删除评分人
    deleteScore(val) {
      this.$confirm("此操作将删除该被评分人, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let data = {
            id: val.id
          };
          new Promise((response, reject) => {
            deleteScore(qs.stringify(data))
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
    //批量删除
    batchDeleteScore(val) {
      if (this.userArr.length <= 0) {
        this.$message.warning("请先勾选删除人员");
        return;
      }
      let tempArr = [];
      this.userArr.forEach(row => {
        tempArr.push(row.id);
      });
      let ids = tempArr.join(",");
      this.$confirm("此操作将删除选中的评分人, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let data = {
            ids: ids,
            dbtype: this.dbtype
          };
          new Promise((response, reject) => {
            batchDelete(qs.stringify(data))
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
    //获取选中的人员
    handleSelectionChange(val) {
      this.userArr = val;
    }
  },
  watch: {
    $route() {
      this.page.scorredstationcode = this.$route.query.code;
      this.page.scoretype = this.$route.query.type;
      this.getList();
    }
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
  margin: 0px 20px;
  border-radius: 4px;
  .el-pagination {
    margin: 20px auto;
    text-align: center;
  }
}
.score-dialog {
  max-height: 500px;
  overflow: auto;
}
</style>
