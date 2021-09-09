<template>
  <div>
    <h4 class="title"><router-link to="/home"><span>首页</span></router-link><i class="el-icon-arrow-right"></i>支部管理</h4>
    <el-row style="padding:20px;">
      <el-col :span="7"  class="left">
        <el-col :span="24">
          <el-input
                  clearable
                  placeholder="搜索支部"
                  v-model="filterText">
          </el-input>
        </el-col>
        <el-col :span="24">
          <el-tree
                  class="filter-tree"
                  :data="data5"
                  default-expand-all
                  :filter-node-method="filterNode"
                  @node-click="handleNodeClick"
                  ref="tree2">
          </el-tree>
        </el-col>
      </el-col>
      <el-col :span="16" class="right">
        <el-form ref="form" :model="form" label-width="80px">
          <el-form-item label="支部名称">
            <el-input v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="支部描述">
            <el-input type="textarea" :rows="5" v-model="form.desc"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button  disabled>保存修改</el-button>
            <el-button type="warning">添加子支部</el-button>
            <el-button type="danger">删除支部</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

  </div>
</template>

<script>
    let id = 1000;
export default {
  data() {
    return {
        filterText: '',
        form:{
            name: '',
            desc: ''
        },
        data5: [{
            id: 2,
            label: '机关一支部'
        },{
            id: 3,
            label: '机关二支部'
        },{
            id: 4,
            label: '机关三支部'
        },{
            id: 5,
            label: '机关四支部'
        },{
            id: 6,
            label: '机关五支部'
        },{
            id: 7,
            label: '后勤一支部'
        },{
            id: 8,
            label: '后勤二支部'
        },{
            id: 9,
            label: '后勤三支部'
        },{
            id: 10,
            label: '门诊党支部'
        },{
            id: 11,
            label: '综合党支部'
        }]
    };
  },
  components: {
  },
  mounted() {
  },
  created() {
  },
  watch: {
      filterText(val) {
          this.$refs.tree2.filter(val);
      }
  },
    methods: {
      filterNode(value, data) {
          if (!value) return true;
          return data.label.indexOf(value) !== -1;
      },
      handleNodeClick(data) {
          this.form.name = data.label
          this.form.desc = '这里是支部描述 ，可修改，修改之后点击保存按钮提交修改'
      }
  }
};
</script>


<style lang="scss" scoped>
  .title{
    height: 50px;
    line-height: 50px;
    border-bottom: 1px solid #eaeaea;
    padding: 0px 10px;
    font-weight: 600;
    color: #424242;
    background: #fff;
    span{
      font-weight: normal;
      color:#9b9b9b;
    }
    i{
      margin: 0px 4px;
      color:#9b9b9b;
    }
  }
  .left{
    box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
    background: #fff;
    padding: 20px;
    margin-right: 20px;
    border-radius: 5px;
    .el-input{
      margin-bottom: 20px;
    }
    .filter-tree{
      height:430px;
      overflow-y: scroll;
    }
  }
  .right{
    box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
    background: #fff;
    padding: 20px;
    border-radius: 5px;
    height:530px;
  }

</style>
