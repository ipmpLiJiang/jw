<template>

  <div>
    <el-dialog
      :title="title"
      :visible.sync="uslDialogVisible"
      :before-close="cancel"
      width="50%"
      custom-class="score-dialog"
    >
      <el-form label-width="130px">
        <el-divider content-position="left" >{{form.typename}}</el-divider>
        <label>{{form.dutycode}}</label>
        <label v-html="form.dutyname"></label>
        <el-divider content-position="left" >评分人列表</el-divider>
      </el-form>
        <el-row class="content">
          <el-table
            :data="tableData"
            border
            style="width: 100%"
            v-loading="tableLoading"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              type="selection"
              width="55"
            >
            </el-table-column>
            <el-table-column
              label="评分员工"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                {{scope.row.scorringname}}({{scope.row.departmentname1}}-{{scope.row.stationname1}})
              </template>
            </el-table-column>
            <el-table-column
              fixed="right"
              label="操作"
              width="180"
            >
              <template slot-scope="scope">
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
    </el-dialog>
  </div>

</template>
<script>

import { getScoreDutyScorringUserlist } from "@/api/score/score";
import { deleteScore } from "@/api/score/score";
import qs from "qs";
export default {
  data() {
    return {
      title: "",
      tableData: [],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      tableLoading: false,
      dbtype: this.$store.state.user.user.dbtype,
      userArr:[],
      selfDialogVisible: this.uslDialogVisible,
    };
  },
  props: {
    uslDialogVisible: {
      required: true
    },
    form: {
      required: true
    }
  },
  created() {
    // this.getList();
  },
  methods: {
    //查询列表
    getList() {
      let data = this.page;
      data.scorredCode = this.form.scorredcode
      data.dutycode = this.form.dutycode
      data.scoretype = this.form.scoretype
      data.dbtype = this.dbtype
      this.tableLoading = true;
      new Promise((response, reject) => {
        getScoreDutyScorringUserlist(qs.stringify(data))
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
    //取消
    cancel() {
      this.selfDialogVisible = false;
      if (!this.selfDialogVisible) {
        this.$emit("childClose", false);
      }
    },
    //删除评分人
    deleteScore(row) {
      this.$confirm("此操作将删除该指标, 评分人, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let data = {
            id: row.id
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
                  this.$emit("childGetList");
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
    handleSelectionChange(val){
      this.userArr = val;
    },
    //翻页
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.getList();
    },
    //设置每页多少条数据
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getList();
    }
  },
  watch: {
    uslDialogVisible(val, oldVal) {
      this.selfDialogVisible = val;
      if (this.selfDialogVisible){
        this.getList()
      }
    }
  }
};
</script>
<style scoped>
.content {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  border-radius: 4px;
}
</style>
