<template>
    <el-dialog v-model="showEditRef" :title="titleRef" width="640px" @close="handleEditClose" destroy-on-close center>
        <el-form :model="formData" label-width="100px">
            <el-form-item label="主机名称" required>
                <el-input v-model="formData.name" type="text" style="width: 240px"></el-input>
            </el-form-item>
            <el-form-item label="SSH地址" required>
                <el-input v-model="formData.host" type="text" style="width: 240px"></el-input>
            </el-form-item>
            <el-form-item label="SSH端口" required>
                <el-input-number v-model.number="formData.port" :max="65535" controls-position="right" style="width: 120px"></el-input-number>
            </el-form-item>
            <el-form-item label="服务目录" required>
                <el-input v-model="formData.dir" type="text" style="width: 100%"></el-input>
            </el-form-item>
            <el-form-item label="SSH用户" required>
                <el-input v-model="formData.user" type="text" style="width: 240px"></el-input>
            </el-form-item>
            <el-form-item label="SSH密码">
                <el-input v-model="formData.password" type="text" style="width: 240px"></el-input>
            </el-form-item>
            <el-form-item label="SSH密钥">
                <el-input v-model="formData.key" type="textarea" rows="4" style="width: 100%"/>
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
    name: "HostEdit"
}
</script>

<script setup>

import {reactive, ref, watch} from "vue";

import {ElMessage, ElMessageBox} from "element-plus";

import serverApi from '@/api/server'

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
    name: '',
    host: '',
    port: 22,
    dir: '',
    user: '',
    password: '',
    key: '',
    remark: '',
})

const titleRef = ref('主机添加')

function init() {
    titleRef.value = '主机添加'

    formData.id = ''
    formData.name = ''
    formData.host = ''
    formData.port = 22
    formData.dir = ''
    formData.user = ''
    formData.password = ''
    formData.key = ''
    formData.remark = ''
}

watch(() => props.editData, (editData) => {
    if (editData == null) {
        init()
    } else {
        titleRef.value = '主机修改'

        formData.id = editData.id
        formData.name = editData.name
        formData.host = editData.host
        formData.port = editData.port
        formData.dir = editData.dir
        formData.user = editData.user
        formData.password = editData.password
        formData.key = editData.key
        formData.remark = editData.remark
    }
})

function handleEditClose() {
    emits('update:modelValue', false)
}

function handleSubmit() {
    if (formData.name === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入主机名称'})
        return
    }
    if (formData.host === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入SSH地址'})
        return
    }
    if (formData.port > 0) {
        if (formData.port < 1 || formData.port > 65535 || (formData.port % 1) !== 0) {
            ElMessage({type: 'error', showClose: true, message: "SSH端口必须是1-65535的整数"})
            return
        }
    }
    if (formData.dir === '') {
        ElMessage({type: 'error', showClose: true, message: '服务目录'})
        return
    }
    if (formData.user === '') {
        ElMessage({type: 'error', showClose: true, message: 'SSH用户'})
        return
    }

    ElMessageBox.confirm('确认提交', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
        .then(() => {
            serverApi.hostSave(formData)
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