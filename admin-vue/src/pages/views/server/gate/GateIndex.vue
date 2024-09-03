<template>
    <div id="gateSetting" class="gateSetting">
        <div class="form">
            <el-form label-width="100px" :model="formData">
                <el-form-item label="最新版本">
                    <el-input v-model="formData.version" style="width: 320px" :disabled="editState.version"></el-input>
                </el-form-item>
                <el-form-item label="公告标题">
                    <el-input v-model="formData.title" style="width: 320px" :disabled="editState.title"></el-input>
                </el-form-item>
                <el-form-item label="公告内容">
                    <el-input v-model="formData.content" type="textarea" rows="8" style="width: 100%" :disabled="editState.content"/>
                </el-form-item>
                <el-form-item label="关闭登录">
                    <el-switch v-model="formData.loginClosed" style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                               :disabled="editState.loginClosed"/>
                    <span style="width: 14px"/>
                    <el-checkbox v-model="formData.offline" label="强制下线" :disabled="editState.offline"/>
                </el-form-item>
                <el-form-item label="定时关闭">
                    <el-switch v-model="formData.timedClose" style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                               :disabled="editState.timedClose"/>
                </el-form-item>
                <el-form-item label="关闭时间" v-show="formData.timedClose">
                    <el-date-picker v-model="formData.closeDate" type="date" format="YYYY-MM-DD" value-format="YYYY-MM-DD" placeholder="日期"
                                    style="width: 158px"
                                    :disabled="editState.closeTime"/>
                    <span style="width: 4px"/>
                    <el-time-picker v-model="formData.closeTime" format="HH:mm:ss" value-format="HH:mm:ss" placeholder="时间" style="width: 158px"
                                    :disabled="editState.closeTime"/>
                </el-form-item>
                <el-form-item label="服务器ID">
                    <el-input v-model="formData.closedList" type="textarea" rows="2" style="width: 100%" :disabled="editState.closedList"/>
                </el-form-item>
                <el-form-item label="ID白名单">
                    <el-input v-model="formData.whiteList" type="textarea" rows="4" style="width: 100%" :disabled="editState.whiteList"/>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleEditState" style="width: 80px">{{ editState.btnText }}</el-button>
                    <el-button v-show="editState.showSubmit" type="primary" style="width: 80px" @click="handleSubmit">提交</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
export default {
    name: 'DetailView'
}
</script>

<script setup>

import {onMounted, reactive} from "vue";

import {ElMessage, ElMessageBox} from "element-plus";
import {closeLoading, openLoading} from "@/util/loading";

import serverApi from '@/api/server'

const editState = reactive({
    version: true,
    title: true,
    content: true,
    loginClosed: true,
    offline: true,
    timedClose: true,
    closeTime: true,
    closedList: true,
    whiteList: true,
    showSubmit: false,
    btnText: '修改',
})

const formData = reactive({
    version: '',
    title: '',
    content: '',
    loginClosed: false,
    offline: true,
    timedClose: false,
    closeDate: null,
    closeTime: null,
    closedList: '',
    whiteList: '',
})

function handleEditState() {
    editState.version = !editState.version
    editState.title = !editState.title
    editState.content = !editState.content
    editState.loginClosed = !editState.loginClosed
    editState.offline = !editState.offline
    editState.timedClose = !editState.timedClose
    editState.closeTime = !editState.closeTime
    editState.closedList = !editState.closedList
    editState.whiteList = !editState.whiteList
    editState.showSubmit = !editState.showSubmit
    editState.btnText = editState.showSubmit ? '取消' : '修改'
}


onMounted(() => {
    getSetting()
})

function getSetting() {
    openLoading('#gateSetting', '正在获取网关设置...')
    serverApi.getGateSetting()
        .then((res) => {
            formData.version = res.version
            formData.title = res.title
            formData.content = res.content
            formData.loginClosed = res.loginClosed
            formData.timedClose = res.timedClose
            formData.closeDate = res.closeDate
            formData.closeTime = res.closeTime
            formData.closedList = res.closedList
            formData.whiteList = res.whiteList
        })
        .catch((e) => {
            ElMessage({type: 'error', showClose: true, message: e.message})
        })
        .finally(() => {
            closeLoading()
        })
}

function handleSubmit() {
    if (formData.version === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入最新版本'})
        return
    }
    if (formData.title === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入公告标题'})
        return
    }
    if (formData.content === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入公告内容'})
        return
    }
    ElMessageBox.confirm('确认提交修改', '警告', {confirmButtonText: '确认', cancelButtonText: '取消'})
        .then(() => {
            openLoading('#gateSetting', '正在保存网关设置...')
            serverApi.saveGateSetting(formData)
                .then((res) => {
                    formData.version = res.version
                    formData.title = res.title
                    formData.content = res.content
                    formData.loginClosed = res.loginClosed
                    formData.timedClose = res.timedClose
                    formData.closeDate = res.closeDate
                    formData.closeTime = res.closeTime
                    formData.closedList = res.closedList
                    formData.whiteList = res.whiteList
                    ElMessage({type: 'success', showClose: true, message: '保存成功'})
                    handleEditState()
                })
                .catch((e) => {
                    ElMessage({type: 'error', showClose: true, message: e.message})
                })
                .finally(() => {
                    closeLoading()
                })
        })
        .catch(() => {
        })
}

</script>

<style scoped>

.gateSetting {
    width: 100%;
    height: 100%;
    /*display: flex;*/
    /*justify-content: center;*/
}

.form {
    width: 680px;
    height: 642px;
    /*background: pink;*/
    /*position: relative;*/
    /*top: 200px;*/
    /*left: 50%;*/
    /*margin: 180px 0 0 -180px;*/
    padding: 40px 40px 40px 40px;
}

</style>
