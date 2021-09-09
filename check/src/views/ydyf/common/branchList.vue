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
import { treelist } from "@/api/post/branch";
export default {
  data() {
    return {
      options: [],
      props: {
        value: "branchcode",
        label: "branchname",
        children: "childBranch"
      },
      selfSelectedOptions:[""],
    };
  },
  props:['selectedOptions'],
  created() {
    this.getBranchList();
    this.selfSelectedOptions = this.selectedOptions;
  },
  methods: {
    //获取部门列表
    getBranchList() {
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
      this.$emit("childSelectBranch", val[val.length - 1],val);
    }
  },
  watch:{
    selectedOptions(val, oldVal){
      this.selfSelectedOptions = val;
    },
    selfSelectedOptions(val,oldVal){
      if(!val){
        this.$emit("childSelectBranch", "","");
      }
    }
  }
};
</script>
<style scoped>
</style>
