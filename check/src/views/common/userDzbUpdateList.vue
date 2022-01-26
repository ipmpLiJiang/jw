<template>

  <div>
    <el-dialog
      :title="title"
      :visible.sync="uslDzbDialogVisible"
      :before-close="cancel"
      width="70%"
      custom-class="score-dialog"
    >
      <el-form label-width="100px">
        <el-row>
          <el-col :span="2" style="padding:10px 1px 2px 10px">
              姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：
          </el-col>
          <el-col :span="3">
              <el-input
                placeholder="请输入姓名"
                v-model="search.username"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
          </el-col>
          <el-col :span="3" style="padding:10px 1px 2px 35px">
            所属支部：
          </el-col>
          <el-col :span="4">
            <BranchList
            @childSelectBranch="getSelectBranch"
            :selectedOptions="tempbranchcode"
          ></BranchList>
          </el-col>
          <el-col :span="3" style="padding:10px 1px 2px 35px">
            党内身份：
          </el-col>
          <el-col :span="4">
            <el-select
              v-model="search.dbbk"
              placeholder="请选择"
              clearable
              style="width:100%;"
            >
              <el-option
                v-for="item in dbbk"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="2" style="padding:0px 0px 0px 15px">
            <el-button
            type="primary"
            @click="getList"
          >查询</el-button>
          </el-col>
        </el-row>
        <el-divider content-position="left" >党支部列表</el-divider>
        <el-button
            type="primary"
            style="margin-bottom: 10px;margin-left: 10px;padding: 6px 10px;"
            :loading="saveLoading"
            @click="save"
          >选择</el-button>
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
              label="姓名"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                {{scope.row.username}}
              </template>
            </el-table-column>
            <el-table-column
              label="所属支部"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                {{scope.row.branchname}}
              </template>
            </el-table-column>
            <el-table-column
              label="党内身份"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                {{scope.row.dbbkName}}
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
          <el-button
            type="primary"
            style="margin-bottom: 10px;margin-left: 10px;padding: 6px 10px;"
            :loading="saveLoading"
            @click="save"
          >选择</el-button>
        </el-row>
    </el-dialog>
  </div>

</template>
<script>

import BranchList from "../common/branchList";
import { addScore, getUserDzbList } from "@/api/score/score";
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
      search: {
        username: "",
        dbbk: "",
        branchcode:""
      },
      total: 0,
      tableLoading: false,
      dbtype: this.$store.state.user.user.dbtype,
      userArr:[],
      tempbranchcode: [],
      dbbk: [
        {
          value: "1",
          label: "组织委员纪检委员"
        },
        {
          value: "2",
          label: "宣传委员青年委员"
        },
        {
          value: "3",
          label: "党支部书记"
        },
        {
          value: "4",
          label: "党总支书记"
        }
      ],
      saveLoading: false,
      selfDzbDialogVisible: this.uslDzbDialogVisible,
    };
  },
  props: {
    uslDzbDialogVisible: {
      required: true
    },
    form: {
      required: true
    }
  },
  created() {
    // this.getList();
  },
  components: {
    BranchList,
  },
  methods: {
    //查询列表
    getList() {
      let data = this.page;
      data.dbbk = this.search.dbbk
      data.username = this.search.username
      data.branchcode = this.search.branchcode
      data.usercode = this.form.scorredcode
      data.political = 0
      this.tableLoading = true;
      new Promise((response, reject) => {
        getUserDzbList(qs.stringify(data))
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
    save(){
      let tempArr = [];
      this.userArr.forEach(row => {
        tempArr.push(row.usercode);
      })
      if (tempArr.length > 0) {
        let usercodes = tempArr.join(',');
        let data = this.form
        data.fullscorringcode = usercodes
        data.dbtype = this.dbtype;
        this.tableLoading = true;
        this.saveLoading = true;
        new Promise((response, reject) => {
          addScore(qs.stringify(data))
            .then((response) => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
                this.cancel();
                this.$emit("childGetLists", "");
              } else {
                this.$message({
                  message: response.data.msg,
                  type: "error",
                });
              }
              this.saveLoading = false;
              this.tableLoading = false;
            })
            .catch((error) => {
              reject(error);
            });
        });
      } else {
        this.$message({
          message: "未选择人员",
          type: "error",
        });
      }
    },
    //取消
    cancel() {
      this.selfDzbDialogVisible = false;
      if (!this.selfDzbDialogVisible) {
        this.$emit("childClose", false);
      }
    },
    //获取支部选择
    getSelectBranch(data, row) {
      this.search.branchcode = data === undefined ? '' : data;
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
    uslDzbDialogVisible(val, oldVal) {
      this.selfDzbDialogVisible = val;
      if (this.selfDzbDialogVisible){
        this.tableLoading = false;
        this.saveLoading = false;
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
