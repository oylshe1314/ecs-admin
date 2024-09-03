<template>
    <div>
        <div>
            <el-form :model="formData" inline>
                <el-form-item>
                    <el-input v-model="formData.appId" type="text" placeholder="服务ID"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="handleQuery">查询</el-button>
                    <el-button type="primary" @click="handleSend">发送</el-button>
                    <template v-if="selectData.rows.length > 0">
                        <el-button type="danger" @click="handleDelete">删除</el-button>
                    </template>
                </el-form-item>
            </el-form>
        </div>
        <div>
            <el-table v-loading="tableLoadingRef" :data="tableData" @selection-change="handleSelect" empty-text="无数据" height="672">
                <el-table-column type="selection" width="40"/>
                <el-table-column type="index" label="序号" width="80" align="center"/>
                <el-table-column prop="appId" label="服务器ID" width="100" align="center"/>
                <!--                <el-table-column prop="id" label="邮件ID" width="120" align="center"/>-->
                <el-table-column prop="title" label="邮件标题" width="200" align="left"/>
                <el-table-column prop="content" label="邮件内容" align="left" :show-overflow-tooltip="true"/>
                <el-table-column prop="itemId" label="物品ID" width="180" align="left" :show-overflow-tooltip="true"/>
                <el-table-column prop="itemNum" label="物品数量" width="180" align="left" :show-overflow-tooltip="true"/>
                <el-table-column prop="createTime" label="创建时间" width="180" align="center"/>
                <el-table-column prop="expiration" label="过期时间" width="180" align="center"/>
            </el-table>
        </div>
        <div>
            <mail-send v-model="showSendRef" @edit-success="handleEditSuccess"></mail-send>
        </div>
    </div>
</template>

<script>
export default {
    name: "MailIndex"
}
</script>

<script setup>

import {onMounted, reactive, ref} from "vue";

import {ElMessage, ElMessageBox} from "element-plus";

import MailSend from "@/pages/views/game/mail/MailSend";

import gameApi from '@/api/game'

const formData = reactive({
    appId: '',
})

const tableLoadingRef = ref(false)
const tableData = ref([])

function query() {
    const params = {}
    if (formData.appId && formData.appId !== '') {
        params.appId = Number(formData.appId)
        if (isNaN(params.appId) || params.appId < 1 || params.appId > 999 || (params.appId % 1) !== 0) {
            ElMessage({type: 'error', showClose: true, message: "服务ID必须是1-999的整数"})
            return
        }
    }

    tableLoadingRef.value = true
    gameApi.mailList(params.appId).then(res => {
        tableData.value = res
    }).catch(e => {
        ElMessage({type: 'error', showClose: true, message: e.message})
    }).finally(() => {
        tableLoadingRef.value = false
    })
}

onMounted(() => {
    query()
})

function handleQuery() {
    query()
}

const showSendRef = ref(false)

function handleSend() {
    showSendRef.value = true
}

function handleEditSuccess() {
    query()
}

const selectData = reactive({
    rows: [],
})

function handleSelect(rows) {
    selectData.rows = []
    for (const row of rows) {
        selectData.rows.push(row)
    }
}

function handleDelete() {
    if (!selectData.rows || selectData.rows.length === 0) {
        ElMessage({type: 'error', showClose: true, message: '请选择邮件再操作'})
        return
    }

    ElMessageBox.confirm('确认删除邮件', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
        .then(() => {
            const selectedList = []
            for (let row of selectData.rows) {
                selectedList.push({appId: row.appId, id: row.id})
            }
            gameApi.mailDelete({selectedList: selectedList})
                .then(() => {
                    ElMessage({type: 'success', showClose: true, message: '删除成功'})
                    query()
                })
                .catch((e) => {
                    ElMessage({type: 'error', showClose: true, message: e.message})
                })
        })
        .catch(() => {
        })
}

</script>

<style scoped>

</style>