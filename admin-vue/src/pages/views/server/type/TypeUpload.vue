<template>
    <el-dialog v-model="showEditRef" :title="titleRef" width="440px" @opened="handleOpened" @close="handleEditClose" destroy-on-close center>
        <el-form :model="formData" label-width="100px">
            <el-form-item label="服务类型">
                <el-select v-model="formData.typeId" placeholder="选择服务类型" style="width: 240px">
                    <el-option :value="'0'" label="选择服务类型"/>
                    <el-option v-for="type in typeSelectOptionsRef" :key="type.id" :value='type.id' :label="type.name"/>
                </el-select>
            </el-form-item>
            <el-form-item prop="name" label="包版本">
                <el-input v-model="formData.version" type="text" style="width: 240px"></el-input>
            </el-form-item>
            <el-form-item prop="package" label="包文件">
                <el-upload ref="uploadRef"
                           name="package"
                           accept=".zip"
                           :http-request="handleUpload"
                           :on-success="onUploadSuccess"
                           :on-error="onUploadError"
                           :auto-upload="false"
                           :limit="1"
                           style="width: 240px;height: 72px"
                >
                    <template #trigger>
                        <el-button type="primary" style="width: 100px">选择文件</el-button>
                    </template>
                </el-upload>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" style="width: 100px" @click="handleSubmit">上传</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</template>

<script>
export default {
    name: "TypeUpload"
}
</script>

<script setup>

import {reactive, ref, watch} from "vue";

import {ElMessage, ElMessageBox} from "element-plus";

import serverApi from '@/api/server'
import commonApi from "@/api/common";

const props = defineProps({
    modelValue: {
        type: Boolean,
        required: true,
        default: () => false,
    },
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
    typeId: '0',
    version: '',
})

const titleRef = ref('服务包上传')

const typeSelectOptionsRef = ref([])

function queryTypes() {
    commonApi.selectServerTypes()
        .then((types) => {
            typeSelectOptionsRef.value = types
        })
        .catch((e) => {
            ElMessage({type: 'error', showClose: true, message: '查询服务类型失败: ' + e.message})
        })
}

function handleOpened() {
    queryTypes()
}

function handleEditClose() {
    emits('update:modelValue', false)
}

const uploadRef = ref()

function handleUpload(options) {
    const postForm = new FormData()
    postForm.append('typeId', formData.typeId)
    postForm.append('version', formData.version)
    postForm.append('file', options.file)

    serverApi.packageUpload(postForm)
        .then(() => {
            options.onSuccess()
        })
        .catch((e) => {
            options.onError(new Error(e.message))
        })
}

function handleSubmit() {
    if (formData.typeId === '0') {
        ElMessage({type: 'error', showClose: true, message: '请选择服务类型'})
        return
    }
    if (formData.version === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入包版本'})
        return
    }

    ElMessageBox.confirm('确认上传', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
        .then(() => {
            uploadRef.value.submit()
        })
        .catch(() => {
        })
}

function onUploadSuccess(res) {
    ElMessage({type: 'success', showClose: true, message: '上传成功'})
    close(true)
}

function onUploadError(err) {
    ElMessage({type: 'error', showClose: true, message: err.message})
}

</script>

<style scoped>

</style>