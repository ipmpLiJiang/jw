<template>
  <div>
    <h4 class="title"><router-link to="/home"><span>首页</span></router-link><i class="el-icon-arrow-right"></i>评分汇总管理</h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="5" v-if="dbtype==2">
            <el-form-item label="所属岗位">
              <PostList
                @childSelectDepartment="getSelectStation"
                :selectedOptions="fullstationcode"
              ></PostList>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="员工姓名">
              <el-input
                placeholder="请输入员工姓名"
                v-model="search.username"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="5" v-if="dbtype==1">
            <el-form-item label="所属支部">
              <BranchList
                @childSelectBranch="getSelectBranch"
                :selectedOptions="tempbranchcode"
              ></BranchList>
            </el-form-item>
          </el-col>
          <el-col :span="4" v-if="dbtype==1">
            <el-form-item label="党内身份">
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
          </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item :label="khtitle+'状态'">
              <el-select
                v-model="search.state"
                clearable
                placeholder="请选择"
              >
                <el-option
                  v-for="item in quarterOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="5" v-if="dbtype=='1'?false:true">
            <el-form-item label="岗位类型">
              <el-select
                v-model="search.postType"
                clearable
                placeholder="请选择"
              >
                <el-option
                  v-for="item in postTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col
            :span="24"
            class="edit-btn"
          >
            <el-form-item>
              <el-button
                style="margin-left:-90px;"
                type="primary"
                @click="searchList"
                :loading="searchLoading"
              >搜索</el-button>
              <el-button
                type="warning"
                @click="exportExcel"
              ><i class="icon iconfont icon-daochu-tianchong"></i>导出excel</el-button>
            </el-form-item>
          </el-col>
        </el-form>
      </el-col>
    </el-row>
    <el-row class="content">
      <el-col style="margin-bottom:20px;">
        <el-button
          type="primary"
          @click="scdata"
          :loading="searchLoading"
        >生成被打分人</el-button>
        <el-button
          type="primary"
          @click="updateGradeZp"
        >批量自评</el-button>
        <el-button
          type="primary"
          @click="updateGrade"
        >批量评分</el-button>
        <el-button
          type="primary"
          @click="gradeFinish"
        >批量评分完成</el-button>
        <el-button
          type="warning"
          @click="updateAllZpStatus"
        >全部自评</el-button>
        <el-button
          type="warning"
          @click="updateAllStatus"
        >全部评分</el-button>
        <el-button
          type="warning"
          @click="gradeAllFinish"
        >全部评分完成</el-button>
        <el-button
          type="warning"
          @click="jisuanChang"
          :loading="searchLoading"
        >计算</el-button>
        <el-button
          type="primary"
          @click="manual"
          :loading="manualLoading"
        >开启下一个考核</el-button>
        <!-- <el-button type="danger">汇总</el-button> -->
        <el-upload
            style="display:inline-block;margin-left: .50rem"
            ref="upload"
            v-show="dbtype=='1'?false:true"
            action=""
            :http-request="uploadFile"
            :show-file-list="false"
            :auto-upload="true">
            <el-button type="primary">党风廉政上传</el-button>
          </el-upload>
      </el-col>
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        v-loading="tableLoading"
        @selection-change="changeFun"
      >
        <el-table-column
          type="selection"
          width="55"
        >
        </el-table-column>
        <el-table-column
          prop="username"
          label="用户姓名"
          show-overflow-tooltip
        >
        <template
            slot-scope="scope"
            v-if="scope.row.username"
          >
            {{scope.row.username}}({{scope.row.moneycard}})
          </template>
        </el-table-column>
        <el-table-column
          prop="branchname"
          v-if="dbtype==1"
          label="所属支部"
          width="160px"
        >
        </el-table-column>
        <el-table-column
          prop="dbbkName"
          v-if="dbtype==1"
          label="党内身份"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="departmentname"
          v-if="dbtype==2"
          label="所属部门"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="stationname"
          v-if="dbtype==2"
          label="所属岗位"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="postTypeName"
          label="岗位类型"
          v-if="dbtype==2"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="评分季度"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            {{scope.row.year}}{{'(第'+scope.row.month+'季度)'}}
          </template>
        </el-table-column>
        <el-table-column
          :label="khtitle+'状态'"
          prop="statename"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="是否生成"
          align="center"
          width="80px"
        >
          <template slot-scope="scope">
            {{scope.row.isSc==1?'是':'否'}}
          </template>
        </el-table-column>
        <el-table-column
          label="修改状态"
          align="center"
          width="80px"
        >
          <template slot-scope="scope">
            <el-button
              @click="openStatus(scope.row)"
              type="text"
              size="small"
            >修改</el-button>
          </template>
        </el-table-column>
        <el-table-column
          label="生成"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              @click="oneShengCheng(scope.row)"
              type="text"
              size="small"
            >生成默认打分</el-button>
          </template>
        </el-table-column>
        <el-table-column
          label="删除"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              @click="oneDelete(scope.row,1)"
              type="text"
              size="small"
            >删除默认打分</el-button>
          </template>
        </el-table-column>
        <el-table-column
          label="删除"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              @click="oneDelete(scope.row,2)"
              type="text"
              size="small"
            >删除被打分人</el-button>
          </template>
        </el-table-column>
        <!-- <el-table-column
          :label="khtitle+'修改/查看'"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              @click="openAdd(scope.row)"
              type="text"
              size="small"
            >编辑</el-button>
          </template>
        </el-table-column> -->
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
    <!-- 添加月结 -->
    <AddQuarter
      :parentForms="forms"
      :dialogVisible="dialogVisible"
      :isAdd="1"
      :type="1"
      @childClose="childClose"
      @childGetList="getList"
    ></AddQuarter>
     <!-- 开启时间选择框 -->
    <el-dialog
      title="提示"
      :visible.sync="timeDialogVisible"
      width="30%"
      :before-close="handleClose"
    >
      <el-form
        ref="form"
        label-width="80px"
      >
        <el-form-item label="开启时间">
          <el-date-picker
            v-model="startTime"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="timeDialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="submitManual"
        >确 定</el-button>
      </span>
    </el-dialog>
    <!-- 修改状态 -->
    <el-dialog
      title="修改状态"
      :visible.sync="statusDialogVisible"
      width="30%"
      :before-close="handleClose"
    >
      <el-select
        v-model="status"
        placeholder="请选择"
        style="width:100%"
      >
        <el-option
          v-for="item in statusOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="statusDialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="updateStatus"
        >确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import PostList from "../common/postList";
import BranchList from "../common/branchList";
import {
  getList,
  updateFinishGradeBySerialNo,
  updateFinishGradeAll,
  upload,
  shengcheng,
  oneDelete
} from "@/api/score/gradeTotal";
import { 
  updateSummaryGradeState,
  updateSummaryGradeStateZp, 
  isAllFinish, 
  openManualAssessment, 
  updateSummaryGradeStateAll, 
  updateSummaryGradeStateAllZp
} from "@/api/score/quarter";
import { updateStateBySerialNo } from "@/api/user/quarter";
import { JiSuan } from "@/api/home/home";
import AddQuarter from "../user/addQuarter";
import qs from "qs";
export default {
  data() {
    return {
      dbtype: this.$store.state.user.user.dbtype,
      tempbranchcode: [],
      khtitle: '季结',
      quarterOptions: [
        {
          value: "0",
          label: "未提交"
        },
        // {
        //   value: "1",
        //   label: "已提交"
        // },
        {
          value: "5",
          label: "自评中"
        },
        {
          value: "6",
          label: "评分中"
        },
        {
          value: "7",
          label: "评分完成"
        }
      ],
      statusOptions: [
        {
          value: "0",
          label: '未提交'
        },
        {
          value: "5",
          label: '季结自评'
        },
        {
          value: "6",
          label: '季结评分'
        },
        {
          value: "7",
          label: '季结评分完成'
        }
      ],
      title: "",
      search: {
        stationcode: "",
        username: "",
        state: "",
        postType: "",
        dbbk: "",
        branchcode: ''
      },
      dbbk: [
        {
          value: "3",
          label: "党支部书记"
        },
        {
          value: "4",
          label: "党总支书记"
        }
      ],
      postTypeOptions: [{
          value: "1",
          label: "科主任"
        },
        {
          value: "2",
          label: "护士长"
        },
        {
          value: "3",
          label: "行政"
        }],
      tableData: [],
      stationcode: [""],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      fullstationcode: [""],
      dialogVisible: false,
      tableLoading: true,
      status: "",
      statusDialogVisible: false,
      checkBoxData: [],
      tempStatusRow: {},
      forms: {},
      searchLoading:false,
      manualLoading: false,
      timeDialogVisible: false,
      startTime: "",
    };
  },
  components: {
    PostList,
    AddQuarter,
    BranchList
  },
  mounted() {},
  created() {
    this.getList();
  },
  methods: {
    //获取支部选择
    getSelectBranch(data, row) {
      this.search.branchcode = data === undefined ? '' : data;
    },
    handleClose() {
      this.statusDialogVisible = false;
    },
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
    scdata () {
      if (this.dbtype=='2' && (this.search.postType == null || this.search.postType == "")) {
        this.$message({
            message: '干部考核，必须选择 岗位类型，才能 生成/更改数据.',
            type: "error"
          });
          return;
      }
      this.$confirm(
        "此操作将生成/更改数据, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      ).then(() => {
        this.searchLoading = true;
        this.tableLoading = true;
        let data = {dbtype: this.dbtype}
        if (this.dbtype=='2' && this.search.postType !=null) {
          data.postType = this.search.postType
        } else {
          data.postType = null
        }
        new Promise((response, reject) => {
          shengcheng(qs.stringify(data))
            .then(response => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
                this.getList();
              } else {
                this.$message({
                  message: response.data.msg,
                  type: "error"
                });
              }
              this.searchLoading = false;
              this.tableLoading = false;
            })
            .catch(error => {
              reject(error);
            });
        });
      }).catch(() => {});
    },
    //搜索
    searchList() {
      this.into();
      this.getList();
    },
    //查询列表
    getList() {
      let params = this.page;
      if (this.search.stationcode.length > 0) {
        params.stationcode = this.search.stationcode[0];
      } else {
        params.stationcode = "";
      }
      params.username = this.search.username;
      params.state = this.search.state;
      params.scorestatus = this.search.scorestatus;
      params.dbtype = this.dbtype
      params.branchcode = this.search.branchcode;
      if (this.dbtype=='2' && this.search.postType !=null) {
        params.postType = this.search.postType
      } else {
        params.postType = null
      }
      if(this.dbtype=='1' && this.search.dbbk !=null){
        params.dbbk = this.search.dbbk
      }
      this.searchLoading = true;
      new Promise((response, reject) => {
        getList(qs.stringify(params))
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
            this.searchLoading = false;
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    uploadFile (param) {
      var fileObj = param.file;
      // FormData 对象
      var form = new FormData();
      form.append("file", fileObj);
      form.append("dbtype", this.$store.state.user.user.dbtype);
      new Promise((response, reject) => {
        upload(form)
          .then((response) => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success",
              });
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
      console.log("shangc")
    },
     jisuanChang() {
      if(this.dbtype == '2' && (this.search.postType == null || this.search.postType == "")) {
        this.$message({
          message: '干部考核，必须选择 岗位类型，才能 操作计算.',
          type: "error"
        });
        return;
      }
      //此操作将计算 重点、目标 指标的平均值, 是否继续?
      this.$confirm(
        "此操作将计算各项指标平均值, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(() => {
          this.searchLoading = true
          this.tableLoading = true
          let params = {}
          params.dbtype = this.dbtype
          if(this.dbtype == '2' && this.search.postType != null && this.search.postType != "") {
            params.postType = this.search.postType
          }
          new Promise((response, reject) => {
            JiSuan(qs.stringify(params))
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error"
                  });
                }
                this.tableLoading = false;
                this.searchLoading = false;
              })
              .catch(error => {
                reject(error);
              });
          });
        })
        .catch(() => {});
    },
    //获取岗位选择
    getSelectStation(data, row) {
      this.search.stationcode = [];
      this.search.stationcode.push(data);
      this.fullstationcode = row;
    },
    //获取选中的值
    changeFun(val) {
      this.checkBoxData = val;
    },
    //关闭月结
    childClose(val) {
      this.dialogVisible = val;
    },
    //打开月结
    openAdd(row) {
      this.forms = {
        serialno: row.serialno,
        title: row.title,
        content: row.content,
        savepath: row.savepath,
        filename: row.filename
      };
      this.dialogVisible = true;
    },
    oneShengCheng(row){
      this.$confirm(
        "此操作将生成/更改数据, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      ).then(() => {
        this.searchLoading = true;
        this.tableLoading = true;
        let data = {dbtype: this.dbtype}
        data.postType = row.postType
        data.userCode = row.usercode
        new Promise((response, reject) => {
          shengcheng(qs.stringify(data))
            .then(response => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
                this.getList();
              } else {
                this.$message({
                  message: response.data.msg,
                  type: "error"
                });
              }
              this.searchLoading = false;
              this.tableLoading = false;
            })
            .catch(error => {
              reject(error);
            });
        });
      }).catch(() => {});
    },
    oneDelete(row,type){
      this.$confirm(
        "此操作将删除数据, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      ).then(() => {
        let data = {dbtype: this.dbtype}
        data.userCode = row.usercode
        data.type = type
        this.searchLoading = true;
        this.tableLoading = true;
        new Promise((response, reject) => {
          oneDelete(qs.stringify(data))
            .then(response => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
                this.getList();
              } else {
                this.$message({
                  message: response.data.msg,
                  type: "error"
                });
              }
              this.searchLoading = false;
              this.tableLoading = false;
            })
            .catch(error => {
              reject(error);
            });
        });
      }).catch(() => {});
    },
    //打开修改状态
    openStatus(row) {
      this.tempStatusRow = row;
      this.statusDialogVisible = true;
    },
    //修改月结状态
    updateStatus() {
      if (!this.status) {
        this.$message.warning("请选择状态");
        return;
      }
      let params = {
        serialno: this.tempStatusRow.serialno,
        employeecode: this.tempStatusRow.moneycard,
        employeename: this.tempStatusRow.username,
        dbtype: this.dbtype,
        state: this.status
      };
      new Promise((response, reject) => {
        updateStateBySerialNo(qs.stringify(params))
          .then(response => {
            if (response.data.code == 0) {
              this.$message.success(response.data.msg);
              this.getList();
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
      this.statusDialogVisible = false;
    },
    //批量修改月结评分
    updateGrade() {
      if (this.checkBoxData.length <= 0) {
        this.$message.warning("请先勾选需要更改的数据");
        return;
      }
      let tData = [];
      this.checkBoxData.forEach(row => {
        tData.push(row.serialno);
      });
      let data = {
        serialnos: tData.join(","),
        dbtype: this.dbtype
      };
      this.$confirm("此操作将"+this.khtitle+"状态改成"+this.khtitle+"评分, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          new Promise((response, reject) => {
            updateSummaryGradeState(qs.stringify(data))
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                  this.getList();
                  this.into();
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
        .catch(() => {});
    },
    updateGradeZp() {
      if (this.checkBoxData.length <= 0) {
        this.$message.warning("请先勾选需要更改的数据");
        return;
      }
      let tData = [];
      this.checkBoxData.forEach(row => {
        tData.push(row.serialno);
      });
      let data = {
        serialnos: tData.join(","),
        dbtype: this.dbtype
      };
      this.$confirm("此操作将"+this.khtitle+"状态改成"+this.khtitle+"评分, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          new Promise((response, reject) => {
            updateSummaryGradeStateZp(qs.stringify(data))
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                  this.getList();
                  this.into();
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
        .catch(() => {});
    },
     //全部月结评分
    updateAllStatus() {
      this.$confirm("此操作将所有人"+this.khtitle+"状态改成"+this.khtitle+"评分, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          new Promise((response, reject) => {
            var params ={
                dbtype: this.dbtype
            }
            if (this.dbtype=='2' && this.search.postType !=null) {
              params.postType = this.search.postType
            } else {
              params.postType = null
            }
            updateSummaryGradeStateAll(qs.stringify(params))
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                  this.getList();
                  // this.into();
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
        .catch(() => {});
    },
     //全部月结自评
    updateAllZpStatus() {
      this.$confirm("此操作将所有人"+this.khtitle+"状态改成"+this.khtitle+"评分, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          new Promise((response, reject) => {
            var params ={
                dbtype: this.dbtype
            }
            if (this.dbtype=='2' && this.search.postType !=null) {
              params.postType = this.search.postType
            } else {
              params.postType = null
            }
            updateSummaryGradeStateAllZp(qs.stringify(params))
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                  this.getList();
                  // this.into();
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
        .catch(() => {});
    },
    //开启手动操作
    submitManual() {
      let data = {
        time: this.startTime,
        createmoneycard:this.$store.state.user.user.moneycard,
        dbtype: this.dbtype
      };
      new Promise((response, reject) => {
        openManualAssessment(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success"
              });
              this.state = 1;
              localStorage.setItem("checkState", 1);
              this.timeDialogVisible = false;
              this.getList()
              console.log(localStorage.getItem("checkState"));
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
    },
      //请求手动操作
    manual() {
      this.manualLoading = true;
      new Promise((response, reject) => {
        var params ={
          dbtype: this.dbtype
        }
        isAllFinish(qs.stringify(params))
          .then(response => {
            if (response.data.code == 0) {
              this.$confirm(response.data.msg, "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
              })
                .then(() => {
                  this.timeDialogVisible = true;
                })
                .catch(() => {});
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
            this.manualLoading = false;
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    //批量修改月结评分完成
    gradeFinish() {
      if (this.checkBoxData.length <= 0) {
        this.$message.warning("请先勾选需要更改的数据");
        return;
      }
      let tData = [];
      this.checkBoxData.forEach(row => {
        tData.push(row.serialno);
      });
      let data = {
        serialnos: tData.join(","),
        dbtype: this.dbtype
      };
      this.$confirm("此操作将"+this.khtitle+"状态改成"+this.khtitle+"评分完成, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          new Promise((response, reject) => {
            updateFinishGradeBySerialNo(qs.stringify(data))
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                  this.getList();
                  this.into();
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
        .catch(() => {});
    },
    //全部修改月结评分完成
    gradeAllFinish() {
      this.$confirm(
        "此操作将所有人"+this.khtitle+"状态改成"+this.khtitle+"评分完成, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(() => {
          let params  = {dbtype: this.dbtype}
          if (this.dbtype=='2' && this.search.postType !=null) {
            params.postType = this.search.postType
          } else {
            params.postType = null
          }
          new Promise((response, reject) => {
            updateFinishGradeAll(qs.stringify(params))
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
        .catch(() => {});
    },
    //导出
    exportExcel() {
      let info = this.search;
      if (info.stationcode.length > 0) {
        info.stationcode = info.stationcode.join();
      }
      
      info.dbtype = this.dbtype
      window.location.href =
        config.actionPath +
        "/" +
        process.env.VUE_APP_ITEM_NAME +
        "history/exportScore?info=" +
        JSON.stringify(info);
    }
  }
};
</script>


<style lang="scss" scoped>
.el-button {
    margin-left: 10px;
    padding: 6px 10px;
  }
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
  margin: 20px;
  border-radius: 4px;
  padding-top: 15px;
  .el-form-item {
    margin: 0px;
  }
  .el-button {
    margin-left: 10px;
    padding: 6px 10px;
  }
  
  .edit-btn {
    padding: 15px 0px;
    margin-top: 15px;
    border: 1px solid #ededee;
    background: #fcfcfc;
    .el-button {
      margin-left: 10px;
      padding: 6px 10px;
    }
    span {
      color: #8a919b;
      font-size: 0.9em;
      float: right;
      .icon-jinggao {
        color: #f3ad0e;
        position: relative;
        top: 1px;
      }
      i {
        margin-right: 3px;
      }
      .icon-daochu-tianchong {
        font-size: 12px;
      }
      .icon-dayin {
        font-size: 12px;
      }
    }
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
<style>
.score-dialog {
  max-height: 500px;
  overflow: auto;
}
</style>
