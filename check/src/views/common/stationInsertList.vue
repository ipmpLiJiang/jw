<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="uslEFDialogVisible"
      :before-close="cancel"
      width="50%"
      custom-class="score-dialog"
    >
      <el-form label-width="130px">
        <el-divider content-position="left">{{ form.typename }}</el-divider>
        <label>{{ form.dutycode }}</label>
        <label v-html="form.dutyname"></label>
        <el-divider content-position="left">评分人列表</el-divider>
      </el-form>
        <el-button
            type="primary"
            style="margin-left: 10px;padding: 6px 10px;"
            :loading="saveLoading"
            @click="save"
          >保存</el-button>
      <el-row class="content">
        <el-table
          :data="tableData"
          ref="multipleTable"
          border
          style="width: 100%"
          v-loading="tableLoading"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55"> </el-table-column>
          <el-table-column label="评分岗位" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.stationname }}({{ scope.row.departmentname }})
            </template>
          </el-table-column>
          <el-table-column
            prop="scoretype"
            label="评分类型"
            show-overflow-tooltip
          >
          </el-table-column>
        </el-table>
        <div style="text-align:center">共{{total}}条</div>
      <el-button
            type="primary"
            style="margin-left: 10px;padding: 6px 10px;"
            :loading="saveLoading"
            @click="save"
          >保存</el-button>
      </el-row>
    </el-dialog>
  </div>
</template>
<script>
import { getScorredTypeDutyEFList,updateScoreStation } from "@/api/people/people";
import qs from "qs";
export default {
  data() {
    return {
      title: "",
      tableData: [],
      page: {
        pageNum: 1,
        pageSize: 10,
      },
      total: 0,
      tableLoading: false,
      saveLoading: false,
      dbtype: this.$store.state.user.user.dbtype,
      userArr: [],
      selfDialogVisible: this.uslEFDialogVisible,
    };
  },
  props: {
    uslEFDialogVisible: {
      required: true,
    },
    form: {
      required: true,
    },
  },
  created() {
    // this.getList();
  },
  methods: {
    //查询列表
    getList() {
      let data = this.page;
      data.scorredStationCode = this.form.scorredstationcode;
      data.dutycode = this.form.dutycode;
      data.scoretype = this.form.scoretype;
      data.dbtype = this.dbtype;
      this.tableLoading = true;
      this.saveLoading = true
      new Promise((response, reject) => {
        getScorredTypeDutyEFList(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.tableData = response.data.data;
              this.total = response.data.totalPages;
              this.$nextTick(()=>{
                if (this.tableData) {
                  this.tableData.forEach(row => {
                    if ( row.scoretype ) {
                    this.$refs.multipleTable.toggleRowSelection(row);
                    }
                  });
                } else {
                  this.$refs.multipleTable.clearSelection();
                }
              })
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
            this.tableLoading = false;
            this.saveLoading = false;
          })
          .catch((error) => {
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
    save(){
      let tempArr = [];
      this.userArr.forEach(row => {
        tempArr.push(row.stationcode);
      })
      let stationcodes = tempArr.join(',');
      let data = {
        fullStationCode:stationcodes
      }
      data.scorredStationCode = this.form.scorredstationcode;
      data.dutycode = this.form.dutycode;
      data.scoretype = this.form.scoretype;
      data.dbtype = this.dbtype;
      this.tableLoading = true;
      this.saveLoading = true;
      new Promise((response, reject) => {
        updateScoreStation(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                  this.getList();
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
            this.tableLoading = false;
            this.saveLoading = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //获取选中的人员
    handleSelectionChange(val) {
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
    },
  },
  watch: {
    uslEFDialogVisible(val, oldVal) {
      this.selfDialogVisible = val;
      if (this.selfDialogVisible) {
        this.getList();
      }
    },
  },
};
</script>
<style scoped>
.content {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  border-radius: 4px;
}
</style>
