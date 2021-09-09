<template>
  <div>
    <h4 class="title">党支部信息</h4>
    <el-row style="padding: 20px">
      <el-col :span="7" class="left">
        <el-col :span="18">
          <el-input
            clearable
            placeholder="搜索支部"
            v-model="searchForm.relationsName"
          >
          </el-input>
        </el-col>
        <!-- <el-col :span="5"> -->
        <el-col :lg="5" :xl="6">
          <el-button type="primary" @click="search">搜索</el-button>
        </el-col>

        <!-- <el-col :span="12">
          <el-input clearable placeholder="搜索支部" v-model="filterText">
          </el-input>
        </el-col> -->

        <!-- <el-col :span="7"> -->
        <el-col :md="12" :sm="24" :lg="8">
          <el-button type="primary" @click="addParye"
           
            >添加党委</el-button
          >
        </el-col>
        <!-- <el-col :span="7"> -->
        <el-col :md="12" :sm="24" :lg="8">
          <el-button
            type="primary"
            @click="headQuarters"
        
            >添加党总支</el-button
          >
        </el-col>
        <!-- <el-col :span="7"> -->
        <el-col :md="12" :sm="24" :lg="8">
          <el-button
            type="primary"
            @click="addBranch"
            
            >添加党支部</el-button
          >
        </el-col>

        <el-col :span="24">
          <el-tree
            class="filter-tree"
            :data="data5"
            :filter-node-method="filterNode"
            @node-click="handleNodeClick"
            ref="tree2"
            :props="defaultProps"
            :default-expanded-keys="treeExpandData"
          >
          </el-tree>
        </el-col>
      </el-col>
      <el-col :span="16" class="right">
        <el-table
          :data="tableData"
          border
          style="width: 100%"
          v-loading="tableLoading"
        >
          <!-- <el-table-column type="selection" width="40" align="center" >
        </el-table-column> -->

          <!-- <el-table-column
          type="selection"
          width="40"
          align="center"
          :selectable="checkboxT"
          disabled="true"
        >
        </el-table-column> -->
                        

          <!-- <el-table-column type="index" width="50" :index="indexMethod">
        </el-table-column> -->
          <el-table-column
            prop="relationsName"
            label="党支部名称"
            show-overflow-tooltip
            align="center"
            width="200"
          >
          </el-table-column>

          <el-table-column
            prop="parentRelationName"
            label="上级党委"
            show-overflow-tooltip
            align="center"
            width="200"
          >
          </el-table-column>
          <!-- 
          <el-table-column
            prop="branchName"
            label="党支部名称"
            show-overflow-tooltip
            align="center"
            width="140"
          >
          </el-table-column> -->
          <el-table-column
            prop="leaderName"
            label="书记姓名"
            show-overflow-tooltip
            align="center"
            width="120"
          >
          </el-table-column>

          <!-- <el-table-column
            prop="leaderName"
            label="科室名称"
            show-overflow-tooltip
            align="center"
            width="140"
          >
          </el-table-column> -->
          <!-- <el-table-column
            prop="directorName"
            label="科室主任姓名"
            show-overflow-tooltip
            align="center"
            width="120"
          >
          </el-table-column> -->
          <el-table-column
            prop=""
            label="是否混合党支部"
            show-overflow-tooltip
            align="center"
            width="120"
          >
          </el-table-column>

          <el-table-column
            label="员工类型"
            show-overflow-tooltip
            align="center"
          >
            <template slot-scope="scope">
              <span>{{
                scope.row.personType == 0 ? "临床人员" : "非临床人员"
              }}</span
              >            
            </template>
          </el-table-column>

          <el-table-column
            fixed="right"
            label="操作"
            width="160"
            align="center"
          >
            <template slot-scope="scope">
              <el-button @click="editUser(scope.row)" type="text" size="small"
                >编辑</el-button
              >
              <el-button @click="deleteUser(scope.row)" type="text" size="small"
                >删除
              </el-button>
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
      </el-col>
    </el-row>

    <!-- 编辑党支部人员 -->
    <el-dialog
      title="编辑党支部"
      :visible.sync="amendDialogFormVisible"
      width="30%"
    >
      <el-form :model="modificationForm">
        <el-form-item label="党支部名字" :label-width="formLabelWidth">
          <!-- <el-input
            v-model="modificationForm.relationsName"
            autocomplete="off"
            :disabled="true"
          ></el-input> -->
          <el-input
            v-model="modificationForm.relationsName"
            autocomplete="off"
          ></el-input>
        </el-form-item>

        <el-form-item label="书记姓名" :label-width="formLabelWidth">
          <el-input
            v-model="modificationForm.leaderName"
            autocomplete="off"
          ></el-input>
        </el-form-item>

        <el-form-item label="书记发薪号" :label-width="formLabelWidth">
          <el-input
            v-model="modificationForm.leaderUserId"
            autocomplete="off"
          ></el-input>
        </el-form-item>

        <el-form-item label="打分书记姓名" :label-width="formLabelWidth">
          <el-input
            v-model="modificationForm.directorName"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="打分书记发薪号" :label-width="formLabelWidth">
          <el-input
            v-model="modificationForm.directorUserId"
            autocomplete="off"
          ></el-input>
        </el-form-item>

        <el-form-item label="评优百分比" :label-width="formLabelWidth">
          <el-slider v-model="modificationForm.excellentPercent" show-input>
          </el-slider>
        </el-form-item>

        <el-form-item label="员工类型" :label-width="formLabelWidth">
          <el-select
            v-model="modificationForm.personType"
            placeholder="请选择员工类型"
            @change="modification"
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
        <el-form-item
          label="所属党支部"
          :label-width="formLabelWidth"
          v-if="partyShow"
        >
          <el-cascader
            v-model="modificationForm.parentRelationName"
            :options="secretaryOptions"
            :placeholder="title"
            @change="handleChange"
        
            :props="{ checkStrictly: true }"
            style="width: 70%"
          ></el-cascader>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="amendDialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="redact" :loading="addLoading"
          >确 定</el-button
        >
      </div>
    </el-dialog>

    <!-- 新增党委人员 -->
    <el-dialog title="添加党委" :visible.sync="addpartyVisible" width="30%">
      <el-form :model="addparty">
        <el-form-item label="党委名称" :label-width="formLabelWidth">
          <el-input
            v-model="addparty.relationsName"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="党委书记名" :label-width="formLabelWidth">
          <el-input v-model="addparty.leaderName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="党委书记发薪号" :label-width="formLabelWidth">
          <el-input
            v-model="addparty.leaderUserId"
            autocomplete="off"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addpartyVisible = false">取 消</el-button>
        <el-button type="primary" @click="addParyesbumit" :loading="addLoading"
          >确 定</el-button
        >
      </div>
    </el-dialog>

    <!-- 添加党总支部 -->
    <el-dialog title="添加党总支部" :visible.sync="headquarters" width="30%">
      <el-form :model="headquartersForm">
        <el-form-item label="党总支部名称" :label-width="formLabelWidth">
          <el-input
            v-model="headquartersForm.relationsName"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="党总支部书记名" :label-width="formLabelWidth">
          <el-input
            v-model="headquartersForm.leaderName"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="党总支部书记发薪号" :label-width="formLabelWidth">
          <el-input
            v-model="headquartersForm.leaderUserId"
            autocomplete="off"
          ></el-input>
        </el-form-item>

        <el-form-item label="所属党委" :label-width="formLabelWidth">
          <el-select
            v-model="headquartersForm.parentId"
            placeholder="请选择所属党委"
            @change="modification"
          >
            <el-option
              v-for="item in committee"
              :key="item.id"
              :label="item.relationsName"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="headquarters = false">取 消</el-button>
        <el-button
          type="primary"
          @click="headquarterSbumit"
          :loading="addLoading"
          >确 定</el-button
        >
      </div>
    </el-dialog>

    <!-- 添加党支部 -->
    <el-dialog title="添加党支部" :visible.sync="branch" width="30%">
      <el-form :model="branchForm">
        <el-form-item label="党支部名称" :label-width="formLabelWidth">
          <el-input
            v-model="branchForm.relationsName"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="党支部书记名" :label-width="formLabelWidth">
          <el-input
            v-model="branchForm.leaderName"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="党支部书记发薪号" :label-width="formLabelWidth">
          <el-input
            v-model="branchForm.leaderUserId"
            autocomplete="off"
          ></el-input>
        </el-form-item>

          <el-form-item label="打分书记姓名" :label-width="formLabelWidth">
          <el-input
            v-model="branchForm.directorName"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="打分书记发薪号" :label-width="formLabelWidth">
          <el-input
            v-model="branchForm.directorUserId"
            autocomplete="off"
          ></el-input>
        </el-form-item>

        <el-form-item label="所属党总支部" :label-width="formLabelWidth">
          <el-cascader
            v-model="branchForm.parentId"
            :options="branchList"
            :placeholder="title"
            @change="branchChange"
            :props="{ checkStrictly: true }"
            style="width: 70%"
          ></el-cascader>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="branch = false">取 消</el-button>
        <el-button type="primary" @click="branchSbmit" :loading="addLoading"
          >确 定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  branchTree,
  list,
  Delete,
  update,
  Add,
  tree,
  headquartersList,
  AddGeneral,
  partyList,
  search,
} from "../../api/party/index";
import qs from "qs";
export default {
  props: {},
  data() {
    return {
      searchForm: {
        relationsName: "",
      },
      value: "15",
      form: {
        branchcode: "",
        upbranchcode: "",
        branchname: "",
        branchdesc: "",
        secretaryValue: "",
      },
      childForm: {
        branchcode: "",
        branchname: "",
        branchdesc: "",
      },
      data5: [], //后台返回树形数据
      treeExpandData: [], //自己定义的用于接收tree树id的数组
      provincialCenterId: "", //用来定义获取的默认显示的id
      tableData: [],
      defaultProps: {
        children: "children",
        label: "relationsName",
      },
      page: {
        pageNum: 1,
        pageSize: 10,
      },
      parentId: "",
      formLabelWidth: "130px",
      saveDisabled: true,
      delDisabled: true,
      subdivisionVisible: false,
      tableLoading: false,
      addLoading: false,
      amendDialogFormVisible: false,
      addpartyVisible: false,
      headquarters: false, //党总支
      branch: false,
      partyShow: false,

      headquartersForm: {
        level: "2",
        relationsName: "",
        leaderName: "",
        leaderUserId: "",
        parentId: "",
      },
      //  党总支表单
      modificationForm: {
        relationsName: "", //党支部名字
        parentId: "",
        leaderName: "",
        leaderUserId: "",
        personType: "",
        id: "",
        // parentRelationName: "",
        excellentPercent: 15,
        directorName: "",
        directorUserId: "",
      },
      //添加党委表单
      partyCommittee: {},
      addparty: {
        leaderName: "",
        relationsName: "",
        leaderUserId: "",
        parentId: -1,
        level: "1",
      },
      branchForm: {
        relationsName: "",
        leaderName: "",
        leaderUserId: "",
        parentId: "",
        level: "3",
      },
      personnelList: [
        {
          value: "",
          label: "全部类型",
        },
        {
          value: 0,
          label: "临床人员",
        },
        {
          value: 1,
          label: "非临床人员",
        },
      ],
      committee: [], //所属党委
      branchList: [], //所属党总支

      secretaryOptions: [],
      title: "",
      total: 0,
    };
  },
  computed: {},
  created() {
    this.getParty();
    this.getList();
  },
  mounted() {},
  watch: {},
  methods: {
    //树形列表查找功能
    filterNode(value, data) {},
    //树形图点击回调
    handleNodeClick(data) {
   
      //   this.parentId = data.id;
      if (data.level == 3) {
        this.parentId = data.parentId;
      } else {
        this.parentId = data.id;
      }
      this.getList();
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
    getParty() {
      new Promise((response, reject) => {
        branchTree()
          .then((response) => {
            if (response.data.code == 0) {
              this.data5 = response.data.data;
              this.provincialCenterId = this.data5[0].id;
              this.getRoleTreeRootNode(response.data.data[0].id);
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
    getList() {
      let data = this.page;
      data.parentId = this.parentId;
      new Promise((response, reject) => {
        list(qs.stringify(data))
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
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    //搜索
    search() {
      let relationsName = this.searchForm.relationsName;
      new Promise((response, reject) => {
        search(relationsName)
          .then((response) => {
            console.log(response);
            if (response.data.code == 0) {
              this.tableData = response.data.data;
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
    //编辑
    editUser(row) {
      this.partyShow = true;

      if (row.level === 1) {
        this.partyShow = false;
      }
      this.amendDialogFormVisible = true;
      this.modificationForm.relationsName = row.relationsName;
      this.modificationForm.parentRelationName = row.parentRelationName;
      this.modificationForm.leaderName = row.leaderName;
      this.modificationForm.parentId = row.parentId;
      this.modificationForm.level = row.level;
      this.modificationForm.leaderUserId = row.leaderUserId;
      this.modificationForm.personType = row.personType;
      this.modificationForm.id = row.id;
      this.modificationForm.directorUserId = row.directorUserId;
      this.modificationForm.directorName = row.directorName;
      this.title = row.parentRelationName;
      this.form.secretaryValue = row.id;
      this.tree();
    },
    //下拉菜单
    modification(val) {
      this.headquartersForm.parentId = val;
    },
    //编辑确定按钮
    redact() {
      // let data = this.modificationForm;
      // console.log(data)
      let data = {
        relationsName: this.modificationForm.relationsName,
        parentId: this.modificationForm.parentId,
        leaderName: this.modificationForm.leaderName,
        leaderUserId: this.modificationForm.leaderUserId,
        personType: this.modificationForm.personType,
        relationidsName: this.modificationForm.id,
        excellentPercent: this.modificationForm.excellentPercent,

        directorUserId: this.modificationForm.directorUserId,
        directorName: this.modificationForm.directorName,
        level: this.modificationForm.level,
        id: this.modificationForm.id,
        directorUserId: this.modificationForm.directorUserId,
      };
      new Promise((response, reject) => {
        update(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              (this.modificationForm = {}), (this.secretaryOptions = []);
              this.amendDialogFormVisible = false;
              this.getList();
              this.$message.success(response.data.msg);
              this.modificationForm = { directorName: "", directorUserId: "" };
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
    //获取支部信息列表
    tree() {
      new Promise((response, reject) => {
        tree(qs.stringify())
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
    //支部菜单点击事件
    handleChange(val) {
      console.log(val);
      if (val.length > 0) {
        this.modificationForm.parentId = val[val.length - 1];
      }
    },
    //添加党委
    addParye() {
      this.addpartyVisible = true;
      this.addparty = {};
    },
    //新增党委确定键
    addParyesbumit() {
      let data = this.addparty;
      data.level = "1";
      data.parentId = "-1";
      new Promise((response, reject) => {
        Add(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.getParty();
              this.getList();
              this.addparty = {};
              this.addpartyVisible = false;
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
    //删除
    deleteUser(row) {
      this.$confirm("此操作将永久删除该人员, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let id = row.id;
          new Promise((response, reject) => {
            Delete(id)
              .then((response) => {
                if (response.data.code == 0) {
                  this.$message.success(response.data.msg);
                  this.getList();
                  this.getParty();
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
          this.$message({
            type: "warning",
            message: "已取消删除",
          });
        });
    },
    //添加党总支部确认
    headquarterSbumit() {
      let data = this.headquartersForm;
      data.level = "2";
      new Promise((response, reject) => {
        Add(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.$message.success(response.data.msg);
              this.headquarters = false;
              this.getParty();
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
    },
    //获取党党委下拉菜单列表
    headQuarters() {
      this.headquarters = true;
      (this.headquartersForm = {}), (this.committee = []);
      let Level = "1";
      new Promise((response, reject) => {
        headquartersList(Level)
          .then((response) => {
            if (response.data.code == 0) {
              this.committee = response.data.data;
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
    //点击党支部
    addBranch() {
      this.branch = true;
      this.branchForm = {};
      new Promise((response, reject) => {
        partyList()
          .then((response) => {
            if (response.data.code == 0) {
              this.branchList = response.data.data.children;
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

    branchChange(val) {
      if (val.length > 0) {
        this.branchForm.parentId = val[val.length - 1];
      }
    },
    //添加党支部确认键
    branchSbmit() {
      let data = this.branchForm;
      data.level = "3";
      new Promise((response, reject) => {
        AddGeneral(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.$message.success(response.data.msg);
              this.branch = false;
              this.getParty();
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
    },

    // 获取树形结构默认展开节点
    getRoleTreeRootNode(provincialCenterId) {
      this.treeExpandData.push(provincialCenterId);
      console.log(this.treeExpandData);
    },
  },
  components: {},
};
</script>

<style scoped lang="scss">
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
    text-align: center;
  }
}
.left {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  margin-right: 20px;
  border-radius: 5px;
  .el-input {
    margin-bottom: 20px;
  }
  .el-button {
    width: 95%;
    margin-left: 5%;
  }
  .filter-tree {
    height: 430px;
    overflow-y: scroll;
  }
}
.right {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  border-radius: 5px;
  height: 530px;
}
.el-button--primary{
  white-space:break-spaces !important;
}
</style>
