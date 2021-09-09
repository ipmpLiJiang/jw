<template>

  <div>
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
  </div>

</template>
<script>
import { postList } from "@/api/post/indicator";
export default {
  data() {
    return {
      options: [],
      props: {
        value: "departmentcode",
        label: "departmentname",
        children: "childDept",
        checkStrictly: true
      },
      selfSelectedOptions: [""]
    };
  },
  props: ["selectedOptions"],
  created() {
    this.getStationList();
    this.selfSelectedOptions = this.selectedOptions;
  },
  methods: {
    //获取岗位列表
    getStationList() {
      new Promise((response, reject) => {
        postList()
          .then(response => {
            if (response.data.code == 0) {
              this.options = this.recursionFunction(response.data.data);
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
      this.$emit("childSelectDepartment", val[val.length - 1], val);
    },
    //递归处理后端数据形成tree
    recursionFunction(list) {
      let str = [];
      list.forEach(row => {
        if (row.stations) {
          if (row.stations.length > 0) {
            if(row.childDept){
              row.childDept = [...row.childDept, ...row.stations];
            }else{
              row.childDept = [...row.stations];
            }
            
          }
        }
        if (row.childDept) {
          this.recursionFunction(row.childDept);
        } else {
          // str.push(row);
        }
      });
      return list;
    }
  },
  watch: {
    selectedOptions(val, oldVal) {
      this.selfSelectedOptions = val;
    },
    selfSelectedOptions(val,oldVal){
      if(!val){
        this.$emit("childSelectDepartment", "","");
      }
    }
  }
};
</script>
<style scoped>
</style>
