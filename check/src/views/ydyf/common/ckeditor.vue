<template>
  <div>
    <vue-editor
      v-model="content"
      :editorToolbar="customToolbar"
      :disabled="type==2 ? true : false"
      @change="onEditorChange($event)"
    ></vue-editor>
    <span class="SizeTishi">{{TiLength}}/1200</span>
  </div>
</template>
<script>
import { VueEditor } from "vue2-editor";
export default {
  data() {
    return {
      content: "",
      customToolbar: [
        ["bold", "italic", "underline"],
        [{ align: "" }, { align: "center" }, { align: "right" }],
        [{ list: "ordered" }, { list: "bullet" }, { list: "check" }],
        [{ background: [] }, { color: [] }],
        ["clean"],
      ],
      TiLength: 0,
    };
  },
  props: {
    type: {
      required: true,
    },
    fatherContent: {
      required: true,
    },
  },
  components: {
    VueEditor,
  },
  created() {
    this.content = this.fatherContent;
  },
  methods: {
    onEditorChange(event) {
      // event.quill.deleteText(2000,1);
      // if(this.addForm.useExplain===0){
      //   this.TiLength = 0
      // }
      // else{
      //   this.TiLength = event.quill.getLength()-1
      // }
    },
  },
  watch: {
    fatherContent(val, oldVal) {
      this.content = val;
    },
    content(val, oldVal) {
      this.$emit("getContent", val);
      var textContent = val
        .replace(/<\/?.+?>/g, "")
        .replace(/<br\/>/g, "\n")
        .replace(/<br\/>/g, "\n")
        .replace(/\s/g, "\n")
        .replace(/[ ]|[&nbsp;]/g, "")
        .replace(/ /g, "");
      // var textContent = texts.replace(/ /g, ""); //dds为得到后的内容
      this.$emit("textCon",textContent)
      this.TiLength = textContent.length;
      if (this.TiLength > 1200) {
        this.$message.warning("最大只可以输入一千二百字哦！");
       
        return;
      }
    },
  },
};
</script>
<style lang='scss'>
.ck-editor__editable {
  min-height: 200px;
}
</style>