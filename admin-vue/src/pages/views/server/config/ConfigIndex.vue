<template>
    <div>
        <div>
            <el-form :model="formData" inline>
                <el-form-item>
                    <el-select v-model="formData.typeId" placeholder="服务类型" style="width: 240px">
                        <el-option :value="'0'" label="所有类型"/>
                        <el-option v-for="type in typeSelectOptionsRef" :key="type.id" :value='type.id' :label="type.name"/>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="formData.appId" type="text" placeholder="服务ID"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="handleQuery">查询</el-button>
                    <el-button @click="handleCommand('add', null)">添加</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div>
            <el-table v-loading="tableLoadingRef" :data="tableData" empty-text="无数据" height="640">
                <el-table-column type="index" label="序号" width="80" align="center"/>
                <el-table-column prop="typeName" label="服务类型" width="100" align="left"/>
                <el-table-column prop="appId" label="服务ID" width="80" align="left"/>
                <el-table-column prop="hostName" label="主机名称" width="120" align="left"/>
                <el-table-column prop="hostAddr" label="主机地址" width="140" align="left"/>
                <el-table-column prop="platform" label="平台" width="100" align="left"/>
                <el-table-column prop="channel" label="渠道" width="120" align="left"/>
                <el-table-column prop="svrArea" label="区域" width="100" align="left"/>
                <el-table-column prop="svrName" label="名称" width="120" align="left"/>
                <el-table-column prop="content" label="服务配置" width="80" align="center">
                    <template #default="scope">
                        <el-popover placement="bottom" trigger="click" width="514px">
                            <div class="configContent-wrapper">
                                <pre class="configContent">{{ scope.row.content }}</pre>
                            </div>
                            <template #reference>
                                <el-button type="primary" link>查看</el-button>
                            </template>
                        </el-popover>
                    </template>
                </el-table-column>
                <el-table-column prop="state" label="状态" width="80" align="center">
                    <template #default="scope">
                        <span :style="{color: scope.row.state === 0 ? '#FF0000' : '#409EFF'}">{{ scope.row.state === 0 ? '禁用' : '启用' }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="remark" label="备注" align="left" :show-overflow-tooltip="true"/>
                <el-table-column prop="updateBy" label="操作人" width="120" align="center"/>
                <el-table-column prop="updateTime" label="操作时间" width="180" align="center"/>
                <el-table-column fixed="right" label="操作" align="center" width="120">
                    <template #default="scope">
                        <el-dropdown trigger="click" @command="(command) => {handleCommand(command, scope.row)}">
                            <el-button link type="primary" size="default">编辑</el-button>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item command="detail">详情</el-dropdown-item>
                                    <el-dropdown-item command="modify">修改</el-dropdown-item>
                                    <el-dropdown-item v-if="scope.row.state === 0" command="enable" :disabled="scope.row.id === 1">启用</el-dropdown-item>
                                    <el-dropdown-item v-else command="disable" :disabled="scope.row.id === 1">禁用</el-dropdown-item>
                                    <el-dropdown-item command="delete">删除</el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div>
            <el-pagination v-bind="pagination" style="justify-content: center"></el-pagination>
        </div>
        <div>
            <config-edit v-model="showEditRef" :edit-data="editDataRef" @edit-success="handleEditSuccess"></config-edit>
        </div>
    </div>
</template>

<script>
export default {
    name: "ConfigIndex"
}
</script>

<script setup>

import {onMounted, reactive, ref} from "vue";

import {ElMessage, ElMessageBox} from "element-plus";

import ConfigEdit from "@/pages/views/server/config/ConfigEdit";

import serverApi from '@/api/server'
import commonApi from "@/api/common";

const typeSelectOptionsRef = ref([])

const formData = reactive({
    typeId: '0',
    appId: '',
})

const tableLoadingRef = ref(false)
const tableData = ref([])

function query() {

    const params = {}
    if (formData.typeId && formData.typeId !== '0') {
        params.typeId = formData.typeId
    }
    if (formData.appId && formData.appId !== '') {
        params.appId = Number(formData.appId)
        if (isNaN(params.appId) || params.appId < 1 || params.appId > 999 || (params.appId % 1) !== 0) {
            ElMessage({type: 'error', showClose: true, message: "服务ID必须是1-999的整数"})
            return
        }
    }

    tableLoadingRef.value = true
    serverApi.configQuery(pagination.value.currentPage, pagination.value.pageSize, params)
        .then(res => {
            tableData.value = res.results
            pagination.value.total = res.total
            pagination.value.pageCount = res.pages < 1 ? 1 : res.pages
        })
        .catch(e => {
            ElMessage({type: 'error', showClose: true, message: e.message})
        })
        .finally(() => {
            tableLoadingRef.value = false
        })
}

function queryTypes() {
    commonApi.selectServerTypes()
        .then((types) => {
            typeSelectOptionsRef.value = types
        })
        .catch((e) => {
            ElMessage({type: 'error', showClose: true, message: '查询服务类型失败: ' + e.message})
        })
}

onMounted(() => {
    queryTypes()
    query()
})

const showEditRef = ref(false)
const editDataRef = ref(null)

const pagination = ref({
        layout: "prev,pager,next",
        background: true,
        currentPage: 1,
        pageSize: 15,
        total: 0,
        pageCount: 1,
        prevIcon: 'CaretLeft',
        nextIcon: 'CaretRight',
        onCurrentChange: (pageNo) => {
            pagination.value.currentPage = pageNo
            query()
        },
        onPrevClick: (pageNo) => {
            if (pageNo > 1) {
                pagination.value.currentPage = pageNo - 1
                query()
            }
        },
        onNextClick: (pageNo) => {
            if (pageNo < pagination.value.pageCount) {
                pagination.value.currentPage = pageNo + 1
                query()
            }
        }
    }
)

function handleQuery() {
    query()
}

function handleEditSuccess() {
    query()
}

function changeState(row, state) {
    serverApi.configChangeState([row.id,], state)
        .then(() => {
            row.state = state
            ElMessage({type: 'success', showClose: true, message: '修改成功'})
        })
        .catch((e) => {
            ElMessage({type: 'error', showClose: true, message: e.message})
        })
}

function handleCommand(command, row) {
    switch (command) {
        case 'add':
            editDataRef.value = null
            showEditRef.value = true
            break
        case 'detail':
            break
        case 'modify':
            editDataRef.value = row
            showEditRef.value = true
            break
        case 'enable':
            changeState(row, 1)
            break
        case 'disable':
            ElMessageBox.confirm('确认禁用', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
                .then(() => {
                    changeState(row, 0)
                })
                .catch(() => {
                })
            break
        case 'delete':
            ElMessageBox.confirm('确认删除主机', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
                .then(() => {
                    serverApi.configDelete([row.id,])
                        .then(() => {
                            query()
                        })
                        .catch((e) => {
                            ElMessage({type: 'error', showClose: true, message: e.message})
                        })
                })
                .catch(() => {
                })
            break
    }
}

</script>

<style>
/*别删*/
.cell {
    height: 23px;
}
</style>

<style scoped>

.configContent-wrapper {
    width: 490px;
    height: 410px;
    border-radius: 5px;
    overflow: hidden;
    border-style: solid;
    border-width: 1px;
    border-color: #79bbff;
    /*background-color: #eeeeee;*/
}

.configContent {
    width: 480px;
    max-width: 480px;
    height: 400px;
    max-height: 400px;
    overflow: auto;
    padding: 5px;
    margin: 0;
}

</style>