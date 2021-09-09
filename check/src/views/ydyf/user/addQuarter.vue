<template>
  <div>

    <!-- <el-dialog
      title="月结管理"
      :visible.sync="dialogVisible"
      :before-close="cancel"
      width="50%"
      :close-on-click-modal="closeModal"
    > -->
      <el-form label-width="80px">
       
<!--        
        <el-form-item
          label="标题"
          :rules="[
              { required: true},
            ]"
        >
          <el-input
            v-model="form.title"
            :disabled="type==2 ? true : false"
          ></el-input>
        </el-form-item> -->
        <el-form-item
          label="内容"
          :rules="[
              { required: true},
            ]"
        >
          <!-- <el-input
            type="textarea"
            v-model="form.content"
            :rows="4"
            :disabled="type==2 ? true : false"
          ></el-input> -->
          <Ckeditor ref="ckditor" :type="type" :fatherContent="form.content"></Ckeditor>
          
        </el-form-item>
        <!-- <el-form-item
          label="附件"
          v-if="type != 2"
        >
          <el-upload
            class="upload-demo"
            :action="actionPath"
            :on-remove="handleRemove"
            :on-exceed="handleExceed"
            :on-success="handleSuccess"
            multiple
            :limit="1"
            :file-list="fileList"
            :data="data"
            :on-preview="handlePreview"
          >
            <el-button
              size="small"
              type="primary"
            >点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item
          label="总结文件"
          v-else
        >
          <a :href="form.savepath">{{form.filename}}</a>
        </el-form-item> -->
      </el-form>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <!-- <el-button @click="cancel">取 消</el-button>
        <el-button
          type="primary"
          @click="addSubmit"
          v-if="type!=2"
        >提交</el-button> -->
      </span>
     <!-- </el-dialog> -->
  </div>
</template>
<script>
import { addQuarter, updateQuarter } from "@/api/user/quarter";
import { download } from "@/api/common/common";
import Ckeditor from "../common/ckeditor";
import qs from "qs";
export default {
  data() {
    return {
      form: {},
      selfDialogVisible: this.dialogVisible,
      fileList: [],
      actionPath:
        process.env.VUE_APP_ITEM_NAME +
        "summaryattachment/upload",
      data: {
        serialno: ""
      },
      closeModal: false,
      content: "",
    };
  },
  props: {
    isAdd: {
      required: true
    },
    dialogVisible: {
      required: true
    },
    type: {
      required: true
    },
    parentForms: {
      required: true
    }
  },
  components: {
    Ckeditor,
  },
  created() {
    this.form = Object.assign({}, this.parentForms);
    this.data.serialno = this.form.serialno;
    if (this.form.filename && this.form.savepath) {
      this.fileList = [{ name: this.form.filename, url: this.form.savepath }];
    } else {
      this.fileList = [];
    }
  },
  methods: {
    //回显填写的值
    setContent(){
      this.$refs.ckditor.content = this.content;
    },
    //添加/修改岗位
    addSubmit() {
    
      if (!this.$refs.ckditor.content || this.$refs.ckditor.content == undefined) {
        this.$message.warning("内容不能为空");
        this.form.content=''; 
        return false ;
      }
      if(this.$refs.ckditor.TiLength>1200){
           this.$message.warning("注意,不能超过1200字哦~");
            return false;
                
      } 
       this.form.content = this.$refs.ckditor.content;
        
     
    },
    //取消
    cancel() {
      this.selfDialogVisible = false;
      if (!this.selfDialogVisible) {
        this.$emit("childClose", false);
      }
    },
    //调用父亲查询列表方法
    getList() {
      this.$emit("childGetList", false);
    },
  },
  watch: {
    parentForms(val, oldVal) {
      this.form = Object.assign({}, val);
      this.data.serialno = this.form.serialno;
      if (this.form.filename && this.form.savepath) {
        this.fileList = [{ name: this.form.filename, url: this.form.savepath }];
      } else {
        this.fileList = [];
      }
    }
  }
};
</script>
