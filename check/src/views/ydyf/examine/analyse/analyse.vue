<template>
  <div>
    <h4 class="title">考核统计分析</h4>

    <el-row class="search">
      <el-col>
        <el-form label-width="100px" show-overflow-tooltip="true">
          <el-col :span="6">
               <el-form-item label="党支部" label-width="100px">
         
                <el-cascader
                v-model="branchId"
                :options="secretaryOptions"
                placeholder="请选择党支部"
                @change="handleChange"
                :props="{ checkStrictly: true }"
                style="width: 100%"
                clearable
              ></el-cascader>
              </el-form-item>
          </el-col>

          <el-col :span="24" class="edit-btn">
            <el-form-item>
              <el-button
                style="margin-left: -90px"
                type="primary"
                @click="searchList"
                ><i class="el-icon-search"></i>搜索
              </el-button>
            </el-form-item>
          </el-col>
        </el-form>
      </el-col>
    </el-row>

  
     
      <el-row class="content">
          <charts :branchid='branchId' ref="branchId" ></charts>
      </el-row>
  </div>
</template>

<script>
import qs from "qs";
import charts from "./charts";

import {
} from "../../api/addData/addData";
import {  tree } from "../../api/department/department";

export default {
  props: {},
  data() {
    return {
      // search: {
      
      // },
       title:'父组件',
      branchId:'',
      secretaryOptions: [], //党支部信息
  
      data: ["大专及以下", "本科", "硕士研究生", "博士研究生"],
      options: [
        {
          value: "",
          label: "全部类型",
        },
        {
          value: "0",
          label: "未提交",
        },
        {
          value: "1",
          label: "已提交",
        },
      ],

     
    };
  },
  computed: {},
  created() {
    this.tree()
  },
  mounted() {
  },
  watch: {},
  methods: {
    
    searchList() {
     
       this.$refs.branchId.educationLevel()
       this.$refs.branchId.scoreLevel()
       this.$refs.branchId.politicalStatus()
    },

    academic() {},
      //党支部书记列表
    tree() {
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

     //选择党支部
    handleChange(val) {
     
       if (val.length > 0) {
        this.branchId = val[val.length - 1];
      }
      
      console.log(this.branchId)
    },

    //初始化
    into() {},
    personnel(){

    },
  },
  components: {
    charts,
  },
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
</style>
<style>
.score-dialog {
  max-height: 500px;
  overflow: auto;
}
</style>