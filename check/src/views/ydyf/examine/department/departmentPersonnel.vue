<template>
  <div class="department">
    <h4 class="title">科室人员管理</h4>

    <el-row class="search">
      <el-col>
        <el-form label-width="100px" show-overflow-tooltip="true">
          <el-col :span="6">
            <el-form-item label="姓名">
              <el-input
                placeholder="请输入员工姓名"
                v-model="search.username"
                clearable
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="发薪号">
              <el-input
                placeholder="请选择发薪号"
                v-model="search.userId"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="员工类型">
              <el-select
                v-model="search.classify"
                placeholder="请选择员工类型"
                @change="personnel"
                clearable
              >
                <el-option
                  v-for="item in personnelList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6" v-if="search.classify">
            <el-form-item label="考核状态">
              <el-select
                v-model="search.step"
                placeholder="请选择考核状态"
                clearable
                multiple
              >
                <el-option
                  v-for="item in searchStatusOption"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24" style="margin-top: 10px">
            <el-col :span="6">
              <el-form-item label="科室">
                <el-input
                  placeholder="请输入科室"
                  v-model="search.departmentName"
                  clearable
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="党支部"
                label-width="55px"
                style="margin-left: 45px"
              >
                <el-cascader
                  v-model="search.secretaryValue"
                  :options="secretaryOptions"
                  placeholder="请选择党支部"
                  @change="handleChange"
                  style="width: 100%"
                  clearable
                ></el-cascader>
              </el-form-item>
            </el-col>
          </el-col>

          <el-col :span="24" class="edit-btn">
            <el-form-item>
              <el-button
                style="margin-left: -90px"
                type="primary"
                @click="searchList"
                ><i class="el-icon-search"></i>搜索
              </el-button>
              <!-- <el-button
                type="danger"
                @click="finishAll"
              >一键完成考评
              </el-button>
              <el-button
                type="warning"
                @click="batchCheck"
              >批量完成考评
              </el-button> -->
            </el-form-item>
          </el-col>
        </el-form>
      </el-col>
    </el-row>

    <el-row class="content">
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        v-loading="tableLoading"
        @selection-change="handleSelectionChange"
        element-loading-text="拼命加载中"
      >
        <!-- <el-table-column type="selection" width="40" align="center" >
        </el-table-column> -->
        <el-table-column
          type="selection"
          width="40"
          align="center"
          :selectable="checkboxT"
          disabled="true"
        >
        </el-table-column>
             
        <el-table-column
          type="index"
          width="60"
          :index="indexMethod"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="username"
          label="用户姓名"
          show-overflow-tooltip
          align="center"
          width="80"
        >
        </el-table-column>
        <el-table-column
          prop="userId"
          label="发薪号"
          show-overflow-tooltip
          align="center"
          width="100"
        >
        </el-table-column>
        <el-table-column
          prop="committeesName"
          label="所属党委"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="generalBranchName"
          label="所属党总支"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="partyBranchName"
          label="所属党支部"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>

        <el-table-column
          prop="departmentName"
          label="所属科室"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>

        <el-table-column label="员工类型" show-overflow-tooltip align="center">
          <template slot-scope="scope">
            <span>{{
              scope.row.personType == 0 ? "临床人员" : "非临床人员"
            }}</span
            >            
          </template>
        </el-table-column>
        <el-table-column
          label="考核状态"
          show-overflow-tooltip
          align="center"
          width="140"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.personType == '1'">
              <span v-if="scope.row.step == 0" style="color: #999">未填写</span>
              <span v-if="scope.row.step == 1" style="color: #e6a23c"
                >用户完成填写<br />等待主任打分</span
              >
              <span v-if="scope.row.step == 2" style="color: #e6a23c"
                >主任已经完成考核<br />等待书记考核</span
              >
              <span v-if="scope.row.step == 3" style="color: #409EFF"
                >书记已经完成考核</span
              >
              <span v-if="scope.row.step == 6" style="color: #409EFF"
                >已结束</span
              >
            </span>
            <span v-if="scope.row.personType == '0'">
              <span v-if="scope.row.step <= 2" style="color: #999">未填写</span>
              <span v-if="scope.row.step == 3" style="color: #e6a23c"
                >用户完成填写<br />等待主任打分</span
              >
              <span v-if="scope.row.step == 4" style="color: #e6a23c"
                >主任已经完成考核<br />等待书记考核</span
              >
              <span v-if="scope.row.step == 5" style="color: #409EFF"
                >书记已经完成考核</span
              >
              <span v-if="scope.row.step == 6" style="color: #409EFF"
                >已结束</span
              >
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="departmentDirectorName"
          label="科室主任"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="partyDirectorName"
          label="打分书记"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="160" align="center">
          <template slot-scope="scope">
            <el-button
              @click="queryDetail(scope.row)"
              type="primary"
              size="small"
              >查看</el-button
            >
            <el-button
              @click="editUser(scope.row)"
              type="warning"
              size="small"
              v-if="roleList.indexOf('100') != -1 || roleList.indexOf('102') != -1"
              >编辑</el-button
            >
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

    <el-dialog
      title="编辑人员信息"
      :visible.sync="dialogEdit"
      width="600px"
      class="edit-dialog"
    >
      <div>
        <p>
          <span>用户发薪号:</span>
          <el-input
            class="party-input"
            v-model="detailData.userId"
            placeholder="请输入内容"
            :disabled="true"
          ></el-input>
        </p>
        <p>
          <span>用户姓名:</span>
          <el-input
            class="party-input"
            v-model="detailData.username"
            placeholder="请输入内容"
            :disabled="true"
          ></el-input>
        </p>
        <p>
          <span>用户科室:</span>
          <el-select
            v-model="detailData.departmentId"
            filterable
            placeholder="请选择"
            style="width: 78%"
          >
            <el-option
              v-for="item in deptOptions"
              :key="item.id"
              :label="item.departmentName"
              :value="item.id"
            >
              <span style="float: left">{{ item.departmentName }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{
                item.branchName
              }}</span>
            </el-option>
          </el-select>
        </p>
        <p>
          <span>用户类型</span>
          <el-select
            v-model="detailData.personType"
            placeholder="请选择员工类型"
            @change="modification"
            style="width: 78%"
          >
            <el-option
              v-for="item in personnelList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </p>
        <p>
          <span>用户状态</span>
          <el-select
            v-model="detailData.step"
            placeholder="请选择用户状态"
            @change="modification"
            style="width: 78%"
          >
            <el-option
              v-for="item in personStatusOption"
              :key="item.value"
              :label="item.label"
              :value="item.value"
              :disabled="item.disabled"
            >
            </el-option>
          </el-select>
        </p>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogEdit = false">取消</el-button>
        <el-button
          size="small"
          type="primary"
          @click="updateStatus"
          :loading="isLoginLoading"
          >确定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import qs from "qs";
import {
  listUser,
  addUse,
  updateBaseInfo,
  deptListAll,
  batchFinish,
  finishAll,
} from "../../api/department/department";
import { tree } from "../../api/secretary/secretary";

export default {
  props: {},
  data() {
    return {
      search: {
        username: "",
        userId: "",
        status: "1",
        personType: "",
        secretaryValue: "",
        classify: "",
        step: "",
      },
      options: [
        // {
        //   value: "",
        //   label: "全部类型",
        // },
        {
          value: "0",
          label: "当前科室",
        },
        {
          value: "1",
          label: "当前党支部",
        },
      ],
      secretaryOptions: [],
      personnelList: [
        {
          value: "0",
          label: "临床人员",
        },
        {
          value: "1",
          label: "非临床人员",
        },
      ],
      value: "",
      condition: "1", //选择状态
      // submitShow:false, //提交按钮显示隐藏
      tableData: [],
      AddDepartments: true, //添加到科室按钮隐藏显示
      useridList: [], //待提交人员
      userId: "",
      page: {
        pageNum: 1,
        pageSize: 10,
      },
      total: 0,
      tableLoading: true,
      excelLoading: false,
      dialogVisible: false,
      detailData: {},
      title: "",
      dialogFormVisible: false,
      amendDialogFormVisible: false,
      formLabelWidth: "80px",
      form: {
        username: "",
        moneycard: "",
        departmentname: "",
      },
      modificationForm: {
        id: "",
        userName: "",
        userId: "",
        personType: "",
      },
      addLoading: false,
      childDept: "",
      excelDialogVisible: false,
      excelPath: "",
      isLoginLoading: false,
      fileList: [],
      file: {},
      dialogEdit: false,
      deptOptions: [],
      personStatusOption: [],
      searchStatusOption: [],
      step: "",
      roleList: [],
      multipleSelection: [],
    };
  },
  computed: {},
  created() {
    this.getList();
    this.EthicsUserList();
    this.deptListAll();
    this.roleList = this.$store.state.user.user.medicalEthicsRoleList;
  },
  mounted() {},
  watch: {},
  methods: {
    indexMethod(index) {
      return (this.page.pageNum - 1) * this.page.pageSize + index + 1;
    },
    //初始化
    into() {
      this.page.pageNum = 1;
      this.page.pageSize = 10;
    },
    //获取科室
    deptListAll() {
      new Promise((response, reject) => {
        deptListAll()
          .then((response) => {
            if (response.data.code == 0) {
              this.deptOptions = response.data.data;
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
      let that = this;
      let params = this.page;
      params.username = this.search.username;
      params.userId = this.search.userId;
      params.status = this.search.status;
      params.personType = this.search.personType;
      params.branchId = this.search.secretaryValue;
      params.classify = this.search.classify;
      params.step = this.search.step.toString();
      params.departmentName = this.search.departmentName;
      this.tableLoading = true;

      new Promise((response, reject) => {
        listUser(qs.stringify(params))
          .then((response) => {
            if (response.data.code == 0) {
              this.tableData = response.data.data;
              this.total = response.data.totalPages;
              this.tableLoading = false;
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
    },

    //删除人员
    deleteUser(val) {
      this.$confirm("此操作将永久删除该人员, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let data = {
            id: val.id,
          };

          new Promise((response, reject) => {
            Delete(qs.stringify(data))
              .then((response) => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success",
                  });
                  this.getList();
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
        })
        .catch(() => {
          // this.$message({
          //   type: "info",
          //   message: "已取消删除",
          // });
        });
    },
    //打开添加人员
    addPerson() {
      this.dialogFormVisible = true;
      this.modificationForm = {
        id: "",
        userName: "",
        userId: "",
        personType: "",
      };
    },
    submitUpload() {
      this.$refs.upload.submit();
    },
    //添加人员
    addUser() {
      let data = {
        userName: this.modificationForm.username,
        userId: this.modificationForm.moneycard,
      };
      if (!data.userName) {
        this.$message.warning("请填写用户名");
        return;
      }
      if (!data.userId) {
        this.$message.warning("请填写发薪号");
        return;
      }

      this.addLoading = true;
      if (data.id) {
        //修改
        new Promise((response, reject) => {
          add(qs.stringify(data))
            .then((response) => {
              if (response.data.code == 0) {
                this.$message({
                  message: response.data.msg[0],
                  type: "success",
                });
                this.getList();
              } else if (response.data.code == 1) {
                this.$confirm(response.data.msg, "提示", {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning",
                })
                  .then(() => {
                    let confirmData = {
                      moneycard: this.form.moneycard,
                    };
                    new Promise((response, reject) => {
                      confirmUpdate(qs.stringify(data))
                        .then((response) => {
                          if (response.data.code == 0) {
                            this.$message({
                              message: response.data.msg,
                              type: "success",
                            });
                            this.getList();
                          } else {
                            this.$message({
                              message: response.data.msg,
                              type: "error",
                            });
                          }
                          this.dialogFormVisible = false;
                          this.addLoading = false;
                          this.getList();
                        })
                        .catch((error) => {
                          reject(error);
                        });
                    });
                  })
                  .catch(() => {});
              } else {
                this.$message({
                  message: response.data.msg,
                  type: "error",
                });
              }
              this.dialogFormVisible = false;
              this.addLoading = false;
              this.getList();
            })
            .catch((error) => {
              reject(error);
            });
        });
      } else {
        //添加
        new Promise((response, reject) => {
          add(qs.stringify(data))
            .then((response) => {
              if (response.data.code == 2) {
                this.$message({
                  message: response.data.msg,
                  type: "error",
                });
                this.getList();
              }
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
              this.dialogFormVisible = false;
              this.addLoading = false;
              this.getList();
            })
            .catch((error) => {
              reject(error);
            });
        });
      }
    },
    //编辑
    editUser(row) {
      this.dialogEdit = true;
      if (row.personType == 1) {
        this.personStatusOption = [
          {
            value: "0",
            label: "未填写",
            // disabled:Number(row.step) <= 0 ? false : true
          },
          {
            value: "1",
            label: "完成自我评价",
            // disabled:Number(row.step) <= 1 ? false : true
          },
          {
            value: "2",
            label: "主任已经完成考核",
            // disabled:Number(row.step) <= 2 ? false : true
          },
          {
            value: "3",
            label: "书记已经完成考核",
            // disabled:Number(row.step) <= 3 ? false : true
          },
          {
            value: "6",
            label: "考核已完成",
            // disabled:Number(row.step) <= 3 ? false : true
          },
        ];
      } else {
        this.personStatusOption = [
          {
            value: "0",
            label: "未填写",
            // disabled:Number(row.step) < 5 ? false : true
          },
          {
            value: "1",
            label: "已交完基本信息",
            // disabled:Number(row.step) <= 4 ? false : true
          },
          {
            value: "2",
            label: "填写完自我总结",
            // disabled:Number(row.step) <= 3 ? false : true
          },
          {
            value: "3",
            label: "填写完自我评分",
            // disabled:Number(row.step) <= 2 ? false : true
          },
          {
            value: "4",
            label: "科室主任打分完毕",
            // disabled:Number(row.step) <= 1 ? false : true
          },
          {
            value: "5",
            label: "书记打分完毕",
          },
          {
            value: "6",
            label: "考核已完成",
            // disabled:Number(row.step) <= 3 ? false : true
          },
        ];
      }

      this.detailData = JSON.parse(JSON.stringify(row));
      this.detailData.step = String(this.detailData.step);
    },
    //查看
    queryDetail(row) {
      if (row.personType == 1) {
        if (row.step > 0) {
        
           let url = this.$router.resolve({
            path: "/ydyf/resultNonclinical",
            query: { userId: row.userId },
          })
           window.open(url.href, "_blank");
        } else {
          this.$message.warning("用户还未填写，暂时无法查看");
          return;
        }
      } else {
        if (row.step > 2) {
       
          let url = this.$router.resolve({
            path: "/ydyf/resultclinicalForm",
            query: { userId: row.userId },
          });

          window.open(url.href, "_blank");
        } else {
          this.$message.warning("用户还未填写，暂时无法查看");
          return;
        }
      }
    },
    //修改人员
    updateStatus() {
      let data = this.detailData;
      if (!data.departmentId) {
        this.$message.warning("请选择科室再提交！");
        return;
      }
      this.isLoginLoading = true;
      return new Promise((response, reject) => {
        updateBaseInfo(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.$message.success(response.data.msg);
              this.getList();
            } else {
              this.$message.warning(response.data.msg);
            }
            this.isLoginLoading = false;
            this.dialogEdit = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //获取table选择的数据
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    //批量完成
    batchCheck() {
      if (this.multipleSelection.length <= 0) {
        this.$message.warning("请先选择用户");
        return;
      }
      let tarr = [];
      this.multipleSelection.forEach((row) => {
        tarr.push(row.userId);
      });
      let data = {
        userIdList: tarr.join(","),
      };
      this.$confirm(
        "此操作会将选中的用户考核状态更改成已完成, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          return new Promise((response, reject) => {
            batchFinish(qs.stringify(data))
              .then((response) => {
                if (response.data.code == 0) {
                  this.$message.success(response.data.msg);
                  this.getList();
                } else {
                  this.$message.warning(response.data.msg);
                }
                this.isLoginLoading = false;
                this.dialogEdit = false;
              })
              .catch((error) => {
                reject(error);
              });
          });
        })
        .catch(() => {});
    },
    //一键完成
    finishAll() {
      this.$confirm(
        "此操作将完成此次医德考评，并将所有人的考核状态更改成已完成, 是否继续?",
        "谨慎操作",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          return new Promise((response, reject) => {
            finishAll()
              .then((response) => {
                if (response.data.code == 0) {
                  this.$message.success(response.data.msg);
                  this.getList();
                } else {
                  this.$message.warning(response.data.msg);
                }
                this.isLoginLoading = false;
                this.dialogEdit = false;
              })
              .catch((error) => {
                reject(error);
              });
          });
        })
        .catch(() => {});
    },
    //提交资料
    submit() {
      let userIdList = {
        userIdList: this.userId,
      };
      if (this.useridList) {
        this.$confirm("请再次提交确认信息", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }).then(() => {
          this.AddDepartments = false;
          new Promise((response, reject) => {
            addUse(qs.stringify(userIdList))
              // addUse(userIdList)
              .then((response) => {
                if (response.data.code == 0) {
                  this.getList();
                  this.$message({
                    message: response.data.msg,
                    type: "success",
                  });
                  this.AddDepartments = true;
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error",
                  });
                  this.AddDepartments = true;
                }
              })
              .catch((error) => {
                reject(error);
              });
          });
        });
      } else {
        this.$message.warning("请先勾选您要提交的选项，再进行提交！");
      }
    },
    //获取下拉选择框数据
    currentSel(selVal) {
      this.search.status = selVal;
    },
    //获取员工类型数据
    personnel(val) {
      this.search.personType = val;
      this.search.step = "";
      if (val == "1") {
        this.searchStatusOption = [
          {
            value: "0",
            label: "未填写",
            // disabled:Number(row.step) <= 0 ? false : true
          },
          {
            value: "1",
            label: "完成自我评价",
            // disabled:Number(row.step) <= 1 ? false : true
          },
          {
            value: "2",
            label: "主任已经完成考核",
            // disabled:Number(row.step) <= 2 ? false : true
          },
          {
            value: "3",
            label: "书记已经完成考核",
            // disabled:Number(row.step) <= 3 ? false : true
          },
          {
            value: "6",
            label: "已结束",
            // disabled:Number(row.step) <= 3 ? false : true
          },
        ];
      } else {
        this.searchStatusOption = [
          {
            value: "0",
            label: "未填写",
            // disabled:Number(row.step) < 5 ? false : true
          },
          {
            value: "1",
            label: "已交完基本信息",
            // disabled:Number(row.step) <= 4 ? false : true
          },
          {
            value: "2",
            label: "填写完自我总结",
            // disabled:Number(row.step) <= 3 ? false : true
          },
          {
            value: "3",
            label: "填写完自我评分",
            // disabled:Number(row.step) <= 2 ? false : true
          },
          {
            value: "4",
            label: "科室主任打分完毕",
            // disabled:Number(row.step) <= 1 ? false : true
          },
          {
            value: "5",
            label: "书记打分完毕",
            // disabled: Number(row.step) <= 0 ? false : true,
          },
          {
            value: "6",
            label: "已结束",
            // disabled:Number(row.step) <= 3 ? false : true
          },
        ];
      }
    },
    //编辑下拉框
    modification(val) {
      this.modificationForm.personType = val;
    },
    //编辑确认
    redact() {
      let data = this.modificationForm;
      new Promise((response, reject) => {
        update(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success",
              });
              this.amendDialogFormVisible = false;
              this.getList();
            } else if (response.data.code == 1) {
              // this.amendDialogFormVisible = false
              this.$confirm(response.data.msg, "提示", {
                confirmButtonText: "确定",
                type: "warning",
              })
                .then(() => {
                  let confirmData = {
                    moneycard: this.form.moneycard,
                  };
                  new Promise((response, reject) => {
                    confirmUpdate(qs.stringify(data))
                      .then((response) => {
                        if (response.data.code == 0) {
                          this.$message({
                            message: response.data.msg,
                            type: "success",
                          });
                          this.getList();
                        } else {
                          this.$message({
                            message: response.data.msg,
                            type: "error",
                          });
                        }

                        this.getList();
                      })
                      .catch((error) => {
                        reject(error);
                      });
                  });
                })
                .catch(() => {});
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
            this.dialogFormVisible = false;
            this.addLoading = false;
            // this.getList();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //判断多选框
    checkboxT(row, index) {
      if (row.step == 6) {
        return false;
      } else {
        return true;
      }
    },

    //获取党支部三级联动
    EthicsUserList() {
      let params = this.page;

      new Promise((response, reject) => {
        tree(qs.stringify(params))
          .then((response) => {
            if (response.data.code == 0) {
              this.secretaryOptions = response.data.data.children;
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
    },
    //获取选中党支部
    handleChange(val) {
      if (val.length > 0) {
        this.search.secretaryValue = val[val.length - 1];
      }
    },
    //重置
    reset() {
      (this.search.username = ""),
        (this.search.userId = ""),
        (this.search.classify = ""),
        (this.search.secretaryValue = ""),
        this.getList();
    },
  },
  components: {},
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
  margin: 20px;
  border-radius: 4px;
  padding-top: 15px;

  .el-form-item {
    margin: 0px;
  }

  .el-button {
    margin-left: 10px;
  }

  .edit-btn {
    padding: 15px 0px;
    margin-top: 15px;
    border: 1px solid #ededee;
    background: #fcfcfc;

    .el-button {
      margin-left: 10px;
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

.upload-grade-leading-in {
  display: inline-block;
}

.monitor {
  color: #f00;
  font-size: 18px;
  margin-bottom: 10px;
  display: block;
  text-align: left;
}

.a-demo {
  display: block;
  color: #e6a23c;
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
  text-decoration: underline;
  text-align: left;
  cursor: pointer;
}
.el-dialog {
  p {
    font-size: 12px;
    border-bottom: 1px solid #ededed;
    line-height: 50px;
    span {
      font-weight: 600;
      display: inline-block;
      width: 120px;
    }
  }
  .party-input {
    width: 78%;
    input {
      border: 0;
    }
  }
}
</style>
<style>
.score-dialog {
  max-height: 500px;
  overflow: auto;
}
</style>