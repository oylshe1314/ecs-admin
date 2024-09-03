<template>
    <el-dialog v-model="showEditRef" :title="titleRef" width="640px" @close="handleEditClose" destroy-on-close center>
        <el-form :model="formData" label-width="100px">
            <el-form-item label="名称" required>
                <el-input v-model="formData.name" type="text" style="width: 240px"></el-input>
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
    name: "RoleExit"
}
</script>

<script setup>

import {ref, reactive, watch} from 'vue'

import {ElMessage, ElMessageBox} from "element-plus";

import adminApi from "@/api/admin";

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
    remark: '',
})

const titleRef = ref('添加角色')

function init() {
    titleRef.value = '添加角色'

    formData.id = ''
    formData.name = ''
    formData.remark = ''
}

watch(() => props.editData, (editData) => {
    if (editData == null) {
        init()
    } else {
        titleRef.value = '修改角色'

        formData.id = editData.id
        formData.name = editData.name
        formData.remark = editData.remark
    }
})

function handleEditClose() {
    emits('update:modelValue', false)
}

function handleSubmit() {
    if (formData.name === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入角色名称'})
        return
    }

    ElMessageBox.confirm('确认提交', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
        .then(() => {
            adminApi.roleSave(formData)
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
