<template>
  <div>
    <el-select
      v-model="value"
      placeholder="请选择"
      style="width:100%"
      clearable
      filterable
    >
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      >
      </el-option>
    </el-select>
  </div>
</template>

<script>
import { getSectionList } from "@/api/personnel/index";
export default {
  data() {
    return {
      options: [],
      value: ""
    };
  },
  props:['dept'],
  created() {
    this.getList();
    this.value = this.dept;
  },
  methods: {
    //查询列表
    getList() {
      new Promise((response, reject) => {
        getSectionList()
          .then(response => {
            if (response.data.code == 0) {
              this.options = [];
              response.data.data.forEach(row => {
                this.options.push({
                  value: row.department,
                  label: row.department
                });
              });
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
  watch:{
    dept(){
      this.value = this.dept;
    }
  }
};
</script>


<style lang="scss" scoped>
</style>

