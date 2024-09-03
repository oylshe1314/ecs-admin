<template>
    <el-dialog v-model="showEditRef" :title="titleRef" width="640px" @opened="handleOpened" @close="handleEditClose" destroy-on-close center>
        <el-form :model="formData" label-width="100px">
            <el-form-item label="服务类型" required>
                <el-select v-model="formData.typeId" placeholder="选择服务类型" style="width: 240px">
                    <el-option :value="'0'" label="选择服务类型"/>
                    <el-option v-for="type in typeSelectOptionsRef" :key="type.id" :value='type.id' :label="type.name"/>
                </el-select>
            </el-form-item>
            <el-form-item label="服务ID" required>
                <el-input-number v-model.number="formData.appId" :min="1" :max="999" controls-position="right" style="width: 120px"></el-input-number>
            </el-form-item>
            <el-form-item label="发布主机" required>
                <el-select v-model="formData.hostId" placeholder="选择发布主机" style="width: 240px">
                    <el-option :value="'0'" label="选择发布主机"/>
                    <el-option v-for="host in hostSelectOptionsRef" :key="host.id" :value='host.id' :label="host.name"/>
                </el-select>
            </el-form-item>
            <el-form-item label="服务配置" required>
                <el-input v-model="formData.content" type="textarea" rows="10" style="width: 100%"/>
            </el-form-item>
            <el-form-item label="备注">
                <el-input v-model="formData.remark" type="textarea" rows="4" style="width: 100%"/>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" style="width: 80px" @click="close(false)">取消</el-button>
                <el-button type="primary" style="width: 80px" @click="handleSubmit">提交</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</template>

<script>
export default {
    name: "ConfigEdit"
}
</script>

<script setup>

import {reactive, ref, watch} from "vue";

import {ElMessage, ElMessageBox} from "element-plus";

import serverApi from '@/api/server'
import commonApi from '@/api/common'

const props = defineProps({
    modelValue: {
        type: Boolean,
        required: true,
        default: () => false,
    },
    editData: {
        type: Object,
        default: () => null,
    }
})

const emits = defineEmits(['update:modelValue', 'editSuccess'])

const showEditRef = ref(props.modelValue)

watch(() => props.modelValue, (value) => {
    showEditRef.value = value
})

function close(success) {
    showEditRef.value = false
    if (success) {
        emits('editSuccess')
    }
}

const formData = reactive({
    id: '',
    typeId: '0',
    appId: 1,
    hostId: '0',
    content: '',
    remark: '',
})

function init() {

    titleRef.value = '配置添加'

    formData.id = ''
    formData.typeId = '0'
    formData.appId = 1
    formData.hostId = '0'
    formData.content = ''
    formData.remark = ''
}

const titleRef = ref('配置添加')

const typeSelectOptionsRef = ref([])
const hostSelectOptionsRef = ref([])

watch(() => props.editData, (editData) => {
    if (editData == null) {
        init()
    } else {

        titleRef.value = '配置修改'

        formData.id = editData.id
        formData.typeId = editData.typeId
        formData.appId = editData.appId
        formData.hostId = editData.hostId
        formData.content = editData.content
        formData.remark = editData.remark
    }
})

function queryTypes() {
    commonApi.selectServerTypes()
        .then((types) => {
            typeSelectOptionsRef.value = types
        })
        .catch((e) => {
            ElMessage({type: 'error', showClose: true, message: '查询服务类型失败: ' + e.message})
        })
}

function queryHosts() {
    commonApi.selectServerHosts()
        .then((hosts) => {
            hostSelectOptionsRef.value = hosts
        })
        .catch((e) => {
            ElMessage({type: 'error', showClose: true, message: '查询主机列表失败: ' + e.message})
        })
}

function handleOpened() {
    queryTypes()
    queryHosts()
}

function handleEditClose() {
    emits('update:modelValue', false)
}

function handleSubmit() {
    if (formData.typeId === '0') {
        ElMessage({type: 'error', showClose: true, message: '请选择服务类型'})
        return
    }
    if (formData.appId < 1 || formData.appId > 999 || (formData.appId % 1) !== 0) {
        ElMessage({type: 'error', showClose: true, message: "服务ID必须是1-999的整数"})
        return
    }
    if (formData.hostId === '0') {
        ElMessage({type: 'error', showClose: true, message: '请选择要发布主机'})
        return
    }
    if (formData.content === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入服务配置'})
        return
    }

    ElMessageBox.confirm('确认提交', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
        .then(() => {
            serverApi.configSave(formData)
                .then(() => {
                    close(true)
                    ElMessage({type: 'success', showClose: true, message: formData.id && formData.id !== '' ? '修改成功' : '添加成功'})
                    init()
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