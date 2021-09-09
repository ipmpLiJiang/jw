<template>
  <div>
    <h4 class="title"><router-link to="/home"><span>首页</span></router-link><i class="el-icon-arrow-right"></i>部门管理</h4>
    <el-row style="padding:20px;">
      <el-col
        :span="7"
        class="left"
      >
        <el-col :span="16">
          <el-input
            clearable
            placeholder="搜索部门"
            v-model="filterText"
          >
          </el-input>
        </el-col>
        <el-col :span="8">
          <el-button
            type="primary"
            @click="add"
          >添加部门</el-button>
        </el-col>
        <el-col :span="24">
          <el-tree
            class="filter-tree"
            :data="data5"
            default-expand-all
            :filter-node-method="filterNode"
            @node-click="handleNodeClick"
            :props="defaultProps"
            ref="tree2"
          >
          </el-tree>
        </el-col>
      </el-col>
      <el-col
        :span="16"
        class="right"
      >
        <el-form
          ref="form"
          :model="form"
          label-width="80px"
        >
          <el-form-item label="部门名称">
            <el-input v-model="form.departmentname"></el-input>
          </el-form-item>
          <el-form-item label="部门描述">
            <el-input
              type="textarea"
              :rows="5"
              v-model="form.departmentdesc"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              :disabled="saveDisabled"
              @click="addDepartment"
            >保存修改</el-button>
            <!-- <el-button
              type="warning"
              :disabled="delDisabled"
              @click="openSubdivision"
            >添加子部门</el-button> -->
            <el-button
              type="danger"
              :disabled="delDisabled"
              @click="deleteDepartment"
            >删除部门</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <!-- 添加子部门 -->
    <el-dialog
      title="添加子部门"
      :visible.sync="subdivisionVisible"
    >
      <el-form :model="form">
        <el-form-item label="部门名称">
          <el-input v-model="childForm.departmentname"></el-input>
        </el-form-item>
        <el-form-item label="部门描述">
          <el-input
            type="textarea"
            :rows="5"
            v-model="childForm.departmentdesc"
          ></el-input>
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="subdivisionVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="addChildDepartment"
        >确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  treelist,
  addDepartment,
  updateDepartment,
  deleteDepartment
} from "@/api/post/department";
import qs from "qs";
export default {
  data() {
    return {
      filterText: "",
      form: {
        departmentcode: "",
        updepartmentcode: "",
        departmentname: "",
        departmentdesc: ""
      },
      childForm: {
        departmentcode: "",
        departmentname: "",
        departmentdesc: ""
      },
      data5: [],
      defaultProps: {
        children: "childDept",
        label: "departmentname"
      },
      saveDisabled: true,
      delDisabled: true,
      subdivisionVisible: false
    };
  },
  components: {},
  mounted() {},
  created() {
    this.getList();
  },
  methods: {
    //树形列表查找功能
    filterNode(value, data) {
      if (!value) return true;
      return data.departmentname.indexOf(value) !== -1;
    },
    //树形图点击回调
    handleNodeClick(data) {
      this.form = data;
    },
    //初始化数据
    into() {
      this.form = {
        departmentcode: "",
        updepartmentcode: "",
        departmentname: "",
        departmentdesc: ""
      };
    },
    //部门列表
    getList() {
      new Promise((response, reject) => {
        treelist()
          .then(response => {
            if (response.data.code == 0) {
              this.data5 = response.data.data;
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
    //添加部门按钮事件
    add() {
      this.into();
    },
    //添加/修改部门
    addDepartment() {
      if (this.form.departmentcode) {
        //修改
        delete this.form.childDept;
        new Promise((response, reject) => {
          updateDepartment(qs.stringify(this.form))
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
      } else {
        //添加
        new Promise((response, reject) => {
          addDepartment(qs.stringify(this.form))
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
      }
    },
    //删除部门
    deleteDepartment() {
      this.$confirm("此操作将永久删除该部门, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let data = {
            departmentcode: this.form.departmentcode
          };
          new Promise((response, reject) => {
            deleteDepartment(qs.stringify(data))
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
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    //打开添加子部门弹框
    openSubdivision() {
      this.subdivisionVisible = true;
    },
    //添加子部门
    addChildDepartment() {
      if (!this.childForm.departmentname) {
        this.$message({
          message: "请填写部门名称",
          type: "warning"
        });
        return;
      }
      if (!this.childForm.departmentdesc) {
        this.$message({
          message: "请填写部门描述",
          type: "warning"
        });
        return;
      }
      this.childForm.departmentcode = this.form.departmentcode;
      //添加
      new Promise((response, reject) => {
        addDepartment(qs.stringify(this.childForm))
          .then(response => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success"
              });
              this.getList();
              this.subdivisionVisible = false;
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
    }
  },
  //监听数据
  watch: {
    filterText(val) {
      this.$refs.tree2.filter(val);
    },
    "form.departmentname": {
      handler(val, oldVal) {
        if (this.form.departmentname && this.form.departmentdesc) {
          this.saveDisabled = false;
        } else {
          this.saveDisabled = true;
        }
      }
    },
    "form.departmentdesc": {
      handler(val, oldVal) {
        if (this.form.departmentname && this.form.departmentdesc) {
          this.saveDisabled = false;
        } else {
          this.saveDisabled = true;
        }
      }
    },
    "form.departmentcode": {
      handler(val, oldVal) {
        if (val) {
          this.delDisabled = false;
        } else {
          this.delDisabled = true;
        }
      }
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
</style>
