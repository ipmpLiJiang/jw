<template>
  <div>
    <h4 class="title"><router-link to="/home"><span>首页</span></router-link><i class="el-icon-arrow-right"></i>人员信息录入</h4>
    <el-row  class="search">
      <el-col>
        <el-form   label-width="100px" show-overflow-tooltip="true">
          <el-col :span="5">
            <el-form-item label="所在部门">
              <el-select v-model="value8" filterable placeholder="请选择">
                <el-option
                        v-for="item in options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="员工姓名">
              <el-input
                      placeholder="请输入内容"
                      v-model="input10"
                      clearable>
              </el-input>
            </el-form-item>
          </el-col>
          <el-button type="primary">搜索</el-button>
          <el-button type="primary">新增员工</el-button>
        </el-form>
      </el-col>
    </el-row>
    <el-row  class="content">
      <el-table
              size="small"
              :data="tableData"
              border
              style="width: 100%">
        <el-table-column
                label="员工姓名"
                prop="id"
                width="100">
        </el-table-column>
        <el-table-column
                label="性别"
                prop="sex"
                width="100">
        </el-table-column>
        <el-table-column
                label="发薪号"
                prop="name"
                width="100">
        </el-table-column>
        <el-table-column
                label="手机号码"
                prop="desc"
                width="150">
        </el-table-column>
        <el-table-column
                label="用户状态"
                prop="type"
                width="100">
        </el-table-column>
        <el-table-column
                label="所属部门"
                prop="bumen"
                width="150">
        </el-table-column>
        <el-table-column
                label="所属岗位"
                prop="gangwei">
        </el-table-column>
        <el-table-column type="expand" label="详情 / 修改" width="100">
          <template slot-scope="props">
            <el-form label-position="left" inline  label-width="80px" size='mini' class="table-expand">
              <el-form-item label="员工姓名">
                <el-input v-model="props.row.id" placeholder="请输入姓名"></el-input>
              </el-form-item>
              <el-form-item label="发薪号">
                <el-input v-model="props.row.name" placeholder="请输入姓名"></el-input>
              </el-form-item>
              <el-form-item label="性别">
                <el-select v-model="props.row.sex" placeholder="请选择">
                  <el-option
                          v-for="item in options"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="邮箱">
                <el-input v-model="props.row.email" placeholder="请输入邮箱"></el-input>
              </el-form-item>
              <el-form-item label="手机号码">
                <el-input v-model="props.row.desc" placeholder="请输入手机号码"></el-input>
              </el-form-item>
              <el-form-item label="民族">
                <el-select v-model="props.row.minzu" placeholder="请选择民族">
                  <el-option
                          v-for="item in options2"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="群众关系">
                <el-select v-model="props.row.guanxi" placeholder="请选择群众关系">
                  <el-option
                          v-for="item in options3"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="学历">
                <el-select v-model="props.row.xueli" placeholder="请选择学历">
                  <el-option
                          v-for="item in options4"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="所属岗位">
                <el-select v-model="props.row.gangwei" placeholder="请选择所属岗位">
                  <el-option
                          v-for="item in options5"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="所属部门">
                <el-select v-model="props.row.bumen" placeholder="请选择所属部门">
                  <el-option
                          v-for="item in options6"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="所属支部">
                <el-select v-model="props.row.zhibu" placeholder="请选择所属支部">
                  <el-option
                          v-for="item in options7"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-form>
            <el-col style="text-align: center;margin-top: 10px;"><el-button type="primary" size="small">保存修改</el-button></el-col>
          </template>
        </el-table-column>
        <el-table-column
                fixed="right"
                label="操作"
                width="100">
          <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)"  type="text" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="currentPage4"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="10"
              layout="total, sizes, prev, pager, next, jumper"
              :total="400">
      </el-pagination>
    </el-row>
  </div>
</template>

<script>
    let id = 1000;
    export default {
        data() {
            return {
                options: [{
                    value: '选项1',
                    label: '1'
                }, {
                    value: '选项2',
                    label: '2'
                }, {
                    value: '选项3',
                    label: '3'
                }, {
                    value: '选项4',
                    label: '4'
                }, {
                    value: '选项5',
                    label: '5'
                }],
                value8: '',
                input10: '',
                tableData: [{
                    id: '李二三',
                    sex:'2',
                    name: '562354',
                    desc: '18265452152',
                    type:'启用',
                    bumen:'1',
                    gangwei:'1',
                    email:'123456@qq.com',
                    minzu:'1',
                    guanxi:'1',
                    xueli:'1',
                    zhibu:'1'
                }],
                options: [{
                    value: '1',
                    label: '男'
                }, {
                    value: '2',
                    label: '女'
                }],
                options2: [{
                    value: '1',
                    label: '汉族'
                }, {
                    value: '2',
                    label: '回族'
                }],
                options3: [{
                    value: '1',
                    label: '党员'
                }, {
                    value: '2',
                    label: '其他'
                }],
                options4: [{
                    value: '1',
                    label: '博士'
                }, {
                    value: '2',
                    label: '本科'
                }],
                options5: [{
                    value: '1',
                    label: '第一临床学院党总支书记'
                }, {
                    value: '2',
                    label: '其他'
                }],
                options6: [{
                    value: '1',
                    label: '教学办公室'
                }, {
                    value: '2',
                    label: '其他'
                }],
                options7: [{
                    value: '1',
                    label: '综合党支部'
                }, {
                    value: '2',
                    label: '其他'
                }],
                currentPage4: 4
            };
        },
        components: {
        },
        mounted() {
        },
        created() {
        },
        methods: {
            handleClick(row) {
            },
            handleSizeChange(val) {
            },
            handleCurrentChange(val) {
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
  .search{
    box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
    background: #fff;
    padding: 20px;
    margin: 20px;
    border-radius:4px;
    .el-form-item{
      margin: 0px;
    }
    .el-button{
      margin-left: 10px;
    }
  }
  .content{
    box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
    background: #fff;
    padding: 20px;
    margin:0px 20px 20px 20px;
    border-radius:4px;
    .el-pagination{
      margin: 20px auto;
      text-align: center;
    }
    .table-expand{
      padding:0px;
      .el-form-item {
        margin-right: 0;
        margin-bottom: 10px;
        width: 33.33%;
      }
      .el-select{
        width:185px;
      }
    }
  }
</style>
