<template>
  <div>
    <vue-editor
      v-model="content"
      :editorToolbar="customToolbar"
      :disabled="type==2 ? true : false"
    ></vue-editor>
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
  },
  watch: {
    fatherContent(val, oldVal) {
      this.content = val;
    },
    content(val, oldVal) {
      this.$emit("getContent", val);
    },
    
  },
};
</script>
<style lang='scss'>
.ck-editor__editable {
  min-height: 200px;
}
</style>