<template>
 
  <div>
  <el-cascader
    :options="options"
    :show-all-levels="false"
    :props="props"
    @change="handleChange"
    filterable
    change-on-select
    style="width:100%"
    :clearable="true"
    v-model="selfSelectedOptions"
  ></el-cascader>   
  </div>

</template>
<script>
import { treelist } from "@/api/post/department";
export default {
  data() {
    return {
      options: [],
      props: {
        value: "departmentcode",
        label: "departmentname",
        children: "childDept"
      },
      selfSelectedOptions:[""],
    };
  },
  props:['selectedOptions'],
  created() {
    this.getDepartmentList();
    this.selfSelectedOptions = this.selectedOptions;
  },
  methods: {
    //获取部门列表
    getDepartmentList() {
      new Promise((response, reject) => {
        treelist()
          .then(response => {
            if (response.data.code == 0) {
              this.options = response.data.data;
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
      this.$emit("childSelectDepartment", val[val.length - 1],val);
    }
  },
  watch:{
    selectedOptions(val, oldVal){
      this.selfSelectedOptions = val;
    }
  }
};
</script>
<style scoped>
</style>
