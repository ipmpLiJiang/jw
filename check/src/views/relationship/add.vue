<template>
    <div>
        <el-row class="step">
          <el-steps
            :active="active"
            finish-status="success"
            align-center
          >
            <el-step title="创建评价关系"></el-step>
            <el-step title="编辑考核"></el-step>
            <el-step title="发布考核"></el-step>
            <!-- <el-step title="评估报告"></el-step> -->
          </el-steps>
        </el-row>
        <el-row class="edit">
          <el-col class="title">创建评价关系</el-col>
          <el-col class="message">默认可同时添加五位被评人，如需添加更多请点击下方“添加被评人”按钮</el-col>
          <el-table
            :data="tableData"
            border
            :header-cell-style="tableHeaderColor"
            style="width: 100%"
          >
            <el-table-column
              label="被评人"
              width="150"
            >
              <template slot-scope="scope">
                <el-input
                        @focus="handleFocus(scope.$index , 1)"
                        placeholder="选择被评人"
                        v-model="scope.row.name"
                        size="mini"
                >
                </el-input>
              </template>
            </el-table-column>
            <el-table-column
              label="A类评分人"
              width="192"
            >
              <template slot-scope="scope">
                <el-input
                        @focus="handleFocus(scope.$index , 2)"
                        placeholder="选择A类评分人"
                        v-model="scope.row.Atype"
                        size="mini"
                >
                </el-input>
              </template>
            </el-table-column>
            <el-table-column
              label="B类评分人"
              width="192"
            >
              <template slot-scope="scope">
                <el-input
                        @focus="handleFocus(scope.$index , 3)"
                        placeholder="选择B类评分人"
                        v-model="scope.row.Btype"
                        size="mini"
                >
                </el-input>
              </template>
            </el-table-column>
            <el-table-column
              label="C类评分人"
              width="192"
            >
              <template slot-scope="scope">
                <el-input
                        @focus="handleFocus(scope.$index , 4)"
                        placeholder="选择C类评分人"
                        v-model="scope.row.Ctype"
                        size="mini"
                >
                </el-input>
              </template>
            </el-table-column>
            <el-table-column label="D类评分人">
              <template slot-scope="scope">
                <el-input
                        @focus="handleFocus(scope.$index , 5)"
                        placeholder="选择D类评分人"
                        v-model="scope.row.Dtype"
                        size="mini"
                >
                </el-input>
              </template>
            </el-table-column>
          </el-table>
        </el-row>
        <el-row class="myself">
          <el-checkbox v-model="checked">被评人是否需要自评</el-checkbox>
        </el-row>
        <el-row class="button">
          <el-button
            type="primary"
            @click="add"
          >添加被评人</el-button>
          <el-button type="warning">保存草稿</el-button>
          <el-button type="primary" @click="next">下一步</el-button>
        </el-row>

      <el-dialog
              title="选择人员"
              :visible.sync="centerDialogVisible"
              width="500px"
              top="10px"
              center>
        <el-input
                placeholder="输入关键字搜索人员"
                v-model="filterText">
        </el-input>
                <el-tree
                        :data="data2"
                        show-checkbox
                        default-expand-all
                        node-key="id"
                        ref="tree"
                        highlight-current
                        :props="defaultProps"
                        :filter-node-method="filterNode">
                </el-tree>
                <span slot="footer" class="dialog-footer">
                  <el-button @click="centerDialogVisible = false">取 消</el-button>
                  <el-button type="primary"  @click="getCheckedNodes">确 定</el-button>
                </span>
      </el-dialog>
      </div>
</template>
<script>

export default {
  data() {
    return {
      centerDialogVisible: false,
      data2: [{
          id: 1,
          label: '门诊办公室',
          children: [{
              id: 2,
              label: '袁莉',
          }]
      }, {
            id: 3,
            label: '教学办公室',
            children: [{
                id: 4,
                label: '杨光辉'
            }, {
                id: 5,
                label: '刘亚玲'
            }]
        }, {
            id: 6,
            label: '院长办公室',
            children: [{
                id: 7,
                label: '胡豫'
            }, {
                id: 8,
                label: '彭义香'
            }]
        }, {
          id: 9,
          label: '党委办公室',
          children: [{
              id: 10,
              label: '汪宏波'
          }]
      }, {
          id: 11,
          label: '感染管理科',
          children: [{
              id: 12,
              label: '邹俊宁'
          }]
      }, {
          id: 13,
          label: '爱卫会',
          children: [{
              id: 14,
              label: '张明'
          }]
      }, {
          id: 15,
          label: '远程医学中心',
          children: [{
              id: 16,
              label: '樊玮'
          }]
      }],
      defaultProps: {
          children: 'children',
          label: 'label'
      },
      active: 0,
      tableData: [
        {
          name: "",
          Atype: "",
          Btype: "",
          Ctype: "",
          Dtype: ""
        },
        {
            name: "",
            Atype: "",
            Btype: "",
            Ctype: "",
            Dtype: ""
        },
        {
            name: "",
            Atype: "",
            Btype: "",
            Ctype: "",
            Dtype: ""
        },
        {
            name: "",
            Atype: "",
            Btype: "",
            Ctype: "",
            Dtype: ""
        },
        {
            name: "",
            Atype: "",
            Btype: "",
            Ctype: "",
            Dtype: ""
        }
      ],
      checked: false,
      tX:"",
      tY:"",
      treeData:'',
        filterText: '',
    };
  },
  components: {
  },
  mounted() {},
  created() {},
    watch: {
        filterText(val) {
            this.$refs.tree.filter(val);
        }
    },
  methods: {
    // 修改table header的背景色
    tableHeaderColor({ row, column, rowIndex, columnIndex }) {
      if (rowIndex === 0) {
        return "background-color: #f5f7fa;font-weight: 600;";
      }
    },
    add() {
      this.tableData.push({
          name: "",
          Atype: "",
          Btype: "",
          Ctype: "",
          Dtype: ""
      });
    },
    next(){
        this.$router.push({
            path: '/detail/relationship/result',
            query: {
            }
        })
    },
    handleFocus(index,count){
        this.tX = index;

        if(count==1){
            this.tY = "name";
        }
        if(count==2){
            this.tY = "Atype";
        }
        if(count==3){
            this.tY = "Btype";
        }
        if(count==4){
            this.tY = "Ctype";
        }
        if(count==5){
            this.tY = "Dtype";
        }
        this.centerDialogVisible = true
    },
    getCheckedNodes(){
        let namedata = '';
        this.$refs.tree.getCheckedNodes().forEach(function (item) {
            if(!item.children){
                namedata+= item.label + " "
            }
        });
        this.tableData[this.tX][this.tY] = namedata;
        this.centerDialogVisible = false;
        this.$refs.tree.setCheckedKeys([]);
        this.filterText = ''
    },
    filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
    }
  }
};
</script>
<style lang="scss" scoped>
.step {
  background: #fff;
  padding: 20px;
  border-radius: 5px;
}
.edit {
  background: #fff;
  padding: 20px;
  margin: 10px 0px;
  border-radius: 5px;
  .title {
    text-align: center;
    font-size: 16px;
    font-weight: bold;
  }
  .message {
    margin: 20px 0px 10px 0px;
    color: #cf9236;
  }
}
.myself {
  background: #fff;
  padding: 20px;
  border-radius: 5px;
  margin-bottom: 10px;
}
.button {
  background: #fff;
  padding: 20px;
  border-radius: 5px;
  text-align: center;
}
  .el-tree{
    margin-top: 10px;
    background: #f8f8f8;
    padding: 10px;
    max-height:400px;
    overflow-y: scroll;
  }
</style>
