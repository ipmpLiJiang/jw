<template>
  <div>
    <el-col
      :span="24"
      class="title"
    >请填写本年度医德医风自评及工作总结</el-col>
    <el-col>
      <Ckeditor ref="ckditor" :type="type" :fatherContent="content"></Ckeditor>
    </el-col>

    <!-- <el-col :span="24" class="title"
          >个人年度工作总结</el-col
        > -->
    <!-- <el-col>
         <p v-html="" class="txet"></p>
        </el-col> -->

  </div>
</template>

<script>
import { updateUserPassword } from "@/api/user/user";
import Ckeditor from "../common/ckeditor";
import qs from "qs";
import {
  baseInfo
} from "@/views/ydyf/api/Form/index";
export default {
  name: "index",

  data() {
    return {
      dialogVisible: false,
      content:""
    };
  },
  components: {
    Ckeditor,
  },
  mounted() {},
  created() {
    this.getForm();
  },
  methods: {
    childClose(val) {
      this.dialogVisible = val;
      this.messageDialogVisible = val;
    },
    nextSubmit() {
       if (!this.$refs.ckditor.content) {
        this.$message.warning("内容不能为空");
        return;
      }
      if(this.$refs.ckditor.TiLength>1200){
           this.$message.warning("注意,不能超过1200字哦~");
            return;
      }

      return this.$refs.ckditor.content;
    },

    SubmitFrom() {},
    getForm() {
      let data = {
        userId:this.$store.state.user.user.moneycard
      }
      new Promise((response, reject) => {
        baseInfo(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.content = response.data.data.selfSummary;
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
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
  },
};
</script>


<style lang="scss" scoped>
.form-container {
  width: 1000px;
  margin: 0 auto;
  .step {
    float: right;
    // margin-top: 20px;
    // margin-bottom: 50px;
    margin: 20px 30px 50px 0;
  }
}
.title {
  height: 50px;
  line-height: 50px;
  border-bottom: 1px solid #eaeaea;
  padding: 0px 10px;
  font-weight: 600;
  color: #424242;
  background: #fff;
  margin-bottom: 20px;
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
  padding: 20px;
  margin: 20px;
  border-radius: 4px;
  .el-form-item {
    margin: 0px;
  }
  .el-button {
    margin-left: 10px;
  }
}
.content {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  margin: 20px;
  border-radius: 4px;
  .el-pagination {
    margin: 20px auto;
    text-align: center;
  }
  .table-expand {
    padding: 0px;
    .el-form-item {
      margin-right: 0;
      margin-bottom: 10px;
      width: 33.33%;
    }
    .el-select {
      width: 185px;
    }
  }
}
.w200 {
  width: 260px;
}
.accessory {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  margin: 20px;
  border-radius: 4px;
  .el-pagination {
    margin: 20px auto;
    text-align: center;
  }
  .table-expand {
    padding: 0px;
    .el-form-item {
      margin-right: 0;
      margin-bottom: 10px;
      width: 33.33%;
    }
    .el-select {
      width: 185px;
    }
  }
}
.active0 {
  width: 100%;
  height: 100%;
  margin-top: 50px;

  .title {
    text-align: center;
    font-size: 20px;
  }
}
.active1 {
  width: 100%;
  height: 100%;
  margin-top: 50px;
  .title {
    text-align: center;
    font-size: 20px;
  }
}
.active2 {
  width: 100%;
  height: 100%;
  margin-top: 50px;
  .title {
    text-align: center;
    font-size: 20px;
  }
}

.indicator {
  display: flex;
  flex-wrap: wrap;
  background: #fff;
  margin: 10px;
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  .li-title {
    width: 100%;
    margin-bottom: 20px;
    padding: 0 20px;
    .label {
      font-weight: 500;
      margin-bottom: 10px;
    }
    .core {
      width: 33%;
      display: inline-block;
    }
    .core-input {
      width: 180px;
      margin-top: 10px;
    }
    .title {
      line-height: 10px;
      background-color: #409eff;
      color: #fff;
      font-size: 16px;
      font-weight: 500;
      width: 100%;
    }
  }
  .title {
    font-size: 16px;
    font-weight: 500;
    padding-top: 20px;
  }
  li {
    width: 100%;
    margin-bottom: 20px;
    padding: 0 20px;
    .label {
      font-weight: 500;
      margin-bottom: 10px;
      margin-top: 20px;
    }
    .core {
      width: 20%;
      display: inline-block;
    }
    .core-input {
      width: 150px;
    }
  }
}
</style>
