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
        <label>{{form.scoreProj}}</label>
        <el-divider content-position="left" >打分详情统计表</el-divider>
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
                {{scope.row.scorringname}}
              </template>
            </el-table-column>
            <el-table-column
              prop="ycyrs"
              label="应考核人数(人)"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="sjcyrs"
              label="实际考核人数(人)"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="ycyzb"
              label="应打分指标(条)"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="sjcyzb"
              label="实际打分指标(条)"
              show-overflow-tooltip
            >
            </el-table-column>
          </el-table>
          <div style="text-align:center">共{{total}}条</div>
        </el-row>
    </el-dialog>
  </div>

</template>
<script>

import { getFlowDetailTjList } from "@/api/score/score";
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
      let data = this.form;
      data.dbtype = this.dbtype
      this.tableLoading = true;
      new Promise((response, reject) => {
        getFlowDetailTjList(qs.stringify(data))
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
