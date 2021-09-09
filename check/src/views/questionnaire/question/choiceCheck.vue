<template>
  <el-col
    style="padding: 10px;"
    @mouseenter.native="selectStyle"
    @mouseleave.native="selectStyleLeave"
    :class="operationShow ? 'activeHover' : ''"
  >
    <el-col
      class="content"
      style="margin-bottom:0;position:relative;"
    >
      <el-col :span="24">
        <div><strong style="margin-right: 10px;">{{order + 1}}.</strong>
          <div
            class="jz-title"
            v-html="content"
          ></div>
          <div
            class="remark"
            v-if="isRemark"
          >[{{remarkText}}]</div>
        </div>
        <el-col class="people">
          <el-col
            v-for="(item,index) in optionTextArr"
            :key="index"
          >
            <el-checkbox
              :label="item.name"
              disabled
              style="margin-right:0;"
            ></el-checkbox>
            <el-input
              v-if="item.gap"
              class="choice-input"
            ></el-input>
          </el-col>
        </el-col>
      </el-col>
      <el-col
        v-if="operationShow"
        class="hover"
      >
        <el-button
          plain
          size="mini"
          @click="editTopic"
        >编辑</el-button>
        <el-button
          plain
          size="mini"
          @click="delTopic"
        >删除</el-button>
        <el-button
          plain
          size="mini"
          @click="upTopic"
        >上移</el-button>
        <el-button
          plain
          size="mini"
          @click="downTopic"
        >下移</el-button>
        <el-button
          plain
          size="mini"
          @click="topmostTopic"
        >最上</el-button>
        <el-button
          plain
          size="mini"
          @click="bottomTopic"
        >最下</el-button>
      </el-col>
    </el-col>
    <el-col
      class="edit-content"
      v-if="editShow"
    >
      <Ckeditor
        ref="ckditor"
        :type="1"
        :fatherContent="content"
        @getContent="getContent"
      ></Ckeditor>
      <el-col class="must">
        <el-checkbox v-model="checked">此题是否必答</el-checkbox>
      </el-col>
      <el-col class="must">
        <span>分组设置：</span>
        <el-select
          v-model="maxCount"
          placeholder="最多选几项"
          @change="selectMaxCount"
        >
          <el-option
            v-for="item in maxOptions"
            :key="item.value"
            :label="item.label"
            :value="{value:item.value,label:item.label}"
          >
          </el-option>
        </el-select>
      </el-col>
      <div class="">
        <el-col class="input-title">
          <el-col :span="5">选项文字</el-col>
          <el-col
            :span="5"
            :offset="1"
          >操作</el-col>
          <el-col
            :span="5"
            :offset="1"
          >是否填空</el-col>
        </el-col>
        <el-col
          class="input"
          v-for="(item,index) in optionTextArr"
          :key="index"
        >
          <el-col :span="5">
            <el-input
              v-model="item.name"
              placeholder="请输入内容"
              size="small"
            ></el-input>
          </el-col>
          <el-col
            :span="5"
            :offset="1"
          >
            <el-button
              type="text"
              @click="add(index)"
            >添加</el-button>
            <el-button
              type="text"
              @click="moveup(index)"
            >上移</el-button>
            <el-button
              type="text"
              @click="movedown(index)"
            >下移</el-button>
            <el-button
              type="text"
              @click="del(index)"
            >删除</el-button>
          </el-col>
          <el-col
            :span="5"
            :offset="1"
          >
            <el-checkbox v-model="item.gap"></el-checkbox>
          </el-col>
        </el-col>
      </div>
      <el-col style="text-align: center">
        <el-button
          type="primary"
          @click="finishEdit"
        >完成本题编辑</el-button>
      </el-col>
    </el-col>
  </el-col>
</template>

<script>
import Ckeditor from "../../common/ckeditor";
export default {
  data() {
    return {
      checked: true,
      operationShow: false,
      content: "请输入标题",
      //矩阵单选变量
      optionTextArr: [
        {
          name: "很不满意",
          gap: false,
          gaptext: "",
        },
        {
          name: "不满意",
          gap: false,
          gaptext: "",
        },
        {
          name: "一般",
          gap: false,
          gaptext: "",
        },
        {
          name: "满意",
          gap: false,
          gaptext: "",
        },
        {
          name: "很满意",
          gap: false,
          gaptext: "",
        },
      ],
      editShow: true,
      isRemark: false,
      maxCount: {
        value: -1,
        label: "最多选几项",
      },
      maxOptions: [
        {
          value: -1,
          label: "最多选几项",
        },
        {
          value: 2,
          label: "最多选2项",
        },
        {
          value: 3,
          label: "最多选3项",
        },
        {
          value: 4,
          label: "最多选4项",
        },
        {
          value: 5,
          label: "最多选5项",
        },
      ],
      remarkText: "",
    };
  },
  props: {
    order: {
      required: true,
    },
    object: {
      required: true,
    },
  },
  components: {
    Ckeditor, //使用编辑器
  },
  mounted() {},
  created() {
    this.into();
    this.editShow = this.object.editShow;
  },
  methods: {
    into() {
      this.content = this.object.title;
      if (this.object.optionInfoList) {
        this.optionTextArr = Array.isArray(this.object.optionInfoList)
          ? this.object.optionInfoList
          : JSON.parse(this.object.optionInfoList);
      } else {
        this.optionTextArr = Array.isArray(this.object.nav)
          ? this.object.nav
          : JSON.parse(this.object.nav);
      }
      if (this.object.iswrite == 1) {
        this.checked = true;
      } else {
        this.checked = false;
      }
      this.maxCount = {
        value: this.object.multiple,
        label: this.object.multipletext,
      };
      if(this.maxCount.value != -1){
        this.isRemark = true;
        this.remarkText = this.maxCount.label;
      }else{
        this.isRemark = false;
        this.remarkText = "";
      }
    },
    getContent(val) {
      this.content = val;
      this.$emit("getTitle", val, this.order);
    },
    //添加题目
    add(index) {
      this.optionTextArr.splice(index + 1, 0, { name: "" });
      this.resetMaxCount();
    },
    //删除题目
    del(index) {
      this.optionTextArr.splice(index, 1);
      this.resetMaxCount();
      this.maxCount = {
        value: -1,
        label: "最多选几项",
      };
    },
    //上移动
    moveup(index) {
      if (index == 0) {
        return;
      }
      this.common.swapItems(this.optionTextArr, index, index - 1);
    },
    //下移动
    movedown(index) {
      if (index == this.optionTextArr.length - 1) {
        return;
      }
      this.common.swapItems(this.optionTextArr, index, index + 1);
    },
    //完成编辑
    finishEdit() {
      this.editShow = false;
      this.$emit(
        "childFinishEdit",
        this.order,
        this.optionTextArr,
        this.checked
      );
    },
    //hover 样式
    selectStyle() {
      if (this.editShow) {
        return;
      }
      this.operationShow = true;
    },
    //hover 样式
    selectStyleLeave() {
      this.operationShow = false;
    },
    //编辑题目
    editTopic() {
      this.operationShow = false;
      this.editShow = true;
    },
    //删除题目
    delTopic() {
      this.$emit("delTopic", this.order);
    },
    //上移
    upTopic() {
      this.$emit("upTopic", this.order);
    },
    //下移
    downTopic() {
      this.$emit("downTopic", this.order);
    },
    //最上
    topmostTopic() {
      this.$emit("topmostTopic", this.order);
    },
    //最下
    bottomTopic() {
      this.$emit("bottomTopic", this.order);
    },
    //选择最多选项
    selectMaxCount(params) {
      const { value, label } = params;
      if (value != -1) {
        this.isRemark = true;
        this.remarkText = label;
      } else {
        this.isRemark = false;
        this.remarkText = "";
      }
      this.$emit("multipleControl", this.order, params);
    },
    //重置最多选项
    resetMaxCount() {
      this.maxOptions = [
        {
          value: -1,
          label: "最多选几项",
        },
      ];
      for (let i = 2; i <= this.optionTextArr.length; i++) {
        this.maxOptions.push({
          value: i,
          label: "最多选" + i + "项",
        });
      }
    },
  },
  watch: {
    content(val) {
      if (val == "") {
        this.content = "标题";
      } else {
        this.content = val;
      }
    },
    object(val) {
      this.into();
    },
    editShow(val) {
      this.$emit("funcEditShow", val, this.order);
    },
  },
};
</script>

<style lang="scss" scoped>
.container {
  width: 1200px;
  margin: 0 auto;
  .design-nav {
    padding: 20px 20px 40px;
    width: 230px;
    background: #fff;
    box-shadow: 0px 0px 4px 0px #dfdfdf;
    overflow: auto;
    position: fixed;
  }
  .nav-ul {
    li {
      margin-bottom: 5px;
    }
    .title {
      font-weight: bold;
      margin-bottom: 15px;
    }
    .dn-content {
      width: 50%;
      float: left;
      margin-bottom: 15px;
      cursor: pointer;
      &:hover {
        color: #409EFF;
        span {
          color: #409EFF;
        }
      }
      span {
        color: #666;
        vertical-align: middle;
      }
      .icon {
        margin-right: 5px;
        vertical-align: middle;
      }
    }
  }
}
.step {
  background: #fff;
  padding: 20px;
  border-radius: 5px;
}
.edit {
  margin-top: 10px;
  border-radius: 2px;
  border: 1px solid rgba(217, 217, 217, 1);
  width: 962px;
  padding-bottom: 5px;
  margin: 0;
  margin-top: 8px;
  overflow: auto;
  background: #ffffff;
  box-shadow: 0px 2px 5px 0px #d9d9d9;
  position: relative;
  margin-left: 238px;
  .title {
    text-align: center;
    font-size: 22px;
    font-weight: bold;
    padding: 20px;
  }
  .left {
    text-align: center;
    background: #fff;
    padding: 20px 20px 0px 20px;
    border-radius: 5px;
    margin-top: 10px;
    position: fixed;
    width: 160px;
    right: 10px;
    top: 100px;
    .el-button {
      margin-bottom: 20px;
    }
    .el-button + .el-button {
      margin-left: 0px;
    }
  }
  .right {
    background: #fff;
    padding: 20px;
    border-radius: 5px;
    .list {
      position: relative;
      border-bottom: 1px solid #e7e7e7;
      padding: 10px;
      .content {
        position: relative;
        .text-message {
          margin-top: 10px;
          margin-bottom: 10px;
          .el-col {
            text-align: center;
            color: #e6a23c;
          }
          .name {
            text-align: center;
          }
        }
        .people {
          padding: 10px 0px;
          .el-col {
            margin: 8px 0;
          }
          .name {
            text-align: center;
          }
          .el-radio {
            margin-left: 15px;
            margin-right: 0;
          }
        }
      }
      .edit-content {
        background: #f8f8f8;
        border: 1px solid #e7e7e7;
        padding: 20px;
        margin-top: 10px;
        .editer {
          background: #fff;
        }
        .must {
          margin: 10px 0px;
        }
        .input-title {
          background: #f3f3f3;
          padding: 10px 10px;
          margin-bottom: 10px;
        }
        .input {
          .el-button {
            position: relative;
            top: -5px;
          }
        }
      }
      .hover {
        position: absolute;
        z-index: 4;
        left: 0px;
        bottom: 6px;
        text-align: right;
        padding-right: 10px;
      }
    }
  }
}
.submit {
  text-align: center;
  padding-top: 20px;
}
.jz-title {
  display: inline-block;
  vertical-align: top;
  font-weight: bold;
}
.activeHover {
  background: #f3f3f3;
}
.choice-input {
  width: 150px;
  margin-left: 10px;
  input {
    border: 0;
    border-bottom: 1px solid #ccc;
    border-radius: 0;
    line-height: 1;
    height: auto;
  }
}
.remark {
  display: inline-block;
  color: #999;
  margin-left: 6px;
}
</style>

