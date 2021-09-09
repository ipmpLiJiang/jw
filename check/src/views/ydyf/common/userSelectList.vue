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
        <el-form-item label="评分员工">
          <el-cascader
            :options="options"
            :show-all-levels="false"
            :props="props"
            @change="handleChange"
            filterable
            style="width:100%"
            :clearable="true"
            v-model="selfSelectedOptions"
          ></el-cascader>
        </el-form-item>
        <el-form-item
          label="请选择评分类型："
        >
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
          @click="editSubmit"
        >确定</el-button>
      </span>
    </el-dialog>
  </div>

</template>
<script>
import { treelist } from "@/api/people/people";
import { updateScore } from "@/api/score/score";
import qs from "qs";
export default {
  data() {
    return {
      title: "",
      options: [],
      props: {
        value: "commoncode",
        label: "commonname",
        children: "childDept"
      },
      filterText: "",
      selfDialogVisible: this.uslDialogVisible,
      selfSelectedOptions: [""],
      scorringcode: "",
      byType: "",
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
    this.selfSelectedOptions = [""];
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
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    handleChange(val) {
      this.scorringcode = val[val.length - 1];
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
    //取消
    cancel() {
      this.selfDialogVisible = false;
      if (!this.selfDialogVisible) {
        this.$emit("childClose", false);
      }
    },
    //修改评分人
    editSubmit() {
      if (!this.scorringcode) {
        this.$message.warning("请选择评分人员");
        return;
      }
      if (!this.byType) {
          this.$message.warning("请选择评分人类型");
          return;
        }
      let data = {
        id: this.form.id,
        scorredcode: this.form.scorredcode,
        scorringcode: this.scorringcode,
        scoretype: this.byType,
        dbtype: this.$store.state.user.user.dbtype
      };
      new Promise((response, reject) => {
        updateScore(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.$message.success(response.data.msg);
              this.cancel();
              this.$emit("childGetList");
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
  watch: {
    uslDialogVisible(val, oldVal) {
      this.selfDialogVisible = val;
      this.selfSelectedOptions = [""];
    },
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  }
};
</script>
<style scoped>
</style>
