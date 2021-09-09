<template>

  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      :before-close="cancel"
      width="50%"
      custom-class="score-dialog"
      :loading="loading"
    >
      <el-input
        placeholder="输入关键字进行过滤"
        v-model="filterText"
      >
      </el-input>
      <el-tree
        :data="options"
        show-checkbox
        node-key="commoncode"
        ref="tree"
        highlight-current
        :props="props"
        :filter-node-method="filterNode"
        style="margin-top:20px"
        v-loading="loading"
        element-loading-text="拼命加载中"
      >
      </el-tree>
      <el-form
        class="demo-ruleForm"
        style="margin-top:20px"
        v-if="addType==2"
      >
        <el-form-item label="请选择评分类型：">
          <el-select
            v-model="byType"
            placeholder="请选择"
            style="width:100%;"
          >
            <el-option
              v-for="item in typeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="cancel">取 消</el-button>
        <el-button
          type="primary"
          @click="addSubmit"
        >确定</el-button>
      </span>
    </el-dialog>
  </div>

</template>
<script>
import { treelist } from "@/api/people/people";
import { addScore, addScorred } from "@/api/score/score";
import qs from "qs";
export default {
  data() {
    return {
      title: "",
      options: [],
      props: {
        id: "commoncode",
        label: "commonname",
        children: "childDept"
      },
      filterText: "",
      selfDialogVisible: this.dialogVisible,
      typeOptions: [
        {
          value: "A",
          label: "A类评分人"
        },
        {
          value: "B",
          label: "B类评分人"
        },
        {
          value: "C",
          label: "C类评分人"
        },
        {
          value: "D",
          label: "D类评分人"
        }
      ],
      byType: "",
      loading:true,
    };
  },
  props: {
    dialogVisible: {
      required: true
    },
    type: {
      required: true
    },
    usercode: {
      required: true
    },
    addType: {
      //1：添加评分人 2：添加被评分人
      default: "1"
    }
  },
  created() {
    this.getStationList();
  },
  methods: {
    //获取岗位列表
    getStationList() {
      new Promise((response, reject) => {
        treelist()
          .then(response => {
            if (response.data.code == 0) {
              this.options = this.recursionPost(
                this.recursionFunction(response.data.data)
              );
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
            this.loading = false;
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    handleChange(val) {
      this.$emit("childSelectDepartment", val[val.length - 1], val);
    },
    //递归处理后端数据形成岗位tree
    recursionFunction(list) {
      let str = [];
      list.forEach(row => {
        if (row.stations) {
          if (row.stations.length > 0) {
            if (row.childDept) {
              row.childDept = [...row.childDept, ...row.stations];
            } else {
              row.childDept = [...row.stations];
            }
          }
        }
        if (row.childDept) {
          this.recursionFunction(row.childDept);
        } else {
        }
      });

      return list;
    },
    //递归处理后端数据形成人员tree
    recursionPost(list) {
      let str = [];
      list.forEach(row => {
        if (row.childDept && row.childDept.length > 0) {
          row.childDept.forEach(res => {
            if (res.userTree) {
              res.childDept = res.userTree;
            }
          });
        }
        if (row.childDept && row.childDept.length > 0) {
          this.recursionFunction(row.childDept);
        }
      });
      return list;
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.commonname.indexOf(value) !== -1;
    },
    //取消
    cancel() {
      this.selfDialogVisible = false;
      if (!this.selfDialogVisible) {
        this.$emit("childClose", false);
      }
    },
    //添加评分人
    addSubmit() {
      let data = this.$refs.tree.getCheckedNodes();
      let usercodeArr = [];
      data.forEach(row => {
        if (row.usercode) {
          usercodeArr.push(row.usercode);
        }
      });
      data = {};
      if (this.addType == 1) {
        data.fullscorringcode = usercodeArr.join(","),
        data.scoretype = this.type;
        data.scorredcode = this.usercode,
         data.dbtype = this.$store.state.user.user.dbtype
        new Promise((response, reject) => {
          addScore(qs.stringify(data))
            .then(response => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
                this.cancel();
                this.$emit("childGetLists", "");
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
      } else if (this.addType == 2) {
        if(!this.byType){
          this.$message.warning("请选择评分人类型");
          return;
        }
         data.scorringcode = this.usercode,
         data.fullscorredcode = usercodeArr.join(","),
         data.scoretype = this.byType;
         data.dbtype = this.$store.state.user.user.dbtype
         new Promise((response, reject) => {
          addScorred(qs.stringify(data))
            .then(response => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
                this.cancel();
                this.$emit("childGetLists", "");
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
      }
    },
    //清空选中项
    setDefaultChecked() {
      this.$nextTick(() => {
        this.$refs.tree.setCheckedKeys([]); // CheckedArr 一个选中的数组element Ui的东西，关键是看$nextTick的用法
      });
    }
  },
  watch: {
    dialogVisible(val, oldVal) {
      this.selfDialogVisible = val;
      if (val) {
        this.setDefaultChecked();
      }
    },
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  }
};
</script>
<style scoped>
</style>
