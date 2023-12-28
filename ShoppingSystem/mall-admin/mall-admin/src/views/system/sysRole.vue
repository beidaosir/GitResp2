<template>
    <div class="search-div">
        <!-- 搜索表单 -->
        <el-form label-width="70px" size="small">
            <el-form-item label="角色名称">
                <el-input v-model="queryDto.roleName" style="width: 100%" placeholder="角色名称"></el-input>
            </el-form-item>
            <el-row style="display: flex">
                <el-button type="primary" size="small" @click="searchSysRole">
                    搜索
                </el-button>
                <el-button size="small" @click="resetData">重置</el-button>
            </el-row>
        </el-form>

        <!-- 添加按钮 -->
        <div class="tools-div">
            <el-button type="success" size="small" @click="addShow">添 加</el-button>
        </div>

        <!-- 添加角色表单对话框 -->
        <el-dialog v-model="dialogVisible" title="添加或修改角色" width="30%">
            <el-form label-width="120px">
                <el-form-item label="角色名称">
                    <el-input v-model="sysRole.roleName" />
                </el-form-item>
                <el-form-item label="角色Code">
                    <el-input v-model="sysRole.roleCode" />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submit">提交</el-button>
                    <el-button @click="dialogVisible = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!--- 角色表格数据 -->
        <el-table :data="list" style="width: 100%">
            <el-table-column prop="roleName" label="角色名称" width="180" />
            <el-table-column prop="roleCode" label="角色code" width="180" />
            <el-table-column prop="createTime" label="创建时间" />
            <el-table-column label="操作" align="center" width="280" #default="scope">

                <!-- #default="scope"  插槽    添加@click="editShow(scope.row)"-->
                <el-button type="primary" size="small" @click="editShow(scope.row)">修改</el-button>
                <el-button type="danger" size="small" @click="deleteById(scope.row)">删除</el-button>
                <el-button type="warning" size="small" @click="showAssignMenu(scope.row)"> 分配菜单</el-button>

            </el-table-column>
        </el-table>

        <!-- 分配菜单的对话框  // tree组件添加ref属性，后期方便进行tree组件对象的获取-->
        <el-dialog v-model="dialogMenuVisible" title="分配菜单" width="40%">
            <el-form label-width="80px">
                <el-tree
                        :data="sysMenuTreeList"
                        ref="tree"   
                        show-checkbox
                        default-expand-all
                        :check-on-click-node="true"
                        node-key="id"
                        :props="defaultProps"
                />
                <el-form-item>
                    <el-button type="primary" @click="doAssign">提交</el-button>
                    <el-button @click="dialogMenuVisible = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>


        <!--分页条-->
        <el-pagination v-model:current-page="pageParams.page" v-model:page-size="pageParams.limit"
            :page-sizes="[10, 20, 50, 100]" @size-change="fetchData" @current-change="fetchData"
            layout="total, sizes, prev, pager, next" :total="total" />
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { DoAssignMenuIdToSysRole,GetSysRoleMenuIds,GetSysRoleListByPage, SaveSysRole,UpdateSysRole,DeleteSysRole } from '@/api/sysRole'
import { ElMessage,ElMessageBox } from 'element-plus'

//=============保存菜单=====================
const doAssign = async () => {
    const checkedNodes = tree.value.getCheckedNodes() ; // 获取选中的节点
    const checkedNodesIds = checkedNodes.map(node => {  // 获取选中的节点的id   全开为0
        return {
            id: node.id,
            isHalf: 0
        }
    })
        
    // 获取半选中的节点数据，当一个节点的子节点被部分选中时，该节点会呈现出半选中的状态
    const halfCheckedNodes = tree.value.getHalfCheckedNodes() ; 
    const halfCheckedNodesIds = halfCheckedNodes.map(node => {   // 获取半选中节点的id  半开为1
        return {
            id: node.id,
            isHalf: 1
        }
    })
        
    // 将选中的节点id和半选中的节点的id进行合并
    const menuIds = [...checkedNodesIds , ...halfCheckedNodesIds]  
    console.log(menuIds);

    // 构建请求数据
    const assignMenuDto = {
        roleId: roleId,
        menuIdList: menuIds
    }
 
    // 发送请求
    await DoAssignMenuIdToSysRole(assignMenuDto) ;
    ElMessage.success('操作成功')
    dialogMenuVisible.value = false

} 


//==============为角色分配菜单===============

const defaultProps = {
  children: 'children',
  label: 'title',
}
const dialogMenuVisible = ref(false)
const sysMenuTreeList = ref([])

// 树对象变量
const tree = ref() 

// 默认选中的菜单数据集合
let roleId = ref()
const showAssignMenu = async row => { 
  dialogMenuVisible.value = true
  roleId = row.id
  const { data } = await GetSysRoleMenuIds(row.id)   // 请求后端地址获取所有的菜单数据，以及当前角色所对应的菜单数据
  sysMenuTreeList.value = data.sysMenuList
  tree.value.setCheckedKeys(data.roleMenuIds)   // 进行数据回显
}


//==============角色删除====================
const deleteById = (row)=>{
    //弹出确认框 element组件
    ElMessageBox.confirm('此操作将永久删除该记录, 是否继续?', 'Warning', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
    }).then(async () => {
       const {code} = await DeleteSysRole(row.id)
       if(code === 200) {
            ElMessage.success('删除成功')
            pageParams.value.page = 1
            fetchData()
       }
    })
}


//==============角色添加/修改================

const roleForm = {
    id: "",
    roleName: "",
    roleCode: ""
}
const sysRole = ref(roleForm)

//弹框设置 true弹出  false关闭
const dialogVisible = ref(false) //dialogVisible 默认关闭false  

//弹出框数据回显
const editShow = (row)=>{
    //对象拓展运算符  ...
    sysRole.value = {...row}
    dialogVisible.value = true
}


//点击添加弹出框
const addShow = () => {
    sysRole.value = {}
    dialogVisible.value = true
}

//添加和修改的方法
//判断SysRole包含id值就是修改  不包含id就是添加操作
const submit = async () => {

    if(!sysRole.value.id){
        //不包含id值  添加操作
        const { code } = await SaveSysRole(sysRole.value)
        if (code === 200) {
            //关闭弹框
            dialogVisible.value = false
            //提示信息
            ElMessage.success("操作成功")

            //刷新页面
            fetchData()
        }

    }else{
        //有id值  修改操作
        const {code} = await UpdateSysRole(sysRole.value)
        if(code===200){
            //关闭弹框
            dialogVisible.value = false
            //提示信息
            ElMessage.success("操作成功")

            //刷新页面
            fetchData()
        }

    }

    
}


//==============角色列表================
//定义数据模型
let list = ref([]) //角色列表

let total = ref(0) //总记录数

const pageParamsForms = {
    page: 1,//当前页
    limit: 3,//每页记录数
}
const pageParams = ref(pageParamsForms)

const queryDto = ref({ "roleName": "" })//条件封装数据

//钩子函数
onMounted(() => {
    fetchData() //调用列表方法
})


//操作方法：列表方法  搜索方法
//列表方法：axios请求调用接口得到列表数据   async异步操作
const fetchData = async () => {
    //data,code,message 与后端Result中返回保持一致
    const { data, code, message } = await GetSysRoleListByPage(pageParams.value.page, pageParams.value.limit, queryDto.value)
    list.value = data.list
    total.value = data.total
}

//搜索方法
const searchSysRole = () => {
    fetchData()
}

// 重置方法
const resetData = () => {
    // 清空搜索条件
    queryDto.value.roleName = ''

    // 重置分页参数
    pageParams.value = { ...pageParamsForms }

    // 触发重新获取数据
    fetchData()
}

</script>

<style scoped>
.search-div {
    margin-bottom: 10px;
    padding: 10px;
    border: 1px solid #ebeef5;
    border-radius: 3px;
    background-color: #fff;
}

.tools-div {
    margin: 10px 0;
    padding: 10px;
    border: 1px solid #ebeef5;
    border-radius: 3px;
    background-color: #fff;
}
</style>