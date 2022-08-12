<template>
<div>
  <h1>success</h1>
  <button @click="select">查询</button>
  <button v-hasPermi="['system:user:select']">权限</button>
  <button @click="logicDelete">删除</button>
  <input type="text" v-model="deptId"/>
  <button @click="selectByDeptId">查询</button>
  <button @click="updateOpen">更新</button>



  <!-- 添加或修改部门对话框 -->
  <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
    <el-form ref="form" :model="dept"  label-width="1rem">
      <el-row>
        <el-col :span="24" v-if="dept.parentId !== 0">
          <el-form-item label="上级部门" prop="parentId">
            <el-input-number v-model="dept.parentId" controls-position="right" :min="0" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="部门名称" prop="deptName">
            <el-input v-model="dept.deptName" placeholder="请输入部门名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="显示排序" prop="orderNum">
            <el-input-number v-model="dept.orderNum" controls-position="right" :min="0" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item label="部门状态" prop="status">
            <el-input-number v-model="dept.status" controls-position="right" :min="0" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="update">确 定</el-button>
      <el-button type="danger" @click="cancel">取 消</el-button>
    </div>
  </el-dialog>

</div>
</template>

<script>
import {logicDelete, select, selectByDeptId, updateDept} from "@/api/test";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "index",
  data(){
    return{
        username:'zzz',
        deptId:'',
        dept:{},
        open:false,
        title:''
    }
  },
  methods:{
    updateOpen(){
      this.open=true
      this.title='更新数据'
    },
    select(){
      select(this.username).then(res=>{
        this.$message.success(res.data)
      })
    },
    logicDelete(){
      logicDelete(this.deptId).then(res=>{
        console.log(res.data)
      })
    },
    selectByDeptId(){
      selectByDeptId(this.deptId).then(res=>{
        console.log(res.data)
        this.dept=res.data.data
      })
    },
    update(){
      console.log(this.dept)
      updateDept(this.dept).then(res=>{
        console.log(res.data)
      })
    },
    cancel(){
      this.open=false
    }
  }
}
</script>

<style scoped>

</style>